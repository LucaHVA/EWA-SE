package org.example.backend;

import jakarta.transaction.Transactional;
import org.example.backend.models.*;
import org.example.backend.repositories.EntityRepository;
import org.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Time;
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
    @Qualifier("GAMEHISTORIES.JPA")
    @Autowired
    private EntityRepository<GameHistory, Long> gameHistoryRepository;

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("Running CommandLine Startup");
        this.createInitialUsers();
        this.createInitialGameData();
        this.createInitialPlayers();
        this.createInitialGameHistories();
    }

    private void createInitialPlayers() {
        List<Game> games = this.gameRepository.findAll();
        List<User> users = this.userRepository.findAll();

        // Check if games and users are not empty before proceeding
        if (!games.isEmpty() && !users.isEmpty()) {
            //Fill a game with players
            Game game1 = this.gameRepository.findById("BACK04");
            // Create new Player instance (user)
            playerRepository.save(new Player(0, game1, userRepository.findById(30003L)));
            // Create new Player instance (bot)
            playerRepository.save(new Player(1, game1));

            // Fill a game with players
            Game game2 = this.gameRepository.findById("SUS420");
            playerRepository.save(new Player(0, game2, userRepository.findById(30004L)));
            playerRepository.save(new Player(1, game2));

            Game game3 = this.gameRepository.findById("BACK05");
            Player filledPlayer = new Player(0, game3, userRepository.findById(30002L));
            filledPlayer.setPlayerColor("blue");
            filledPlayer.setPlayerGameInfo(1, 1, 1, 1);
            filledPlayer.setPlayerResources(1, 1, 1, 1, 1);
            filledPlayer.setPlayerDevelopments(0, 0, 0, 0, 0);
            playerRepository.save(filledPlayer);

            Game game4 = this.gameRepository.findById("BACK03");
            playerRepository.save(new Player(0, game4, userRepository.findById(30003L)));
        }
    }

    private void createInitialUsers() {
        List<User> users = this.userRepository.findAll();

        users.add(this.userRepository.save(new User(0L, "Armando", "Cringe@ball.nl", "fout")));
        users.add(this.userRepository.save(new User(0L, "Ballmando", "yep@huh.be", "goed")));
        users.add(this.userRepository.save(new User(0L, "user1", "user1@mail.com", "password")));
        users.add(this.userRepository.save(new User(0L, "user2", "user2@mail.com", "password")));
        users.add(this.userRepository.save(new User(0L, "user3", "user3@mail.com", "password")));
        users.add(this.userRepository.save(new User(0L, "user4", "user4@mail.com", "password")));
        users.add(this.userRepository.save(new User(0L, "user5", "user5@mail.com", "password")));

    }

    private void createInitialGameData() {

        List<User> users = this.userRepository.findAll();
        User host = users.get(0); // Armando


        List<Game> games = this.gameRepository.findAll();
        games.add(this.gameRepository.save(new Game("BACK03", 3, 33, 3,  Game.STATUS_OPEN, host)));
        games.add(this.gameRepository.save(new Game("BACK04", 4, 40, 4, Game.STATUS_OPEN, host)));
        games.add(this.gameRepository.save(new Game("BACK05", 4, 50, 5,  Game.STATUS_OPEN, host)));
        games.add(this.gameRepository.save(new Game("SUS420", 4, 60, 10,  Game.STATUS_CLOSED, host)));
    }

    private void createInitialGameHistories() {
        List<User> users = this.userRepository.findAll();
        Time startTime = new Time(System.currentTimeMillis() - 3600 * 1000); // 1 hour ago
        Time endTime = new Time(System.currentTimeMillis());

        if (!users.isEmpty()) {
            // Assuming users.get(0) is "Armando"
            gameHistoryRepository.save(new GameHistory(0L, users.get(0), 1, startTime, endTime));
            gameHistoryRepository.save(new GameHistory(0L, users.get(0), 2, startTime, endTime));

            // Assuming users.get(1) is "Ballmando"
            gameHistoryRepository.save(new GameHistory(0L, users.get(1), 1, startTime, endTime));
            gameHistoryRepository.save(new GameHistory(0L, users.get(1), 3, startTime, endTime));
            gameHistoryRepository.save(new GameHistory(0L, users.get(1), 1, startTime, endTime));
            gameHistoryRepository.save(new GameHistory(0L, users.get(1), 3, startTime, endTime));
            gameHistoryRepository.save(new GameHistory(0L, users.get(1), 1, startTime, endTime));
            gameHistoryRepository.save(new GameHistory(0L, users.get(1), 3, startTime, endTime));


            // Additional game histories for other users
            for (int i = 2; i < users.size(); i++) {
                gameHistoryRepository.save(new GameHistory(0L, users.get(i), (i % 4) + 1, startTime, endTime));
            }
        }
    }
}
