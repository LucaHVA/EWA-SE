package org.example.backend.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"Game\"")
public class Game implements Identifiable<String> {

    @Id
    private String gameId;
    private int numberOfPlayers;
    private int turnDuration;
    private int pointsToWin;
    private Status status;

    @OneToMany(mappedBy = "game")
    private List<Player> players;

    @ManyToOne
    @JoinColumn(name = "master_id", referencedColumnName = "userId")
    private User host;

    // Possible values for status
    public static final String STATUS_OPEN = "open";
    public static final String STATUS_CLOSED = "closed";

    public enum Status {
        open,
        ongoing,
        closed
    }

    public Game(String gameId, int numberOfPlayers, int turnDuration, int pointsToWin, Status status, User host) {
        this.gameId = gameId;
        this.numberOfPlayers = numberOfPlayers;
        this.turnDuration = turnDuration;
        this.pointsToWin = pointsToWin;
        this.status = status;
        this.host = host;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }
}
