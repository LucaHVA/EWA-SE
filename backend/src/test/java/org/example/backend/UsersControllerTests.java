package org.example.backend;


import org.example.backend.controllers.UsersController;
import org.example.backend.exceptions.ResourceNotFoundException;
import org.example.backend.models.GameHistory;
import org.example.backend.models.User;
import org.example.backend.repositories.GameHistoriesRepository;
import org.example.backend.repositories.UsersRepository;
import org.example.backend.security.APIConfig;
import org.example.backend.security.JWToken;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
//heeloo
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UsersControllerTests {

    @InjectMocks
    private UsersController usersController;

    @Mock
    private GameHistoriesRepository gameHistoriesRepository;

    @Mock
    private UsersRepository usersRepository;

    @Test
// Test case to verify the getUserGameHistory method of UsersController
    public void testGetUserGameHistory() {
        // Arrange
        long userId = 1L;
        List<GameHistory> expectedGameHistory = new ArrayList<>();
        // Mocking the behavior of UsersRepository
        User user = new User();
        when(usersRepository.findById(userId)).thenReturn(user);
        // Mocking the behavior of GameHistoriesRepository
        when(gameHistoriesRepository.findByUserId(userId)).thenReturn(expectedGameHistory);

        // Act
        ResponseEntity<List<GameHistory>> response = usersController.getUserGameHistory(userId);

        // Assert
        // Verifying that the response body contains the expected game history
        assertEquals(expectedGameHistory, response.getBody());
    }

    @Test
    // Test case to verify the addGameToUserGameHistory method of UsersController
    public void testAddGameToUserGameHistory() {
        // Arrange
        long userId = 1L;
        GameHistory gameHistory = new GameHistory();
        // Mocking the behavior of UsersRepository and GameHistoriesRepository
        when(usersRepository.findById(userId)).thenReturn(new User());
        when(gameHistoriesRepository.save(gameHistory)).thenReturn(gameHistory);

        // Act
        ResponseEntity<GameHistory> response = usersController.addGameToUserGameHistory(userId, gameHistory);

        // Assert
        // Verifying that the response body contains the added game history
        assertEquals(gameHistory, response.getBody());
    }
    @Test
    public void testRegisterUser_Success() {
        // Arrange
        User newUser = new User();
        newUser.setId(1L);
        newUser.setUsername("testuser");
        newUser.setPassword("password");
        newUser.setEmail("testuser@example.com");

        when(usersRepository.save(newUser)).thenReturn(newUser);

        // Act
        ResponseEntity<User> response = usersController.createUser(newUser);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(newUser, response.getBody());
    }
    @Test
    public void testLoginUser_Failure() {
        // Arrange
        User loginUser = new User();
        loginUser.setUsername("testuser");
        loginUser.setPassword("wrongpassword");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");
        existingUser.setPassword("password");

        when(usersRepository.findByUsername("testuser")).thenReturn(existingUser);

        // Act and Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            usersController.loginUser(loginUser);
        });

        assertEquals("Invalid username or password.", thrown.getMessage());
    }

}
