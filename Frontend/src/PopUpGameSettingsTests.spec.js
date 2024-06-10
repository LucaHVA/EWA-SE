import { shallowMount } from '@vue/test-utils';
import { createRouter, createWebHistory } from 'vue-router';
import { describe, it, beforeEach, expect, jest } from '@jest/globals';

import PopUpGameSettings from '@/components/pages/PopUpGameSettingsComponent.vue';
import Game from "@/models/game";
import GameComponent from '@/components/pages/Game.vue';


const mockGameService = {
    saveGame: jest.fn().mockImplementation(async (game) => {
        return game;
    })
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
describe('PopUpGameSettingsComponent', () => {
    let wrapper;

    beforeEach(async () => {
        const router = createRouter({
            history: createWebHistory(),
            routes: [
                { path: '/gameSettings/:id', component: PopUpGameSettings },
                { name: 'game', path: '/game/:id', component: GameComponent },
            ]
        });

        router.replace = jest.fn();

        wrapper = shallowMount(PopUpGameSettings, {
            propsData: {
                show: true,
                numberOfPlayers: 4,
                turnDuration: 30,
                pointsToWin: 10,
                botCount: 0,
                totalPlayers: 1,
                gameSettings: {id: 'ABCDE'}
            },
            global: {
                plugins: [router],
                provide: {
                    gameService: mockGameService,
                },
                mocks: {
                    announcementsService: announcementsServiceMock,
                    $route: {
                        params: {
                            id: 'ABCDE'
                        }
                    }
                }
            }
        });

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
    it('returns the correct missing player amount', async() => {
        // Test for insufficient players
        await wrapper.setProps({ totalPlayers: 2 });
        expect(wrapper.vm.isCorrectPlayerCount).toBe(2);

        // Test for sufficient players
        await wrapper.setProps({ totalPlayers: 4 });
        expect(wrapper.vm.isCorrectPlayerCount).toBe(0);

        // Test for too many players
        await wrapper.setProps({ totalPlayers: 5 });
        expect(wrapper.vm.isCorrectPlayerCount).toBe(-1);
    });

    /**
     * @Author Stephanie
     */
    it('saves the game and continues to next page', async () => {
        const expectedGame = new Game(
            wrapper.props().gameSettings.id,
            wrapper.props().numberOfPlayers,
            wrapper.props().turnDuration,
            wrapper.props().pointsToWin,
        );
        // Make the game valid to start
        await wrapper.setProps({ totalPlayers: 4 });
        await wrapper.setProps({ isHost: true });

        // Call the method under test
        await wrapper.vm.initializeGame();

        // Assert saveGame was called with the correct game object
        expect(await mockGameService.saveGame).toHaveBeenCalledWith(expectedGame);

        // Assert router.replace was called with the correct route
        expect(wrapper.vm.$router.replace).toHaveBeenCalledWith({
            name: 'game',
            params: { id: wrapper.props().gameSettings.id }
        });
    });

});