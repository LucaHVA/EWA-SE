package org.example.backend.repositories;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.example.backend.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("USERS.JPA")
@Transactional
public class UsersRepository extends AbstractEntityRepositoryJpa<User> {
    @PersistenceContext
    protected EntityManager entityManager;

    public UsersRepository() {
        super(User.class);
    }

    @Override
    public List<User> findAll() {
        TypedQuery<User> query =
                this.entityManager.createQuery(
                        "SELECT t FROM User t", User.class);
        return query.getResultList();
    }

    @Override
    public User findById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User save(User entity) {
        return entityManager.merge(entity);
    }

    @Override
    public User deleteById(long id) {
        User user = this.findById(id);
        entityManager.remove(user);

        return user;
    }

    @Override
    public List<User> findByQuery(String jpqlName, Object... params) {
        Query query = entityManager.createNamedQuery(jpqlName);

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}