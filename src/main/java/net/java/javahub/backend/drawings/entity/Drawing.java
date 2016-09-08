package net.java.javahub.backend.drawings.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "drawings")
@NamedQuery(name = Drawing.QUERY_FIND_ALL, query = "select d from Drawing d")
public class Drawing {

    public static final String QUERY_FIND_ALL = "Drawing.findAll";

    @Id
    private String id;

    @Lob
    @Basic(optional = false)
    private String pathContent;

    @Basic(optional = false)
    private Instant created;

    public Drawing() {
    }

    public Drawing(final String pathContent) {
        this.pathContent = pathContent;
    }

    @PrePersist
    private void generateInformation() {
        if (id == null)
            id = UUID.randomUUID().toString();
        if (created == null)
            created = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getPathContent() {
        return pathContent;
    }

    public void setPathContent(final String pathContent) {
        this.pathContent = pathContent;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(final Instant created) {
        this.created = created;
    }

}
