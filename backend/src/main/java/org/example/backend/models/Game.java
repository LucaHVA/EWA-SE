package org.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "GAME")
public class Game implements Identifiable<String>{

    @Id
    private String id;
    private int numberOfPlayers;
    private int turnDuration;
    private int pointsToWin;

    public Game(String id, int numberOfPlayers, int turnDuration, int pointsToWin) {
        this.id = id;
        this.numberOfPlayers = numberOfPlayers;
        this.turnDuration = turnDuration;
        this.pointsToWin = pointsToWin;
    }

    public Game() {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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
