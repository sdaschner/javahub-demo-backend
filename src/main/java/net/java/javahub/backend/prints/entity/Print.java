package net.java.javahub.backend.prints.entity;

import net.java.javahub.backend.images.entity.ImageEntity;

import javax.persistence.*;

@Entity
@Table(name = "prints")
@NamedQuery(name = Print.QUERY_FIND_ALL, query = "select p from Print p")
public class Print implements ImageEntity {

    public static final String QUERY_FIND_ALL = "Print.findAll";

    @Id
    @GeneratedValue
    private long id;

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

    public long getId() {
        return id;
    }

    public void setId(final long id) {
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
