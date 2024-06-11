import { AnnouncementsAdaptor } from "@/services/announcements-adaptor";

describe('AnnouncementsAdaptor', () => {
    let mockWebSocket;
    let mockHandler;
    let adaptor;

    beforeEach(() => {
        // Mock WebSocket
        mockWebSocket = {
            send: jest.fn(),
            close: jest.fn(),
            addEventListener: jest.fn((event, handler) => {
                mockWebSocket[event] = handler;
            }),
        };
        global.WebSocket = jest.fn(() => mockWebSocket);

        // Mock handler callback
        mockHandler = jest.fn();

        // Create instance of AnnouncementsAdaptor
        adaptor = new AnnouncementsAdaptor('http://localhost:8085', mockHandler);
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('should create a WebSocket connection with the correct URL', () => {
        // Arrange
        const expectedUrl = 'ws://localhost:8085';

        // Assert
        expect(global.WebSocket).toHaveBeenCalledWith(expectedUrl);
        expect(adaptor.socket).toBe(mockWebSocket);
    });

    it('should call handler callback on receiving a message', () => {
        // Arrange
        const message = { data: 'test message' };

        // Act
        mockWebSocket.onmessage(message);

        // Assert
        expect(mockHandler).toHaveBeenCalledWith('test message');
    });

    it('should log an error on WebSocket error', () => {
        // Arrange
        const consoleSpy = jest.spyOn(console, 'log').mockImplementation(() => {});
        const error = new Error('WebSocket error');

        // Act
        mockWebSocket.onerror(error);

        // Assert
        expect(consoleSpy).toHaveBeenCalledWith(error);
        consoleSpy.mockRestore();
    });

    it('should log "Open socket" on WebSocket open', () => {
        // Arrange
        const consoleSpy = jest.spyOn(console, 'log').mockImplementation(() => {});

        // Act
        mockWebSocket.onopen();

        // Assert
        expect(consoleSpy).toHaveBeenCalledWith('Open socket');
        consoleSpy.mockRestore();
    });

    it('should log "Closed socket" on WebSocket close', () => {
        // Arrange
        const consoleSpy = jest.spyOn(console, 'log').mockImplementation(() => {});

        // Act
        mockWebSocket.onclose();

        // Assert
        expect(consoleSpy).toHaveBeenCalledWith('Closed socket');
        consoleSpy.mockRestore();
    });

    it('should send a message via WebSocket', () => {
        // Arrange
        const message = 'test message';

        // Act
        adaptor.sendMessage(message);

        // Assert
        expect(mockWebSocket.send).toHaveBeenCalledWith(message);
    });

    it('should close the WebSocket connection', () => {
        // Arrange
        const consoleSpy = jest.spyOn(console, 'log').mockImplementation(() => {});

        // Act
        adaptor.close();

        // Assert
        expect(mockWebSocket.close).toHaveBeenCalled();
        expect(consoleSpy).toHaveBeenCalledWith('Closed announcements adaptor on ws://localhost:8085');
        consoleSpy.mockRestore();
    });
});