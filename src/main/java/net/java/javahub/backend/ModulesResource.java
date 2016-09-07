package net.java.javahub.backend;

import net.java.javahub.backend.coffee.boundary.CoffeeResource;
import net.java.javahub.backend.drawings.boundary.DrawingsResource;
import net.java.javahub.backend.embroideries.boundary.EmbroideriesResource;
import net.java.javahub.backend.games.boundary.GamesResource;
import net.java.javahub.backend.prints.boundary.PrintsResource;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ModulesResource {

    @Context
    UriInfo uriInfo;

    @GET
    public JsonObject getModules() {
        final URI drawingsUri = uriInfo.getRequestUriBuilder().path(DrawingsResource.class).build();
        final URI coffeeUri = uriInfo.getRequestUriBuilder().path(CoffeeResource.class).build();
        final URI gamesUri = uriInfo.getRequestUriBuilder().path(GamesResource.class).build();
        final URI embroideriesUri = uriInfo.getRequestUriBuilder().path(EmbroideriesResource.class).build();
        final URI printsUri = uriInfo.getRequestUriBuilder().path(PrintsResource.class).build();
        return Json.createObjectBuilder().add("_links", Json.createObjectBuilder()
                .add("drawings", drawingsUri.toString())
                .add("coffee", coffeeUri.toString())
                .add("games", gamesUri.toString())
                .add("embroideries", embroideriesUri.toString())
                .add("3d_prints", printsUri.toString()))
                .build();
    }

}
