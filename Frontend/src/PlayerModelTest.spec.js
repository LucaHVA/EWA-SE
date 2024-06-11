const Player = require('@/models/player.js');

describe('Player Model Test', () => {
    let original;

    beforeEach(() => {
        // Arrange
        original = new Player(1, 'game1', 'user1', 'red', ['ore', 'brick'], ['knight', 'road'], 3, 3, 2, 1);
    });

    /**
     * @Author Stephanie
     */
    it('should create a new Player instance using the copyConstructor', () => {
        // Act
        const copy = Player.copyConstructor(original);

        // Assert
        // Verify that the new instance matches the original in terms of property values
        expect(copy).toEqual(expect.objectContaining({
            playerNumber: 1,
            gameId: 'game1',
            user: 'user1',
            playerColor: 'red',
            resources: ['ore', 'brick'],
            developmentCards: ['knight', 'road'],
            pointAmount: 3,
            longestRoad: 3,
            settlementAmount: 2,
            knightsUsed: 1
        }));
    });

    /**
     * @Author Stephanie
     */
    it('should return null when copying null or undefined', () => {
        // Act
        const copyNull = Player.copyConstructor(null);
        const copyUndefined = Player.copyConstructor(undefined);

        // Assert
        // Verify that the result is null for both null and undefined inputs
        expect(copyNull).toBeNull();
        expect(copyUndefined).toBeNull();
    });

    /**
     * @Author Stephanie
     */
    it('should create a new Player instance from a JSON object', () => {
        // Arrange: Create a JSON object representing a player
        const playerJson = {
            playerNumber: 1,
            id: { gameId: 'game1' },
            user: 'user1',
            playerColor: 'blue',
            resourceOre: 3,
            resourceBrick: 2,
            resourceWheat: 1,
            resourceWood: 0,
            resourceSheep: 4,
            developKnight: 2,
            developRoad: 1,
            developPlenty: 0,
            developMonopoly: 1,
            developVictory: 1,
            amountOfPoints: 5,
            longestRoad: true,
            settlementPoints: 3,
            knightsUsed: 2
        };

        // Act: Use the dbConstructor to create a new Player instance
        const playerInstance = Player.dbConstructor(playerJson);

        // Assert: Verify that the new instance has the correct properties
        expect(playerInstance).toEqual(expect.objectContaining({
            playerNumber: 1,
            gameId: 'game1',
            user: 'user1',
            playerColor: 'blue',
            resources: ['ore', 'ore', 'ore', 'brick', 'brick', 'wheat', 'sheep', 'sheep', 'sheep', 'sheep'],
            developmentCards: ['knight', 'knight', 'road', 'monopoly', 'victory'],
            pointAmount: 5,
            longestRoad: true,
            settlementAmount: 3,
            knightsUsed: 2
        }));
    });
});
