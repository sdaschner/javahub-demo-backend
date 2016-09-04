package net.java.javahub.backend;

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
        final URI drawingsUri = uriInfo.getRequestUriBuilder().path(ModulesResource.class).build();
        // TODO add remaining
//        "drawings": ".../modules/drawings",
//        "coffee": ".../modules/coffee",
//        "games": ".../modules/games",
//        "embroideries": ".../modules/embroideries",
//        "3d_prints": ".../modules/3d_prints",

        return Json.createObjectBuilder().add("_links", Json.createObjectBuilder()
                .add("drawings", drawingsUri.toString()))
                .build();
    }

}
