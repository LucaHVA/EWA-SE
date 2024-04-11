class Player {

    playerId;
    user;
    resources;
    developmentCards;
    pointAmount;
    longestRoad;
    settlementAmount;
    knightsUsed;

    static resourcesList = {
        ORE: 'ore',
        BRICK: 'brick',
        WHEAT: 'wheat',
        WOOD: 'wood',
        SHEEP: 'sheep'
    }

    static developmentCardsList = {
        KNIGHT: "Move the robber",
        ROAD: "Place 2 roads for free",
        PLENTY: "Draw 2 resource cards of choice from the bank",
        MONOPOLY: "Claim all resource cards of a specific type",
        VICTORY: "1 additional Victory Point"
    }

    constructor(playerId, user, resources, developmentCards, pointAmount, longestRoad, settlementAmount, knightsUsed) {
        this.playerId = playerId;
        this.user = user;
        this.resources = resources;
        this.developmentCards = developmentCards;
        this.pointAmount = pointAmount;
        this.longestRoad = longestRoad;
        this.settlementAmount = settlementAmount;
        this.knightsUsed = knightsUsed;
    }

}

module.exports = Player;