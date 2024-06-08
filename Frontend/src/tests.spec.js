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
            { id: 1, type: 'human', user: { email: 'mockUser@example.com' }, playerColor: 'red', resources: {}, developmentCards: {} },
            { id: 2, type: 'bot', user: { email: 'bot1@example.com' }, playerColor: 'blue', resources: {}, developmentCards: {} },
            { id: 3, type: 'bot', user: { email: 'bot2@example.com' }, playerColor: 'green', resources: {}, developmentCards: {} },
            { id: 4, type: 'bot', user: { email: 'bot3@example.com' }, playerColor: 'yellow', resources: {}, developmentCards: {} },
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
});