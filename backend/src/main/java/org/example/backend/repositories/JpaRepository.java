package org.example.backend.repositories;

import org.example.backend.models.User;

import java.util.List;

public interface JpaRepository<T> {
    List<User> findAll();

    User findById(long id);

    User save(User user);

    User deleteById(long id);
}
