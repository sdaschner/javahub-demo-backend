package net.java.javahub.backend;

import org.junit.rules.ExternalResource;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.concurrent.locks.LockSupport;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class JavaHubDrawing extends ExternalResource {

    private static final int STARTUP_TIMEOUT = 30;
    private static final int STARTUP_PING_DELAY = 2;

    private String baseUri = "http://localhost:8080/javahub-cutter-backend/modules";
    private Client client;
    private WebTarget baseTarget;
    private WebTarget drawingsTarget;

    public URI createDrawing(final String svgContent) {
        final Response response = postDrawing(svgContent);
        assertStatus(response, Response.Status.CREATED);

        final URI location = response.getLocation();
        assertDrawingUri(location);

        return location;
    }

    private Response postDrawing(final String svgContent) {
        return drawingsTarget.request().post(Entity.text(svgContent));
    }

    public String retrieveDrawing(final URI drawing) {
        final Response response = getDrawing(drawing);
        assertStatus(response, Response.Status.OK);
        return response.readEntity(String.class);
    }

    private Response getDrawing(final URI drawing) {
        return client.target(drawing).request(MediaType.TEXT_PLAIN_TYPE).get();
    }

    public List<URI> listDrawings() {
        final Response response = getDrawings();
        assertStatus(response, Response.Status.OK);
        return extractDrawingUris(response);
    }

    private Response getDrawings() {
        return drawingsTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
    }

    private List<URI> extractDrawingUris(final Response response) {
        final JsonArray drawings = response.readEntity(JsonArray.class);
        return drawings.getValuesAs(JsonObject.class).stream()
                .map(o -> o.getJsonObject("_links").getString("self"))
                .map(URI::create).collect(Collectors.toList());
    }

    private void assertStatus(final Response response, final Response.Status expectedStatus) {
        assertThat(response.getStatus(), is(expectedStatus.getStatusCode()));
    }

    private void assertDrawingUri(final URI location) {
        assertThat(location, notNullValue());
        final String uriPattern = baseUri + "/drawings/[a-z0-9]+";
        assertTrue("URI " + location + " doesn't match pattern: " + uriPattern, Pattern.matches(uriPattern, location.toString()));
    }

    @Override
    protected void before() throws Throwable {
        client = ClientBuilder.newClient();
        // TODO read from property
//        baseTarget = client.target(UriBuilder.fromUri("http://{host}:8080/javahub-cutter-backend/modules").build(host));
        baseTarget = client.target(URI.create(baseUri));
        drawingsTarget = baseTarget.path("drawings");

        waitForApplicationStartUp();
    }

    private void waitForApplicationStartUp() {
        final long timeout = System.currentTimeMillis() + STARTUP_TIMEOUT * 1000;
        while (baseTarget.request().head().getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            System.out.println("waiting for application startup");
            LockSupport.parkNanos(1_000_000_000 * STARTUP_PING_DELAY);
            if (System.currentTimeMillis() > timeout)
                throw new AssertionError("Application wasn't started before timeout!");
        }
    }

}
