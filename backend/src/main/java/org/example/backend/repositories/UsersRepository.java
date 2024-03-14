package org.example.backend.repositories;

import org.example.backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsersRepository<E> implements JpaRepository {

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(long id) {
        return null;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User deleteById(long id) {
        return null;
    }
}