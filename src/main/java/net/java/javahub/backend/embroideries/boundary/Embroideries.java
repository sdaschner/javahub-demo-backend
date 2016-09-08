package net.java.javahub.backend.embroideries.boundary;

import net.java.javahub.backend.embroideries.entity.Embroidery;
import net.java.javahub.backend.images.control.Images;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class Embroideries {

    @Inject
    Images images;

    @PersistenceContext
    EntityManager entityManager;

    public Embroidery create(final String name) {
        final Embroidery embroidery = new Embroidery(name);
        return entityManager.merge(embroidery);
    }

    public void addImage(final String id, final InputStream imageInput) {
        final Embroidery embroidery = entityManager.find(Embroidery.class, id);
        if (embroidery == null)
            throw new NoSuchElementException("Could not find embroidery with id " + id);

        images.addImage(embroidery, imageInput);
        entityManager.merge(embroidery);
    }

    public Embroidery getEmbroidery(final String id) {
        return entityManager.find(Embroidery.class, id);
    }

    public byte[] getImageData(final String id) {
        final Embroidery embroidery = entityManager.find(Embroidery.class, id);
        if (embroidery == null)
            throw new NoSuchElementException("Could not find embroidery with id " + id);

        return embroidery.getImageData();
    }

    public List<Embroidery> getEmbroideries() {
        return entityManager.createNamedQuery(Embroidery.QUERY_FIND_ALL, Embroidery.class).getResultList();
    }

    public void delete(final String id) {
        final Embroidery embroidery = entityManager.find(Embroidery.class, id);
        if (embroidery == null)
            throw new NoSuchElementException("Could not find embroidery with id " + id);

        entityManager.remove(embroidery);
        entityManager.flush();
    }

}
