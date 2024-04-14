package org.example.backend.repositories;

import org.example.backend.models.User;

public interface UserRepository extends EntityRepository<User, Long> {
    User findByUsername(String username);
}
