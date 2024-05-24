export class GameHistory {
    gameHistoryId;
    user;
    placement;
    startTime;
    endTime;

    constructor(gameHistoryId, user, placement, startTime, endTime) {
        this.gameHistoryId = gameHistoryId;
        this.user = user;
        this.placement = placement
        this.startTime = startTime;
        this.endTime = endTime;
    }

    static copyConstructor(gameHistory) {
        if (gameHistory === null || gameHistory === undefined) return null;
        return Object.assign(new GameHistory(0), gameHistory);
    }
}
