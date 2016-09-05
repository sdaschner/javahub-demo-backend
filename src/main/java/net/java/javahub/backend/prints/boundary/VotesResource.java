package net.java.javahub.backend.prints.boundary;

import net.java.javahub.backend.prints.entity.Print;
import net.java.javahub.backend.prints.entity.Vote;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class VotesResource {

    @Inject
    Prints prints;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonArray getVotes() {
        return prints.getVotes().stream()
                .map(this::createVoteJson)
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add).build();
    }

    @DELETE
    public void resetVotes() {
        prints.resetVotes();
    }

    private JsonObject createVoteJson(final Vote vote) {
        return Json.createObjectBuilder()
                .add("3d_print", createUri(vote.getPrint()).toString())
                .add("votes", vote.getCount())
                .build();
    }

    private URI createUri(final Print print) {
        return uriInfo.getBaseUriBuilder().path(PrintsResource.class).path(PrintsResource.class, "getPrint").build(print.getId());
    }

}
