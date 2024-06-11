package org.example.backend;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.backend.exceptions.ResourceNotFoundException;
import org.example.backend.models.Game;
import org.example.backend.models.Player;
import org.example.backend.models.PlayerKey;
import org.example.backend.models.User;
import org.example.backend.repositories.GamesRepository;
import org.example.backend.repositories.PlayersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class GamesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GamesRepository gamesRepository;

    @MockBean
    private PlayersRepository playersRepository;

    private Game testGame;
    private Player testPlayer;

    @BeforeEach
    void setup() {
        // Test data
        User testHost = new User();
        testGame = new Game("1", 4, 60, 100, Game.Status.open, testHost);
        testPlayer = new Player(1, testGame);
        testPlayer.setGame(testGame);

        // Mock player repo
        PlayerKey playerKey = new PlayerKey(testPlayer.getPlayerNumber(), testGame.getId());
        when(playersRepository.findById(playerKey)).thenReturn(testPlayer);

        // Mock game repo
        when(gamesRepository.findAll()).thenReturn(Collections.singletonList(testGame));
        when(gamesRepository.save(Mockito.any(Game.class))).thenReturn(testGame);
    }

    @Test
    public void testGetAllGames() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/game/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].numberOfPlayers", is(4)))
                .andExpect(jsonPath("$[0].turnDuration", is(60)))
                .andExpect(jsonPath("$[0].pointsToWin", is(100)))
                .andExpect(jsonPath("$[0].status", is("open")));
    }


    @Test
    public void testCreateGame() throws Exception {
        // Arrange
        Game newGame = new Game("2", 2, 30, 50, Game.Status.closed, null);
        when(gamesRepository.save(Mockito.any(Game.class))).thenReturn(newGame);

        // Act & Assert
        mockMvc.perform(post("/game")
                        .contentType("application/json")
                        .content("{\"id\":\"2\",\"numberOfPlayers\":2,\"turnDuration\":30,\"pointsToWin\":50,\"status\":\"closed\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("2")))
                .andExpect(jsonPath("$.numberOfPlayers", is(2)))
                .andExpect(jsonPath("$.turnDuration", is(30)))
                .andExpect(jsonPath("$.pointsToWin", is(50)))
                .andExpect(jsonPath("$.status", is("closed")));
    }

    /**
     * @Author Stephanie
     * @throws Exception
     */
    @Test
    public void testDeletePlayer() throws Exception {
        // Arrange
        String gameId = testGame.getId();
        int playerNumber = testPlayer.getPlayerNumber();

        // Act & Assert
        mockMvc.perform(delete("/game/" + gameId + "/players/" + playerNumber))
                .andExpect(status().isNoContent());
    }

    /**
     * @Author Stephanie
     * @throws Exception
     */
    @Test
    public void testGetPlayerFromGame() throws Exception {
        // Arrange
        String gameId = testPlayer.getGame().getId();
        int playerNumber = testPlayer.getPlayerNumber();

        // Act & Assert
        mockMvc.perform(get("/game/{gameId}/players/{playerNumber}", gameId, playerNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerId.gameId").value(gameId))
                .andExpect(jsonPath("$.playerId.playerNumber").value(playerNumber));
    }

    /**
     * @Author Stephanie
     * @throws Exception
     */
    @Test
    public void getGameById_WhenGameDoesNotExist_ShouldThrowException() throws Exception {
        // Arrange
        String nonExistentGameId = "non-existent-id";
        when(gamesRepository.findById(nonExistentGameId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/game/{id}", nonExistentGameId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Game with ID: " + nonExistentGameId + " not found.", result.getResolvedException().getMessage()));
    }

}