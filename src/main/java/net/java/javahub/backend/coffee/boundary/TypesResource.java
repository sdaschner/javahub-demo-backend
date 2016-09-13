package net.java.javahub.backend.coffee.boundary;

import net.java.javahub.backend.ValidName;
import net.java.javahub.backend.coffee.entity.Type;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.NoSuchElementException;

public class TypesResource {

    @Inject
    CoffeeShop coffeeShop;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonArray getCoffeeTypes() {
        return coffeeShop.getTypes().stream()
                .map(this::createCoffeeTypeJson)
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add).build();
    }

    @POST
    public Response createCoffeeType(@ValidName JsonObject type) {
        final Type coffeeType = coffeeShop.createType(type.getString("name"));
        return Response.created(createUri(coffeeType)).build();
    }

    @GET
    @Path("{id}")
    public JsonObject getCoffeeType(@PathParam("id") final String id) {
        final Type coffeeType = coffeeShop.getType(id);

        if (coffeeType == null)
            throw new NotFoundException();

        return createCoffeeTypeJson(coffeeType);
    }

    @DELETE
    @Path("{id}")
    public Response deleteCoffeeType(@PathParam("id") final String id) {
        try {
            coffeeShop.deleteType(id);
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    private JsonObject createCoffeeTypeJson(final Type coffeeType) {
        return Json.createObjectBuilder()
                .add("name", coffeeType.getName())
                .add("type", coffeeType.getId())
                .add("_links", Json.createObjectBuilder().add("self", createUri(coffeeType).toString()))
                .build();
    }

    private URI createUri(final Type coffeeType) {
        return uriInfo.getRequestUriBuilder().path(CoffeeResource.class)
                .path(CoffeeResource.class, "types")
                .path(TypesResource.class, "getCoffeeType").build(coffeeType.getId());
    }

}
