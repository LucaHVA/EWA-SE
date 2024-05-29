<template>
  <div class="front-blob-container">
    <header-component></header-component>
    <nav-bar-component v-if="!isIgnoredRoute(this.$route.path, ignoredNavBarPaths)"></nav-bar-component>
    <router-view></router-view>
  </div>

  <!-- Background items -->
  <div id="background-blob" v-if="!isIgnoredRoute(this.$route.path, ignoredBackgroundPaths)">
    <div class="blob-top-left"></div>
    <div class="blob-bottom-left"></div>
    <div class="blob-bottom-Right"></div>
  </div>
</template>

<script>
import NavBarComponent from "@/components/NavBarComponent";
import HeaderComponent from "@/components/HeaderComponent";
import { GameService } from "@/services/GameService";
import { shallowReactive } from "vue";
import { FetchInterceptor } from "@/services/fetch-interceptor";
import { AnnouncementsAdaptor } from "@/services/AnnouncementsAdaptor";
import { UsersAdaptor } from "@/services/UsersAdaptor"; // Import the WebSocket adaptor

export default {
  name: "AppComponent",
  components: {
    HeaderComponent,
    NavBarComponent
  },
  data() {
    return {
      ignoredBackgroundPaths: [/^\/game(\/.*)?$/],
      ignoredNavBarPaths: [/^\/game(\/.*)?$/],
      announcementsAdaptor: null // Initialize WebSocket adaptor variable
    }
  },
  provide() {
    this.userService = shallowReactive(new UsersAdaptor(process.env.VUE_APP_API_URL + '/users'));
    this.theFetchInterceptor = new FetchInterceptor(this.userService, this.$router);

    // Initialize WebSocket adaptor
    this.announcementsAdaptor = new AnnouncementsAdaptor(process.env.VUE_APP_WEBSOCKET_URL || 'ws://localhost:8080/ws', this.handleWebSocketMessage);

    return {
      usersService: this.userService,
      fetchInterceptor: this.theFetchInterceptor,
      gameService: new GameService(process.env.VUE_APP_API_URL + '/game'),
      $socketConnection: this.announcementsAdaptor // Provide WebSocket adaptor to child components
    }
  },

  unmounted() {
    console.log('App.unmounted has been called');
    this.theFetchInterceptor.unregister();
    this.announcementsAdaptor.close(); // Close WebSocket connection when component is unmounted
  },

  methods: {
    isIgnoredRoute(path, ignoredPaths) {
      return ignoredPaths.some(ignoredPath => path.match(ignoredPath));
    },

    handleWebSocketMessage(message) {
      // Handle WebSocket messages
      console.log('Received WebSocket message:', message);
      // You can process the message here as needed
    }
  }
}
</script>


<style scoped>

</style>
