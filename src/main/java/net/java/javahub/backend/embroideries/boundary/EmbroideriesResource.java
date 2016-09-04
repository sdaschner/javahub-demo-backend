package net.java.javahub.backend.embroideries.boundary;

import net.java.javahub.backend.ValidName;
import net.java.javahub.backend.embroideries.entity.Embroidery;

import javax.inject.Inject;
import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.net.URI;
import java.util.NoSuchElementException;

@Path("embroideries")
public class EmbroideriesResource {

    @Inject
    Embroideries embroideries;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonArray getEmbroideries() {
        return embroideries.getEmbroideries().stream()
                .map(this::createEmbroideryJson)
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add).build();
    }

    @POST
    public Response createEmbroidery(@ValidName final JsonObject object) {
        final Embroidery embroidery = embroideries.create(object.getString("name"));
        return Response.created(createUri(embroidery)).build();
    }

    @GET
    @Path("{id}")
    public JsonObject getEmbroidery(@PathParam("id") long id) {
        final Embroidery embroidery = embroideries.getEmbroidery(id);

        if (embroidery == null)
            throw new NotFoundException();

        return createEmbroideryJson(embroidery);
    }

    @DELETE
    @Path("{id}")
    public void deleteEmbroidery(@PathParam("id") final String id) {
        try {
            embroideries.delete(id);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
    }

    @PUT
    @Path("{id}/image")
    @Consumes("image/png")
    public void uploadImage(@PathParam("id") final String id, final InputStream imageInput) {
        try {
            embroideries.addImage(id, imageInput);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("{id}/image")
    @Produces("image/png")
    public StreamingOutput downloadImage(@PathParam("id") final String id) {
        try {
            final byte[] imageData = embroideries.getImageData(id);
            return output -> output.write(imageData);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
    }

    private JsonObject createEmbroideryJson(Embroidery embroidery) {
        final JsonObjectBuilder linksBuilder = Json.createObjectBuilder()
                .add("self", createUri(embroidery).toString());

        if (embroidery.getImageData() != null)
            linksBuilder.add("image", createImageUri(embroidery).toString());

        return Json.createObjectBuilder()
                .add("name", embroidery.getName())
                .add("_links", linksBuilder)
                .build();
    }

    private URI createUri(final Embroidery embroidery) {
        return uriInfo.getBaseUriBuilder().path(EmbroideriesResource.class).path(EmbroideriesResource.class, "getEmbroidery").build(embroidery.getId());
    }

    private URI createImageUri(final Embroidery embroidery) {
        return uriInfo.getBaseUriBuilder().path(EmbroideriesResource.class).path(EmbroideriesResource.class, "getImage").build(embroidery.getId());
    }

}
