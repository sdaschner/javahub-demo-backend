package net.java.javahub.cutter.backend;

import net.java.javahub.cutter.backend.drawings.boundary.DrawingsResource;

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
public class RootResource {

    @Context
    UriInfo uriInfo;

    @GET
    public JsonObject getModules() {
        final URI drawingsUri = uriInfo.getRequestUriBuilder().path(DrawingsResource.class).build();
        return Json.createObjectBuilder().add("_links", Json.createObjectBuilder()
                .add("drawings", drawingsUri.toString()))
                .build();
    }

}
