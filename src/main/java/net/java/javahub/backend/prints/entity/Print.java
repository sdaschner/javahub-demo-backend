package net.java.javahub.backend.prints.entity;

import net.java.javahub.backend.images.entity.ImageEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "prints")
@NamedQuery(name = Print.QUERY_FIND_ALL, query = "select p from Print p")
public class Print implements ImageEntity {

    public static final String QUERY_FIND_ALL = "Print.findAll";

    @Id
    private String id;

    @Basic(optional = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageData;

    public Print() {
    }

    public Print(final String name) {
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

    @Override
    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public void setImageData(final byte[] imageData) {
        this.imageData = imageData;
    }

}
