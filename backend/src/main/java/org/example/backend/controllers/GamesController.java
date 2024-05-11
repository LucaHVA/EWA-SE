package org.example.backend.controllers;

import org.example.backend.exceptions.ResourceNotFoundException;
import org.example.backend.models.Game;
import org.example.backend.repositories.GamesRepository;
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

    @GetMapping("/all")
    public List<Game> getAllGames(){
        return gamesRepository.findAll();
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

}
