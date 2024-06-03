<template>
  <div class="leaderboard-container">
    <h1 class="leaderboard-title">Leaderboard</h1>
    <table class="leaderboard-table">
      <thead>
      <tr>
        <th class="leaderboard-th">Rank</th>
        <th class="leaderboard-th">Player</th>
        <th class="leaderboard-th">Points</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="player in limitedPlayersLeaderBoard" :key="player.id" class="leaderboard-tr transition"
          @click="showPlayerDetails(player, false)">
        <td>{{ player.rank }}</td>
        <td>{{ player.username }}</td>
        <td>{{ player.points }}</td>
      </tr>
      </tbody>
    </table>
    <button v-if="playersLeaderBoard.length > 10" @click="showMorePlayers"
            class="see-more-button transition">
      See More
    </button>
    <transition name="modal">
      <div v-if="isModalOpen" class="modal-mask" @click="closeModal">
        <div class="modal-wrapper">
          <div class="modal-container" :class="{ 'large-modal': showAllPlayers }" @click.stop>
            <div class="input_box">
              <input v-if="showAllPlayers" type="text" v-model="searchQuery" ref="searchInput" @blur="keepFocus"/>
              <span v-if="showAllPlayers">Search players by name</span>
            </div>
            <h1 v-if="!showAllPlayers">Player Details</h1>
            <h1 v-else>All Players</h1>
            <div v-if="selectedPlayer && !showAllPlayers && !friendRequestSent">
              <p><strong>Name: </strong> {{ selectedPlayer.username }}</p>
              <p><strong>Points: </strong>{{ selectedPlayer.points }}</p>
              <p><strong>Rank: </strong> {{ selectedPlayer.rank }}</p>
              <button class="orange-button transition" @click="sendFriendRequest(selectedPlayer.id)">Send friend request</button>
            </div>
            <div v-if="friendRequestSent && !showAllPlayers">
              <h3>You sent a friend request to {{ selectedPlayer.username }}!</h3>
            </div>
            <div v-if="showAllPlayers" class="modal-scrollable-content">
              <table class="modal-leaderboard-table">
                <thead>
                <tr>
                  <th class="leaderboard-th">Rank</th>
                  <th class="leaderboard-th">Player</th>
                  <th class="leaderboard-th">Points</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="player in filteredPlayersLeaderBoard" :key="player.id" class="leaderboard-tr transition"
                    @click.stop="showPlayerDetails(player, true)">
                  <td>{{ player.rank }}</td>
                  <td>{{ player.username }}</td>
                  <td>{{ player.points }}</td>
                </tr>
                </tbody>
              </table>
            </div>
            <button class="orange-button transition" @click="closeModal">Close</button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import {nextTick} from "vue";

export default {
  name: "LeaderboardComponent",
  inject: ['usersService'],
  data() {
    return {
      playersLeaderBoard: [],
      displayLimit: 10,
      searchQuery: '',
      isModalOpen: false,
      selectedPlayer: null,
      showAllPlayers: false,
      friendRequestSent: false, // new state property
    };
  },

  async created() {
    await this.fetchAllUsers();
    this.rankPlayers();
  },

  computed: {
    limitedPlayersLeaderBoard() {
      return this.playersLeaderBoard.slice(0, this.displayLimit);
    },
    filteredPlayersLeaderBoard() {
      return this.playersLeaderBoard.filter(player => player.username.toLowerCase().includes(this.searchQuery.toLowerCase()));
    }
  },

  methods: {
    async sendFriendRequest(friendId) {
      try {
        const userInfo = await this.usersService.getCurrentUser;
        await this.usersService.sendFriendRequest(userInfo.id, friendId);
        this.friendRequestSent = true; // update state
      } catch (error) {
        console.error('Error sending friend request:', error);
        alert("Something went wrong when trying to send a friend request!");
      }
    },
    showMorePlayers() {
      this.showAllPlayers = true;
      this.isModalOpen = true;
      document.body.style.overflow = 'hidden';
    },
    showPlayerDetails(player, fromModal) {
      if (fromModal) {
        this.selectedPlayer = player;
        this.showAllPlayers = false;
      } else {
        this.selectedPlayer = player;
        this.isModalOpen = true;
        this.showAllPlayers = false;
      }
      this.friendRequestSent = false; // reset state when showing player details
      document.body.style.overflow = 'hidden';
    },
    closeModal() {
      this.isModalOpen = false;
      this.selectedPlayer = null;
      this.showAllPlayers = false;
      this.searchQuery = '';
      this.friendRequestSent = false; // reset state when closing modal
      document.body.style.overflow = '';
    },
    async fetchAllUsers() {
      try {
        this.playersLeaderBoard = await this.usersService.asyncFindAll();
        this.rankPlayers();
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },
    rankPlayers() {
      this.playersLeaderBoard.sort((a, b) => b.points - a.points);
      this.playersLeaderBoard.forEach((player, index) => {
        player.rank = index + 1;
      });
    },
    keepFocus() {
      if (this.searchQuery) {
        //ensures that the DOM updates are applied before running the code
        nextTick(() => {
          this.$refs.searchInput && this.$refs.searchInput.focus();
        });
      }
    }
  },
};
</script>

<style scoped>
/*Leaderboard styling*/
.leaderboard-container {
  width: 80%;
  margin: 0 auto;
  text-align: center;
  position: relative;
  bottom: 50px;
}

.leaderboard-title {
  font-size: 2em;
  margin-bottom: 20px;
}

.leaderboard-table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}

th, td {
  padding: 10px;
}

th {
  background-color: #f4f4f4;
}

.leaderboard-th {
  font-weight: bolder;
  align-items: center;
  justify-content: center;
  padding: 0.5rem 1rem;
  font-size: 18px;
  width: 250px;
  height: 30px;
}

.leaderboard-tr {
  font-weight: bold;
  align-items: center;
  justify-content: center;
  padding: 0.5rem 1rem;
  width: 250px;
  height: 30px;
}

.leaderboard-tr:hover {
  background-color: var(--black);
  color: var(--white);
}

/* Modal Styles */
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  transition: opacity 0.5s ease;
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
  align-items: center;
  width: 450px;
  max-height: 80vh;
  padding: 20px 30px;
  background-color: var(--white);
  border-radius: 2px;
  box-shadow: 0 15px 20px rgba(0, 0, 0, 0.5);
  transition: all 0.4s ease;
  overflow: hidden;
}

.large-modal {
  width: 80%;
  height: 80%;
}

.modal-scrollable-content {
  overflow-y: auto;
  max-height: calc(80vh - 60px);
}

.modal-leaderboard-table {
  width: 100%;
  border-collapse: collapse;
}

.modal-leaderboard-table th, .modal-leaderboard-table td {
  padding: 10px;
}

.modal-leaderboard-table th {
  background-color: #f4f4f4;
}

.modal-leaderboard-table .leaderboard-th {
  font-weight: bolder;
  background-color: var(--white);
  padding: 0.5rem 1rem;
}

.modal-leaderboard-table .leaderboard-tr {
  font-weight: bolder;
  padding: 0.5rem 1rem;
}

/* Transition for modal */
.modal-enter-active, .modal-leave-active {
  transition: opacity 0.5s, transform 0.5s;
}

.modal-enter-from, .modal-leave-to {
  opacity: 0;
  transform: scale(1.3);
}

/*buttons*/
.orange-button {
  font-family: 'Raleway', sans-serif;
  background-color: var(--coral);
  border: none;
  color: var(--white);
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  width: max-content;
  font-size: 20px;
  margin: 6px 2rem;
  cursor: pointer;
  border-radius: 8px;
  font-weight: bold;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.1);
}

.orange-button:hover {
  background-color: var(--white);
  color: var(--coral);
}

.see-more-button {
  font-family: 'Raleway', sans-serif;
  background-color: var(--coral);
  border: none;
  color: var(--white);
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  width: max-content;
  font-size: 20px;
  margin: 0 0 10px 0;
  cursor: pointer;
  border-radius: 8px;
  font-weight: bold;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.1);
}

.see-more-button:hover {
  background-color: var(--white);
  color: var(--coral);
}

/*search bar*/
.input_box {
  display: flex;
  position: relative;
  width: 80%;
}

.input_box input {
  width: 100%;
  padding: 10px;
  border: 2px solid var(--black);
  background: var(--white);
  border-radius: 5px;
  outline: none;
  color: var(--coral);
  font-size: 1em;
  font-weight: bold;
}

.input_box span {
  position: absolute;
  left: 0;
  padding: 10px;
  pointer-events: none;
  font-size: 1em;
  color: var(--black);
  text-transform: uppercase;
  transition: 0.5s;
}

.input_box input:focus ~ span {
  color: var(--coral);
  transform: translateX(10px) translateY(-7px);
  font-size: 0.8em;
  padding: 0 10px;
  background: var(--white);
  border-left: 2px solid var(--coral);
  border-right: 2px solid var(--coral);
  letter-spacing: 0.2em;
}

.input_box input:focus {
  border: 3px solid var(--coral);
}
</style>
