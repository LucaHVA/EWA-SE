import { UsersAdaptor } from "@/services/UsersAdaptor";

describe('UsersAdaptor', () => {
    let usersAdaptor;

    beforeEach(() => {
        usersAdaptor = new UsersAdaptor('http://localhost:8080', 'test-token');
        usersAdaptor.fetchJson = jest.fn();
    });

    it('should fetch friends for a given user ID', async () => {
        // Arrange
        const userId = 1;
        const friends = [{ id: 2, username: "JohnDoe" }];
        usersAdaptor.fetchJson.mockResolvedValue(friends);

        // Act
        const result = await usersAdaptor.getFriends(userId);

        // Assert
        expect(usersAdaptor.fetchJson).toHaveBeenCalledWith('http://localhost:8080/1/friends', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        expect(result).toEqual(friends);
    });

    it('should send a friend request', async () => {
        // Arrange
        const userId = 1;
        const friendId = 2;
        const friendResponse = { id: 3, user: { id: 1 }, friend: { id: 2 }, status: "PENDING" };
        usersAdaptor.fetchJson.mockResolvedValue(friendResponse);

        // Act
        const result = await usersAdaptor.sendFriendRequest(userId, friendId);

        // Assert
        expect(usersAdaptor.fetchJson).toHaveBeenCalledWith('http://localhost:8080/1/friends/2', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        expect(result).toEqual(friendResponse);
    });
});