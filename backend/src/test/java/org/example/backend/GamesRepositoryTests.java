package org.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.example.backend.models.Game;
import org.example.backend.models.Player;
import org.example.backend.repositories.GamesRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GamesRepositoryTests {

    @Autowired
    private GamesRepository gamesRepository;

    @Test
    public void testFindPlayersByGameId() {
        // Arrange
        String gameId = "BACK04";

        // Act
        List<Player> players = gamesRepository.findPlayersByGameId(gameId);

        // Assert
        assertNotNull(players);
        assertEquals(1, players.size());
        assertEquals(gameId, players.get(0).getGame().getId());
    }

    @Test
    public void testFilteredGames() {
        // Act
        List<Game> filteredGames = gamesRepository.filteredGames();

        // Assert
        assertNotNull(filteredGames);
        assertEquals(4, filteredGames.size());
        assertEquals(Game.Status.open, filteredGames.get(0).getStatus());
    }

    @Test
    public void testSaveGame() {
        // Arrange
        Game game = new Game("2", 2, 30, 50, Game.Status.closed, null);

        // Act
        Game savedGame = gamesRepository.save(game);

        // Assert
        assertNotNull(savedGame);
        assertEquals(game.getId(), savedGame.getId());
        assertEquals(game.getNumberOfPlayers(), savedGame.getNumberOfPlayers());
        assertEquals(game.getTurnDuration(), savedGame.getTurnDuration());
        assertEquals(game.getPointsToWin(), savedGame.getPointsToWin());
        assertEquals(game.getStatus(), savedGame.getStatus());
    }


}