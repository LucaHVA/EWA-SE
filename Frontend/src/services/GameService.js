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

    // Get a game instance by its gameId
    async asyncGetById(gameId) {
        const game = await this.fetchJson(`${this.resourcesUrl}/${gameId}`);
        return game;
    }

    // Get all games
    async asyncFindAll() {
        try {
            const games = await this.fetchJson(this.resourcesUrl + "/all", {
                method: 'GET'
            })
            return games?.map(Game.copyConstructor);
        } catch (e) {
            console.log(e)
        }
    }

// adds a game if there is no gameId found if there is one updates it with the new values
    async saveGame(game, queryParams) {

        if (!game) {
            game = {
                numberOfPlayers: 4,
                turnDuration: 60,
                pointsToWin: 8,
                id: this.generateUniqueGameId()
            };
        }
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

            console.log('Saving game:', game);
            const response = await this.fetchJson(url, options);

            if (response) {
                console.log('Game saved successfully:', response);

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
            })
            return Game.copyConstructor(res);
        } catch (error) {
            console.error("Error saving game:", error);
            throw error; //throw the error to be handled by the caller
        }
    }

    async addNewPlayerToGame(gameId, user, playerNumber) {

        console.log("game service query params");
        console.log(user);

        // let userId = user["userId"];
        // let user = await UsersAdaptor.asyncFindById(userId);

        let playerJson = {
            "gameId": gameId,
            "user": user,
            "playerNumber": playerNumber
        }

        let player = Player.dbConstructor(playerJson);

        //FIXME post request is not being send
        try {
            const url = `${this.resourcesUrl}/${gameId}/players`;
            const options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(player)
            };

            console.log('Adding player to game:', player);
            const response = await this.fetchJson(url, options);

            if (response) {
                console.log('Player added successfully:', response);

                return Player.dbConstructor(response);
            } else {
                console.error('Failed to add player to game.');
                return null;
            }
        } catch (error) {
            console.error("Error adding player to game:", error);
            throw error; // Throw the error to be handled by the caller
        }
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

    async asyncFindAllPlayersForGameId(gameId){

        try {
            const players = await this.fetchJson(`${this.resourcesUrl}/${gameId}` +  "/players", {
                method: 'GET'
            })
            return players?.map(Player.dbConstructor);
        } catch (e) {
            console.log(e)
        }
    }

}

// Export a singleton instance of the GameService
export default new GameService();
