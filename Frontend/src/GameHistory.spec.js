import {GameHistory} from '@/models/gameHistory';

describe('GameHistory Model', () => {
    it('should create a new GameHistory instance using the copyConstructor', () => {
        // Arrange
        const original = new GameHistory(1, 'user1', 1,
            '2023-06-08T12:00:00', '2023-06-08T13:00:00');

        // Act
        const copy = GameHistory.copyConstructor(original);

        // Assert
        expect(copy).toEqual(expect.objectContaining({
            gameHistoryId: 1,
            user: 'user1',
            placement: 1,
            startTime: '2023-06-08T12:00:00',
            endTime: '2023-06-08T13:00:00'
        }));
    });

    it('should return null when copying null or undefined', () => {
        // Arrange & Act
        const copyNull = GameHistory.copyConstructor(null);
        const copyUndefined = GameHistory.copyConstructor(undefined);

        // Assert
        expect(copyNull).toBeNull();
        expect(copyUndefined).toBeNull();
    });
});
