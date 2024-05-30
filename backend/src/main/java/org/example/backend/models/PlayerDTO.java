package org.example.backend.models;

public class PlayerDTO {
    public int playerNumber;
    public String gameId;
    public UserDTO user;
    public String playerColor;

    public PlayerDTO(int playerNumber, String gameId, User user, String playerColor) {
        this.playerNumber = playerNumber;
        this.gameId = gameId;
        this.user = new UserDTO(user);
        this.playerColor = playerColor;
    }

    public PlayerDTO(Player player){
        this.playerNumber = player.getPlayerNumber();
        this.gameId = player.getGame().getId();
        this.user = new UserDTO(player.getUser());
        this.playerColor = player.getPlayerColor();
    }
}
