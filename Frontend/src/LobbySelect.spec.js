import { shallowMount } from '@vue/test-utils';
import { createRouter, createWebHistory } from 'vue-router';
import LobbySelectComponent from '@/components/pages/LobbySelectComponent.vue';
import { describe, it, beforeEach, expect, jest } from '@jest/globals';

// Mock object for usersService
const mockUsersService = {
    async getCurrentUser() {
        return {
            id: 1,
            username: "mockUser",
            email: "mockUser@example.com"
        };
    },
};

// Mock object for gameService
const mockGameService = {
    async asyncGetAllFiltered() {
        return [
            {
                id: 'GAME123',
                numberOfPlayers: 4,
                host: { id: 1, username: "hostUser", email: "host@example.com" },
                duration: 60, // Duration in minutes
                status: "open",
            },
            {
                id: 'GAME456',
                numberOfPlayers: 4,
                host: { id: 2, username: "anotherHost", email: "anotherhost@example.com" },
                duration: 45,
                status: "closed",
            },
        ];
    },
    async asyncFindAllPlayersForGameId(gameId) {
        if (gameId === 'GAME123') {
            return [
                { id: 1, type: 'human', user: { email: 'mockUser@example.com' } },
                { id: 2, type: 'bot', user: { email: 'bot1@example.com' } },
            ];
        } else {
            return [
                { id: 1, type: 'bot', user: { email: 'bot2@example.com' } },
            ];
        }
    },
    async createGame(currentUser) {
        return {
            id: 'GAME122',
            numberOfPlayers: 4,
            host: currentUser,
        };
    },
    async addNewPlayerToGame(gameId, user) {
        // Mock implementation
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

describe('LobbySelectComponent.vue', () => {
    let wrapper;

    beforeEach(async () => {
        const router = createRouter({
            history: createWebHistory(),
            routes: [{ path: '/lobbySelect', component: LobbySelectComponent }]
        });

        router.push('/');
        await router.isReady();

        wrapper = shallowMount(LobbySelectComponent, {
            global: {
                plugins: [router],
                provide: {
                    usersService: mockUsersService,
                    gameService: mockGameService,
                },
            },
        });

        // Ensure announcementsService is properly instantiated
        await wrapper.vm.$nextTick();
        const socketInstance = wrapper.vm.announcementsService.socket;
        socketInstance.simulateOpen();
    });

    it('should fetch and display games correctly', async () => {
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.games.length).toBe(2);
        expect(wrapper.vm.games[0].id).toBe('GAME123');
        expect(wrapper.vm.games[1].id).toBe('GAME456');
    });

    it('should correctly filter games based on search query', async () => {
        wrapper.setData({ searchQuery: 'GAME123' });
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.filteredGames.length).toBe(1);
        expect(wrapper.vm.filteredGames[0].id).toBe('GAME123');
    });

    it('should correctly initialize player counts for each game', async () => {
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.playerCounts['GAME123']).toBe(2);
        expect(wrapper.vm.playerCounts['GAME456']).toBe(1);
    });



    it('should show modal when a game is selected', async () => {
        wrapper.vm.selectGame({ id: 'GAME123' });
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.selectedGame.id).toBe('GAME123');
        expect(wrapper.vm.showModal).toBe(true);
    });

});






