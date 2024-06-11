import { mount } from '@vue/test-utils';
import LeaderboardComponent from "@/components/pages/LeaderboardComponent.vue";
//heeloo
describe('LeaderboardComponent', () => {
    let wrapper;

    beforeEach(() => {
        // Mocking the playersLeaderBoard data
        const playersLeaderBoard = [
            { id: 1, username: 'user1', points: 100, rank: 1 },
            { id: 2, username: 'user2', points: 90, rank: 2 },
            { id: 3, username: 'user3', points: 80, rank: 3 },
            // { id: 4, username: 'user4', points: 70, rank: 4 },
            // { id: 5, username: 'user5', points: 60, rank: 5 },
            // { id: 6, username: 'user6', points: 50, rank: 6 },
            // { id: 7, username: 'user7', points: 40, rank: 7 },
            // { id: 8, username: 'user8', points: 30, rank: 8 },
            // { id: 9, username: 'user9', points: 20, rank: 9 },
            // { id: 9, username: 'user10', points: 10, rank: 10 },
        ];

        // Mocking the usersService
        const usersService = {
            asyncFindAll: jest.fn().mockResolvedValue(playersLeaderBoard),
            getCurrentUser: jest.fn(),
            sendFriendRequest: jest.fn()
        };

        wrapper = mount(LeaderboardComponent, {
            data() {
                return {
                    playersLeaderBoard: playersLeaderBoard
                };
            },
            global: {
                provide: {
                    usersService: usersService
                }
            }
        });
    });

    it('renders the top 10 users (have 3 fake users)', async () => {
        // Wait for component to update
        await wrapper.vm.$nextTick();

        // Check if the rendered table has 10 rows (excluding the header row)
        const tableRows = wrapper.findAll('tbody > tr');
        expect(tableRows.length).toBe(3); // Assuming the header row is not counted
    });
});
