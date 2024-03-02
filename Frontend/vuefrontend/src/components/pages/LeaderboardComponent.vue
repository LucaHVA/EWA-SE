<template>
  <div class="container">
    <h2>All cabins overview:</h2>
    <table class="cabins-table">
      <thead>
      <tr>
        <th>ID:</th>
        <th>Type:</th>
        <th>Location:</th>
        <th>Description</th>
        <th>Price p/wk:</th>
        <th>Total available</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="cabin in cabins" :key="cabin.id">
        <td>{{ cabin.id }}</td>
        <td>{{ cabin.type }}</td>
        <td>{{cabin.location}}</td>
        <td v-if="cabin.type!== 'BeachGear'">{{ cabin.descriptionId }}</td>
        <td v-else></td>
        <td>{{ cabin.pricePerWeek}}</td>
        <td>{{cabin.numAvailable}}</td>
      </tr>
      </tbody>
    </table>
    <button @click="onNewCabin">New Cabin</button>
  </div>

  <div id="background-blob">
    <div class="blob-top-left"></div>
    <div class="blob-bottom-left"></div>
    <div class="blob-bottom-Right"></div>
  </div>
</template>

<script>
import {createSampleCabin} from "@/models/cabin";



export default {
  name: "LeaderboardComponent",

  data() {
    return {
      cabins:[],
      nextCabinId: 10000,
    }
  },
  methods:{
    onNewCabin() {
      const cabin = createSampleCabin(this.nextCabinId);
      this.cabins.push(cabin);
      this.nextCabinId += Math.floor(Math.random() * 5) + 1;
    },
  },
  created(){
    for (let i = 0; i < 8; i++) {
      const cabin=createSampleCabin(this.nextCabinId);
      this.cabins.push(cabin);
      this.nextCabinId += Math.floor(Math.random()* 5)+1;
    }
  }
}
</script>

<style scoped>
.cabins-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.cabins-table th,
.cabins-table td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}

.cabins-table th {
  background-color: #f2f2f2;
}

.cabins-table tbody tr:nth-child(even) {
  background-color: #f2f2f2;
}
button{
  align-content: flex-end;
  background-color: #007BFF;
  color: #fff;

}
button:hover{
  background-color: #0056b3;
}


</style>