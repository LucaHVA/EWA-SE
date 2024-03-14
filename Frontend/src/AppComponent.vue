<template>
  <nav-bar-component></nav-bar-component>
  <router-view></router-view>
</template>

<script>
import navBarComponent from "@/components/NavBarComponent";
import {UsersAdaptor} from "@/services/UsersAdaptor";
import CONFIG from "@/app-config";
export default {
  name: "AppComponent",
  components: {
    navBarComponent
  },
  provide(){
    return{
      usersService: new UsersAdaptor(CONFIG.BACKEND_URL+ '/users')
    }
  }
}
</script>

<style scoped>

</style>