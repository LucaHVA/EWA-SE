package org.example.backend;

import jakarta.transaction.Transactional;
import org.example.backend.models.Game;
import org.example.backend.models.User;
import org.example.backend.repositories.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Qualifier("USERS.JPA")
    @Autowired
    private EntityRepository<User, Long> userRepository;
    @Autowired
    private EntityRepository<Game, String> gameRepository;

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("Running CommandLine Startup");
        this.createInitialUsers();
        this.createInitialGames();
    }

    private void createInitialUsers() {
        List<User> users = this.userRepository.findAll();

        users.add(this.userRepository.save(new User(0L, "Armando2", "Cringe@ball.nl", "fout")));
        users.add(this.userRepository.save(new User(0L, "Ballmando2", "yep@huh.be", "goed")));
    }

    private void createInitialGames(){
        List<Game> games = this.gameRepository.findAll();
        games.add(this.gameRepository.save(new Game("BACK03", 3, 3,3)));
        games.add(this.gameRepository.save(new Game("BACK04", 4, 4, 4)));
    }
}
