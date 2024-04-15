<template>
  <div class="container">
    <div class="center-column-lobby-select-page">
      <div>
        <h2 class="header-title">Find a game lobby</h2>
        <div class="search-bar-lobby-select-page">
          <input type="text" v-model="searchQuery" placeholder="Search Available games...">
        </div>
        <h2 class="header-title">Game/Player</h2>
      </div>
      <div :class="{'scrollable': filteredGames.length > 4}">
        <table>
          <tbody>
          <tr v-for="(game, index) in filteredGames" :key="index" class="player-pill transition"
              @click="selectGame(index)">
            <td>
              <div>{{ game.name }}</div>
            </td>
            <td>
              <div>Players {{ game.players }}/{{ game.maxPlayers }}</div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="buttons-container-lobby-select-page">
        <router-link to="/gameSettings">
          <button class="create-game-button transition">Create Game</button>
        </router-link>
      </div>
    </div>
  </div>
  <popUpLobbySelectComponent :show="showModal"
                             @close="showModal = false"
                             :selectedGame="selectedGame">
  </popUpLobbySelectComponent>
</template>

<script>
import popUpLobbySelectComponent from "@/components/pages/popUpLobbySelectComponent.vue";

export default {
  name: "LobbySelectComponent",
  components: {popUpLobbySelectComponent},

  data() {
    return {
      searchQuery: '',
      games: [
        {name: "random Game", players: 1, maxPlayers: 4},
        {name: "Galactic Conquest", players: 2, maxPlayers: 4},
        {name: "Pirate's Plunder", players: 4, maxPlayers: 4},
        {name: "Jurassic Journey", players: 1, maxPlayers: 4},
        {name: "Fantasy Frenzy", players: 2, maxPlayers: 4},
        {name: "Cybernetic Showdown", players: 4, maxPlayers: 4},
        {name: "The noobs", players: 3, maxPlayers: 4},
        {name: "random Game", players: 1, maxPlayers: 4},
        {name: "Galactic Conquest", players: 2, maxPlayers: 4},
        {name: "Pirate's Plunder", players: 4, maxPlayers: 4},
        {name: "Jurassic Journey", players: 1, maxPlayers: 4},
        {name: "Fantasy Frenzy", players: 2, maxPlayers: 4},
        {name: "Cybernetic Showdown", players: 4, maxPlayers: 4},
        {name: "The noobs", players: 3, maxPlayers: 4},
      ],
      selectedGameIndex: null,
      showModal: false,
    }
  },

  computed: {
    filteredGames() {
      return this.games.filter(game => game.name.toLowerCase().includes(this.searchQuery.toLowerCase()));
    },
    selectedGame() {
      return this.selectedGameIndex !== null ? this.games[this.selectedGameIndex] : null;
    }
  },

  methods: {
    selectGame(index) {
      this.selectedGameIndex = index;
      this.showModal = true;
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
  color: white;
  background-color: #60BFB2;
  text-align: center;
  border-radius: 5px;
  font-weight: bold;
  margin-bottom: 1rem;
}

.search-bar-lobby-select-page input[type="text"] {
  padding: 8px;
  width: 900px;
  border-radius: 10px;
  border: 1px solid #ccc;
  box-sizing: border-box;
  font-weight: bold;
}

.join-game-button {
  color: white;
  background-color: #60BFB2;
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
  color: #60BFB2;
  background-color: white;
}

.create-game-button {
  color: white;
  background-color: #60BFB2;
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
  color: #60BFB2;
  background-color: white;
}

.player-pill {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  margin: 1rem;
  width: 840px;
  height: 50px;
  font-size: 18px;
  font-weight: bold;
}

.player-pill:hover {
  background-color: black;
  color: white;
  cursor: pointer; /* Add cursor pointer when hovering over player pill */
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
