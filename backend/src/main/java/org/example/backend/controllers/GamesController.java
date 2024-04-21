package org.example.backend.controllers;

import org.example.backend.models.Game;
import org.example.backend.models.User;
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
}
