class Game {
    id;
    numberOfPlayers;
    turnDuration;
    pointsToWin;

    constructor(id, numberOfPlayers, turnDuration, pointsToWin) {
        this.id = id;
        this.numberOfPlayers = numberOfPlayers;
        this.turnDuration = turnDuration;
        this.pointsToWin = pointsToWin;
    }

    static copyConstructor(game){
        if(game == null) return null;

        const copy = new Game(
            game.id,
            game.numberOfPlayers,
            game.turnDuration,
            game.pointsToWin
        );

        return copy;
    }

    static createGame(id, numberOfPlayers, turnDuration, pointsToWin) {
        return new Game(id, numberOfPlayers, turnDuration, pointsToWin);
    }

}

module.exports = Game;