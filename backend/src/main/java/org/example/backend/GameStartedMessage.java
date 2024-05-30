package org.example.backend;

public class GameStartedMessage {

    private String gameId;

    public GameStartedMessage(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}