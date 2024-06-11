import {GameHistory} from '@/models/gameHistory';
//heeloo
// Describe defines a group of tests
describe('GameHistory Model Test', () => {
    //variable available for use across multiple test cases within this describe
    let original;

    // beforeEach runs before each test case, setting up common test data or performing necessary arrangements
    beforeEach(() => {
        // Arrange: Create an original GameHistory instance with predefined values
        original = new GameHistory(1, 'user1', 1, '2023-06-08T12:00:00', '2023-06-08T13:00:00');
    });

    //'it' block for a test
    it('should create a new GameHistory instance using the copyConstructor', () => {
        // Act: Use the copyConstructor to create a new instance based on the original
        const copy = GameHistory.copyConstructor(original);

        // Assert: Verify that the new instance matches the original in terms of property values
        // expect used to define assertions, checking if the tested condition holds true
        // toEqual matcher checks if the actual value matches the expected value
        // expect.objectContaining matcher checks if the received object contains all of the expected key/value pairs
        expect(copy).toEqual(expect.objectContaining({
            gameHistoryId: 1,
            user: 'user1',
            placement: 1,
            startTime: '2023-06-08T12:00:00',
            endTime: '2023-06-08T13:00:00'
        }));
    });

    // Another 'it' block for a different test scenario
    it('should return null when copying null or undefined', () => {
        // Act: Call the copyConstructor with null and undefined
        const copyNull = GameHistory.copyConstructor(null);
        const copyUndefined = GameHistory.copyConstructor(undefined);

        // Assert: Verify that the result is null for both null and undefined inputs
        // expect used to define assertions, checking if the tested condition holds true
        expect(copyNull).toBeNull();
        expect(copyUndefined).toBeNull();
    });
});
