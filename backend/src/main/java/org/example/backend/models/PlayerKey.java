package org.example.backend.models;

import java.io.Serializable;
import java.util.Objects;

public class PlayerKey implements Serializable {
    private int playerNumber;
    private String gameId;

    public PlayerKey() {
    }

    public PlayerKey(int playerNumber, String gameId) {
        this.playerNumber = playerNumber;
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerKey)) return false;
        PlayerKey that = (PlayerKey) o;
        return playerNumber == that.playerNumber &&
                Objects.equals(gameId, that.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerNumber, gameId);
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "game id: " + getGameId() + " / player number: " + getPlayerNumber();
    }
}
