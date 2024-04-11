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

    static createGame(id, numberOfPlayers, turnDuration, pointsToWin) {
        return new Game(id, numberOfPlayers, turnDuration, pointsToWin);
    }

}

module.exports = Game;