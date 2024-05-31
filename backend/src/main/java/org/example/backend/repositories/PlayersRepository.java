package org.example.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.example.backend.models.Player;
import org.example.backend.models.PlayerKey;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PlayersRepository extends AbstractEntityRepositoryJpa<Player, PlayerKey> implements PlayerRepository {

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

    @Transactional
    public List<Player> updatePlayerNumbers(String gameId, Map<Integer, Integer> playerNumberUpdates) {
        // List to hold the original players
        List<Player> originalPlayers = new ArrayList<>();
        // Find each player and add to the list
        for (Integer originalNumber : playerNumberUpdates.keySet()) {
            PlayerKey originalKey = new PlayerKey(originalNumber, gameId);
            Player player = findById(originalKey);
            if (player != null) {
                originalPlayers.add(player);
            }
        }

        // Update the playerNumber for each player in the list
        for (Player player : originalPlayers) {
            Integer newPlayerNumber = playerNumberUpdates.get(player.getPlayerNumber());

            if (newPlayerNumber != null) {
                player.setPlayerNumber(newPlayerNumber);
            }

        }

        entityManager.clear();

        // Merge each updated player with the entityManager
        for (Player player : originalPlayers) {
            entityManager.merge(player);
        }

        // Flush the entityManager after all players have been merged
        entityManager.flush();

        return originalPlayers;
    }

    public void deleteByUserId(Long userId) {
        Query query = entityManager.createQuery("DELETE FROM Player p WHERE p.user.id = :userId");
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

}
