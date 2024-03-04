<template>
  <img alt="Logo" src="assets/images/logo.png">
  <nav-bar-component></nav-bar-component>
  <welcome-component></welcome-component>
</template>

<script>
import navBarComponent from "@/components/NavBarComponent";
import welcomeComponent from "@/components/WelcomeComponent";

export default {
  name: 'App',
  components: {
    navBarComponent,
    welcomeComponent
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
