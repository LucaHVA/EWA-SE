package org.example.backend;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.backend.models.Game;
import org.example.backend.models.User;
import org.example.backend.repositories.GamesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    private Game testGame;

    @BeforeEach
    void setup() {
        User testHost = new User();
        testGame = new Game("1", 4, 60, 100, Game.Status.open, testHost);

        Mockito.when(gamesRepository.findAll()).thenReturn(Collections.singletonList(testGame));
        Mockito.when(gamesRepository.save(Mockito.any(Game.class))).thenReturn(testGame);
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
        Mockito.when(gamesRepository.save(Mockito.any(Game.class))).thenReturn(newGame);

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
}