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
import {UsersAdaptor} from "@/services/UsersAdaptor";
import HeaderComponent from "@/components/HeaderComponent";
import {GameService} from "@/services/GameService";

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
    }
  },
  provide(){
    return{
      usersService: new UsersAdaptor(process.env.VUE_APP_API_URL + '/users'),
      gameService: new GameService(process.env.VUE_APP_API_URL +'/game')
    }
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
