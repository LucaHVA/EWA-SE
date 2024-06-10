import { UsersAdaptor } from "@/services/UsersAdaptor";
import { GameHistory } from '@/models/gameHistory';

describe('UsersAdaptor', () => {
    let service;

    beforeEach(() => {
        // Mocking the UsersAdaptor service directly
        service = new UsersAdaptor('http://localhost:8080');
        // Mock the fetchMatchHistory method
        service.fetchMatchHistory = jest.fn();
    });

    it('fetches match history correctly', async () => {
        // Arrange
        const userId = 1;
        const mockMatchHistory = [
            new GameHistory(1, 'testUser', 1, '2024-06-08T12:00:00', '2024-06-08T13:00:00'),
            new GameHistory(2, 'testUser', 2, '2024-06-07T12:00:00', '2024-06-07T13:00:00'),
        ];
        // Mock the fetchMatchHistory method to return mockMatchHistory
        service.fetchMatchHistory.mockResolvedValue(mockMatchHistory);

        // Act
        const matchHistory = await service.fetchMatchHistory(userId);

        // Assert
        // Verify that the fetchMatchHistory method is called with the expected userId
        expect(service.fetchMatchHistory).toHaveBeenCalledWith(userId);
        // Verify that the result matches the mockMatchHistory
        expect(matchHistory).toEqual(mockMatchHistory);
    });

    it('saves game histories correctly', async () => {
        // Arrange
        const userId = 1;
        const mockGameHistories = [
            new GameHistory(1, 'testUser', 1, '2024-06-08T12:00:00', '2024-06-08T13:00:00'),
            new GameHistory(2, 'testUser', 2, '2024-06-07T12:00:00', '2024-06-07T13:00:00'),
        ];
        // Mock the fetchJson method to return a response
        service.fetchJson = jest.fn().mockResolvedValue(mockGameHistories);

        // Act
        const savedGameHistories = await service.saveGameHistories(userId, mockGameHistories);

        // Assert
        // Verify that the fetchJson method is called with the expected URL and options
        expect(service.fetchJson).toHaveBeenCalledWith(`http://localhost:8080/${userId}/saveGameHistories`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(mockGameHistories)
        });
        // Verify that the result matches the mockGameHistories
        expect(savedGameHistories).toEqual(mockGameHistories);
    });
});
