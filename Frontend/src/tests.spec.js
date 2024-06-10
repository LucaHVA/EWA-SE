
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
    let readyState = 0; // Initial CONNECTING state
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

process.env.VUE_APP_API_URL = 'http://localhost:3000';

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

    it('should initialize players correctly', async () => {
        jest.spyOn(wrapper.vm, 'fetchGameDetails').mockImplementation(async () => {});
        jest.spyOn(wrapper.vm, 'fetchPlayers').mockImplementation(async () => {});

        await wrapper.vm.$options.created.call(wrapper.vm);

        await wrapper.vm.initializePlayers();

        expect(wrapper.vm.players.length).toBe(4);

        wrapper.vm.players.forEach((player, index) => {
            expect(player).toHaveProperty('type');
            expect(player).toHaveProperty('playerColor');
            expect(player).toHaveProperty('resources');
            expect(player).toHaveProperty('developmentCards');
            expect(player.playerColor).toBe(wrapper.vm.playerColors[index]);
        });

        expect(wrapper.vm.players.filter(p => p.type === 'bot').length).toBeGreaterThan(0);

        expect(wrapper.vm.loggedinPlayerIndex).toBeDefined();
    });

    it('should roll dice correctly and update the UI', () => {
        // Mock Math.random to return fixed values
        jest.spyOn(Math, 'random').mockReturnValueOnce(0.1).mockReturnValueOnce(0.5);

        const updateGameStateSpy = jest.spyOn(wrapper.vm, 'updateGameState').mockImplementation(() => {});
        const updateDiceOutcomeUISpy = jest.spyOn(wrapper.vm, 'updateDiceOutcomeUI').mockImplementation(() => {});

        wrapper.vm.rollDice();

        // Validate the values produced by Math.random mock
        const expectedLeftDiceOutcome = 1 + Math.floor(0.5 * 6); // 4
        const expectedRightDiceOutcome = 1 + Math.floor(0.1 * 6); // 1

        expect(updateGameStateSpy).toHaveBeenCalledWith({
            action: 'rollDice',
            leftDiceOutcome: expectedLeftDiceOutcome,
            rightDiceOutcome: expectedRightDiceOutcome
        });

        expect(updateDiceOutcomeUISpy).toHaveBeenCalledWith(expectedLeftDiceOutcome, expectedRightDiceOutcome);

        Math.random.mockRestore();
    });

    it('should advance to the next turn correctly', () => {
        // Mock valid player objects
        const mockPlayers = [
            { id: 1, type: 'human', resources: [], hasBuiltFirstTurn: false, hasBuiltSecondTurn: false },
            { id: 2, type: 'bot', resources: [], hasBuiltFirstTurn: false, hasBuiltSecondTurn: false },
            // Add more mock players if needed
        ];

        // Set up the game state with mock players
        wrapper.vm.players = mockPlayers;
        wrapper.vm.loggedinPlayerIndex = 0; // Assuming the logged-in player is the first player
        wrapper.vm.currentPlayerIndex = 0; // Assuming it's the first player's turn
        wrapper.vm.turn = 1; // Assuming it's the first turn

        // Spy on dependent methods
        const displayErrorSpy = jest.spyOn(wrapper.vm, 'displayError').mockImplementation(() => {});
        const checkForWinnerSpy = jest.spyOn(wrapper.vm, 'checkForWinner').mockImplementation(() => {});
        const botLogicSpy = jest.spyOn(wrapper.vm, 'botLogic').mockImplementation(() => {});
        const startCountdownSpy = jest.spyOn(wrapper.vm, 'startCountdown').mockImplementation(() => {});
        const updateGameStateSpy = jest.spyOn(wrapper.vm, 'updateGameState').mockImplementation(() => {});

        wrapper.vm.rollDice();
        // Call the nextTurn method
        wrapper.vm.nextTurn();

        // Expectations
        expect(displayErrorSpy).not.toHaveBeenCalled(); // No error should be displayed
        expect(checkForWinnerSpy).toHaveBeenCalled(); // Expect checkForWinner to be called
        expect(botLogicSpy).toHaveBeenCalled(); // Expect botLogic to be called
        expect(startCountdownSpy).toHaveBeenCalled(); // Expect startCountdown to be called
        expect(updateGameStateSpy).toHaveBeenCalledWith({ action: 'nextTurn' }); // Expect updateGameState to be called with the correct parameters
    });

    it('should build correctly when it is the player\'s turn', () => {
        // Mock dependencies
        const currentPlayerIndex = 0; // Assuming the current player is the first player
        const loggedinPlayerIndex = 0; // Assuming the logged-in player is also the first player
        const mockPlayers = [
            { id: 1, type: 'human', resources: ['wood', 'brick', 'sheep', 'wheat'], hasBuiltFirstTurn: false, hasBuiltSecondTurn: false },
            // Mock other players if needed
        ];

        // Set up the game state
        wrapper.vm.currentPlayerIndex = currentPlayerIndex;
        wrapper.vm.loggedinPlayerIndex = loggedinPlayerIndex;
        wrapper.vm.players = mockPlayers;

        // Spy on dependent methods
        const displayErrorSpy = jest.spyOn(wrapper.vm, 'displayError').mockImplementation(() => {});
        const updateGameStateSpy = jest.spyOn(wrapper.vm, 'updateGameState').mockImplementation(() => {});

        // Call the build method
        wrapper.vm.build(0); // Assuming building at index 0

        // Expectations
        expect(displayErrorSpy).not.toHaveBeenCalled(); // No error should be displayed
        expect(updateGameStateSpy).toHaveBeenCalledWith({ action: 'build', index: 0 }); // Expect updateGameState to be called with the correct parameters
    });

});