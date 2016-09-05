package net.java.javahub.backend.prints.boundary;

import net.java.javahub.backend.prints.entity.Print;

import javax.inject.Inject;
import javax.json.*;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.net.URI;
import java.util.NoSuchElementException;

@Path("3d_prints")
public class PrintsResource {

    @Inject
    Prints prints;

    @Context
    UriInfo uriInfo;

    @Context
    ResourceContext rc;

    @GET
    public JsonArray getPrints() {
        return prints.getPrints().stream()
                .map(this::createPrintJson)
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add).build();
    }

    @GET
    @Path("{id}")
    public JsonObject getPrint(@PathParam("id") long id) {
        final Print print = prints.getPrint(id);

        if (print == null)
            throw new NotFoundException();

        return createPrintJson(print);
    }

    @GET
    @Path("in_progress")
    public Response getPrintInProgress() {
        final Print print = prints.getPrintInProgress();

        if (print == null)
            return Response.noContent().build();

        return Response.ok(createPrintJson(print)).build();
    }

    @Path("votes")
    public VotesResource votes() {
        return rc.getResource(VotesResource.class);
    }

    @POST
    public Response createPrint(@NotNull String content) {
        final Print print = prints.create(content);
        return Response.created(createUri(print)).build();
    }

    @PUT
    @Path("{id}/image")
    @Consumes("image/png")
    public void uploadImage(@PathParam("id") final String id, final InputStream imageInput) {
        try {
            prints.addImage(id, imageInput);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("{id}/image")
    @Produces("image/png")
    public StreamingOutput downloadImage(@PathParam("id") final String id) {
        try {
            final byte[] imageData = prints.getImageData(id);
            return output -> output.write(imageData);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
    }

    private JsonObject createPrintJson(Print print) {
        final JsonObjectBuilder linksBuilder = Json.createObjectBuilder()
                .add("self", createUri(print).toString());

        if (print.getImageData() != null)
            linksBuilder.add("image", createImageUri(print).toString());

        return Json.createObjectBuilder()
                .add("id", print.getId())
                .add("name", print.getName())
                .add("_links", linksBuilder)
                .build();
    }

    private URI createUri(final Print print) {
        return uriInfo.getBaseUriBuilder().path(PrintsResource.class).path(PrintsResource.class, "getPrint").build(print.getId());
    }

    private URI createImageUri(final Print print) {
        return uriInfo.getBaseUriBuilder().path(PrintsResource.class).path(PrintsResource.class, "downloadImage").build(print.getId());
    }

}
