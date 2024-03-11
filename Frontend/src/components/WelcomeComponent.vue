<template>
  <div class="container">
    <div class="centered-content">
      <div class="title">Play Catan Online</div>
      <div class="sub-title">Play for free</div>
      <p class="extra-info">Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed do eiusmod tempor incididunt
        ut labore et dolore magna.</p>
      <button class="pos-button"><router-link class="multiplayer" to="/Game">Play Singleplayer</router-link></button>
      <button class="pos-button"><router-link class="multiplayer" to="/lobbySelect">Play Multiplayer</router-link></button>

    </div>
  </div>

  <div id="background-blob">
    <div class="blob-top-left"></div>
    <div class="blob-bottom-left"></div>
    <div class="blob-bottom-Right"></div>
  </div>
</template>

<script>
export default {
  name: "WelcomeComponent"
}

</script>

<style scoped>
/* General */
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
  color: rgba(51, 38, 35, 1);
}

.centered-content {
  text-align: center;
}

/* Text */
.title {
  font-size: 8vw;
  font-family: 'Gochi Hand', sans-serif;
}

.sub-title {
  font-family: 'Baloo 2', cursive;
  font-weight: 600;
  font-size: 40px;
}

.extra-info {
  font-family: 'Baloo 2', cursive;
  font-size: 20px;
}

.multiplayer{
  text-decoration: none;
  color: white;
}

</style>
