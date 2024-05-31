<template>
  <Transition name="modal">
    <div v-if="show" class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <div class="modal-header">
            <slot name="header">Confirm Game Settings</slot>
          </div>
          <div class="modal-body">
            <p class="modal-body-header">Current Settings:</p>
            <ul class="game-settings-modal">
              <li>Number of Players: {{ numberOfPlayers }}</li>
              <li>Turn Duration: {{ turnDuration }} seconds</li>
              <li>Points to Win: {{ pointsToWin }}</li>
            </ul>
            <p v-if="missingBotsCount > 0" class="error-message">
              Current number of players chosen: {{ numberOfPlayers }}, but there
              {{ missingBotsCount === 1 ? 'is' : 'are' }}
              still {{ missingBotsCount }} {{ missingBotsCount === 1 ? 'bot' : 'bots' }} needed to fill the lobby.
            </p>
          </div>
          <div class="modal-footer">
            <slot name="footer">
              <button class="close-button-pop-up transition" @click="$emit('close')">Close</button>
              <button class="start-game-button-pop-up transition" @click="initializeGame">Start Game</button>
            </slot>
          </div>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script>

import Game from "@/models/game";

export default {
  name: "popUpGameSettingsComponent",
  props: {
    show: Boolean,
    numberOfPlayers: Number,
    turnDuration: Number,
    pointsToWin: Number,
    botCount: Number,
    totalPlayers: Number,
    gameSettings:Object
  },
  inject:['gameService'],
  methods: {
    async initializeGame() {
      //todo check if all game conditions are valid
      if (this.totalPlayers !== this.numberOfPlayers){
        return;
      }

      // Create a game
      // this.game = GameService.generateNewGame(this.numberOfPlayers, this.turnDuration, this.pointsToWin);

      // Save the game instance to a service
      const newGame=  new Game(this.gameSettings.id,this.numberOfPlayers,this.turnDuration,this.pointsToWin)
      await this.gameService.saveGame(newGame)
      console.log("check this,",newGame)

      // Go to game page
      this.$router.replace({ name: 'game', params: { id: this.gameSettings.id } });
    },
  },
  computed: {
    missingBotsCount() {
      // Calculate the number of bots needed to fill the lobby
      return Math.max(this.numberOfPlayers - this.totalPlayers, 0);
    },
  },
};
</script>


<style scoped>
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
  margin: 0 auto;
  padding: 20px 30px;
  background-color: var(--white);
  border-radius: 2px;
  box-shadow: 0 15px 20px rgba(0, 0, 0, 0.5);
  transition: all 0.4s ease;
}

.modal-header {
  color: var(--shade-of-tea);
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
  color: var(--red);
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
</style>
