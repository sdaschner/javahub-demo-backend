package net.java.javahub.cutter.backend.drawings.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "drawings")
@NamedQuery(name = Drawing.QUERY_FIND_ALL, query = "select d from Drawing d")
public class Drawing {

    public static final String QUERY_FIND_ALL = "Drawing.findAll";

    @Id
    @GeneratedValue
    private long id;

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
    private void updateCreated() {
        created = Instant.now();
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
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
