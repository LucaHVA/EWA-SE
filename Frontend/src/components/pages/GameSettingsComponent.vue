<template>
  <div class="container">
    <div class="left-column-gamesettings-page">
      <div class="left-column-players">
        <h2 class="header-title">Players ({{ totalPlayers }})</h2>
      </div>
      <div class="left-column-players-in-lobby">
        <div v-for="(player, index) in players" :key="index" class="player-pill">
          <p class="player-name">{{ player.name }} <span v-if="player.host">(Host)</span></p>
          <div class="player-status">
            <button class="ready-button">Ready</button>
            <button v-if="!player.host" class="left-column-kick-button" @click="kickPlayer(index)">Kick</button>
          </div>
        </div>
        <div v-if="canAddBot" class="player-pill">
          <button class="left-column-add-button" @click="addBot">Add Bot</button>
        </div>
      </div>
    </div>
    <div class="center-column-gamesettings-page">
      <div>
        <h2 class="header-title">Game Settings</h2>
      </div>
      <div>
        <h3>Number of Players</h3>
        <span>{{ numberOfPlayers }}</span>
        <input type="range" min="2" max="4" v-model="numberOfPlayers" class="center-column-slider">
      </div>
      <div>
        <h3>Turn Duration (In minutes)</h3>
        <span>{{ turnDuration }}</span>
        <input type="range" min="1" max="3" v-model="turnDuration" class="center-column-slider">
      </div>
      <div>
        <h3>Points to win</h3>
        <span>{{ pointsToWin }}</span>
        <input type="range" min="8" max="10" v-model="pointsToWin" class="center-column-slider">
      </div>
    </div>
    <div class="right-column-gamesettings-page">
    </div>
  </div>

  <div id="background-blob">
    <div class="blob-top-left"></div>
    <div class="blob-bottom-left"></div>
    <div class="blob-bottom-Right"></div>
  </div>
</template>

<script>
export default {
  name: "GameSettingsComponent",
  data() {
    return {
      players: [
        {name: "Susano", host: true},
      ],
      numberOfPlayers: 4,
      turnDuration: 1,
      pointsToWin: 10,
      botCount: 0
    };
  },
  computed: {
    totalPlayers() {
      return this.players.length;
    },
    canAddBot() {
      return this.totalPlayers < this.numberOfPlayers;
    }
  },
  methods: {
    addBot() {
      if (this.botCount < this.numberOfPlayers - 1) {
        this.botCount++;
        const randomNames = ["Naruto", "Sasuke", "Goku", "Vegeta", "Luffy", "Ichigo", "Eren", "Levi",
          "Gon", "Killua", "Saitama", "Mikasa", "Kurama"];
        const randomIndex = Math.floor(Math.random() * randomNames.length);
        const botName = randomNames[randomIndex] + " (Bot)"; // add "(bot)" to the random name
        this.players.push({ name: botName });
      }
    },
    kickPlayer(index) {
        if (!this.players[index].host) {
          this.players.splice(index, 1);
          if (this.players.every(player => !player.host)) {
            this.botCount--;
          } else {
            this.botCount--;
          }
        }
      }
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
  color: white;
  background-color: #60BFB2;
  text-align: center;
  border-radius: 5px;
  font-weight: bold;
  margin-bottom: 10%;
}

.player-name {
  font-weight: bold;
}

.ready-button {
  color: white;
  background-color: #1FAB1C;
  font-size: 20px;
  font-weight: bold;
  transition: all 0.4s ease;
  border: none;
  width: 125px;
  height: 30px;
  border-radius: 25px;
}

.ready-button:hover {
  color: #1FAB1C;
  background-color: white;
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
  border: none;
}

.left-column-kick-button {
  color: white;
  background-color: #FC1212;
  font-size: 20px;
  font-weight: bold;
  transition: all 0.4s ease;
  border: none;
  width: 125px;
  height: 30px;
  border-radius: 25px;
}

.left-column-kick-button:hover {
  color: #FC1212;
  background-color: white;
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
  border: none;
}

.left-column-add-button {
  color: white;
  background-color: #FFB54A;
  font-size: 20px;
  font-weight: bold;
  transition: all 0.4s ease;
  border: none;
  width: 125px;
  height: 30px;
  margin: auto;
  border-radius: 25px;
}

.left-column-add-button:hover {
  color: #FFB54A;
  background-color: white;
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
  border: none;
}

.player-pill {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  margin: 1rem;
  width: 400px;
  height: 50px;
  transition: all 0.4s ease;
}

.player-pill:hover {
  background-color: black;
  color: white;
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
}

.player-status {
  display: flex;
  gap: 0.5rem;
}

.center-column-slider {
  -webkit-appearance: none;
  width: 100%;
  height: 15px;
  border-radius: 5px;
  background: #d3d3d3;
  outline: none;
  opacity: 0.7;
  -webkit-transition: .2s;
  transition: all 0.4s ease;;
}

.center-column-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background: #FFB54A;
  cursor: pointer;
  transition: all 0.4s ease;
}

.center-column-slider::-moz-range-thumb {
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background: #FFB54A;
  cursor: pointer;
  transition: all 0.4s ease;
}

.center-column-slider:hover {
  background: #60BFB2;
  height: 17px;
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
}

.center-column-slider::-webkit-slider-thumb:hover {
  background: #FF7B4D;
  width: 30px;
  height: 30px;
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
}

.center-column-slider::-moz-range-thumb:hover {
  background: #FF7B4D;
  width: 30px;
  height: 30px;
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
}

</style>