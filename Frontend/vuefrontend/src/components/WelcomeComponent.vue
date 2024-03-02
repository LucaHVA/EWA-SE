<template>
  <div class="navbar">
    <router-link to="/home" class="nav-link " active-class="active">Home</router-link>
    <router-link to="/pages/Leaderbord" class="nav-link" active-class="active">Leaderbord</router-link>
  </div>
  <div class="blob-top-left"></div>
  <div class="blob-bottom-left"></div>
  <div class="blob-bottom-Right"></div>

  <div class="container">
    <div class="centered-content">
      <div class="titel">Play Catan Online</div>
      <div class="undertitel">Play for free</div>
      <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna.</p>
      <button>Play Singleplayer</button>
    </div>
  </div>

</template>

<script>
export default {
  name: "WelcomeComponent"
}
</script>

<style scoped>

.navbar{
  margin-top: 3vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.nav-link {
  color: black; /* Change text color to black */
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 1vw;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 8px;
  font-family: 'Gochi Hand', cursive;
  position: relative; /* Add this line */
}

.nav-link:hover::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: black; /* Add black line under link on hover */
}

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
}

.centered-content {
  text-align: center;
}

.titel{
  font-size: 4vw;
}

.undertitel{
  font-size: 2vw;
}

button {
  background-color: #60BFB2;
  border: none;
  color: white;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 8px;
  font-family: 'Gochi Hand', cursive;
}


</style>