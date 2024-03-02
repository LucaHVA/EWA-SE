<template>
  <div id="app">
    <header-component></header-component>
    <nav-bar-component></nav-bar-component>
  </div>
</template>

<script>
import headerComponent from "@/components/HeaderComponent";
import navBarComponent from "@/components/NavbarComponent.vue";


export default {
  name: 'App',
  components: {

    headerComponent,
    navBarComponent,

  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*text-align: center;*/
  color: #2c3e50;
  padding: 0;
}
</style>