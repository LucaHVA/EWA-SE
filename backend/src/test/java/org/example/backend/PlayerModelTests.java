package org.example.backend;

import org.example.backend.models.Game;
import org.example.backend.models.Player;
import org.example.backend.models.PlayerKey;
import org.example.backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerModelTests {

    private Game game;
    private User user;
    private PlayerKey playerKey;
    private Player player;

    @BeforeEach
    void setUp() {
        game = new Game("1", 4, 60, 100, Game.Status.open, null);
        user = new User();
        user.setId(1L);
        playerKey = new PlayerKey(1, game.getId());
        player = new Player(playerKey, user, "red");
    }

    /**
     * @Author Stephanie
     */
    @Test
    public void testPlayerCreation() {
        // Assert
        assertEquals(playerKey, player.getId());
        assertEquals(user, player.getUser());
        assertEquals("red", player.getPlayerColor());
    }

    /**
     * @Author Stephanie
     */
    @Test
    public void testPlayerToString() {
        // Arrange
        player.setGame(game);

        // Act
        String playerString = player.toString();

        // Assert
        String expectedString = "Player{" +
                "playerId=game id: " + playerKey.getGameId() + " | player number: " + playerKey.getPlayerNumber() +
                ", game=1" +
                ", user=1" +
                ", playerColor='red'" +
                '}';
        assertEquals(expectedString, playerString);
    }

    /**
     * @Author Stephanie
     */
    @Test
    void testSetPlayerGameInfo() {
        // Arrange
        int amountOfPoints = 10;
        int longestRoad = 5;
        int settlementPoints = 3;
        int knightsUsed = 2;

        // Act
        player.setPlayerGameInfo(amountOfPoints, longestRoad, settlementPoints, knightsUsed);

        // Assert
        assertEquals(amountOfPoints, player.getAmountOfPoints());
        assertEquals(longestRoad, player.getLongestRoad());
        assertEquals(settlementPoints, player.getSettlementPoints());
        assertEquals(knightsUsed, player.getKnightsUsed());
    }
}
