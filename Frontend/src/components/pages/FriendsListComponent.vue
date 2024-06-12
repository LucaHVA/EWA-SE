<template>
  <div class="container">
    <h2>Friends List</h2>
    <div class="table-container scrollable">
      <div v-if="friends.length === 0">
        Sadly you don't have friends yet :(
      </div>
      <table v-else>
        <tr v-for="friend in friends" :key="friend.id" class="user-item">
          <td>{{ friend.username }}</td>
          <button @click="declineFriendRequest(friend.id)">Remove Friend</button>
        </tr>
      </table>
    </div>

    <div class="sticky-button-container">
      <button class="search-users-button transition" type="button" @click="openModal(true, false)">
        Search for players
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 icon-small">
          <path stroke-linecap="round" stroke-linejoin="round" d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z" />
        </svg>
      </button>
      <button class="search-users-button transition" type="button" @click="openModal(true, true)">
        Received friend requests
      </button>
    </div>

    <transition name="modal">
      <div v-if="isModalOpen" class="modal-mask" @click="openModal(false, false)">
        <div class="modal-wrapper">
          <div class="modal-container" @click.stop>
            <h2>Search for Players</h2>
            <div class="search-bar">
              <input type="text" placeholder="Search for players..." v-model="searchQuery">
            </div>
            <h2>Search Results</h2>
            <div v-if="!receivedList" class="table-container scrollable">
              <table>
                <tr v-for="user in filteredUsers" :key="user.id" class="user-item">
                  <td>{{ user.username }}</td>
                  <button @click="sendFriendRequest(user.id)">Add as Friend</button>
                </tr>
              </table>
            </div>
            <div v-else class="table-container scrollable">
              <div v-if="receivedFriendRequests.length === 0" >
                You have no friend requests.
              </div>
              <table v-else>
                <tr v-for="user in receivedFriendRequests" :key="user.id" class="user-item">
                  <td>{{ user.username }}</td>
                  <button class="accept-button" @click="acceptFriendRequest(user.id)">Accept friend request</button>
                  <button @click="declineFriendRequest(user.id)">Decline friend request</button>
                </tr>
              </table>
            </div>
            <div class="close-button-box">
              <button class="close-button transition" @click="openModal(false, false)">Close</button>
            </div>
          </div>
        </div>
      </div>
    </transition>

  </div>
</template>

<script>
export default {
  name: "FriendsListComponent",
  inject: ['usersService'],
  data() {
    return {
      friends: [],
      users: [],
      receivedFriendRequests: [],
      friendRequestSend: [],
      searchQuery: "",
      isModalOpen: false,
      currentUser: null,
      receivedList: false,
    };
  },
  async created() {
    await this.fetchFriends();
    await this.fetchReceivedFriendRequests();
    await this.fetchSentFriendRequests();
    this.users = await this.usersService.asyncFindAll();
    this.currentUser = await this.usersService.getCurrentUser;
  },
  computed: {
    filteredUsers() {
      if (!this.currentUser) {
        return [];
      }
      const excludedUserIds = [
        ...this.friends.map(friend => friend.id),
        ...this.receivedFriendRequests.map(req => req.id),
        ...this.friendRequestSend.map(req => req.id),
        this.currentUser.id
      ];
      return this.users.filter(user =>
          user.username.toLowerCase().includes(this.searchQuery.toLowerCase()) &&
          !excludedUserIds.includes(user.id)
      );
    }
  },
  methods: {
    openModal(modal, requests) {
      this.isModalOpen = modal;
      this.receivedList = requests;
    },
    async fetchFriends() {
      const userInfo = await this.usersService.getCurrentUser;
      this.friends = await this.usersService.getFriends(userInfo.id);
    },
    async fetchReceivedFriendRequests() {
      const userInfo = await this.usersService.getCurrentUser;
      this.receivedFriendRequests = await this.usersService.getFriendRequests(userInfo.id);
    },
    async fetchSentFriendRequests() {
      const userInfo = await this.usersService.getCurrentUser;
      this.friendRequestSend = await this.usersService.getSentFriendRequests(userInfo.id);
    },
    async sendFriendRequest(friendId) {
      try {
        const userInfo = await this.usersService.getCurrentUser;
        await this.usersService.sendFriendRequest(userInfo.id, friendId);
        alert("You send a friend request!");
      } catch (error) {
        console.error('Error sending friend request:', error);
        alert("Something went wrong when trying to send a friend request!");
      }
    },
    async acceptFriendRequest(requestId) {
      const userInfo = await this.usersService.getCurrentUser;
      await this.usersService.acceptFriendRequest(userInfo.id, requestId);
      alert("You are now friends :)");
      await this.fetchReceivedFriendRequests();
      await this.fetchFriends();
    },
    async declineFriendRequest(requestId) {
      const userInfo = await this.usersService.getCurrentUser;
      await this.usersService.declineFriendRequest(userInfo.id, requestId);
      alert("You have removed your friend :(");
      await this.fetchReceivedFriendRequests();
      await this.fetchFriends();
    }
  }
}
</script>

<style scoped>
.container {
  text-align: center;
  margin-top: 50px;
  margin-bottom: 10px;
  font-size: 30px;
  font-family: "Inria Sans", sans-serif;
}

/* css voor scrollbar */
::-webkit-scrollbar {
  width: 10px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 10px;
}

::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.search-bar input[type="text"] {
  padding: 15px;
  margin-right: 10px;
  width: 70%;
  border-radius: 10px;
  border: 1px solid var(--light-gray);
  box-sizing: border-box;
  font-size: 16px;
  font-weight: bold;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.2);
}

table tr.user-item {
  border-bottom: 1px solid black!important;
}

.table-container {
  display: flex;
  justify-content: center;
}

.table-container.scrollable {
  max-height: 80%;
  overflow-y: auto;
}

.user-item td {
  padding-right: 5rem;
}

.user-item button {
  background-color: #ff6347;
  border: none;
  color: white;
  padding: 9px 18px;
  cursor: pointer;
  border-radius: 5px;
}

.search-users-button {
  font-family: 'Raleway', sans-serif;
  background-color: var(--shade-of-tea);
  border: none;
  color: var(--white);
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  width: max-content;
  font-size: 20px;
  margin: 2rem 2rem;
  cursor: pointer;
  border-radius: 8px;
  font-weight: bold;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.1);
}

.search-users-button:hover {
  background-color: var(--white);
  color: var(--shade-of-tea);
}

.icon-small {
  width: 25px;
  height: 25px;
  margin-left: 10px;
  vertical-align: middle;
}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.modal-container {
  display: flex;
  flex-direction: column;
  background: #ebeef1;
  padding: 20px 20px 0 20px;
  border-radius: 5px;
  width: 75%;
  max-width: 700px;
  max-height: 85%;
}

.modal-container h2 {
  margin: 1rem;
}

.close-button {
  font-family: 'Raleway', sans-serif;
  background-color: var(--coral);
  border: none;
  color: var(--white);
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  width: max-content;
  font-size: 20px;
  margin: 2rem;
  cursor: pointer;
  border-radius: 8px;
  font-weight: bold;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.1);
}

.close-button:hover {
  background-color: var(--white);
  color: var(--coral);
}

.close-button-box {
  display: flex;
  justify-content: center;
}

.accept-button {
  background-color: var(--shade-of-tea)!important;
}
</style>