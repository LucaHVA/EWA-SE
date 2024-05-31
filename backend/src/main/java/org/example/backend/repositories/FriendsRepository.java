package org.example.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.example.backend.models.Friend;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FRIENDS.JPA")
@Transactional
public class FriendsRepository extends AbstractEntityRepositoryJpa<Friend, Long> {
    @PersistenceContext
    protected EntityManager entityManager;

    public FriendsRepository() {
        super(Friend.class);
    }

    @Override
    public List<Friend> findAll() {
        TypedQuery<Friend> query =
                this.entityManager.createQuery(
                        "SELECT s FROM Friend s", Friend.class);
        return query.getResultList();
    }

    @Override
    public Friend findById(Long id) {
        return entityManager.find(Friend.class, id);
    }

    @Override
    public Friend save(Friend friend) {
        return entityManager.merge(friend);
    }

    @Override
    public Friend deleteById(Long id) {
        Friend friend = this.findById(id);
        entityManager.remove(friend);

        return friend;
    }

    @Override
    public List<Friend> findByQuery(String jpqlName, Object... params) {
        Query query = entityManager.createNamedQuery(jpqlName);

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }
}
