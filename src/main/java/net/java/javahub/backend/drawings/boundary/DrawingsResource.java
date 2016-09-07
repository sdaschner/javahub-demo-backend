package net.java.javahub.backend.drawings.boundary;

import net.java.javahub.backend.drawings.entity.Drawing;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("drawings")
public class DrawingsResource {

    @Inject
    Drawings drawings;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonArray getDrawings() {
        return drawings.getDrawings().stream()
                .map(this::createDrawingJson)
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add).build();
    }

    private JsonObject createDrawingJson(Drawing drawing) {
        return Json.createObjectBuilder()
                .add("created", drawing.getCreated().toString())
                .add("_links", Json.createObjectBuilder().add("self", createUri(drawing).toString()))
                .build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDrawing(@PathParam("id") long id) {
        final Drawing drawing = drawings.getDrawing(id);

        if (drawing == null)
            throw new NotFoundException();

        return drawing.getPathContent();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createDrawing(@NotNull String content) {
        final Drawing drawing = drawings.create(content);
        return Response.created(createUri(drawing)).build();
    }

    private URI createUri(final Drawing drawing) {
        return uriInfo.getBaseUriBuilder().path(DrawingsResource.class).path(DrawingsResource.class, "getDrawing").build(drawing.getId());
    }

}
