<template>
  <img alt="Vue logo" src="./assets/logo.png">
<!--  <HelloWorld msg="Welcome to Your Vue.js App"/>-->
  <HeaderComponent></HeaderComponent>
  <welcome-component></welcome-component>
</template>

<script>
import headerComponent from "@/components/HeaderComponent";
import welcomeComponent from "@/components/WelcomeComponent";
export default {
  name: 'App',
  components: {
    headerComponent,
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
