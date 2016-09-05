package net.java.javahub.backend.games.boundary;

import net.java.javahub.backend.games.entity.Game;
import net.java.javahub.backend.games.entity.Round;
import net.java.javahub.backend.images.control.Images;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.time.Instant;
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
        final Game game = retrieveGame(id);

        images.addImage(game, imageInput);
        entityManager.merge(game);
    }

    public byte[] getImageData(final String id) {
        return retrieveGame(id).getImageData();
    }

    public Game getGame(final long id) {
        return entityManager.find(Game.class, id);
    }

    public List<Game> getGames() {
        return entityManager.createNamedQuery(Game.QUERY_FIND_ALL, Game.class).getResultList();
    }

    public void delete(final String id) {
        final Game game = retrieveGame(id);

        // TODO consider rounds

        entityManager.remove(game);
        entityManager.flush();
    }

    public Round startRound(final String gameId, final String deviceId) {
        final Game game = retrieveGame(gameId);

        if (findActiveRound(game, deviceId) != null)
            throw new IllegalStateException("There is an active round at that device");

        final Round round = new Round(deviceId);
        round.setGame(game);
        round.setActive(true);

        return entityManager.merge(round);
    }

    private Round findActiveRound(final Game game, final String deviceId) {
        return entityManager.createNamedQuery(Round.QUERY_FIND_ACTIVE, Round.class)
                .setParameter("game", game)
                .setParameter("deviceId", deviceId)
                .getSingleResult();
    }

    public Round getActiveRound(final String roundId) {
        final Round round = entityManager.find(Round.class, roundId);
        if (round == null || !round.isActive())
            return null;
        return round;
    }

    public void finishRound(final String roundId) {
        final Round round = entityManager.find(Round.class, roundId);
        if (round == null || !round.isActive())
            throw new NoSuchElementException("Could not find active round with id " + roundId);

        round.setActive(false);
        round.setEnded(Instant.now());

        entityManager.merge(round);
        entityManager.flush();
    }

    private Game retrieveGame(final String gameId) {
        final Game game = entityManager.find(Game.class, gameId);

        if (game == null)
            throw new NoSuchElementException("Could not find game with id " + gameId);
        return game;
    }

}
