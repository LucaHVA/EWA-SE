package org.example.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.example.backend.models.GameHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("GAMEHISTORIES.JPA")
@Transactional
public class GameHistoriesRepository extends AbstractEntityRepositoryJpa<GameHistory, Long> implements GameHistoryRepository {

    @PersistenceContext
    protected EntityManager entityManager;

    public GameHistoriesRepository() {
        super(GameHistory.class);
    }

    @Override
    public List<GameHistory> findAll() {
        TypedQuery<GameHistory> query =
                this.entityManager.createQuery(
                        "SELECT gh FROM GameHistory gh", GameHistory.class);
        return query.getResultList();
    }

    @Override
    public GameHistory findById(Long id) {
        return entityManager.find(GameHistory.class, id);
    }

    @Override
    public GameHistory save(GameHistory entity) {
        return entityManager.merge(entity);
    }

    @Override
    public GameHistory deleteById(Long id) {
        GameHistory gameHistory = this.findById(id);
        entityManager.remove(gameHistory);

        return gameHistory;
    }

    @Override
    public List<GameHistory> findByQuery(String jpqlName, Object... params) {
        Query query = entityManager.createQuery(jpqlName);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        return query.getResultList();
    }

    @Override
    public List<GameHistory> findByUserId(Long userId) {
        TypedQuery<GameHistory> query = entityManager.createQuery(
                "SELECT gh FROM GameHistory gh WHERE gh.user.id = :userId", GameHistory.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }



    public void deleteAllByUserId(Long userId) {
        Query query = entityManager.createQuery("DELETE FROM GameHistory gh WHERE gh.user.id = :userId");
        query.setParameter("userId", userId);
        query.executeUpdate();
    }
}
