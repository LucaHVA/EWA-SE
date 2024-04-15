<template>
  <Transition name="modal">
    <div v-if="show" class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <div class="modal-header">
            <slot name="header">Confirm Joining Game</slot>
          </div>
          <div class="modal-body">
            <p class="modal-body-header">Current game:</p>
            <ul class="game-settings-modal">
              <li>Game name: {{ selectedGame.name }}</li>
              <li>Lobby size: {{ selectedGame.players }} / {{ selectedGame.maxPlayers }}</li>
            </ul>
          </div>
          <div class="modal-footer">
            <slot name="footer">
              <button class="close-button-pop-up transition" @click="$emit('close')">Close</button>
              <router-link to="/gameSettings">
                <button class="start-game-button-pop-up transition">Join Game</button>
              </router-link>
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
  },
};
</script>

<style scoped>
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
  background-color: #1FAB1C;
  color: white;
  font-size: 20px;
  font-weight: bold;
  border: none;
  width: 150px;
  height: 30px;
  border-radius: 10px;
}

.start-game-button-pop-up:hover {
  color: #1FAB1C;
  border: 1px solid #1FAB1C;
  background-color: white;
}

.close-button-pop-up {
  background-color: #FC1212;
  color: white;
  font-size: 20px;
  font-weight: bold;
  border: none;
  width: 150px;
  height: 30px;
  border-radius: 10px;
}

.close-button-pop-up:hover {
  color: #FC1212;
  border: 1px solid #FC1212;
  background-color: white;
}
</style>