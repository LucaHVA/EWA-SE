package org.example.backend.repositories;

import org.example.backend.models.GameHistory;

import java.util.List;

public interface GameHistoryRepository extends EntityRepository<GameHistory, Long>{
    List<GameHistory> findByUserId(Long userId);
}
