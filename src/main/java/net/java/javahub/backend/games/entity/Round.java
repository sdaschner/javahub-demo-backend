package net.java.javahub.backend.games.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "game_rounds")
@NamedQueries(value = {
        @NamedQuery(name = Round.QUERY_FIND_ALL, query = "select r from Round r"),
        @NamedQuery(name = Round.QUERY_FIND_ACTIVE, query = "select r from Round r where game = :game and deviceId = :deviceId and active = true")
})
public class Round {

    public static final String QUERY_FIND_ALL = "Round.findAll";
    public static final String QUERY_FIND_ACTIVE = "Round.findByGameDevice";

    @Id
    private String id;

    @ManyToOne(optional = false)
    private Game game;

    @Basic(optional = false)
    private String deviceId;

    private boolean active;

    private Instant started;
    private Instant ended;

    public Round() {
    }

    public Round(final String deviceId) {
        this.deviceId = deviceId;
    }

    @PrePersist
    private void generateInformation() {
        if (id != null)
            id = UUID.randomUUID().toString();
        if (started != null)
            started = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(final Game game) {
        this.game = game;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Instant getStarted() {
        return started;
    }

    public void setStarted(final Instant started) {
        this.started = started;
    }

    public Instant getEnded() {
        return ended;
    }

    public void setEnded(final Instant ended) {
        this.ended = ended;
    }

}
