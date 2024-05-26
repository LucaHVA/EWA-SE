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
import {shallowReactive} from "vue";
import {FetchInterceptor} from "@/services/fetch-interceptor";

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

    this.userService = shallowReactive(new UsersAdaptor(process.env.VUE_APP_API_URL + '/users') ) ;
    this.theFetchInterceptor = new FetchInterceptor(this.userService, this.$router);


    return{
      usersService: this.userService,
      fetchInterceptor:this.theFetchInterceptor,
      gameService: new GameService(process.env.VUE_APP_API_URL +'/game')
    }
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
