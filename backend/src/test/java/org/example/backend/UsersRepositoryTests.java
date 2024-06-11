package org.example.backend;

import org.example.backend.models.User;
import org.example.backend.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsersRepositoryTests {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    public void testFindAllUsers() {
        // Act
        List<User> users = usersRepository.findAll();

        // Assert
        assertNotNull(users);
        assertFalse(users.isEmpty(), "User list should not be empty");
    }
    @Test
    public void testSaveUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");

        // Act
        User savedUser = usersRepository.save(user);

        // Assert
        assertNotNull(savedUser, "Saved user should not be null");
        assertEquals(user.getUsername(), savedUser.getUsername(), "Usernames should match");
        assertEquals(user.getPassword(), savedUser.getPassword(), "Passwords should match");
        assertEquals(user.getEmail(), savedUser.getEmail(), "Emails should match");
    }

    @Test
    public void testFindByUsername_Success() {
        // Arrange
        String username = "newUser";
        User userToSave = new User();
        userToSave.setUsername(username);
        userToSave.setPassword("newPassword");
        userToSave.setEmail("testuser@example.com");

        // Act
        // Save the user
        usersRepository.save(userToSave);

        // Try to find the saved user
        User foundUser = usersRepository.findByUsername(username);

        // Assert
        assertNotNull(foundUser, "User should not be null");
        assertEquals(username, foundUser.getUsername(), "Usernames should match");
    }


    @Test
    public void testFindByUsername_NoResult() {
        // Arrange
        String username = "nonexistentuser";

        // Act
        User user = usersRepository.findByUsername(username);

        // Assert
        assertNull(user, "User should be null when no result is found");
    }
}

