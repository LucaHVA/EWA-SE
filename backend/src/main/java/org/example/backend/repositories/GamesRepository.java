package org.example.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.example.backend.models.Game;
import org.example.backend.models.Player;
import org.example.backend.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GamesRepository extends AbstractEntityRepositoryJpa<Game, String> implements GameRepository {

    @PersistenceContext
    protected EntityManager entityManager;

    public GamesRepository() {
        super(Game.class);
    }

    @Override
    public List<Game> findAll() {
        TypedQuery<Game> query =
                this.entityManager.createQuery(
                        "SELECT g FROM Game g", Game.class);
        return query.getResultList();
    }

    @Override
    public List<Game> findByQuery(String jpqlName, Object... params) {
        Query query = entityManager.createNamedQuery(jpqlName);

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }

    @Override
    public Game save(Game entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Game update(Game oldEntity, Game newEntity) {
        //todo update game
        return entityManager.merge(newEntity);
    }

    @Override
    public Game deleteById(String id) {
        Game game = this.findById(id);
        entityManager.remove(game);

        return game;
    }

    @Override
    public Game findById(String id) {
        return entityManager.find(Game.class, id);
    }

    public List<Player> findPlayersByGameId(String gameId) {
        TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.game.gameId = :gameId", Player.class);

        query.setParameter("gameId", gameId);

        return query.getResultList();
    }
}
