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
    private String status;

    @OneToMany(mappedBy = "game")
    private List<Player> players;

    @ManyToOne
    @JoinColumn(name = "master_id", referencedColumnName = "userId")
    private User host;

    // Possible values for status
    public static final String STATUS_OPEN = "open";
    public static final String STATUS_CLOSED = "closed";

    public Game(String gameId, int numberOfPlayers, int turnDuration, int pointsToWin, String status, User host) {
        this.gameId = gameId;
        this.numberOfPlayers = numberOfPlayers;
        this.turnDuration = turnDuration;
        this.pointsToWin = pointsToWin;
        this.host=host;
        setStatus(status);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!STATUS_OPEN.equals(status) && !STATUS_CLOSED.equals(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        this.status = status;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }
}
