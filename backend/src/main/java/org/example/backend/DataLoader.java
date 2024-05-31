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

        //!!! Do not use user0, user1, or user2 for filling and keep them clean for testing purposes

        // Check if games and users are not empty before proceeding
        if (!games.isEmpty() && !users.isEmpty()) {
            //Fill a game with players
            Game game1 = this.gameRepository.findById("BACK04");
            // Create new Player instance (user) and set host
            User user3 = userRepository.findById(30003L);
            playerRepository.save(new Player(0, game1, user3));
            game1.setHost(user3);
            // Create new Player instance (bot)
            playerRepository.save(new Player(1, game1));

            // Fill games with players
            Game game2 = this.gameRepository.findById("SUS420");
            playerRepository.save(new Player(0, game2, user3));
            game2.setHost(user3);
            playerRepository.save(new Player(1, game2));

            Game game3 = this.gameRepository.findById("BACK05");
            Player filledPlayer = new Player(0, game3, user3);
            game3.setHost(user3);
            filledPlayer.setPlayerColor("blue");
            filledPlayer.setPlayerGameInfo(1, 1, 1, 1);
            filledPlayer.setPlayerResources(1, 1, 1, 1, 1);
            filledPlayer.setPlayerDevelopments(0, 0, 0, 0, 0);
            playerRepository.save(filledPlayer);

            Game game4 = this.gameRepository.findById("BACK03");
            playerRepository.save(new Player(0, game4, user3));
            game4.setHost(user3);

            // Fill a game fully
            Game fullGame1 = this.gameRepository.findById("FULL01");
            playerRepository.save(new Player(0, fullGame1, user3));
            fullGame1.setHost(user3);
            playerRepository.save(new Player(1, fullGame1, userRepository.findById(30004L)));
            playerRepository.save(new Player(2, fullGame1, userRepository.findById(30005L)));
            playerRepository.save(new Player(3, fullGame1, userRepository.findById(30006L)));

            Game fullGame2 = this.gameRepository.findById("FULL02");
            playerRepository.save(new Player(0, fullGame2, user3));
            fullGame2.setHost(user3);
            playerRepository.save(new Player(1, fullGame2, userRepository.findById(30004L)));
            playerRepository.save(new Player(2, fullGame2, userRepository.findById(30005L)));
            playerRepository.save(new Player(3, fullGame2, userRepository.findById(30006L)));

            Game game11 = this.gameRepository.findById("BACK11");
            playerRepository.save(new Player(0, game11, user3));
            game11.setHost(user3);

            Game game12 = this.gameRepository.findById("BACK12");
            playerRepository.save(new Player(0, game12, user3));
            game12.setHost(user3);

            Game game13 = this.gameRepository.findById("BACK13");
            playerRepository.save(new Player(0, game13, user3));
            game13.setHost(user3);
        }
    }

    private void createInitialUsers() {
        List<User> users = this.userRepository.findAll();

        users.add(this.userRepository.save(new User(0L, "user0", "user0@mail.com", "password")));
        users.add(this.userRepository.save(new User(0L, "user1", "user1@mail.com", "password")));
        users.add(this.userRepository.save(new User(0L, "user2", "user2@mail.com", "password")));
        users.add(this.userRepository.save(new User(0L, "user3", "user3@mail.com", "password")));
        users.add(this.userRepository.save(new User(0L, "user4", "user4@mail.com", "password")));
        users.add(this.userRepository.save(new User(0L, "user5", "user5@mail.com", "password")));
        users.add(this.userRepository.save(new User(0L, "Armando", "Cringe@ball.nl", "fout")));
        users.add(this.userRepository.save(new User(0L, "Ballmando", "yep@huh.be", "goed")));
        User adminUser = new User(0L, "admin", "admin@mail.com", "adminPassword");
        adminUser.getRoles().add(User.Role.ADMIN);
        users.add(this.userRepository.save(adminUser));

    }

    private void createInitialGameData() {

        List<User> users = this.userRepository.findAll();
        User host = users.get(0); // Armando


        List<Game> games = this.gameRepository.findAll();
        games.add(this.gameRepository.save(new Game("BACK03", 3, 33, 3, Game.Status.open, host)));
        games.add(this.gameRepository.save(new Game("BACK04", 4, 40, 4,Game.Status.open, host)));
        games.add(this.gameRepository.save(new Game("BACK05", 4, 50, 5, Game.Status.open, host)));
        games.add(this.gameRepository.save(new Game("SUS420", 4, 60, 10, Game.Status.closed, host)));
        games.add(this.gameRepository.save(new Game("FULL01", 4, 60, 10, Game.Status.ongoing, null)));
        games.add(this.gameRepository.save(new Game("FULL02", 4, 60, 10, Game.Status.ongoing, null)));
        games.add(this.gameRepository.save(new Game("BACK11", 4, 60, 8, Game.Status.open, null)));
        games.add(this.gameRepository.save(new Game("BACK12", 4, 60, 8, Game.Status.open, null)));
        games.add(this.gameRepository.save(new Game("BACK13", 4, 60, 8, Game.Status.open, null)));
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
