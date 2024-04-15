package org.example.backend.controllers;

import org.example.backend.models.Game;
import org.example.backend.repositories.GamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
