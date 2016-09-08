package net.java.javahub.backend.games.boundary;

import net.java.javahub.backend.ValidName;
import net.java.javahub.backend.games.entity.Game;

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

@Path("games")
public class GamesResource {

    @Inject
    Games games;

    @Context
    UriInfo uriInfo;

    @Context
    ResourceContext rc;

    @GET
    public JsonArray getGames() {
        return games.getGames().stream()
                .map(this::createGameJson)
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add).build();
    }

    @GET
    @Path("{id}")
    public JsonObject getGame(@PathParam("id") final String id) {
        final Game game = games.getGame(id);

        if (game == null)
            throw new NotFoundException();

        return createGameJson(game);
    }

    @POST
    public Response createGame(@ValidName final JsonObject object) {
        final Game game = games.create(object.getString("name"));
        return Response.created(createUri(game)).build();
    }

    @DELETE
    @Path("{id}")
    public void deleteGame(@PathParam("id") final String id) {
        try {
            games.delete(id);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
    }

    @Path("{gameId}/rounds")
    public RoundsResource rounds() {
        return rc.getResource(RoundsResource.class);
    }

    @PUT
    @Path("{id}/image")
    @Consumes("image/png")
    public void uploadImage(@PathParam("id") final String id, final InputStream imageInput) {
        try {
            games.addImage(id, imageInput);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("{id}/image")
    @Produces("image/png")
    public StreamingOutput downloadImage(@PathParam("id") final String id) {
        try {
            final byte[] imageData = games.getImageData(id);
            return output -> output.write(imageData);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
    }

    private JsonObject createGameJson(final Game game) {
        final JsonObjectBuilder linksBuilder = Json.createObjectBuilder()
                .add("self", createUri(game).toString());

        if (game.getImageData() != null) {
            linksBuilder.add("image", createImageUri(game).toString())
                    .add("rounds", createRoundsUri(game).toString());
        }

        return Json.createObjectBuilder()
                .add("name", game.getName())
                .add("_links", linksBuilder)
                .build();
    }

    private URI createUri(final Game game) {
        return uriInfo.getBaseUriBuilder().path(GamesResource.class).path(GamesResource.class, "getGame").build(game.getId());
    }

    private URI createImageUri(final Game game) {
        return uriInfo.getBaseUriBuilder().path(GamesResource.class).path(GamesResource.class, "downloadImage").build(game.getId());
    }

    private URI createRoundsUri(final Game game) {
        return uriInfo.getBaseUriBuilder().path(GamesResource.class).path(GamesResource.class, "rounds").build(game.getId());
    }

}
