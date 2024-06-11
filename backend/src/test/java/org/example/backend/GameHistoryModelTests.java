package org.example.backend;
//heeloo
import org.example.backend.models.GameHistory;
import org.example.backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class GameHistoryModelTests {

    // Declaring stupid variables to be used in tests
    private User user;
    private Long id;
    private int placement;
    private LocalTime startTime;
    private LocalTime endTime;

    //runs before each test case
    @BeforeEach
    public void setup() {
        //Arrange: Setting up test data and expected values
        user = new User(1L, "Sufy", "Sufy.noob@example.com", "sufynoob123");
        id = 1L;
        placement = 1;
        startTime = LocalTime.of(10, 0);
        endTime = LocalTime.of(11, 0);
    }

    //This indicates that the method is a test method.
    @Test
    public void testGameHistoryCreation() {
        //Act: Creating a new GameHistory instance with the arranged values
        GameHistory gameHistory = new GameHistory(id, user, placement, startTime, endTime);

        //Assert: Verifying that the GameHistory instance has the expected properties
        //checks if the expected value matches the actual value
        assertEquals(id, gameHistory.getGameHistoryId());
        assertEquals(user, gameHistory.getUser());
        assertEquals(placement, gameHistory.getPlacement());
        assertEquals(startTime, gameHistory.getStartTime());
        assertEquals(endTime, gameHistory.getEndTime());
    }
}
