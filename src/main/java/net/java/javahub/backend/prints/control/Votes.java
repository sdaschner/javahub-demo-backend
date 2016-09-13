package net.java.javahub.backend.prints.control;

import net.java.javahub.backend.prints.entity.Print;
import net.java.javahub.backend.prints.entity.Vote;

import javax.annotation.PostConstruct;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Votes {

    private static final String VOTES_BACKEND_URL = "https://otnbackend.gluonhq.com/backend/rest/javahub/votes";

    @PersistenceContext
    EntityManager entityManager;

    private WebTarget votesTarget;

    @PostConstruct
    public void initTarget() {
        votesTarget = ClientBuilder.newClient().target(VOTES_BACKEND_URL);
        // TODO set vendor specific timeout for JAX-RS client impl
    }


    public List<Vote> getVotes() {
        final JsonArray votes = votesTarget.request(MediaType.APPLICATION_JSON_TYPE).get(JsonArray.class);

        final List<Print> prints = entityManager.createNamedQuery(Print.QUERY_FIND_ALL, Print.class).getResultList();

        return votes.getValuesAs(JsonObject.class).stream()
                .map(v -> new Vote(findPrint(v.getString("id"), prints), v.getInt("votes", 0)))
                .collect(Collectors.toList());
    }

    public void resetVotes() {
        final Response response = votesTarget.request().delete();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL)
            throw new IllegalStateException("Status of votes reset was not successful: backend responded " + response.getStatus());
    }

    private static Print findPrint(final String id, final List<Print> prints) {
        return prints.stream().filter(p -> p.getId().equals(id)).findAny().orElseThrow(() -> new NoSuchElementException("Could not find print with id " + id));
    }

}
