<template>
  <Transition name="modal">
    <div v-if="show" class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <div class="modal-header">
            <slot name="header">Confirm Joining Game</slot>
          </div>
          <div class="modal-body">
            <ul class="game-settings-modal">
              <li>
                Game id: <span @click="copyText" title="click to copy id" id="lobbyGameId">{{ selectedGame.id }}</span>
              </li>
              <li>Lobby size: {{ this.playerCounts[selectedGame.id] }} / {{ selectedGame.numberOfPlayers }}</li>
              <li>Turn duration: {{ selectedGame.turnDuration }}</li>
              <li>Points to win: {{ selectedGame.pointsToWin }}</li>
            </ul>
          </div>
          <div id="pop-up-lobby-error" class="error-message" v-if="this.popUpErrorMessage">{{this.popUpErrorMessage}}</div>
          <div class="modal-footer">
            <slot name="footer">
              <button class="close-button-pop-up transition" @click="closePopUp">Close</button>
              <button class="start-game-button-pop-up transition" @click="moveToLobby">Join Game</button>
            </slot>
          </div>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script>
export default {
  name: "popUpLobbySelectComponent",
  props: {
    show: Boolean,
    selectedGame: Object,
    playerCounts: Array,
  },
  inject:['usersService','gameService'],
  data() {
    return {
      popUpErrorMessage: "",
    }
  },
  methods: {
    copyText() {
      // Copy id to clipboard
      let copyText = document.getElementById("lobbyGameId").textContent;
      navigator.clipboard.writeText(copyText)
    },
    closePopUp(){
      this.popUpErrorMessage = "";
      this.$emit('close')
    },
    async moveToLobby() {
      // Get current user
      const currentUser = await this.usersService.getCurrentUser;

      // Check if a new player can be added
        if (!(await this.gameService.canAddNewPlayerToGame(this.selectedGame.id))){
          this.popUpErrorMessage = "Sorry, this game is full";
        } else {
          // Add user to game as player
          await this.gameService.addNewPlayerToGame(this.selectedGame.id, currentUser);
          // Route to game settings
          this.$router.replace({name: 'gameSettings', params: {id: this.selectedGame.id}});
        }
    }
  }
};
</script>

<style scoped>
/* Copy text */
#lobbyGameId {
  background-color: rgba(145, 142, 142, 0.5);
  border-radius: 2px;
  padding: 0 6px;
}

#lobbyGameId:hover {
  background-color: rgba(206, 198, 198, 0.5);
  cursor: pointer;
}

/**Transitions**/
.transition {
  transition: all 0.4s ease;
}

.transition:hover {
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
  border: none;
}

/**Modal (Pop Up)**/
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: table;
  transition: opacity 0.5s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.modal-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 450px;
  height: 350px;
  margin: 0 auto;
  padding: 20px 30px;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 15px 20px rgba(0, 0, 0, 0.5);
  transition: all 0.4s ease;
}

.modal-header {
  color: #60BFB2;
  font-weight: bolder;
  font-size: 30px;
  text-align: center
}

.modal-body {
  margin: 30px 0;
}

.modal-body-header {
  font-size: 20px;
  font-weight: bolder;
}

.game-settings-modal {
  list-style: none;
  font-size: 18px;
}

.modal-footer {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.error-message {
  font-size: 16px;
  color: red;
  font-weight: bolder;
}

/*
 * following styles are auto-applied to elements with
 * transition="modal" when their visibility is toggled
 * by Vue.js.
 */

.modal-enter-from {
  opacity: 0;
}

.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
  -webkit-transform: scale(1.3);
  transform: scale(1.3);
}

/**Buttons**/
.start-game-button-pop-up {
  background-color: var(--green);
  color: var(--white);
  font-size: 20px;
  font-weight: bold;
  border: none;
  width: 150px;
  height: 30px;
  border-radius: 10px;
}

.start-game-button-pop-up:hover {
  color: var(--green);
  border: 1px solid var(--green);
  background-color: var(--white);
}

.close-button-pop-up {
  background-color: var(--red);
  color: var(--white);
  font-size: 20px;
  font-weight: bold;
  border: none;
  width: 150px;
  height: 30px;
  border-radius: 10px;
}

.close-button-pop-up:hover {
  color: var(--red);
  border: 1px solid var(--red);
  background-color: var(--white);
}

/* pop-up error */
#pop-up-lobby-error {
  align-self: center;
}
</style>
