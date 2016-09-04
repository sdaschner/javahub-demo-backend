package net.java.javahub.backend.drawings.boundary;

import net.java.javahub.backend.drawings.entity.Drawing;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class Drawings {

    @PersistenceContext
    EntityManager entityManager;

    public Drawing create(final String content) {
        final Drawing drawing = new Drawing(content);
        return entityManager.merge(drawing);
    }

    public Drawing getDrawing(final long id) {
        return entityManager.find(Drawing.class, id);
    }

    public List<Drawing> getDrawings() {
        return entityManager.createNamedQuery(Drawing.QUERY_FIND_ALL, Drawing.class).getResultList();
    }

}
