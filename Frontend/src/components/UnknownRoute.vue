<template>
  <div class="errorMessage">
    <h1>No route has been found</h1>
    <p>There is no component available to load</p>
  </div>
</template>

<script>
export default {
  name: 'UnknownRoute',
}
</script>

<style scoped>
.errorMessage{
  color: red;
  background-image: url(@/assets/images/daDawg.png);
  background-repeat:no-repeat;
  height: 1200px;
  width:800px;
  margin-bottom: 20px;
}
</style>