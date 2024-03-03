
<template>
<div class="container">
  <div class="title">
    {{title}}
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
  name: "LobbySelectComponent",

  data(){
    return{
      title:'Select a lobby'
    }

  }
}

</script>





<style scoped>
.title {
  text-align: center;
  margin-top: 50px;
  font-size: 30px;
  font-family: "Inria Sans", sans-serif;
}


</style>