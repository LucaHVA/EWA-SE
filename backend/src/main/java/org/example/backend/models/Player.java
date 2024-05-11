package org.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "\"Player\"")
public class Player implements Identifiable<PlayerKey>{
    @EmbeddedId
    private PlayerKey playerId;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public Player(){

    }

    public Player(int playerNumber, String gameId){
        this.playerId = new PlayerKey(playerNumber, gameId);
    }


    public int getPlayerNumber() {
        return playerId != null ? playerId.getPlayerNumber() : 0;
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
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

