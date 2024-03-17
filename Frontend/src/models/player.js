class Player {

    playerId;
    user;
    resources;
    developmentCards;

    static resourcesList = {
        ORE: 'ore',
        BRICK: 'brick',
        GRAIN: 'grain',
        WOOD: 'wood',
        SHEEP: 'sheep'
    }

    static developmentCardsList = {
        KNIGHT: "Move the robber",
        ROAD: "Place 2 roads for free",
        PLENTY: "Draw 2 resource cards of choice from the bank",
        MONOPOLY: " Claim all resource cards of a specific type",
        VICTORY: " 1 additional Victory Point"
    }

    constructor(playerId, user, resources, developmentCards) {
        this.playerId = playerId;
        this.user = user;
        this.resources = resources;
        this.developmentCards = developmentCards;
    }

    createPlayer(playerId = 0, user= "NaN") {
        let resources = [];
        let developmentCards = [];
        return new Player(playerId, user, resources, developmentCards);
    }
}

module.exports = Player;