package org.example.backend.controllers;

import org.example.backend.exceptions.ResourceNotFoundException;
import org.example.backend.models.Game;
import org.example.backend.repositories.GamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
