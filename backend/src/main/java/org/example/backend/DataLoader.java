package org.example.backend;

import jakarta.transaction.Transactional;
import org.example.backend.models.Game;
import org.example.backend.models.Player;
import org.example.backend.models.PlayerKey;
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
    @Autowired
    private EntityRepository<Player, PlayerKey> playerRepository;

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("Running CommandLine Startup");
        this.createInitialUsers();
        this.createInitialGameData();
        this.createInitialPlayers();
    }

    private void createInitialPlayers() {
        // Assuming you have already created some games and users
        List<Game> games = this.gameRepository.findAll();
        List<User> users = this.userRepository.findAll();


        // Check if games and users are not empty before proceeding
        if (!games.isEmpty() && !users.isEmpty()) {
            Game game1 = games.get(0); // Get the first game

            // Create a new Player instance
            Player player1 = new Player(1, game1.getId());
            player1.setGame(game1);   // Set the game

            // Save the new Player instance to the repository
            playerRepository.save(player1);
        }
    }

    private void createInitialUsers() {
        List<User> users = this.userRepository.findAll();

        users.add(this.userRepository.save(new User(0L, "Armando", "Cringe@ball.nl", "fout")));
        users.add(this.userRepository.save(new User(0L, "Ballmando", "yep@huh.be", "goed")));
    }

    private void createInitialGameData(){
        List<Game> games = this.gameRepository.findAll();
        games.add(this.gameRepository.save(new Game("BACK03", 3, 30,3)));
        games.add(this.gameRepository.save(new Game("BACK04", 4, 40, 4)));
        games.add(this.gameRepository.save(new Game("BACK05", 5, 50, 5)));
        games.add(this.gameRepository.save(new Game("THY06R", 4, 60, 10)));
        games.add(this.gameRepository.save(new Game("97FYM2", 4, 60, 10)));
        games.add(this.gameRepository.save(new Game("SUS420", 4, 60, 10)));
    }
}
