package net.java.javahub.backend.prints.boundary;


import net.java.javahub.backend.images.control.Images;
import net.java.javahub.backend.prints.entity.Print;
import net.java.javahub.backend.prints.entity.Vote;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class Prints {

    @Inject
    Images images;

    @PersistenceContext
    EntityManager entityManager;
    private List<Vote> votes;

    public Print create(final String content) {
        final Print print = new Print(content);
        return entityManager.merge(print);
    }

    public void addImage(final String id, final InputStream imageInput) {
        final Print print = entityManager.find(Print.class, id);
        if (print == null)
            throw new NoSuchElementException("Could not find print with id " + id);

        images.addImage(print, imageInput);
        entityManager.merge(print);
    }

    public Print getPrint(final long id) {
        return entityManager.find(Print.class, id);
    }

    public byte[] getImageData(final String id) {
        final Print print = entityManager.find(Print.class, id);
        if (print == null)
            throw new NoSuchElementException("Could not find print with id " + id);

        return print.getImageData();
    }

    public List<Print> getPrints() {
        return entityManager.createNamedQuery(Print.QUERY_FIND_ALL, Print.class).getResultList();
    }

    public Print getPrintInProgress() {
        // TODO
        return null;
    }

    public List<Vote> getVotes() {
        // TODO
        return Collections.emptyList();
    }

    public void resetVotes() {
        // TODO
    }

}
