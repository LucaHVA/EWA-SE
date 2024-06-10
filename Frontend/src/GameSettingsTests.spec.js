import { shallowMount } from '@vue/test-utils';
import { createRouter, createWebHistory } from 'vue-router';
import { describe, it, beforeEach, expect, jest, test } from '@jest/globals';

import GameSettings from '@/components/pages/GameSettingsComponent.vue';
import PopUpGameSettings from '@/components/pages/PopUpGameSettingsComponent.vue';

const mockGameService = {
    saveGame: jest.fn().mockImplementation(async (game) => {
        return game;
    }),
    addNewPlayerToGame: jest.fn().mockImplementation(async (player) => {
        return player;
    }),
    async asyncGetById(gameId) {
        return {
            id: gameId,
        };
    },

    asyncFindAllPlayersForGameId: jest.fn().mockResolvedValue([
        { user: { id: 1 } },
        { user: { id: 2 } },
    ]),
};

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

// Mock the announcementsService with a socket object
const announcementsServiceMock = {
    socket: {
        simulateOpen: jest.fn(),
    }
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

// Mock router
const routerMock = {
    replace: jest.fn()
};

process.env.VUE_APP_API_URL = 'http://localhost:3000';

// Pop up in game settings
describe('GameSettingsComponent', () => {
    let wrapper;

    beforeEach(async () => {
        const router = createRouter({
            history: createWebHistory(),
            routes: [
                {path: '/gameSettings/:id', component: GameSettings},
            ]
        });

        router.replace = jest.fn();

        wrapper = shallowMount(GameSettings, {
            propsData: {
                show: true,
                numberOfPlayers: 4,
                turnDuration: 60,
                pointsToWin: 8,
                gameId: 'ABCDE',
                userDetails: await mockUsersService._currentUser(),
                currentGame: {
                    id: 'ABCDE',
                    host: {
                        id: 1
                    }
                }
            },
            global: {
                plugins: [router],
                provide: {
                    gameService: mockGameService,
                    usersService: mockUsersService,
                },
                mocks: {
                    announcementsService: announcementsServiceMock,
                    $route: {
                        params: {
                            id: 'ABCDE'
                        }
                    }
                },
            }
        });

        wrapper.vm.fetchGameById = jest.fn().mockResolvedValue({
            id: 'ABCDE',
            host: {
                id: 1
            }
        });

        wrapper.vm.currentGame.host = await mockUsersService._currentUser();

        await wrapper.vm.$router.push('/gameSettings/ABCDE');
        await wrapper.vm.$router.isReady();

        // Ensure announcementsService is properly instantiated
        await wrapper.vm.$nextTick();
        const socketInstance = wrapper.vm.announcementsService.socket;
        socketInstance.simulateOpen();
    });

    /**
     * @Author Stephanie
     */
    it('adds the current user to the game if not already a player', async () => {
        // Set up a spy
        const onUpdatePlayersAnnouncementSpy = jest.spyOn(wrapper.vm, 'onUpdatePlayersAnnouncement');

        // Mock the isCurrentUserAlreadyPlayer method to return false
        wrapper.vm.isCurrentUserAlreadyPlayer = jest.fn().mockResolvedValue(false);

        // Call the addCurrentUserToGame method
        await wrapper.vm.addCurrentUserToGame();

        // Assert that addNewPlayerToGame was called with the correct parameters
        expect(await mockGameService.addNewPlayerToGame).toHaveBeenCalledWith(
            wrapper.vm.currentGame.id,
            wrapper.vm.userDetails
        );

        // Assert that onUpdatePlayersAnnouncement was called
        expect(onUpdatePlayersAnnouncementSpy).toHaveBeenCalled();

        // Remove spy
        onUpdatePlayersAnnouncementSpy.mockRestore();
    });

    /**
     * @Author Stephanie
     */
    it('should determine if the current user is already part of this game', async () => {
        // User that already in the game instance
        wrapper.vm.userDetails = { id: 1 };
        // Call the method with the mocked services
        const isPlayerTrue = await wrapper.vm.isCurrentUserAlreadyPlayer();
        // Assertions
        expect(mockGameService.asyncFindAllPlayersForGameId).toHaveBeenCalledWith('ABCDE');
        expect(isPlayerTrue).toBe(true);

        // User that is not in the game instance
        wrapper.vm.userDetails = { id: 3 };
        // Call the method with the mocked services
        const isPlayerFalse = await wrapper.vm.isCurrentUserAlreadyPlayer();
        // Assertions
        expect(mockGameService.asyncFindAllPlayersForGameId).toHaveBeenCalledWith('ABCDE');
        expect(isPlayerFalse).toBe(false);
    });

    /**
     * @Author Stephanie
     */
    it('host kicks another player from the game', async () => {
        // Mock players data
        const players = [
            { user: { id: 1 }, host: true },
            { user: { id: 2 }, host: false }
        ];
        wrapper.vm.players = players;

        // Mock the deletePlayerFromGame
        mockGameService.deletePlayerFromGame = jest.fn().mockResolvedValue({});

        // Mock the announcementsService.sendMessage
        wrapper.vm.announcementsService.sendMessage = jest.fn();

        // Index of the player to kick and their number
        const indexToKick = 1;
        const playerNumberToKick = 2;

        // Call the kickPlayer method
        await wrapper.vm.kickPlayer(indexToKick, playerNumberToKick);

        // Assert that deletePlayerFromGame was called with the correct parameters
        expect(mockGameService.deletePlayerFromGame).toHaveBeenCalledWith(
            wrapper.vm.gameId,
            playerNumberToKick
        );

        // Assert that sendMessage was called with the correct message
        expect(wrapper.vm.announcementsService.sendMessage).toHaveBeenCalledWith(
            JSON.stringify({ action: 'playerKicked', kickedId: players[indexToKick].user.id })
        );
    });

});