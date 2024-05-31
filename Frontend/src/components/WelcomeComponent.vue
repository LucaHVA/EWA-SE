<template>
  <div class="container">
    <div class="centered-content">
      <div class="title">Play Catan Online</div>
      <div class="sub-title">Play for free</div>
      <p class="extra-info">Enjoy an online game of the classic board game Settlers of Catan!</p>
      <div class="welcome-page-button-container">
        <router-link to="/gameSettings">
          <button class="singleplayer-button buttons-welcome-page transition">Play SinglePlayer</button>
        </router-link>
        <router-link to="/lobbySelect">
          <button class="multiplayer-button buttons-welcome-page transition">Play Multiplayer</button>
        </router-link>
      </div>
      <textarea v-on:keyup.enter="onNewAnnouncement($event)"></textarea>
    </div>
  </div>
</template>

<script>
import {AnnouncementsAdaptor} from "@/services/announcements-adaptor";
import CONFIG from '@/app-config.js';
export default {
  name: "WelcomeComponent",
  created() {
    // setup a new service with a web socket
    this.announcementsService = new AnnouncementsAdaptor(CONFIG.ANNOUNCEMENTS, this.onReceiveAnnouncement)
  },
  beforeUnmount() {
    // close down the service with the web socket
    this.announcementsService.close();
  },
  data() { return {
    announcements: []
  }
  },

  methods: {
    onReceiveAnnouncement(message) {
      // this method is called when an announcement is distributed
      console.log("Received announcement:", message);
      this.announcements.push(message);
    },
    onNewAnnouncement(event) {
      // this method is called when enter is pressed within the input text field

      // for demo purpose of a simple web socket
      this.announcementsService.sendMessage(event.target.value);
      // a persistent announcement system would save the announcement here via the REST api
      // and let the rest controller issue the websocket notification to inform all clients about the update

      // reset the input in the text area
      event.target.value = "";
    }
  }
};
</script>


<style scoped>
/* General */
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
  color: rgba(51, 38, 35, 1);
}

.centered-content {
  text-align: center;
}

/* Text */
.title {
  font-size: 8vw;
  font-family: 'Gochi Hand', sans-serif;
}

.sub-title {
  font-family: 'Baloo 2', cursive;
  font-weight: 600;
  font-size: 40px;
}

.extra-info {
  font-family: 'Baloo 2', cursive;
  font-size: 20px;
}

.transition {
  transition: all 0.4s ease;
}

.transition:hover {
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
  border: none;
}

.welcome-page-button-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 3rem;
}

.buttons-welcome-page {
  font-size: 30px;
  font-weight: bold;
  border: none;
  width: 350px;
  height: 70px;
  border-radius: 10px;
  margin: auto;
  text-decoration: none;
}

.singleplayer-button {
  color: white;
  background-color: #60BFB2;
}

.singleplayer-button:hover {
  color: #60BFB2;
  background-color: white;
}

.multiplayer-button {
  color: white;
  background-color: #FFB54A;
}

.multiplayer-button:hover {
  color: #FFB54A;
  background-color: white;
}
</style>
