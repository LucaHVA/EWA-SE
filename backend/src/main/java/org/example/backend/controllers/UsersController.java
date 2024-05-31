package org.example.backend.controllers;

import jakarta.transaction.Transactional;
import org.example.backend.enums.FriendshipStatus;
import org.example.backend.models.Friend;
import org.example.backend.models.GameHistory;
import org.example.backend.repositories.FriendsRepository;
import org.example.backend.repositories.GameHistoriesRepository;
import org.example.backend.security.APIConfig;
import org.example.backend.exceptions.ResourceNotFoundException;
import org.example.backend.models.User;
import org.example.backend.repositories.UsersRepository;
import org.example.backend.security.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Luca Rijkbost & Armando Labega
 */

@RestController
@Transactional
@RequestMapping("/users")
public class UsersController {

    @Qualifier("USERS.JPA")
    @Autowired
    private UsersRepository usersRepository;

    @Qualifier("GAMEHISTORIES.JPA")
    @Autowired
    private GameHistoriesRepository gameHistoriesRepository;

    @Qualifier("FRIENDS.JPA")
    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private APIConfig apiConfig;

    @GetMapping("/test")
    public List<User> getMockUsers(){
        return List.of(
                new User(10L,"Armando","Cringe@ball.nl", "fout"),
                new User(11L,"Ballmando","yep@huh.be", "goed")
        );
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        // Call the findAll method from the repository and return the list of all users
        return usersRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User user = usersRepository.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        usersRepository.save(user);

        // Build URI for new scooter and return it with a 201 Created status code
        URI componentBuilder = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(componentBuilder).body(user);
    }

    @PostMapping(path="/login", produces = "application/json")
    public ResponseEntity<User> loginUser(@RequestBody User loginUser) {
        // Retrieve the user from the repository by username
        User user = usersRepository.findByUsername(loginUser.getUsername());

        // Check if the user exists and if the password matches
        if (user != null && user.getPassword().equals(loginUser.getPassword())) {

            JWToken jwToken= new JWToken(user.getUsername(), user.getId(), "user");

            String tokenString = jwToken.encode(apiConfig.getIssuer(), apiConfig.getPassphrase(), apiConfig.getTokenDurationOfValidity());

            return ResponseEntity.accepted().header(HttpHeaders.AUTHORIZATION,"Bearer " + tokenString)
                    .body(user);// Return the user with a 200 OK status code if login is successful

        } else {
            throw new ResourceNotFoundException("Invalid username or password."); // Throw exception if login fails
        }
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable long id, @RequestBody User user) {
        if (id != user.getId()) {
            throw new ResourceNotFoundException("user-id = " + user.getId() + " does not match path parameter = " + id);
        }
        return usersRepository.save(user);
    }

    @GetMapping("/{userId}/games")
    public ResponseEntity<List<GameHistory>> getUserGameHistory(@PathVariable long userId) {
        // Check if the user exists
        User user = usersRepository.findById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found.");
        }

        // Fetch game history for the user using GameHistoriesRepository
        List<GameHistory> gameHistory = gameHistoriesRepository.findByUserId(userId);
        return ResponseEntity.ok(gameHistory);
    }

    @PostMapping("/{userId}/games")
    public ResponseEntity<GameHistory> addGameToUserGameHistory(@PathVariable long userId, @RequestBody GameHistory gameHistory) {
        // Check if the user exists
        User user = usersRepository.findById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found.");
        }

        // Set the user for the game history
        gameHistory.setUser(user);

        // Save the game history using GameHistoriesRepository
        GameHistory savedGameHistory = gameHistoriesRepository.save(gameHistory);
        return ResponseEntity.ok(savedGameHistory);
    }

    @PostMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Friend> sendFriendRequest(@PathVariable long id, @PathVariable long friendId) {
        User user = usersRepository.findById(id);
        User friend = usersRepository.findById(friendId);

        if (user == null || friend == null) {
            throw new ResourceNotFoundException("User or friend not found.");
        }

        Friend friendRequest = new Friend(0L, user, friend, FriendshipStatus.PENDING);
        friendsRepository.save(friendRequest);

        return ResponseEntity.ok(friendRequest);
    }

    @PutMapping("/{id}/friends/{friendId}/accept")
    public ResponseEntity<Friend> acceptFriendRequest(@PathVariable long id, @PathVariable long friendId) {
        List<Friend> friendRequests = friendsRepository.findByQuery("Get_received_friend_request", id);

        for (Friend friendRequest : friendRequests) {
            if (friendRequest.getUser().getId().equals(friendId)) {
                friendRequest.setStatus(FriendshipStatus.ACCEPTED);
                friendsRepository.save(friendRequest);
                return ResponseEntity.ok(friendRequest);
            }
        }

        throw new ResourceNotFoundException("Friend request not found.");
    }

    @DeleteMapping("/{id}/friends/{friendId}/decline")
    public ResponseEntity<Void> declineFriendRequest(@PathVariable long id, @PathVariable long friendId) {
        List<Friend> friends = friendsRepository.findByQuery("Find_friends_by_userId_or_friendId", id);

        for (Friend friend : friends) {
            if (friend.getUser().getId() == friendId || friend.getFriend().getId() == friendId) {
                friendsRepository.deleteById(friend.getId());
                return ResponseEntity.noContent().build();
            }
        }

        throw new ResourceNotFoundException("Friend request not found.");
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable long id) {
        List<Friend> friends = friendsRepository.findByQuery("Get_accepted_friends", id);

        List<User> friendList = new ArrayList<>();
        for (Friend friend : friends) {
            if (friend.getUser().getId() == id) {
                friendList.add(friend.getFriend());
            } else if (friend.getFriend().getId() == id) {
                friendList.add(friend.getUser());
            }
        }

        return ResponseEntity.ok(friendList);
    }

    @GetMapping("/{id}/friendRequests")
    public ResponseEntity<List<User>> getFriendRequests(@PathVariable long id) {
        List<Friend> friendRequests = friendsRepository.findByQuery("Get_received_friend_request", id);

        // Create a list to hold the users
        List<User> users = new ArrayList<>();

        // Iterate over the friend requests and collect the users
        for (Friend friendRequest : friendRequests) {
            users.add(friendRequest.getUser());
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/sentFriendRequests")
    public ResponseEntity<List<User>> getSentFriendRequests(@PathVariable long id) {
        List<Friend> friendRequests = friendsRepository.findByQuery("Get_sent_friend_request", id);

        // Create a list to hold the users
        List<User> users = new ArrayList<>();

        // Iterate over the friend requests and collect the users
        for (Friend friendRequest : friendRequests) {
            users.add(friendRequest.getFriend());
        }

        return ResponseEntity.ok(users);
    }
}
