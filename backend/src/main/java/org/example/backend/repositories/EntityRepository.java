package org.example.backend.repositories;

import org.example.backend.models.Identifiable;

import java.util.List;

public interface EntityRepository<E extends Identifiable<T>, T> {
    List<E> findAll();
    List<E> findByQuery(String jpqlName, Object... params);

    E findById(T id);

    E save(E entity);

    E deleteById(T id);

    E update(E oldEntity, E newEntity);
}
