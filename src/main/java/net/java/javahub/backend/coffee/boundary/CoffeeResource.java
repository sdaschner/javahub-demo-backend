package net.java.javahub.backend.coffee.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("coffee")
public class CoffeeResource {

    @Context
    ResourceContext rc;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonObject getLinks() {
        final URI typesUri = uriInfo.getRequestUriBuilder().path(CoffeeResource.class).path(CoffeeResource.class, "types").build();
        final URI ordersUri = uriInfo.getRequestUriBuilder().path(CoffeeResource.class).path(CoffeeResource.class, "orders").build();

        return Json.createObjectBuilder().add("_links", Json.createObjectBuilder()
                .add("types", typesUri.toString())
                .add("orders", ordersUri.toString()))
                .build();
    }

    @Path("types")
    public TypesResource types() {
        return rc.getResource(TypesResource.class);
    }

    @Path("orders")
    public OrdersResource orders() {
        return rc.getResource(OrdersResource.class);
    }

}
