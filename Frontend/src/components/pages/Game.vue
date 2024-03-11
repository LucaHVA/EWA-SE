<template>
  <div class="container">
    <div class="mainContent">
      <div class="box">
        <!-- one -->
        <ol class="even">
          <li class='hex spacer'></li>
          <li class='hex water'></li>
          <li class='hex water'>
            <div class='harbor three-one any'>
              <div class='harbor-piece br'></div>
            </div>
          </li>
          <li class='hex water'></li>
          <li class='hex water'>
            <div class='harbor three-one any'>
              <div class='harbor-piece bl'></div>
            </div>
          </li>
        </ol>
        <!-- two -->
        <ol class="odd">
          <li class='hex spacer'></li>
          <li class='hex water'>
            <div class='harbor two-one sheep'>
              <div class='harbor-piece br'></div>
            </div>
          </li>
          <li v-for="(hex, index) in row1" :key="'h' + (index + 1)" :class="'hex ' + hex.resource">
            <div class="number">{{ hex.number }}</div>
            <div class="settlement target tl" @click="build(index * 4 + 3)" :id="'s' + (index * 4 + 3)"></div>
            <div class="settlement target t" @click="build(index * 4)" :id="'s' + (index * 4)"></div>
            <div class="road target tl" @click="buildRoad(0, index * 4 + 3)"></div>
            <div class="road target l" @click="buildRoad(7, index * 4 + 3)"></div>
            <div class="road target tr" @click="buildRoad(0, index * 4 + 4)"></div>
          </li>
          <li class="hex water">
            <div class="settlement target tl" @click="build(6)" id="s6"></div>
            <div class="road target l r6 r10" @click="buildRoad(6,10)"></div>
          </li>
        </ol>
        <!-- three -->
        <ol class="even">
          <li class="hex water"></li>
          <li v-for="(hex, index) in row2" :key="'h' + (index + 4)" :class="'hex ' + hex.resource">
            <div class="number">{{ hex.number }}</div>
            <div class="settlement target tl" @click="build(index * 4 + 11)" :id="'s' + (index * 4 + 11)"></div>
            <div class="settlement target t" @click="build(index * 4 + 7)" :id="'s' + (index * 4 + 7)"></div>
            <div class="road target tl" @click="buildRoad(11, index * 4 + 7)"></div>
            <div class="road target l" @click="buildRoad(11, index * 4 + 16)"></div>
            <div class="road target tr" @click="buildRoad(7, index * 4 + 12)"></div>
          </li>
          <li class="hex water">
            <div class="settlement target tl" @click="build(15)" id="s15"></div>
            <div class="road target l r15 r20" @click="buildRoad(15,20)"></div>

            <div class="harbor two-one brick">
              <div class="harbor-piece l"></div>
            </div>
          </li>
        </ol>
        <!-- four -->
        <ol class="odd">
          <li class="hex water">
            <div class="harbor three-one any">
              <div class="harbor-piece r"></div>
            </div>
          </li>
          <li v-for="(hex, index) in row3" :key="'h' + (index + 8)" :class="'hex '+ hex.resource">
            <div class="number">{{ hex.number }}</div>
            <div class="settlement target tl" @click="build(index * 4 + 21)" :id="'s' + (index * 4 + 21)"></div>
            <div class="settlement target t" @click="build(index * 4 + 16)" :id="'s' + (index * 4 + 16)"></div>
            <div class="road target tl" @click="buildRoad(21, index * 4 + 16)"></div>
            <div class="road target l" @click="buildRoad(21, index * 4 + 27)"></div>
            <div class="road target tr" @click="buildRoad(16, index * 4 + 22)"></div>
          </li>
          <li class="hex water">
            <div class="settlement target tl" @click="build(26)" id="s26"></div>
            <div class="road target l r26 r32" @click="buildRoad(26,32)"></div>
          </li>
        </ol>
        <!-- five -->
        <ol class="even">
          <li class="hex water">
            <div class="settlement target t" @click="build(27)" id="s27"></div>
            <div class="road target tr r27 r33" @click="buildRoad(27,33)"></div>
          </li>
          <li v-for="(hex, index) in row4" :key="'h' + (index + 13)" :class="'hex '+ hex.resource">
            <div class="number">{{ hex.number }}</div>
            <div class="settlement target tl" @click="build(index * 4 + 33)" :id="'s' + (index * 4 + 33)"></div>
            <div class="settlement target t" @click="build(index * 4 + 28)" :id="'s' + (index * 4 + 28)"></div>
            <div class="road target tl" @click="buildRoad(33, index * 4 + 28)"></div>
            <div class="road target l" @click="buildRoad(33, index * 4 + 38)"></div>
            <div class="road target tr" @click="buildRoad(28, index * 4 + 34)"></div>
          </li>
          <li class="hex water">
            <div class="settlement target tl" @click="build(37)" id="s37"></div>
            <div class="road target tl r37 r32" @click="buildRoad(37,32)"></div>
            <div class="road target l r37 r42" @click="buildRoad(37,42)"></div>
            <div class="settlement target t"></div>
            <div class="harbor two-one wood">
              <div class="harbor-piece l"></div>
            </div>
          </li>
        </ol>
        <!-- six -->
        <ol class="odd">
          <li class="hex spacer"></li>
          <li class="hex water">
            <div class="road target tr r38 r43" @click="buildRoad(38,43)"></div>
            <div class="settlement target t" @click="build(38)" id="s38"></div>
            <div class="harbor two-one ore">
              <div class="harbor-piece tr"></div>
            </div>
          </li>
          <li v-for="(hex, index) in row5" :key="'h' + (index + 17)" :class="'hex '+ hex.resource">
            <div class="number">{{ hex.number }}</div>
            <div class="settlement target tl" @click="build(index * 4 + 43)" :id="'s' + (index * 4 + 43)"></div>
            <div class="settlement target t" @click="build(index * 4 + 39)" :id="'s' + (index * 4 + 39)"></div>
            <div class="road target tl" @click="buildRoad(43, index * 4 + 39)"></div>
            <div class="road target l" @click="buildRoad(43, index * 4 + 47)"></div>
            <div class="road target tr" @click="buildRoad(39, index * 4 + 44)"></div>
          </li>
          <li class="hex water">
            <div class="settlement target tl" @click="build(37)" id="s37"></div>
            <div class="road target tl r37 r32" @click="buildRoad(37,32)"></div>
            <div class="road target l r37 r42" @click="buildRoad(37,42)"></div>
            <div class="settlement target t"></div>
          </li>
        </ol>

        <!-- seven -->
        <ol class="even">
          <li class="hex spacer"></li>
          <li class="hex water">
            <div class="settlement target t" @click="build(47)" id="s47"></div>
            <div class="road target tr r47 r51" @click="buildRoad(47,51)"></div>
          </li>
          <li class="hex water">
            <div class="settlement target tl" @click="build(51)" id="s51"></div>
            <div class="settlement target t" @click="build(48)" id="s48"></div>
            <div class="road target tl r51 r48" @click="buildRoad(51,48)"></div>
            <div class="road target tr r48 r52" @click="buildRoad(48,52)"></div>
            <div class="harbor two-one wheat">
              <div class="harbor-piece tr"></div>
            </div>
          </li>
          <li class="hex water">
            <div class="settlement target tl" @click="build(52)" id="s52"></div>
            <div class="settlement target t" @click="build(49)" id="s49"></div>
            <div class="road target tr r49 r53" @click="buildRoad(49,53)"></div>
            <div class="road target tl r52 r49" @click="buildRoad(52,49)"></div>
          </li>
          <li class="hex water">
            <div class="settlement target tl" @click="build(53)" id="s53"></div>
            <div class="settlement target t" @click="build(50)" id="s50"></div>
            <div class="road target tl r53 r50" @click="buildRoad(53,50)"></div>
            <div class="harbor three-one any">
              <div class="harbor-piece tl"></div>
            </div>
          </li>
        </ol>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "gameComponent",
  data() {
    return {
      row1: [],
      row2: [],
      row3: [],
      row4: [],
      row5: [],
      resourceCounts: {
        desert: 1,
        ore: 3,
        brick: 3,
        wheat: 4,
        wood: 4,
        sheep: 4
      }
    };
  },
  mounted() {
    this.initializeBoard();
  },
  methods: {
    initializeBoard() {
      for (let i = 0; i < 3; i++) {
        const resource = this.getRandomResource();
        const number = this.assignRandomNumber();
        console.log("Resource:", resource, "Number:", number);
        this.row1.push({ resource, number });
      }
      for (let i = 0; i < 4; i++) {
        const resource = this.getRandomResource();
        const number = this.assignRandomNumber();
        console.log("Resource:", resource, "Number:", number);
        this.row2.push({ resource, number });
      }
      for (let i = 0; i < 5; i++) {
        const resource = this.getRandomResource();
        const number = this.assignRandomNumber();
        console.log("Resource:", resource, "Number:", number);
        this.row3.push({ resource, number });
      }
      for (let i = 0; i < 4; i++) {
        const resource = this.getRandomResource();
        const number = this.assignRandomNumber();
        console.log("Resource:", resource, "Number:", number);
        this.row4.push({ resource, number });
      }
      for (let i = 0; i < 3; i++) {
        const resource = this.getRandomResource();
        const number = this.assignRandomNumber();
        console.log("Resource:", resource, "Number:", number);
        this.row5.push({ resource, number });
      }
    },
    getRandomResource() {
      const resources = ['desert', 'ore', 'brick', 'wheat', 'wood', 'sheep'];
      const availableResources = resources.filter(resource => this.resourceCounts[resource] > 0);

      const randomIndex = Math.floor(Math.random() * availableResources.length);
      const selectedResource = availableResources[randomIndex];
      this.resourceCounts[selectedResource]--; // Decrease count for selected resource
      return selectedResource;
    },

    assignRandomNumber() {
      let randomNumber;
      do {
        randomNumber = Math.floor(Math.random() * 11) + 2; // Random number between 2 and 12
      } while (randomNumber === 7); // Ensure the number is not 7
      return randomNumber;
    },
  }
};
</script>

<style scoped>
/* ----------------------------------------- */

.box {
  margin: 0 auto;
  margin-left: 31%;
  width: 1200px;
  /* background-color: #2fa4ed; */
  /* border: 1px solid black; */
  line-height: 1.3;
  margin-top: -.5%;
}

ol.even {
  position: relative;
  left: 68.18182px;
}

ol.odd {
  position: relative;
  margin-top: -78px;
  margin-bottom: -80px;
}

/* ----------------------------------------- */
/* begin base hex */
.hex {
  position: relative;
  margin: 25px auto;
  width: 75px;
  height: 127.5px;
  border-radius: 2px;
  background: #ccc;
  transform: rotate(-90deg);
  display: inline-block;
  margin-right: 60px;
  transition: all 150ms ease-in-out;
  /* position: relative;
  margin: 19px auto;
  width: 79px;
  height: 133.5px;
  border-radius: 2px;
  background: #ccc;
  transform: rotate(-90deg);
  display: inline-block;
  margin-right: 50px;
  transition: all 150ms ease-in-out; */
}

.hex:before, .hex:after {
  position: absolute;
  width: inherit;
  height: inherit;
  border-radius: inherit;
  background: inherit;
  content: '';
}

.hex:hover {
  cursor: pointer;
}

.hex:before {
  transform: rotate(60deg);
}

.hex:after {
  transform: rotate(-60deg);
}



/* end base hex */
/* ----------------------------------------- */
/* ----------------------------------------- */
/* begin types of hexs */
.hex.spacer {
  opacity: 0;
}

.hex.water {
  background-color: #5c86ae;
}

.hex.desert {
  background-color: #ba9e5c;
}

.hex.sheep {
  background-color: #93b248;
}

.hex.ore {
  background-color: #636363;
}

.hex.wheat {
  background-color: #e0b13c;
}

/* .hex.wheat:after {
  background-image: url('/../../assets/img/wood.jpeg');
} */

.hex.brick {
  background-color: #97593c;
}

.hex.wood {
  background-color: #536d35;
}
/* .hex.wood:after {
  background-image: url('/../../assets/img/wood.jpg');
} */



/* end types of hexs */
/* ----------------------------------------- */
/* ----------------------------------------- */
/* begin roads */
.road {
  border-radius: 3px;
  background-color: #ffffff00;
  position: absolute;
  width: 9px;
  height: 58px;
  z-index: 150;
}

.road.target {
  background-color: transparent;
  position: absolute;
  /* border: 2px dashed black; */
}

.road.target:hover {
  background-color: #ffffff6b;
}

.road.l {
  right: 33px;
  bottom: 104px;
  transform: rotate(90deg);
}

.road.tl {
  left: 94px;
  top: 1px;
  transform: rotate(150deg);
}

.road.tr {
  left: 93px;
  top: 69px;
  transform: rotate(30deg);
}

/* end roads */
/* ----------------------------------------- */
/* ----------------------------------------- */
/* begin harbors */
.harbor {
  position: absolute;
  left: 45px;
  top: 0px;
  text-align: center;
  transform: rotate(90deg);
  z-index: 600;
}

.harbor::before, .harbor::after {
  font-size: 20px;
  line-height: 25px;
  height: 25px;
  width: 125px;
}

.harbor::before {
  top: -15px;
  position: absolute;
}

.harbor::after {
  top: 10px;
  position: absolute;
}

.harbor-piece {
  border-radius: 7px;
  background-color: #93b4bf;
  position: absolute;
  width: 18px;
  height: 75px;
  z-index: 150;
}

.harbor-piece.tl {
  top: -74px;
  left: 30px;
  transform: rotate(60deg);
}

.harbor-piece.l {
  top: -29px;
  left: 5px;
  transform: rotate(180deg);
}

.harbor-piece.bl {
  top: 13px;
  left: 30px;
  transform: rotate(120deg);
}

.harbor-piece.tr {
  top: -73px;
  left: 80px;
  transform: rotate(300deg);
}

.harbor-piece.r {
  top: -31px;
  left: 105px;
  transform: rotate(360deg);
}

.harbor-piece.br {
  top: 13px;
  left: 80px;
  transform: rotate(240deg);
}

.harbor.two-one::before {
  content: "2:1";
  font-size: 20px;
  color: white;
}

.harbor.three-one::before {
  content: "3:1";
  font-size: 20px;
  color: white;
}

.harbor.any::after {
  content: "ANY";
  font-size: 15px;
  color: white;
}

.harbor.wood::after {
  content: "WOOD";
  font-size: 15px;
  color: white;
}

.harbor.ore::after {
  content: "ORE";
  font-size: 15px;
  color: white;
}

.harbor.sheep::after {
  content: "SHEEP";
  font-size: 15px;
  color: white;
}

.harbor.brick::after {
  content: "BRICK";
  font-size: 15px;
  color: white;
}

.harbor.wheat::after {
  content: "WHEAT";
  font-size: 15px;
  color: white;
}

/* end harbors */
/* ----------------------------------------- */
/* ----------------------------------------- */
/* begin numbers */

.hex .number {
  position: absolute;
  font-size: 25px;
  line-height: 40px;
  text-align: center;
  z-index: 200;
  height: 45px;
  width: 45px;
  border-radius: 45px;
  border: 1px solid black;
  top: 42px;
  left: 18px;
  transform: rotate(90deg);
  background-color: #f9e4c6;
}

.hex .number::after {
  position: absolute;
  font-size: 25px;
  line-height: 40px;
  text-align: center;
  z-index: 500;
  height: 45px;
  width: 45px;
  top: 8px;
  left: 0px;
}

.hex #robber {
  opacity: .4;
  border: 2px dotted;
}
/* .hex #robber::after {
  content: "R";
} */

.hex .number.two::before {
  content: "2";
}

.hex .number.two::after {
  content: ".";
}

.hex .number.three::before {
  content: "3";
}

.hex .number.three::after {
  content: "..";
}

.hex .number.four::before {
  content: "4";
}

.hex .number.four::after {
  content: "...";
}

.hex .number.five::before {
  content: "5";
}

.hex .number.five::after {
  content: "....";
}

.hex .number.six::before {
  content: "6";
  color: red;
}

.hex .number.six::after {
  content: ".....";
  color: red;
}

.hex .number.eight::before {
  content: "8";
  color: red;
}

.hex .number.eight::after {
  content: ".....";
  color: red;
}

.hex .number.nine::before {
  content: "9";
}

.hex .number.nine::after {
  content: "....";
}

.hex .number.ten::before {
  content: "10";
}

.hex .number.ten::after {
  content: "...";
}

.hex .number.eleven::before {
  content: "11";
}

.hex .number.eleven::after {
  content: "..";
}

.hex .number.twelve::before {
  content: "12";
}

.hex .number.twelve::after {
  content: ".";
}

/* end numbers */
/* ----------------------------------------- */
/* ----------------------------------------- */
/* begin settlements */
.hex .settlement {
  /* border-top: 1px solid black;
  border-left: 1px solid black;
  border-bottom: 1px solid black;
  border-right: 1px solid black; */

  height: 20px;
  width: 20px;
  position: absolute;
  z-index: 800;
}


.hex .settlement.target {
}

.hex .settlement.target:hover {
  border: 1px dashed black;
  background-color: #ffffff5e;
}

.hex .settlement.target::before {
  display: none;
}

.hex .settlement.t {
  top: 55px;
  left: 100px;
}

.hex .settlement.tl {
  top: -15px;
  left: 65px;
}

/* end  settlements */
/* ----------------------------------------- */
/* ----------------------------------------- */
/* begin cities */
.hex .city {
  border-top: 1px solid black;
  border-left: 1px solid black;
  border-bottom: 1px solid black;
  border-right: 1px solid black;
  height: 30px;
  width: 30px;
  position: absolute;
  z-index: 800;
}

.hex .city.t {
  top: 55px;
  left: 100px;
}

.hex .city.tl {
  top: -15px;
  left: 65px;
}

/* end cities */
/* ----------------------------------------- */


</style>

