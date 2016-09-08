package net.java.javahub.backend.games.entity;

import net.java.javahub.backend.images.entity.ImageEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "games")
@NamedQuery(name = Game.QUERY_FIND_ALL, query = "select g from Game g")
public class Game implements ImageEntity {

    public static final String QUERY_FIND_ALL = "Game.findAll";

    @Id
    private String id;

    @Basic(optional = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageData;

    public Game() {
    }

    public Game(final String name) {
        this.name = name;
    }

    @PrePersist
    private void generateId() {
        if (id == null)
            id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(final byte[] imageData) {
        this.imageData = imageData;
    }

}

