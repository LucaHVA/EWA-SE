
import { shallowMount } from '@vue/test-utils';
import { createRouter, createWebHistory } from 'vue-router';
import Game from '@/components/pages/Game.vue';
import { describe, it, beforeEach, expect, jest } from '@jest/globals';

// Mock object for usersService
const mockUsersService = {
    async _currentUser() {
        return {
            id: 1,
            username: "mockUser",
            email: "mockUser@example.com"
        };
    },
};

// Mock object for gameService
const mockGameService = {
    async asyncGetById(gameId) {
        return {
            id: gameId,
        };
    },
    async asyncFindAllPlayersForGameId(gameId) {
        return [
            { id: 1, type: 'human', user: { email: 'mockUser@example.com' }, playerColor: 'red', resources: [], developmentCards: {}, hasBuiltFirstTurn: false, hasBuiltSecondTurn: false },
            { id: 2, type: 'bot', user: { email: 'bot1@example.com' }, playerColor: 'blue', resources: [], developmentCards: {} },
            { id: 3, type: 'bot', user: { email: 'bot2@example.com' }, playerColor: 'green', resources: [], developmentCards: {} },
            { id: 4, type: 'bot', user: { email: 'bot3@example.com' }, playerColor: 'yellow', resources: [], developmentCards: {} },
        ];
    },
};

// Mock WebSocket
global.WebSocket = jest.fn(() => {
    const events = {};
    let readyState = 0;
    return {
        send: jest.fn(),
        close: jest.fn(),
        addEventListener: (event, handler) => {
            events[event] = handler;
        },
        removeEventListener: (event) => {
            delete events[event];
        },
        simulateOpen: () => {
            readyState = WebSocket.OPEN;
            if (events.open) {
                events.open();
            }
        },
        simulateMessage: (data) => {
            if (events.message) {
                events.message({ data });
            }
        },
        simulateError: (error) => {
            if (events.error) {
                events.error(error);
            }
        },
        simulateClose: () => {
            readyState = WebSocket.CLOSED;
            if (events.close) {
                events.close();
            }
        },
        get readyState() {
            return readyState;
        },
    };
});


describe('Game.vue', () => {
    let wrapper;

    beforeEach(async () => {
        const router = createRouter({
            history: createWebHistory(),
            routes: [{ path: '/game/:id', component: Game }]
        });

        router.push('/game/123');
        await router.isReady();

        wrapper = shallowMount(Game, {
            global: {
                plugins: [router],
                provide: {
                    usersService: mockUsersService,
                    gameService: mockGameService,
                },
                mocks: {
                    $route: {
                        params: {
                            id: '123'
                        }
                    }
                }
            },
        });

        // Ensure announcementsService is properly instantiated
        await wrapper.vm.$nextTick();
        const socketInstance = wrapper.vm.announcementsService.socket;
        socketInstance.simulateOpen();
    });

    // Test to initialize players correctly
    it('should initialize players correctly', async () => {
        // Mock the fetchGameDetails and fetchPlayers methods to not perform actual API calls
        jest.spyOn(wrapper.vm, 'fetchGameDetails').mockImplementation(async () => {});
        jest.spyOn(wrapper.vm, 'fetchPlayers').mockImplementation(async () => {});

        // Call the created lifecycle hook to initialize the component
        await wrapper.vm.$options.created.call(wrapper.vm);

        // Call the initializePlayers method to set up players
        await wrapper.vm.initializePlayers();

        // Assert that the players array has 4 players
        expect(wrapper.vm.players.length).toBe(4);

        // Assert that each player has the required properties and correct colors
        wrapper.vm.players.forEach((player, index) => {
            expect(player).toHaveProperty('type');
            expect(player).toHaveProperty('playerColor');
            expect(player).toHaveProperty('resources');
            expect(player).toHaveProperty('developmentCards');
            expect(player.playerColor).toBe(wrapper.vm.playerColors[index]);
        });

        // Assert that there are bot players in the players array
        expect(wrapper.vm.players.filter(p => p.type === 'bot').length).toBeGreaterThan(0);

        // Assert that loggedinPlayerIndex is defined
        expect(wrapper.vm.loggedinPlayerIndex).toBeDefined();
    });

// Test to check dice roll functionality
    it('should roll dice correctly and update the UI', () => {
        // Mock Math.random to return fixed values for predictable outcomes
        jest.spyOn(Math, 'random').mockReturnValueOnce(0.1).mockReturnValueOnce(0.5);

        // Spy on the methods that update the game state and UI
        const updateGameStateSpy = jest.spyOn(wrapper.vm, 'updateGameState').mockImplementation(() => {});
        const updateDiceOutcomeUISpy = jest.spyOn(wrapper.vm, 'updateDiceOutcomeUI').mockImplementation(() => {});

        // Call the rollDice method
        wrapper.vm.rollDice();

        // Validate the expected dice outcomes based on mocked Math.random values
        const expectedLeftDiceOutcome = 1 + Math.floor(0.5 * 6); // 4
        const expectedRightDiceOutcome = 1 + Math.floor(0.1 * 6); // 1

        // Assert that the game state was updated with the correct dice outcomes
        expect(updateGameStateSpy).toHaveBeenCalledWith({
            action: 'rollDice',
            leftDiceOutcome: expectedLeftDiceOutcome,
            rightDiceOutcome: expectedRightDiceOutcome
        });

        // Assert that the UI was updated with the correct dice outcomes
        expect(updateDiceOutcomeUISpy).toHaveBeenCalledWith(expectedLeftDiceOutcome, expectedRightDiceOutcome);

        // Restore the original Math.random method
        Math.random.mockRestore();
    });

// Test to advance to the next turn correctly
    it('should advance to the next turn correctly', () => {
        // Mock valid player objects
        const mockPlayers = [        { id: 1, type: 'human', resources: [], hasBuiltFirstTurn: false, hasBuiltSecondTurn: false },
            { id: 2, type: 'bot', resources: [], hasBuiltFirstTurn: false, hasBuiltSecondTurn: false },
        ];

        // Set up the game state with mock players
        wrapper.vm.players = mockPlayers;
        wrapper.vm.loggedinPlayerIndex = 0;
        wrapper.vm.currentPlayerIndex = 0;
        wrapper.vm.turn = 1;

        // Spy on dependent methods to check if they are called correctly
        const displayErrorSpy = jest.spyOn(wrapper.vm, 'displayError').mockImplementation(() => {});
        const checkForWinnerSpy = jest.spyOn(wrapper.vm, 'checkForWinner').mockImplementation(() => {});
        const botLogicSpy = jest.spyOn(wrapper.vm, 'botLogic').mockImplementation(() => {});
        const startCountdownSpy = jest.spyOn(wrapper.vm, 'startCountdown').mockImplementation(() => {});
        const updateGameStateSpy = jest.spyOn(wrapper.vm, 'updateGameState').mockImplementation(() => {});

        // Call the rollDice method to simulate a dice roll
        wrapper.vm.rollDice();

        // Call the nextTurn method to advance to the next turn
        wrapper.vm.nextTurn();

        // Assert that no error was displayed
        expect(displayErrorSpy).not.toHaveBeenCalled();

        // Assert that the checkForWinner method was called
        expect(checkForWinnerSpy).toHaveBeenCalled();

        // Assert that the botLogic method was called
        expect(botLogicSpy).toHaveBeenCalled();

        // Assert that the startCountdown method was called
        expect(startCountdownSpy).toHaveBeenCalled();

        // Assert that the game state was updated with the correct action
        expect(updateGameStateSpy).toHaveBeenCalledWith({ action: 'nextTurn' });
    });

// Test to check build functionality when it's the player's turn
    it('should build correctly when it is the player\'s turn', () => {
        // Mock dependencies for the test
        const currentPlayerIndex = 0;
        const loggedinPlayerIndex = 0;
        const mockPlayers = [        { id: 1, type: 'human', resources: ['wood', 'brick', 'sheep', 'wheat'], hasBuiltFirstTurn: false, hasBuiltSecondTurn: false },
        ];

        // Set up the game state with the mock players
        wrapper.vm.currentPlayerIndex = currentPlayerIndex;
        wrapper.vm.loggedinPlayerIndex = loggedinPlayerIndex;
        wrapper.vm.players = mockPlayers;

        // Spy on dependent methods to check if they are called correctly
        const displayErrorSpy = jest.spyOn(wrapper.vm, 'displayError').mockImplementation(() => {});
        const updateGameStateSpy = jest.spyOn(wrapper.vm, 'updateGameState').mockImplementation(() => {});

        // Call the build method with the player index
        wrapper.vm.build(0);

        // Assert that no error was displayed
        expect(displayErrorSpy).not.toHaveBeenCalled();

        // Assert that the game state was updated with the correct action and index
        expect(updateGameStateSpy).toHaveBeenCalledWith({ action: 'build', index: 0 });
    });

});