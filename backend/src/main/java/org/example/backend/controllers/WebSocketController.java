package org.example.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpUserRegistry userRegistry;

    // Add method to start game and notify other players
    @GetMapping("/startGame")
    public String startGame() {
        String gameId = "someGeneratedGameId"; // Replace with logic to generate or retrieve a game ID

        // Get the list of all users in the lobby
        userRegistry.getUsers().forEach(user -> {
            // Notify each user individually that the game has started
            messagingTemplate.convertAndSendToUser(user.getName(), "/topic/startGame", gameId);
        });

        return "Game started";
    }

    @GetMapping("/gamePage")
    public String gamePage() {
        // Logic to display the game page
        return "gamePage"; // Assuming "gamePage" is the name of your game page view
    }

}