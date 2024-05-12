package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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

    public void setPlayerGameInfo(int amountOfPoints, int longestRoad, int settlementPoints, int knightsUsed){
        this.amountOfPoints = amountOfPoints;
        this.longestRoad = longestRoad;
        this.settlementPoints = settlementPoints;
        this.knightsUsed = knightsUsed;
    }

    public void setPlayerResources(int resourceOre, int resourceBrick, int resourceWheat, int resourceWood, int resourceSheep) {
        this.resourceOre = resourceOre;
        this.resourceBrick = resourceBrick;
        this.resourceWheat = resourceWheat;
        this.resourceWood = resourceWood;
        this.resourceSheep = resourceSheep;
    }

    public void setPlayerDevelopments(int developKnight, int developRoad, int developPlenty, int developMonopoly, int developVictory) {
        this.developKnight = developKnight;
        this.developRoad = developRoad;
        this.developPlenty = developPlenty;
        this.developMonopoly = developMonopoly;
        this.developVictory = developVictory;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
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

    public Integer getAmountOfPoints() {
        return amountOfPoints;
    }

    public void setAmountOfPoints(Integer amountOfPoints) {
        this.amountOfPoints = amountOfPoints;
    }

    public Integer getLongestRoad() {
        return longestRoad;
    }

    public void setLongestRoad(Integer longestRoad) {
        this.longestRoad = longestRoad;
    }

    public Integer getSettlementPoints() {
        return settlementPoints;
    }

    public void setSettlementPoints(Integer settlementPoints) {
        this.settlementPoints = settlementPoints;
    }

    public Integer getKnightsUsed() {
        return knightsUsed;
    }

    public void setKnightsUsed(Integer knightsUsed) {
        this.knightsUsed = knightsUsed;
    }

    public Integer getResourceOre() {
        return resourceOre;
    }

    public void setResourceOre(Integer resourceOre) {
        this.resourceOre = resourceOre;
    }

    public Integer getResourceBrick() {
        return resourceBrick;
    }

    public void setResourceBrick(Integer resourceBrick) {
        this.resourceBrick = resourceBrick;
    }

    public Integer getResourceWheat() {
        return resourceWheat;
    }

    public void setResourceWheat(Integer resourceWheat) {
        this.resourceWheat = resourceWheat;
    }

    public Integer getResourceWood() {
        return resourceWood;
    }

    public void setResourceWood(Integer resourceWood) {
        this.resourceWood = resourceWood;
    }

    public Integer getResourceSheep() {
        return resourceSheep;
    }

    public void setResourceSheep(Integer resourceSheep) {
        this.resourceSheep = resourceSheep;
    }

    public Integer getDevelopKnight() {
        return developKnight;
    }

    public void setDevelopKnight(Integer developKnight) {
        this.developKnight = developKnight;
    }

    public Integer getDevelopRoad() {
        return developRoad;
    }

    public void setDevelopRoad(Integer developRoad) {
        this.developRoad = developRoad;
    }

    public Integer getDevelopPlenty() {
        return developPlenty;
    }

    public void setDevelopPlenty(Integer developPlenty) {
        this.developPlenty = developPlenty;
    }

    public Integer getDevelopMonopoly() {
        return developMonopoly;
    }

    public void setDevelopMonopoly(Integer developMonopoly) {
        this.developMonopoly = developMonopoly;
    }

    public Integer getDevelopVictory() {
        return developVictory;
    }

    public void setDevelopVictory(Integer developVictory) {
        this.developVictory = developVictory;
    }
}

