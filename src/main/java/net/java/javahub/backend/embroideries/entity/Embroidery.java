package net.java.javahub.backend.embroideries.entity;

import net.java.javahub.backend.images.entity.ImageEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "embroideries")
@NamedQuery(name = Embroidery.QUERY_FIND_ALL, query = "select e from Embroidery e")
public class Embroidery implements ImageEntity {

    public static final String QUERY_FIND_ALL = "Embroidery.findAll";

    @Id
    private String id;

    @Basic(optional = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageData;

    public Embroidery() {
    }

    public Embroidery(final String name) {
        this.name = name;
    }

    @PrePersist
    private void generateId() {
        if (id != null)
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
