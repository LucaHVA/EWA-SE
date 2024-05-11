package org.example.backend.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "\"Game\"")
public class Game implements Identifiable<String>{

    @Id
    private String gameId;
    private int numberOfPlayers;
    private int turnDuration;
    private int pointsToWin;
    @OneToMany(mappedBy = "game")
    private List<Player> players;

    public Game(String gameId, int numberOfPlayers, int turnDuration, int pointsToWin) {
        this.gameId = gameId;
        this.numberOfPlayers = numberOfPlayers;
        this.turnDuration = turnDuration;
        this.pointsToWin = pointsToWin;
    }

    public Game() {

    }

    public String getId() {
        return gameId;
    }

    public void setId(String id) {
        this.gameId = id;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getTurnDuration() {
        return turnDuration;
    }

    public void setTurnDuration(int turnDuration) {
        this.turnDuration = turnDuration;
    }

    public int getPointsToWin() {
        return pointsToWin;
    }

    public void setPointsToWin(int pointsToWin) {
        this.pointsToWin = pointsToWin;
    }
}
