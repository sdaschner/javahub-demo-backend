package net.java.javahub.backend.games.boundary;

import net.java.javahub.backend.games.entity.Game;
import net.java.javahub.backend.images.control.Images;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class Games {

    @Inject
    Images images;

    @PersistenceContext
    EntityManager entityManager;

    public Game create(final String content) {
        final Game drawing = new Game(content);
        return entityManager.merge(drawing);
    }

    public void addImage(final String id, final InputStream imageInput) {
        final Game game = entityManager.find(Game.class, id);
        if (game == null)
            throw new NoSuchElementException("Could not find game with id " + id);

        images.addImage(game, imageInput);
        entityManager.merge(game);
    }

    public byte[] getImageData(final String id) {
        final Game game = entityManager.find(Game.class, id);
        if (game == null)
            throw new NoSuchElementException("Could not find game with id " + id);

        return game.getImageData();
    }

    public Game getGame(final long id) {
        return entityManager.find(Game.class, id);
    }

    public List<Game> getGames() {
        return entityManager.createNamedQuery(Game.QUERY_FIND_ALL, Game.class).getResultList();
    }

    public void delete(final String id) {
        final Game game = entityManager.find(Game.class, id);
        if (game == null)
            throw new NoSuchElementException("Could not find game with id " + id);

        entityManager.remove(game);
        entityManager.flush();
    }

}
