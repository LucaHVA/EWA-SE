class Player {

    playerNumber;
    gameId;
    user;
    playerColor;
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

    constructor(playerNumber, gameId, user, playerColor, resources, developmentCards, pointAmount, longestRoad, settlementAmount, knightsUsed) {
        this.playerNumber = playerNumber;
        this.gameId = gameId;
        this.user = user;
        this.playerColor = playerColor;
        this.resources = resources;
        this.developmentCards = developmentCards;
        this.pointAmount = pointAmount;
        this.longestRoad = longestRoad;
        this.settlementAmount = settlementAmount;
        this.knightsUsed = knightsUsed;
    }



    static copyConstructor(player){
        if (player==null) return null;

        const copy = new player(
            player.playerNumber,
            player.gameId,
            player.user,
            player.playerColor,
            player.resources,
            player.developmentCards,
            player.pointAmount,
            player.longestRoad,
            player.settlementAmount,
            player.knightsUsed
        )

        return copy;
    }

    static dbConstructor(playerJson) {
        if (playerJson == null) return null;

        // Extracting individual fields from the JSON object
        const playerNumber = playerJson.playerNumber;
        const gameId = playerJson.id.gameId;
        const user = playerJson.user;
        const playerColor = playerJson.playerColor;

        // Create arrays for resources and development cards based on the quantity
        const resources = [];
        for (let i = 0; i < playerJson.resourceOre; i++) resources.push('ore');
        for (let i = 0; i < playerJson.resourceBrick; i++) resources.push('brick');
        for (let i = 0; i < playerJson.resourceWheat; i++) resources.push('wheat');
        for (let i = 0; i < playerJson.resourceWood; i++) resources.push('wood');
        for (let i = 0; i < playerJson.resourceSheep; i++) resources.push('sheep');

        const developmentCards = [];
        for (let i = 0; i < playerJson.developKnight; i++) developmentCards.push('knight');
        for (let i = 0; i < playerJson.developRoad; i++) developmentCards.push('road');
        for (let i = 0; i < playerJson.developPlenty; i++) developmentCards.push('plenty');
        for (let i = 0; i < playerJson.developMonopoly; i++) developmentCards.push('monopoly');
        for (let i = 0; i < playerJson.developVictory; i++) developmentCards.push('victory');

        const pointAmount = playerJson.amountOfPoints;
        const longestRoad = playerJson.longestRoad;
        const settlementAmount = playerJson.settlementPoints;
        const knightsUsed = playerJson.knightsUsed;

        // Creating a new Player instance with the extracted values
        const playerInstance = new Player(
            playerNumber,
            gameId,
            user,
            playerColor,
            resources,
            developmentCards,
            pointAmount,
            longestRoad,
            settlementAmount,
            knightsUsed
        );

        return playerInstance;
    }

    static dbConstructorNewPlayer(playerJson){
        if (playerJson == null) return null;

        // Extracting individual fields from the JSON object
        const playerNumber = playerJson.playerNumber;
        const user = playerJson.user;
        const playerColor = playerJson.playerColor;

        const playerInstance = new Player(playerNumber, null, user, playerColor);

        return playerInstance;
    }

}

module.exports = Player;