package org.example.backend.controllers;

import jakarta.transaction.Transactional;
import org.example.backend.exceptions.ResourceNotFoundException;
import org.example.backend.models.Game;
import org.example.backend.models.Player;
import org.example.backend.models.PlayerKey;
import org.example.backend.models.User;
import org.example.backend.repositories.GamesRepository;
import org.example.backend.repositories.PlayersRepository;
import org.example.backend.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GamesController {

    @Autowired
    private GamesRepository gamesRepository;

    @Autowired
    private PlayersRepository playersRepository;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/all")
    public List<Game> getAllGames() {
        return gamesRepository.findAll();
    }

    @GetMapping("/filter")
    public List<Game> getOpenGames() {
        return gamesRepository.filteredGames();
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        gamesRepository.save(game);

        // Build URI for new scooter and return it with a 201 Created status code
        URI componentBuilder = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(game.getId())
                .toUri();

        return ResponseEntity.created(componentBuilder).body(game);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        //Find game
        Game game = gamesRepository.findById(id);

        //Check if game exist
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            throw new ResourceNotFoundException("Game with ID: " + id + " not found.");
        }
    }

    @PutMapping("/{id}")
    public Game updateGame(@PathVariable String id, @RequestBody Game game) {
        if (id != game.getId()) {
            throw new ResourceNotFoundException("Game-id = " + game.getId() + " does not match path parameter = " + id);
        }
        return gamesRepository.save(game);
    }

    @GetMapping(path = "/{id}/players", produces = "application/json")
    public ResponseEntity<List<Player>> getPlayersByGameId(@PathVariable String id) {
        List<Player> players = gamesRepository.findPlayersByGameId(id);

        if (players.isEmpty()) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(players);
    }

    @Transactional
    @PostMapping("/{id}/players")
    public ResponseEntity<Player> addPlayerToGame(@PathVariable String id, @RequestBody Player player) {
        // Find relationship entities
        Game game = gamesRepository.findById(id);
        User user = usersRepository.findById(player.getUser().getId());

        // Handle no game or user instances found
        if (game == null) {
            throw new ResourceNotFoundException("No game found with id " + id);
        } else if (user == null) {
            throw new ResourceNotFoundException("No user found with id " + player.getUser().getId());
        }

        // Set relationships
        player.setUser(user);
        player.setGame(game);

        // Save to repo
        playersRepository.save(player);

        return ResponseEntity.ok(player);
    }

    @PutMapping("/{gameId}/players/{playerNumber}")
    public ResponseEntity<Player> updatePlayer(@PathVariable String gameId, @PathVariable int playerNumber, @RequestBody Player updatedPlayer) {
        // Find the existing player using the composite key
        PlayerKey playerKey = new PlayerKey(playerNumber, gameId);
        Player existingPlayer = playersRepository.findById(playerKey);

        // Handle no player found
        if (existingPlayer == null) {
            throw new ResourceNotFoundException("No player found with id " + playerKey);
        }

        // Call the update method from the repository
        Player updatedEntity = playersRepository.update(existingPlayer, updatedPlayer);

        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{gameId}/players/{playerNumber}")
    public ResponseEntity<?> deletePlayer(@PathVariable String gameId, @PathVariable int playerNumber) {
        // Find the existing player using the composite key
        PlayerKey playerKey = new PlayerKey(playerNumber, gameId);
        Player existingPlayer = playersRepository.findById(playerKey);

        // Handle no player found
        if (existingPlayer == null) {
            throw new ResourceNotFoundException("No player found with id " + playerKey);
        }

        // Call the delete method from the repository
        playersRepository.deleteById(playerKey);

        // Return a response entity with no content to indicate successful deletion
        return ResponseEntity.noContent().build();
    }

}
