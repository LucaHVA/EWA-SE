<template>
  <nav-bar-component></nav-bar-component>
  <router-view></router-view>
</template>

<script>
import navBarComponent from "@/components/NavBarComponent";

export default {
  name: "AppComponent",
  components: {
    navBarComponent
  },
}
</script>

<style scoped>

</style>