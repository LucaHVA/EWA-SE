<template>
  <div class="container">
    <div class="centered-content">
      <div class="title">Play Catan Online</div>
      <div class="sub-title">Play for free</div>
      <p class="extra-info">Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed do eiusmod tempor incididunt
        ut labore et dolore magna.</p>
        <router-link to="/gameSettings"><button class="play-button transition">Play SinglePlayer</button></router-link>
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

.transition {
  transition: all 0.4s ease;
}

.transition:hover {
  transition: all 0.4s ease;
  top: 10%;
  transform: translateY(-10%);
  border: none;
}

.play-button {
  color: white;
  background-color: #60BFB2;
  font-size: 30px;
  font-weight: bold;
  border: none;
  width: 350px;
  height: 70px;
  border-radius: 10px;
  margin: auto;
  text-decoration: none;
}

.play-button:hover {
  color: #60BFB2;
  background-color: white;
}
</style>
