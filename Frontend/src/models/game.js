class Game {
    id;
    numberOfPlayers;
    turnDuration;
    pointsToWin;
    status;
    host;



    constructor(id, numberOfPlayers, turnDuration, pointsToWin,status,host) {
        this.id = id;
        this.numberOfPlayers = numberOfPlayers;
        this.turnDuration = turnDuration;
        this.pointsToWin = pointsToWin;
        this.status=status;
        this.host=host;
    }

    static copyConstructor(game){
        if(game == null) return null;

        const copy = new Game(
            game.id,
            game.numberOfPlayers,
            game.turnDuration,
            game.pointsToWin,
            game.status,
            game.host
        );

        return copy;
    }

    static createGame(id, numberOfPlayers, turnDuration, pointsToWin, status='open', host) {
        return new Game(id, numberOfPlayers, turnDuration, pointsToWin, status, host);
    }

    open() {
        this.status = 'open';
    }

    close() {
        this.status = 'closed';
    }


}

module.exports = Game;