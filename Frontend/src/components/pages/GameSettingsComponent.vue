<!--testing-->

<template>
  <div class="container">
    <div class="left-column-gamesettings-page">
      <div class="left-column-players">
        <h2 class="header-title">Players ({{ totalPlayers }})</h2>
      </div>
      <div class="left-column-players-in-lobby">
        <div v-for="(player, index) in playerNames" :key="index" class="player-pill transition">
          <p class="player-name">{{ player.name }} <span v-if="player.host">(Host)</span></p>
          <div class="player-status">
            <button class="ready-button transition">Ready</button>
            <button v-if="!player.host" class="kick-button transition" @click="kickPlayer(index)">Kick
            </button>
          </div>
        </div>
        <div v-if="canAddBot" class="player-pill transition">
          <button class="add-bot-button transition" @click="addBot">Add Bot</button>
        </div>
      </div>
    </div>
    <div class="center-column-gamesettings-page">
      <div>
        <h2 class="header-title">Game Settings</h2>
      </div>
      <div>
        <h3>Number of Players</h3>
        <span>{{ this.currentGame.numberOfPlayers }}</span>
        <input type="range" min="2" max="4" v-model.number="currentGame.numberOfPlayers" class="transition center-column-slider">
      </div>
      <div>
        <h3>Turn Duration (In seconds)</h3>
        <span>{{ this.currentGame.turnDuration}}</span>
        <input type="range" min="30" max="90" v-model.number="currentGame.turnDuration" class="transition center-column-slider">
      </div>
      <div>
        <h3>Points to win</h3>
        <span>{{ this.currentGame.pointsToWin }}</span>
        <input type="range" min="8" max="10" v-model.number="currentGame.pointsToWin" class="transition center-column-slider">
      </div>
      <div class="start-game-div">
        <button class="start-game-button transition" @click="showModal = true">Start Game</button>
      </div>
    </div>
    <div class="right-column-gamesettings-page"></div>
    <popUpGameSettingsComponent :show="showModal"
                                @close="showModal = false"
                                :botCount="botCount"
                                :totalPlayers="totalPlayers"

                                :gameSettings="currentGame"
                                :numberOfPlayers="currentGame.numberOfPlayers"
                                :turnDuration="currentGame.turnDuration"
                                :pointsToWin="currentGame.pointsToWin">
    </popUpGameSettingsComponent>


  </div>
</template>

<script>
import popUpGameSettingsComponent from "@/components/pages/popUpGameSettingsComponent.vue";

export default {
  name: "GameSettingsComponent",
  components: {
    popUpGameSettingsComponent,
  },
  inject:['gameService', 'usersService', '$socketConnection'],
  props:{
    selectedGame: Object,
  },
  data() {
    return {
      gameId:null,
      userDetails: null,
      playerNames:[],
      players:{},
      numberOfPlayers: 4,
      turnDuration: 60,
      pointsToWin: 8,
      botCount: 0,
      showModal: false,
      currentGame:Object
    };
  },
  async created() {
    // Listen for WebSocket open event
    this.$socketConnection.socket.onopen = () => {
      console.log("WebSocket connection opened");
    };

    // Listen for WebSocket message event
    this.$socketConnection.socket.onmessage = (event) => {
      console.log("Received WebSocket message:", event.data);
      // Process the received message as needed
    };

    // Listen for WebSocket close event
    this.$socketConnection.socket.onclose = () => {
      console.log("WebSocket connection closed");
    };

    // Listen for WebSocket error event
    this.$socketConnection.socket.onerror = (error) => {
      console.error("WebSocket error:", error);
    };
    // Fetch current user info
    await this.fetchCurrentUserInfo();
    this.userDetails = await this.usersService._currentUser;
    // Get current game id for lobby
    this.gameId = this.$route.params.id;
    // Get game
    this.currentGame = await this.fetchGameById(this.gameId);
    // Fetch all players from game
    this.fetchedPlayers = await this.gameService.asyncFindAllPlayersForGameId(this.gameId);
    this.players = this.fetchedPlayers;
    // Add players to player list
    this.players.forEach(player => {
      const username = player.user && player.user.username ? player.user.username : 'bot';
      this.playerNames.push({name: username})
    });
    // Add current player to player list
    await this.addCurrentUserToPlayers();

    // Subscribe to WebSocket messages
    this.$socketConnection.socket.onmessage = (event) => {
      const message = JSON.parse(event.data);
      if (message.type === 'START_GAME') {
        // Redirect to the game page when game starts
        this.$router.replace({ name: 'game', params: { id: message.gameId } });
      }
    };
  },
  computed: {
    totalPlayers() {
      return this.playerNames.length;
    },
    canAddBot() {
      return this.totalPlayers < this.numberOfPlayers;
    }
  },
  methods: {
    async addBot() {
      if (this.botCount < this.numberOfPlayers - 1) {
        this.botCount++;
        const randomNames = ["Naruto", "Sasuke", "Goku", "Vegeta", "Luffy", "Ichigo", "Eren", "Levi",
          "Gon", "Killua", "Saitama", "Mikasa", "Kurama"];
        const randomIndex = Math.floor(Math.random() * randomNames.length);
        const botName = randomNames[randomIndex] + " (Bot)"; // add "(bot)" to the random name

        //FIXME add bot player to backend
        // Getting a token error from the back-end ~Steef
        //TODO player numbers available
        try {
          this.player = await this.gameService.addNewPlayerToGame(this.gameId, null);
        } catch (error) {
          console.error("Error adding new bot player to game:", error);
        }

        this.playerNames.push({name: botName});
      }
    },
    kickPlayer(index) {
      //TODO delete player in backend
      // deletePlayerFromGame() method is already present in gameService and backend DELETE works
      if (!this.playerNames[index].host) {
        this.playerNames.splice(index, 1);
        if (this.playerNames.every(player => !player.host)) {
          this.botCount--;
        } else {
          this.botCount--;
        }
        // Update the indexes of the bots when deleted
        this.playerNames.forEach((player, i) => {
          if (!player.host && player.index !== undefined) {
            player.index = i + 1;
          }
        });
      }
    },

    async  fetchCurrentUserInfo() {
      const userInfo = this.usersService.getCurrentUser;
      if (userInfo) {
        this.playerNames.push({ name: userInfo.username });
      }
    },
    async addCurrentUserToPlayers(){
      const user =  this.usersService.getCurrentUser


      if (user){
        if (!(await this.gameService.canAddNewPlayerToGame(this.gameId))){
          //TODO cancel adding user

          console.error("User cannot be added, sorry");
        } else {
          // Add user as player to game
          await this.gameService.addNewPlayerToGame(this.gameId, user);
        }
      }
    },
    async fetchGameById(gameId) {
      try {
        this.game = await this.gameService.asyncGetById(gameId);
        return this.game
      } catch (error) {
        console.error("Error fetching game by ID:", error);
      }
    },


  }
}
</script>

<style scoped>
.container {
  display: flex;
}

.left-column-gamesettings-page, .right-column-gamesettings-page {
  flex: 1;
  padding: 1rem;
}

.center-column-gamesettings-page {
  flex: 2;
  padding: 1rem;
  justify-content: space-between;
}

.header-title {
  color: var(--white);
  background-color: var(--shade-of-tea);
  text-align: center;
  border-radius: 5px;
  font-weight: bold;
  margin-bottom: 10%;
}

.player-name {
  font-weight: bold;
}

.player-status {
  display: flex;
  gap: 0.5rem;
}

.player-pill {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: var(--white);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  margin: 1rem;
  width: 400px;
  height: 50px;
}

.player-pill:hover {
  background-color: var(--black);
  color: var(--white);
}

.ready-button {
  color: var(--white);
  background-color: var(--green);
  font-size: 20px;
  font-weight: bold;
  border: none;
  width: 125px;
  height: 30px;
  border-radius: 25px;
}

.ready-button:hover {
  color: var(--green);
  background-color: var(--white);
}

.start-game-button {
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

.start-game-button:hover {
  color: var(--shade-of-tea);
  background-color: var(--white);
}

.start-game-div {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.kick-button {
  color: var(--white);
  background-color: var(--red);
  font-size: 20px;
  font-weight: bold;
  border: none;
  width: 125px;
  height: 30px;
  border-radius: 25px;
}

.kick-button:hover {
  color: var(--red);
  background-color: var(--white);
}

.add-bot-button {
  color: var(--white);
  background-color: var(--yellow-orange);
  font-size: 20px;
  font-weight: bold;
  border: none;
  width: 125px;
  height: 30px;
  margin: auto;
  border-radius: 25px;
}

.add-bot-button:hover {
  color: var(--yellow-orange);
  background-color: var(--white);
}

.center-column-slider {
  -webkit-appearance: none;
  width: 100%;
  height: 15px;
  border-radius: 5px;
  background: var(--light-gray);
  outline: none;
  opacity: 0.7;
  -webkit-transition: .2s;
}

.center-column-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background: var(--yellow-orange);
  cursor: pointer;
  transition: all 0.4s ease;
}

.center-column-slider::-moz-range-thumb {
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background: var(--yellow-orange);
  cursor: pointer;
  transition: all 0.4s ease;
}

.center-column-slider:hover {
  background: var(--shade-of-tea);
  height: 17px;
}

.center-column-slider::-webkit-slider-thumb:hover {
  background: var(--coral);
  width: 30px;
  height: 30px;
}

.center-column-slider::-moz-range-thumb:hover {
  background: var(--coral);
  width: 30px;
  height: 30px;
}

.game-list-container {
  max-height: 300px; /* Adjust as needed */
  overflow-y: auto;
}

</style>
