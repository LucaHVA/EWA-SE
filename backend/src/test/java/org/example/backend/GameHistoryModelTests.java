package org.example.backend;

import org.example.backend.models.GameHistory;
import org.example.backend.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class GameHistoryModelTests {

    @Test
    public void testGameHistoryCreation() {
        // Arrange
        User user = new User(1L, "Sufy", "Sufy.noob@example.com", "sufynoob123");
        Long id = 1L;
        int placement = 1;
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        // Act
        GameHistory gameHistory = new GameHistory(id, user, placement, startTime, endTime);

        // Assert
        assertEquals(id, gameHistory.getGameHistoryId());
        assertEquals(user, gameHistory.getUser());
        assertEquals(placement, gameHistory.getPlacement());
        assertEquals(startTime, gameHistory.getStartTime());
        assertEquals(endTime, gameHistory.getEndTime());
    }
}
