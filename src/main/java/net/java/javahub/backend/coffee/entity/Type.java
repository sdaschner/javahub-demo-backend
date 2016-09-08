package net.java.javahub.backend.coffee.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "coffee_types")
@NamedQuery(name = Type.QUERY_FIND_ALL, query = "select t from net.java.javahub.backend.coffee.entity.Type t")
public class Type {

    public static final String QUERY_FIND_ALL = "Type.findAll";

    @Id
    private String id;

    @Basic(optional = false)
    private String name;

    public Type() {
    }

    public Type(final String name) {
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

}
