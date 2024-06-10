package org.example.backend;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.backend.models.GameHistory;
import org.example.backend.repositories.GameHistoriesRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameHistoriesRepositoryTests {

    @InjectMocks
    private GameHistoriesRepository gameHistoriesRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<GameHistory> query;

    @Test
    // Test case to verify the findByUserId method of GameHistoriesRepository
    public void testFindByUserId() {
        // Arrange
        long userId = 1L;
        List<GameHistory> expectedGameHistories = new ArrayList<>();
        // Mocking the EntityManager and Query behavior
        when(entityManager.createQuery("SELECT gh FROM GameHistory gh WHERE gh.user.id = :userId", GameHistory.class)).thenReturn(query);
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedGameHistories);

        // Act
        List<GameHistory> result = gameHistoriesRepository.findByUserId(userId);

        // Assert
        // Verifying that the result matches the expected game histories
        assertEquals(expectedGameHistories, result);
    }

    @Test
    // Test case to verify the findAll method of GameHistoriesRepository
    public void testFindAll() {
        // Arrange
        List<GameHistory> expectedGameHistories = new ArrayList<>();
        // Mocking the EntityManager and Query behavior
        when(entityManager.createQuery("SELECT gh FROM GameHistory gh", GameHistory.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedGameHistories);

        // Act
        List<GameHistory> result = gameHistoriesRepository.findAll();

        // Assert
        // Verifying that the result matches the expected list of all game histories
        assertEquals(expectedGameHistories, result);
    }
}
