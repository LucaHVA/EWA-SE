package org.example.backend;

import org.example.backend.enums.FriendshipStatus;
import org.example.backend.models.Friend;
import org.example.backend.models.User;
import org.example.backend.repositories.FriendsRepository;
import org.example.backend.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FriendsRepositoryTests {
    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private UsersRepository usersRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@example.com");
        user1 = usersRepository.save(user1);

        user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setEmail("user2@example.com");
        user2 = usersRepository.save(user2);
    }

    @Test
    public void testSaveFriend() {
        // Arrange
        Friend friend = new Friend();
        friend.setUser(user1);
        friend.setFriend(user2);
        friend.setStatus(FriendshipStatus.PENDING);

        // Act
        Friend savedFriend = friendsRepository.save(friend);

        // Assert
        assertNotNull(savedFriend, "Saved friend should not be null");
        assertEquals(FriendshipStatus.PENDING, savedFriend.getStatus(), "Statuses should match");
        assertEquals(user1.getId(), savedFriend.getUser().getId(), "User IDs should match");
        assertEquals(user2.getId(), savedFriend.getFriend().getId(), "Friend IDs should match");
    }

    @Test
    public void testFindById() {
        // Arrange
        Friend friend = new Friend();
        friend.setUser(user1);
        friend.setFriend(user2);
        friend.setStatus(FriendshipStatus.PENDING);
        friend = friendsRepository.save(friend);

        // Act
        Friend foundFriend = friendsRepository.findById(friend.getId());

        // Assert
        assertNotNull(foundFriend, "Friend should not be null");
        assertEquals(friend.getId(), foundFriend.getId(), "Friend IDs should match");
    }

    @Test
    public void testFindByQuery() {
        // Arrange
        Friend friend = new Friend();
        friend.setUser(user1);
        friend.setFriend(user2);
        friend.setStatus(FriendshipStatus.PENDING);
        friendsRepository.save(friend);

        // Act
        List<Friend> pendingRequests = friendsRepository.findByQuery("Get_sent_friend_request", user1.getId());

        // Assert
        assertNotNull(pendingRequests, "Pending requests list should not be null");
        assertFalse(pendingRequests.isEmpty(), "Pending requests list should not be empty");
        assertEquals(FriendshipStatus.PENDING, pendingRequests.get(0).getStatus(), "Statuses should match");
    }
}
