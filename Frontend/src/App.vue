<template>
  <div class="front-blob-container">
    <header-component></header-component>
    <nav-bar-component v-if="!isIgnoredRoute($route.path, ignoredNavBarPaths)"></nav-bar-component>
    <router-view></router-view>
  </div>
  <div id="background-blob" v-if="!isIgnoredRoute($route.path, ignoredBackgroundPaths)">
    <div class="blob-top-left"></div>
    <div class="blob-bottom-left"></div>
    <div class="blob-bottom-Right"></div>
  </div>
</template>

<script>
import NavBarComponent from "@/components/NavBarComponent";
import { UsersAdaptor } from "@/services/UsersAdaptor";
import { GameService } from "@/services/GameService";
import { shallowReactive } from "vue";
import { FetchInterceptor } from "@/services/fetch-interceptor";
import CONFIG from '@/app-config.js'
import {NotificationAdaptor} from "@/services/notification-adaptor";


export default {
  name: "AppComponent",
  components: {
    NavBarComponent
  },
  data() {
    return {
      ignoredBackgroundPaths: [/^\/game(\/.*)?$/],
      ignoredNavBarPaths: [/^\/game(\/.*)?$/],
    }
  },
  provide() {

    this.theNotificationService = new NotificationAdaptor(CONFIG.NOTIFICATIONS);
    this.userService = shallowReactive(new UsersAdaptor(process.env.VUE_APP_API_URL + '/users'));
    this.theFetchInterceptor = new FetchInterceptor(this.userService, this.$router);

    return {
      usersService: this.userService,
      fetchInterceptor: this.theFetchInterceptor,
      gameService: new GameService(process.env.VUE_APP_API_URL + '/game'),
      // service for notification framework
      notificationService: this.theNotificationService,
    }
  },
  beforeUnmount() {
    this.theNotificationService.disconnect();
  },

  unmounted() {
    console.log('App.unmounted has been called');
    this.theFetchInterceptor.unregister();
  },
  methods: {
    isIgnoredRoute(path, ignoredPaths) {
      return ignoredPaths.some(ignoredPath => path.match(ignoredPath));
    }
  }
}
</script>



<style scoped>

</style>
