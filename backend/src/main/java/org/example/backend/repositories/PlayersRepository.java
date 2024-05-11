package org.example.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.example.backend.models.Game;
import org.example.backend.models.Player;
import org.example.backend.models.PlayerKey;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayersRepository extends AbstractEntityRepositoryJpa<Player, PlayerKey> implements PlayerRepository{

    @PersistenceContext
    protected EntityManager entityManager;

    public PlayersRepository() {
        super(Player.class);
    }

    @Override
    public List<Player> findAll() {
        TypedQuery<Player> query =
                this.entityManager.createQuery(
                        "SELECT p FROM Player p", Player.class);
        return query.getResultList();
    }

    @Override
    public List<Player> findByQuery(String jpqlName, Object... params) {
        Query query = entityManager.createNamedQuery(jpqlName);

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }

    @Override
    public Player findById(PlayerKey id) {
        return entityManager.find(Player.class, id);
    }

    @Override
    public Player save(Player entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Player deleteById(PlayerKey id) {
        Player player = this.findById(id);
        entityManager.remove(player);

        return player;
    }

    @Override
    public Player update(Player oldEntity, Player newEntity) {
        return null;
    }

}
