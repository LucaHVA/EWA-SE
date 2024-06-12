import { mount } from "@vue/test-utils";
import FriendsListComponent from "@/components/pages/FriendsListComponent.vue";

describe('FriendsListComponent', () => {
    let wrapper;
    let mockUsersService;

    beforeEach(() => {
        // Mocking the usersService
        mockUsersService = {
            asyncFindAll: jest.fn().mockResolvedValue([]),
            getCurrentUser: { id: 1 },
            getFriends: jest.fn().mockResolvedValue([]),
            getFriendRequests: jest.fn().mockResolvedValue([]),
            getSentFriendRequests: jest.fn().mockResolvedValue([])
        };

        wrapper = mount(FriendsListComponent, {
            global: {
                provide: {
                    usersService: mockUsersService
                }
            }
        });
    });

    it('renders the correct initial message when there are no friends', async () => {
        // Arrange
        await wrapper.vm.$nextTick(); // Wait for any pending updates

        // Act
        const message = wrapper.text();

        // Assert
        expect(message).toContain("Sadly you don't have friends yet :(");
    });

    it('displays a friend\'s username in the list', async () => {
        // Arrange
        const friends = [{ id: 2, username: "JohnDoe" }];
        mockUsersService.getFriends.mockResolvedValue(friends);

        // Act
        await wrapper.vm.fetchFriends();
        await wrapper.vm.$nextTick();
        const message = wrapper.text();

        // Assert
        expect(message).toContain("JohnDoe");
    });

    it('opens the modal when the button is clicked', async () => {
        // Arrange
        const button = wrapper.find('button.search-users-button');

        // Act
        await button.trigger('click');

        // Assert
        expect(wrapper.vm.isModalOpen).toBe(true);
    });
});
