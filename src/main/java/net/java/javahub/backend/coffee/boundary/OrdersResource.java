package net.java.javahub.backend.coffee.boundary;

import net.java.javahub.backend.coffee.entity.Order;
import net.java.javahub.backend.coffee.entity.ValidOrder;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class OrdersResource {

    @Inject
    CoffeeShop coffeeShop;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonArray getOrders() {
        return coffeeShop.getOrders().stream()
                .map(this::createOrderJson)
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add).build();
    }

    @POST
    public Response orderCoffee(@ValidOrder JsonObject orderObject) {
        try {
            final Order order = coffeeShop.orderCoffee(orderObject.getString("type"), orderObject.getInt("strength"));
            return Response.created(createUri(order)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).header("Error", e.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    public JsonObject getOrder(@PathParam("id") final String id) {
        final Order order = coffeeShop.getOrder(id);

        if (order == null)
            throw new NotFoundException();

        return createOrderJson(order);
    }

    private JsonObject createOrderJson(final Order order) {
        return Json.createObjectBuilder()
                .add("type", order.getType().getId())
                .add("strength", order.getStrength())
                .add("_links", Json.createObjectBuilder().add("self", createUri(order).toString()))
                .build();
    }

    private URI createUri(final Order order) {
        return uriInfo.getBaseUriBuilder().path(OrdersResource.class).path(OrdersResource.class, "getOrder").build(order.getId());
    }

}
