<template>
  <div id="left-logo">
    <router-link to="/home">
      <img alt="logo" class="navbar_logo" src="../assets/images/codeboard_black_logo.png">
    </router-link>
  </div>
</template>

<script>
export default {
  name: "HeaderComponent"
}
</script>

<style scoped>
/* Logo */
#left-logo {
  margin: 20px;
}

.navbar_logo {
  width: 20%;
}
</style>