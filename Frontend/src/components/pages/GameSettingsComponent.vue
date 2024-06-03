<!--testing-->

<template>
  <div class="container">
    <div class="left-column-gamesettings-page">
      <div class="left-column-players">
        <h2 class="header-title">Players ({{ totalPlayers }})</h2>
      </div>
      <div class="left-column-players-in-lobby">
        <div v-for="(player, index) in players" :key="index" @click="console.log(player.playerNumber)" class="player-pill transition">
          <p class="player-name">{{ player.user ? player.user.username : 'bot' }} <span v-if="isHost(player)">(Host)</span></p>
          <div class="player-status">
<!--            <button v-if="isCurrentUser(player.user)" class="ready-button transition">Ready</button>-->
            <button v-if="isCurrentUserHost" class="kick-button transition" @click="kickPlayer(index, player.playerNumber)">Kick</button>
          </div>
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
        <input type="range" min="1" max="4" v-model.number="currentGame.numberOfPlayers" class="transition center-column-slider">
      </div>
      <div>
        <h3>Turn Duration (In seconds)</h3>
        <span>{{ this.currentGame.turnDuration}}</span>
        <input type="range" min="30" max="90" v-model.number="currentGame.turnDuration" class="transition center-column-slider">
      </div>
      <div>
        <h3>Points to win</h3>
        <span>{{ this.currentGame.pointsToWin }}</span>
        <input type="range" min="5" max="10" v-model.number="currentGame.pointsToWin" class="transition center-column-slider">
      </div>

      <div class="start-game-div">
        <button class="start-game-button transition" @click="showModal = true">Start Game</button>
      </div>
    </div>
    <div class="right-column-gamesettings-page">
      <div>
        <h2 class="header-title">Chat</h2>
      </div>
      <div class="chat-container">
        <div class="scrollable" ref="chatContainer">
          <div v-if="chatMessages.length === 0" class="noMessageText">
            Here you can talk to other players!
          </div>
          <div v-else class="chat-messages" v-for="msg in chatMessages" :key="msg.id">
            <div :class="['message', { 'current-user-message': isCurrentUserMessage(msg.sender) }]">
              {{ msg.sender }}: {{ msg.text }}
            </div>
          </div>
        </div>
        <input type="text" v-model="chatInput" @keyup.enter="sendChatMessage" placeholder="Type a message..." class="chat-input">
      </div>
    </div>
    <popUpGameSettingsComponent :show="showModal"
                                @close="showModal = false"
                                @startGameForAll="onStartGameAnnouncement"
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
import {AnnouncementsAdaptor} from "@/services/announcements-adaptor";

export default {
  name: "GameSettingsComponent",
  components: {
    popUpGameSettingsComponent,
  },
  inject: ['gameService', 'usersService'],
  props: {
    selectedGame: Object,
  },
  data() {
    return {
      gameId: null,
      userDetails: null,
      players: [],
      numberOfPlayers: 4,
      turnDuration: 60,
      pointsToWin: 8,
      botCount: 0,
      showModal: false,
      currentGame: Object,
      announcements: [],
      chatMessages: [],
      chatInput: '',
    };
  },
  async created() {
    this.announcementsService = new AnnouncementsAdaptor(process.env.VUE_APP_API_URL + '/announcements', this.onReceiveAnnouncement);

    this.userDetails = await this.usersService._currentUser;

    // Get current game id for lobby
    this.gameId = this.$route.params.id;

    // Get game
    this.currentGame = await this.fetchGameById(this.gameId);

    // Add current user to players
    await this.addCurrentUserToGame();

    // Fetch all players from game
    await this.updateCurrentPlayers();

  },
  beforeUnmount() {
    // close down the service with the web socket
    this.announcementsService.close();
  },
  computed: {
    totalPlayers() {
      return this.players ? this.players.length : 0;
    },
    isCurrentUserHost() {
      return this.userDetails && this.currentGame && this.userDetails.id === this.currentGame.host.id
    }
  },

  beforeRouteLeave(to, from, next) {
    // Check if the player is leaving the page by clicking on the "Start Game" button
    const isLeavingByStartGame = to.name === 'game';

    if (!isLeavingByStartGame) {
      // Player is leaving the page for another reason (not starting the game)
      this.removeCurrentUserFromGame().then(async () => {
        const remainingPlayers = await this.gameService.asyncFindAllPlayersForGameId(this.gameId);
        console.log("remaining players ", remainingPlayers);

        this.announcementsService.sendMessage(JSON.stringify({action: 'playerLeft'}));
        if (this.isCurrentUserHost || !remainingPlayers || remainingPlayers.length === 0 || remainingPlayers === "0") {
          console.log(`No remaining players. Deleting game with ID: ${this.gameId}`);
          await this.gameService.deleteGame(this.gameId);

        } else {
          console.log("no reason to delete the game")
        }
        next();
      }).catch(error => {
        console.error("Error removing current user from game:", error);
        next();
      });
    } else {
      next();
    }
  },

  methods: {
    sendChatMessage() {
      if (this.chatInput.trim() !== '') {
        const message = { sender: this.userDetails.username, text: this.chatInput };
        this.announcementsService.sendMessage(JSON.stringify({ action: 'chatMessage', content: message }));
        this.chatInput = '';
      }
    },
    async onReceiveAnnouncement(message) {
      const parsedMessage = JSON.parse(message);

      if (parsedMessage.action === 'sendPlayersToGame') {
        this.$router.replace({ name: 'game', params: { id: this.gameId } });
      } else if (parsedMessage.action === 'updatePlayersList'){
        await this.updateCurrentPlayers();
      } else if (parsedMessage.action === 'playerKicked'){
        if (this.userDetails.id === parsedMessage.kickedId){
          this.$router.push("/lobbySelect");
        await this.updateCurrentPlayers();
        }
      } else if (parsedMessage.action === 'playerLeft'){
        await this.updateCurrentPlayers();
      } else if (parsedMessage.action === 'chatMessage') {
        this.chatMessages.push(parsedMessage.content);
        this.scrollToBottom();
      }
    },

    onStartGameAnnouncement() {
      this.announcementsService.sendMessage(JSON.stringify({action: "sendPlayersToGame"}));
    },
    onUpdatePlayersAnnouncement() {
      this.announcementsService.sendMessage(JSON.stringify({action: "updatePlayersList"}));
    },
    async isCurrentUserAlreadyPlayer(){
      // Save current player id's
      let players = await this.gameService.asyncFindAllPlayersForGameId(this.gameId);
      let playerUserIds = [];
      players.forEach(player => {
        playerUserIds.push(player.user.id);
      })

      // Check if current user is already a player
      return playerUserIds.includes(this.userDetails.id);
    },
    async updateCurrentPlayers(){
      this.players = await this.gameService.asyncFindAllSimplePlayersForGameId(this.gameId);
    },
    async addCurrentUserToGame(){
      // Check if user is already in this game
      if (!await this.isCurrentUserAlreadyPlayer()){
        // Add new player
        await this.gameService.addNewPlayerToGame(this.currentGame.id, this.userDetails);
        // Send websocket update
        this.onUpdatePlayersAnnouncement();
      } else console.warn("User is already a player. No new player was added.");
    },
    isHost(player){
      return player.playerNumber===0;
    },
    async kickPlayer(index, playerNumber) {
      if (!this.players[index].host) {
        try {
          const kickedPlayerUserId = this.players[index].user.id;
          await this.gameService.deletePlayerFromGame(this.gameId, playerNumber);

          this.announcementsService.sendMessage(JSON.stringify({action: 'playerKicked', kickedId: kickedPlayerUserId}));
        } catch (error) {
          console.error("Error deleting player from game:", error);
        }
      }
    },

    isCurrentUser(user) {
      return user && user.id === this.userDetails.id;
    },
    async removeCurrentUserFromGame() {
      this.currentPlayer= await this.gameService.findPlayerByUserId(this.gameId, this.userDetails.id)

      console.log("this is the user: ", this.currentPlayer)
        try {
          await this.gameService.deletePlayerFromGame(this.gameId, this.currentPlayer.playerNumber);
        } catch (error) {
          console.error("Error deleting player from game:", error);
        }

    },
    async fetchGameById(gameId) {
      try {
        return await this.gameService.asyncGetById(gameId);
      } catch (error) {
        console.error("Error fetching game by ID:", error);
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const chatContainer = this.$refs.chatContainer;
        chatContainer.scrollTop = chatContainer.scrollHeight;
      });
    },
    isCurrentUserMessage(sender) {
      return sender === this.userDetails.username;
    },
  }
};
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

.chat-container {
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  padding: 10px;
  height: 400px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border-radius: 10px;
}

.scrollable {
  overflow-y: auto;
  flex-grow: 1;
}

.chat-messages {
  max-height: 240px;
  display: flex;
  flex-direction: column;
}

.message {
  word-break: break-all;
  padding: 5px;
}

.chat-input {
  border: 1px solid #ddd;
  padding: 8px;
  width: calc(100% - 20px);
  border-radius: 5px;
}

.noMessageText {
  display: flex;
  justify-content: center;
  font-size: 20px;
  margin-top: 9rem;
}

.current-user-message {
  background-color: #d3d3d3;
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
</style>
