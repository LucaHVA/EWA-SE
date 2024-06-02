<template>
  <div class="container">
    <div class="centered-content">
      <div class="title">Play Catan Online</div>
      <div class="sub-title">Play for free</div>
      <p class="extra-info">Enjoy an online game of the classic board game Settlers of Catan!</p>
      <div class="welcome-page-button-container">
          <button class="multiplayer-button buttons-welcome-page transition" @click="tokenCheck()">Play Game</button>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "WelcomeComponent",
  inject:['usersService'],

  methods:{
tokenCheck(){
  const token= this.usersService._currentToken
  if (token===null){
    this.$router.push({name:'login'})
  }else {
    this.$router.push({name:'lobbySelect'})
  }
}
  }


};
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

.welcome-page-button-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 3rem;
}

.buttons-welcome-page {
  font-size: 30px;
  font-weight: bold;
  border: none;
  width: 350px;
  height: 70px;
  border-radius: 10px;
  margin: auto;
  text-decoration: none;
}

.singleplayer-button {
  color: white;
  background-color: #60BFB2;
}

.singleplayer-button:hover {
  color: #60BFB2;
  background-color: white;
}

.multiplayer-button {
  color: white;
  background-color: #FFB54A;
}

.multiplayer-button:hover {
  color: #FFB54A;
  background-color: white;
}
</style>
