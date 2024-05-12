package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "\"Player\"")
public class Player implements Identifiable<PlayerKey> {
    @EmbeddedId
    private PlayerKey playerId;

    @JsonIgnore
    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id", referencedColumnName = "gameId")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = true)
    private User user;

    private String playerColor;

    private Integer amountOfPoints;
    private Integer longestRoad;
    private Integer settlementPoints;
    private Integer knightsUsed;

    private Integer resourceOre;
    private Integer resourceBrick;
    private Integer resourceWheat;
    private Integer resourceWood;
    private Integer resourceSheep;

    private Integer developKnight;
    private Integer developRoad;
    private Integer developPlenty;
    private Integer developMonopoly;
    private Integer developVictory;

    public Player() {

    }

    public Player(int playerNumber, Game game) {
        this.playerId = new PlayerKey(playerNumber, game.getId());
        this.setGame(game);
    }

    public Player(int playerNumber, Game game, User user) {
        this.playerId = new PlayerKey(playerNumber, game.getId());
        this.setGame(game);
        this.setUser(user);
    }

    public Player(PlayerKey playerId, User user, String playerColor) {
        // Initialize fields
        this.playerId = playerId;
        this.user = user;
        this.playerColor = playerColor;
    }


    public int getPlayerNumber() {
        return playerId != null ? playerId.getPlayerNumber() : 0;
    }

    public void setPlayerNumber(int playerNumber) {
        if (this.playerId == null) {
            this.playerId = new PlayerKey();
        }
        this.playerId.setPlayerNumber(playerNumber);
        updatePlayerKey();
    }

    @Override
    public PlayerKey getId() {
        return this.playerId;
    }

    @Override
    public void setId(PlayerKey id) {
        this.playerId = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        updatePlayerKey();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updatePlayerKey() {
        if (this.game != null && this.playerId != null) {
            this.playerId.setGameId(this.game.getId());
        }
    }
}

