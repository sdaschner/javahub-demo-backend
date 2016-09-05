package net.java.javahub.backend.games.boundary;

import net.java.javahub.backend.games.entity.Round;
import net.java.javahub.backend.games.entity.ValidRound;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.NoSuchElementException;

public class RoundsResource {

    @Inject
    Games games;

    @PathParam("gameId")
    String gameId;

    @Context
    UriInfo uriInfo;

    @POST
    public Response startRound(@ValidRound final JsonObject object) {
        try {
            final Round round = games.startRound(gameId, object.getString("device"));

            return Response.created(createUri(round)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("{roundId}")
    public JsonObject getActiveRound(@PathParam("roundId") final String roundId) {
        final Round round = games.getActiveRound(roundId);

        if (round == null)
            throw new NotFoundException();

        return createRoundJson(round);
    }

    @DELETE
    @Path("{roundId}")
    public void finishRound(@PathParam("roundId") final String roundId) {
        try {
            games.finishRound(roundId);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
    }

    private JsonObject createRoundJson(final Round round) {
        return Json.createObjectBuilder().add("device", round.getDeviceId()).build();
    }

    private URI createUri(final Round round) {
        return uriInfo.getBaseUriBuilder().path(RoundsResource.class).path(RoundsResource.class, "getActiveRound").build(round.getGame().getId(), round.getId());
    }

}
