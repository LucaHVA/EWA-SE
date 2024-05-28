<template>
  <div class="container">
    <div class="center-column-lobby-select-page">
      <div>
        <h2 class="header-title">Find a game lobby</h2>
        <div class="search-bar-lobby-select-page">
          <input type="text" v-model="searchQuery" placeholder="Search available games...">
        </div>
        <h2 class="header-title">Game/Player</h2>
      </div>
      <div :class="{'scrollable': filteredGames.length > 4}">
        <table>
          <tbody>
          <tr v-for="(game) in filteredGames" :key="game" class="player-pill transition"
              @click="selectGame(game)">
            <td>
              <div>{{ game.id }}</div>
            </td>
            <td>
              <div>Players: {{ playerCounts[game.id] }}/{{ game.numberOfPlayers }}</div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="buttons-container-lobby-select-page">
        <button class="create-game-button transition" @click="createGame">Create Game</button>
      </div>
    </div>
  </div>
  <popUpLobbySelectComponent :show="showModal"
                             @close="showModal = false"
                              :selectedGame="selectedGame"
                              :playerCounts="playerCounts">
  </popUpLobbySelectComponent>
</template>

<script>
import popUpLobbySelectComponent from "@/components/pages/popUpLobbySelectComponent.vue";


export default {
  name: "LobbySelectComponent",
  components: {popUpLobbySelectComponent},
  inject: ['gameService', 'usersService'],

  data() {
    return {
      searchQuery: '',
      games: [],
      playerCounts: {},
      selectedGame: null,
      showModal: false,
    }
  },

  async created() {
    this.games = await this.gameService.asyncGetAllFiltered();

    this.games.forEach(game => {
      this.getCurrentAmountOfPlayers(game.id);
    });
  },

  computed: {
    filteredGames() {
      return this.games.filter(game => game.id.includes(this.searchQuery.toUpperCase()));
    },
  },

  methods: {
    selectGame(game) {
      this.selectedGame = game;
      this.showModal = true;
    },
    async createGame() {
      // Get current user (host)
      const currentUser = await this.usersService.getCurrentUser;
      // Generate new game
      const game = await this.gameService.createGame(currentUser);

      await this.gameService.addNewPlayerToGame(game.id, currentUser, 1);

      // Route to next page
      this.$router.replace({name: 'gameSettings', params: {id: game.id}});
    },
    async getCurrentAmountOfPlayers(gameId) {
      try {
        // Find players of a game
        let players = await this.gameService.asyncFindAllPlayersForGameId(gameId);
        // Add key:value to current player counts
        this.playerCounts[gameId] = players.length;
      } catch (e) {
        console.error(e);
      }
    }
  }
}
</script>

<style scoped>
.transition {
  transition: all 0.4s ease;
}

.transition:hover {
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
  border: none;
}

.center-column-lobby-select-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1rem;
  justify-content: space-between;
}

.header-title {
  color: var(--white);
  background-color: var(--shade-of-tea);
  text-align: center;
  border-radius: 5px;
  font-weight: bold;
  margin-bottom: 1rem;
}

.search-bar-lobby-select-page input[type="text"] {
  padding: 8px;
  width: 900px;
  border-radius: 10px;
  border: 1px solid var(--white);
  box-sizing: border-box;
  font-weight: bold;
}

.join-game-button {
  color: var(--white);
  background-color: var(--shade-of-tea);
  font-size: 20px;
  font-weight: bold;
  border: none;
  width: 200px;
  height: 40px;
  border-radius: 10px;
  margin: auto;
  text-decoration: none;
}

.join-game-button:hover {
  color: var(--shade-of-tea);
  background-color: var(--white);
}

.create-game-button {
  color: var(--white);
  background-color: var(--shade-of-tea);
  font-size: 20px;
  font-weight: bold;
  border: none;
  width: 200px;
  height: 40px;
  border-radius: 10px;
  margin: auto;
  text-decoration: none;
}

.create-game-button:hover {
  color: var(--shade-of-tea);
  background-color: var(--white);
}

.player-pill {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: var(--white);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  margin: 1rem;
  width: 840px;
  height: 50px;
  font-size: 18px;
  font-weight: bold;
}

.player-pill:hover {
  background-color: var(--black);
  color: white;
  cursor: pointer;
}

.buttons-container-lobby-select-page {
  margin-top: 5rem;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.scrollable {
  max-height: 350px;
  overflow-y: auto;
}
</style>

