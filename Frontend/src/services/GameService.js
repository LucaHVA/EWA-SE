import Game from "@/models/game";


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

    // Save a game instance with a unique gameId
    // saveGame(game) {
    //         this.games.set(game);
    // }

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

    // generateNewGame(numberOfPlayers, turnDuration, pointsToWin) {
    //     return Game.createGame(this.generateUniqueGameId(), numberOfPlayers, turnDuration, pointsToWin);
    // }



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


    generateUniqueGameId(){
        //todo check (backend) for existing id and generate new one
        let uniqueId;

        // Generate a random alphanumeric string of length 5 in uppercase
        do {
            uniqueId = Math.random().toString(36).substring(2, 7).toUpperCase();
        } while (this.games.has(uniqueId)); // Check if this ID already exists in games

        return uniqueId;
    }

    // async updateGame(game) {
    //     try {
    //         if (!game.id) {
    //             console.error("Cannot update game without an ID.");
    //             return null;
    //         }
    //
    //         const url = `${this.resourcesUrl}/${game.id}`;
    //         const options = {
    //             method: 'PUT',
    //             headers: {
    //                 'Content-Type': 'application/json'
    //             },
    //             body: JSON.stringify(game)
    //         };
    //
    //         console.log('Updating game:', game);
    //         const response = await this.fetchJson(url, options);
    //
    //         if (response) {
    //             console.log('Game updated successfully:', response);
    //             return response;
    //         } else {
    //             console.error('Failed to update game.');
    //             return null;
    //         }
    //     } catch (error) {
    //         console.error("Error updating game:", error);
    //         throw error;
    //     }
    // }


}

// Export a singleton instance of the GameService
export default new GameService();
