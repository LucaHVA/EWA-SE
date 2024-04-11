import Game from "@/models/game";

export class GameService {
    games = {};

    constructor() {
        this.games = new Map();
    }

    // Save a game instance with a unique gameId
    saveGame(gameId, game) {
        // this.games[gameId] = game;
            this.games.set(gameId, game);
    }

    // Get a game instance by its gameId
    getGame(gameId) {
        return this.games.get(gameId);
    }

    // Get all games
    getGames() {
        return this.games;
    }

    generateGame(numberOfPlayers, turnDuration, pointsToWin){
        return Game.createGame(this.generateUniqueGameId(), numberOfPlayers, turnDuration, pointsToWin);
    }

    generateUniqueGameId(){
        //todo check (backend) for existing id and generate new one
        let uniqueId;

        // Generate a random alphanumeric string of length 5 in uppercase
        do {
            uniqueId = Math.random().toString(36).substring(2, 7).toUpperCase();
        } while (this.games.has(uniqueId)); // Check if this ID already exists in games

        return uniqueId;
    }
}

// Export a singleton instance of the GameService
export default new GameService();
