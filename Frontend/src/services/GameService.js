import Game from "@/models/game";
import Player from "@/models/player";

export class GameService {
    games = {};
    resourcesUrl;
    constructor(resourcesUrl) {
        this.games = new Map();
        this.resourcesUrl=resourcesUrl;

    }

    async fetchJson(url, options = null) {
        let res = await fetch(url, options);
        if (res.ok && res.headers.get('content-type')?.includes('application/json')) {
            return res.json();
        } else {
            console.log(res, !res.bodyUsed ? await res.text() : "")
            return null;
        }
    }

    /**
     * Get a game instance by its id
     * @param gameId
     * @returns {Promise<any>}
     */
    async asyncGetById(gameId) {
        const game = await this.fetchJson(`${this.resourcesUrl}/${gameId}`);
        return game;
    }

    /**
     * Find all games from backend
     * @returns {Promise<*>}
     */
    async asyncFindAll() {
        try {
            const games = await this.fetchJson(this.resourcesUrl + "/all", {
                method: 'GET'
            })
            return games?.map(Game.copyConstructor);
        } catch (e) {
            console.error(e)
        }
    }

    async asyncGetAllFiltered() {
        try {
            const games = await this.fetchJson(this.resourcesUrl + "/filter", {
                method: 'GET'
            })
            return games?.map(Game.copyConstructor);
        } catch (e) {
            console.error(e)
        }
    }
    /**
     * Based on the given game's id this method will create or update a game.
     * @param game
     * @param queryParams
     * @returns {Promise<Game|null|any>}
     */
    async saveGame(game, queryParams) {

        try {
            if (game.id) {

            const url = `${this.resourcesUrl}${queryParams ? `?${queryParams}` : ''}`;
            const options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(game)
            };

            const response = await this.fetchJson(url, options);

            if (response) {
                return response;
            } else {
                console.error('Failed to save game.');
                return null;
            }

        }
            const createdGame = Game.copyConstructor(game);
            let res;
            res = this.fetchJson(this.resourcesUrl + "/" + game.id, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(createdGame)
            });
            res.status = "closed";
            return Game.copyConstructor(res);
        } catch (error) {
            console.error("Error saving game:", error);
            throw error; //throw the error to be handled by the caller
        }
    }

    async createGame(user){
        let newId = await this.generateUniqueGameId();
        // Create game instance with default settings
        let newGame = await new Game(newId, 4, 60, 8,"open", user);
        // Save created game
        return await this.saveGame(newGame);
    }

    /**
     * Add a new player to the game with id <gameId>
     * @param {String} gameId
     * @param {object} user
     * @returns {Promise<Player|null>}
     */
    async addNewPlayerToGame(gameId, user) {

        //TODO error catching: check if user is already in game

        if (!(await this.canAddNewPlayerToGame(gameId))) {
            console.error("Game is full. Player not added.");
            return null;
        }

        let playerNumber = await this.getLastAvailablePlayerNumber(gameId);


        let playerJson = {
            "gameId": gameId,
            "user": user,
            "playerNumber": playerNumber
        }

        let player = Player.dbConstructorNewPlayer(playerJson);

        try {
            const url = `${this.resourcesUrl}/${gameId}/players`;
            const options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(player)
            };

            const response = await this.fetchJson(url, options);

            if (response) {
                return player;
            } else {
                console.error('Failed to add player to game.');
                return null;
            }
        } catch (error) {
            console.error("Error adding player to game:", error);
            throw error; // Throw the error to be handled by the caller
        }
    }

    async getLastAvailablePlayerNumber(gameId) {
        // Check if there are open slots
        if (!(await this.canAddNewPlayerToGame(gameId))){
            console.error("cannot add new player to ", gameId)
            return;
        }

        let players = await this.asyncFindAllPlayersForGameId(gameId);

        return players.length;
    }

    async deletePlayerFromGame(gameId, playerNumber){

        try {
            const url = `${this.resourcesUrl}/${gameId}/players/${playerNumber}`;
            const options = {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            };

            const response = await this.fetchJson(url, options);

            console.log("attempted delete player in service")
            console.log(response);

        } catch (e) {
            console.error(e);
        }
    }

    /**
     * Get all existing game id's from backend
     * @returns {Promise<*>}
     */
    async getGameIds() {
        try {
            const games = await this.asyncFindAll();
            const gameIds = games.map(game => game.gameId);
            return gameIds;
        } catch (e) {
            console.error('Error fetching game IDs:', e);
        }
    }

    /**
     * Check existing game id's and generate a new one
     * @returns {Promise<string>} new and unique id
     */
    async generateUniqueGameId() {
        // Fetch backend for existing id's
        const gameIds = await this.getGameIds();

        let uniqueId;

        // Generate a random alphanumeric string of length 5 in uppercase
        do {
            uniqueId = Math.random().toString(36).substring(2, 7).toUpperCase();
        } while (this.games.has(uniqueId) && gameIds.has(uniqueId)); // Check if this ID already exists in games

        return uniqueId;
    }

    /**
     * Find all players for a game with id <gameId>
     * @param gameId
     * @returns {Promise<*>} map of players
     */
    async asyncFindAllPlayersForGameId(gameId){

        try {
            const players = await this.fetchJson(`${this.resourcesUrl}/${gameId}` +  "/players", {
                method: 'GET'
            })
            if (players && players.length > 0) {
                return players.map(Player.dbConstructor);
            } else {
                return "0"; // Return "0" if no players are found
            }
        } catch (e) {
            console.error(e)
        }
    }

    /**
     * Find all players for game with id <gameId> and return simplified JSON
     * @param gameId
     * @returns {Promise<string|*>} map of simplified players
     */
    async asyncFindAllSimplePlayersForGameId(gameId){
        try {
            const players = await this.fetchJson(`${this.resourcesUrl}/${gameId}` +  "/players/simple", {
                method: 'GET'
            })
            if (players && players.length > 0) {
                return players.map(Player.dbConstructor);
            } else {
                return "0"; // Return "0" if no players are found
            }
        } catch (e) {
            console.error(e)
        }
    }

    /**
     * Check if a game is full
     * @param gameId
     * @returns {Promise<boolean>}
     */
    async canAddNewPlayerToGame(gameId) {
        let players;
        let game;

        try {
            players = await this.asyncFindAllPlayersForGameId(gameId);
            game = await this.asyncGetById(gameId);
        } catch (e) {
            console.error(e);
        }

        if (players.length >= game.numberOfPlayers){
            return false;
        } else return true;
    }

}

// Export a singleton instance of the GameService
export default new GameService();
