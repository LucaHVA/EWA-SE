<template>
  <div v-if="resourcesInitialized" class="container">
    <div class="mainContent">
      <div class="current-player" :style="{ color: currentPlayerColor }">Current Player: {{ currentPlayer }}</div>
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
          <li :class="'hex ' + row1[0].resource" id="h1">
            <div class="settlement target tl" @click="build(3)" id="s3"></div>
            <div class="settlement target t" @click="build(0)" id="s0"></div>
            <div class="road target tl r3 r0" @click="buildRoad(0,3)"></div>
            <div class="road target l r3 r7" @click="buildRoad(7,3)"></div>
            <div class="road target tr r0 r4" @click="buildRoad(0,4)"></div>
            <div class="number">{{ row1[0].number }}</div>
          </li>
          <li :class="'hex ' + row1[1].resource" id="h2">
            <div class="settlement target tl" @click="build(4)" id="s4"></div>
            <div class="settlement target t" @click="build(1)" id="s1"></div>
            <div class="road target tl r4 r1" @click="buildRoad(4,1)"></div>
            <div class="road target l r4 r8" @click="buildRoad(4,8)"></div>
            <div class="road target tr r1 r5" @click="buildRoad(1,5)"></div>
            <div class="number">{{ row1[1].number }}</div>
          </li>
          <li :class="'hex ' + row1[2].resource" id="h3">
            <div class="settlement target tl" @click="build(5)" id="s5"></div>
            <div class="settlement target t" @click="build(2)" id="s2"></div>
            <div class="road target tl r5 r2" @click="buildRoad(5,2)"></div>
            <div class="road target l r9 r5" @click="buildRoad(9,5)"></div>
            <div class="road target tr r2 r6" @click="buildRoad(2,6)"></div>
            <div class="number">{{ row1[2].number }}</div>
          </li>
          <li class="hex water">
            <div class="settlement target tl" @click="build(6)" id="s6"></div>
            <div class="road target l r6 r10" @click="buildRoad(6,10)"></div>
          </li>
        </ol>

        <!-- three -->
        <ol class="even">
          <li class="hex water"></li>
          <li :class="'hex ' + row2[0].resource" id="h4">
            <div class="settlement target tl" @click="build(11)" id="s11"></div>
            <div class="settlement target t" @click="build(7)" id="s7"></div>
            <div class="road target tl r11 r7" @click="buildRoad(11,7)"></div>
            <div class="road target l r11 r16" @click="buildRoad(11,16)"></div>
            <div class="road target tr r7 r12" @click="buildRoad(7,12)"></div>
            <div class="number">{{ row2[0].number }}</div>
          </li>
          <li :class="'hex ' + row2[1].resource" id="h5">
            <div class="road target tl r12 r8" @click="buildRoad(12,8)"></div>
            <div class="road target l r12 r17" @click="buildRoad(12,17)"></div>
            <div class="road target tr r8 r13" @click="buildRoad(8,13)"></div>
            <div class="settlement target tl" @click="build(12)" id="s12"></div>
            <div class="settlement target t" @click="build(8)" id="s8"></div>
            <div class="number">{{ row2[1].number }}</div>
          </li>
          <li :class="'hex ' + row2[2].resource" id="h6">
            <div class="settlement target tl" @click="build(13)" id="s13"></div>
            <div class="settlement target t" @click="build(9)" id="s9"></div>
            <div class="road target tl r13 r9" @click="buildRoad(13,9)"></div>
            <div class="road target l r13 r18" @click="buildRoad(13,18)"></div>
            <div class="road target tr r9 r14" @click="buildRoad(9,14)"></div>
            <div class="number">{{ row2[2].number }}</div>
          </li>
          <li :class="'hex ' + row2[3].resource" id="h7">
            <div class="settlement target tl"></div>
            <div class="settlement target t"></div>
            <div class="road target tl r14 r10" @click="buildRoad(14,10)"></div>
            <div class="road target l r14 r19" @click="buildRoad(14,19)"></div>
            <div class="road target tr r10 r15" @click="buildRoad(10,15)"></div>
            <div class="settlement target tl" @click="build(14)" id="s14"></div>
            <div class="settlement target t" @click="build(10)" id="s10"></div>
            <div class="number">{{ row2[3].number }}</div>
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
          <li :class="'hex '+ row3[0].resource" id="h8">
            <div class="settlement target tl" @click="build(21)" id="s21"></div>
            <div class="settlement target t" @click="build(16)" id="s16"></div>
            <div class="road target tl r21 r16" @click="buildRoad(21,16)"></div>
            <div class="road target l r21 r27" @click="buildRoad(21,27)"></div>
            <div class="road target tr r16 r22" @click="buildRoad(16,22)"></div>
            <div class="number">{{ row3[0].number }}</div>
          </li>
          <li :class="'hex '+ row3[1].resource" id="h9">
            <div class="settlement target tl" @click="build(22)" id="s22"></div>
            <div class="settlement target t" @click="build(17)" id="s17"></div>
            <div class="road target tl r22 r17" @click="buildRoad(22,17)"></div>
            <div class="road target l r22 r28" @click="buildRoad(22,28)"></div>
            <div class="road target tr r17 r23" @click="buildRoad(17,23)"></div>
            <div class="number">{{ row3[1].number }}</div>
          </li>
          <li :class="'hex '+ row3[2].resource" id="h10">
            <div class="settlement target tl" @click="build(23)" id="s23"></div>
            <div class="settlement target t" @click="build(18)" id="s18"></div>
            <div class="road target tl r23 r18" @click="buildRoad(23,18)"></div>
            <div class="road target l r29 r23" @click="buildRoad(29,23)"></div>
            <div class="road target tr r18 r24" @click="buildRoad(18,24)"></div>
            <div class="number">{{ row3[2].number }}</div>
          </li>
          <li :class="'hex '+ row3[3].resource" id="h11">
            <div class="settlement target tl" @click="build(24)" id="s24"></div>
            <div class="settlement target t" @click="build(19)" id="s19"></div>
            <div class="road target tl r24 r19" @click="buildRoad(24,19)"></div>
            <div class="road target l r24 r30" @click="buildRoad(24,30)"></div>
            <div class="road target tr r19 r25" @click="buildRoad(19,25)"></div>
            <div class="number">{{ row3[3].number }}</div>
          </li>
          <li :class="'hex '+ row3[4].resource" id="h12">
            <div class="settlement target tl" @click="build(25)" id="s25"></div>
            <div class="settlement target t" @click="build(20)" id="s20"></div>
            <div class="road target tl r25 r20" @click="buildRoad(25,20)"></div>
            <div class="road target l r25 r31" @click="buildRoad(25,31)"></div>
            <div class="road target tr r20 r26" @click="buildRoad(20,26)"></div>
            <div class="number">{{ row3[4].number }}</div>
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
          <li :class="'hex '+ row4[0].resource" id="h13">
            <div class="settlement target tl" @click="build(33)" id="s33"></div>
            <div class="settlement target t" @click="build(28)" id="s28"></div>
            <div class="road target l r33 r38" @click="buildRoad(33,38)"></div>
            <div class="road target tr r34 r28" @click="buildRoad(34,28)"></div>
            <div class="road target tl r28 r33" @click="buildRoad(28,33)"></div>
            <div class="number">{{ row4[0].number }}</div>
          </li>
          <li :class="'hex '+ row4[1].resource" id="h14">
            <div class="settlement target tl" @click="build(34)" id="s34"></div>
            <div class="settlement target t" @click="build(29)" id="s29"></div>
            <div class="road target tl r34 r29" @click="buildRoad(34,29)"></div>
            <div class="road target l r34 r39" @click="buildRoad(34,39)"></div>
            <div class="road target tr r29 r35" @click="buildRoad(29,35)"></div>
            <div class="number">{{ row4[1].number }}</div>
          </li>
          <li :class="'hex '+ row4[2].resource" id="h15">
            <div class="settlement target tl" @click="build(35)" id="s35"></div>
            <div class="settlement target t" @click="build(30)" id="s30"></div>
            <div class="road target tl r35 r30" @click="buildRoad(35,30)"></div>
            <div class="road target l r35 r40" @click="buildRoad(35,40)"></div>
            <div class="road target tr r30 r36" @click="buildRoad(30,36)"></div>
            <div class="number">{{ row4[2].number }}</div>
          </li>
          <li :class="'hex '+ row4[3].resource" id="h16">
            <div class="road target tl r36 r31" @click="buildRoad(36,31)"></div>
            <div class="road target l r36 r41" @click="buildRoad(36,41)"></div>
            <div class="road target tr r31 r37" @click="buildRoad(31,37)"></div>
            <div class="settlement target tl" @click="build(36)" id="s36"></div>
            <div class="settlement target t" @click="build(31)" id="s31"></div>
            <div class="number">{{ row4[3].number }}</div>
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
          <li :class="'hex '+ row5[0].resource" id="h17">
            <div class="settlement target tl" @click="build(43)" id="s43"></div>
            <div class="settlement target t" @click="build(39)" id="s39"></div>
            <div class="road target tl r43 r39" @click="buildRoad(43,39)"></div>
            <div class="road target l r43 r47" @click="buildRoad(43,47)"></div>
            <div class="road target tr r39 r44" @click="buildRoad(39,44)"></div>
            <div class="number">{{ row5[0].number }}</div>
          </li>
          <li :class="'hex '+ row5[1].resource" id="h18">
            <div class="settlement target tl" @click="build(44)" id="s44"></div>
            <div class="settlement target t" @click="build(40)" id="s40"></div>
            <div class="road target tl r44 r40" @click="buildRoad(44,40)"></div>
            <div class="road target l r44 r48" @click="buildRoad(44,48)"></div>
            <div class="road target tr r40 r45" @click="buildRoad(40,45)"></div>
            <div class="number">{{ row5[1].number }}</div>
          </li>
          <li :class="'hex '+ row5[2].resource" id="h19">
            <div class="settlement target tl" @click="build(45)" id="s45"></div>
            <div class="settlement target t" @click="build(41)" id="s41"></div>
            <div class="road target tl r45 r41" @click="buildRoad(45,41)"></div>
            <div class="road target l r45 r49" @click="buildRoad(45,49)"></div>
            <div class="road target tr r41 r46" @click="buildRoad(41,46)"></div>
            <div class="number">{{ row5[2].number }}</div>
          </li>
          <li class="hex water">
            <div class="settlement target tl" @click="build(46)" id="s46"></div>
            <div class="settlement target t" @click="build(42)" id="s42"></div>
            <div class="road target tl r46 r42" @click="buildRoad(46,42)"></div>
            <div class="road target l r46 r50" @click="buildRoad(46,50)"></div>
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
        <button @click="nextTurn">Next Turn</button>
      </div>
    </div>

    <div class="game-buttons-container">
      <div class="dice-container">
        <button class="pos-button" id="roll-dice-button" @click="rollDice">Roll the dices!
          <img alt="roll dice" src="../../assets/images/game/dices/clear_rolling-dices.png">
        </button>
        <div class="dice-outcome-container">
          <img :src="leftDiceImg" alt="left dice outcome">
          <img :src="rightDiceImg" alt="right dice outcome">
          <h2>Total outcome: {{dicesOutcome}}</h2>
        </div>
      </div>
      <div>
<!--        <button class="pos-button" id="next-turn-button" @click="continueNextPlayerTurn">Next turn</button>-->
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "gameComponent",
  data() {
    return {
      currentPlayerIndex: 0,
      playerColors: ["red", "blue", "green", "orange"],
      row1: [],
      row2: [],
      row3: [],
      row4: [],
      row5: [],
      resourcesInitialized: false,
      settlements: [],
      roads: [],
      resourceCounts: {
        desert: 1,
        ore: 3,
        brick: 3,
        wheat: 4,
        wood: 4,
        sheep: 4
      },
      dicesOutcome: "NaN",
      leftDiceImg: require("../../assets/images/game/dices/clear_perspective-dice-six-faces-random.png"),
      rightDiceImg: require("../../assets/images/game/dices/clear_perspective-dice-six-faces-random.png"),
      clearWhiteDiceImg: {
        1: require("../../assets/images/game/dices/clear_dice-six-faces-one.png"),
        2: require("../../assets/images/game/dices/clear_dice-six-faces-two.png"),
        3: require("../../assets/images/game/dices/clear_dice-six-faces-three.png"),
        4: require("../../assets/images/game/dices/clear_dice-six-faces-four.png"),
        5: require("../../assets/images/game/dices/clear_dice-six-faces-five.png"),
        6: require("../../assets/images/game/dices/clear_dice-six-faces-six.png"),
      },
    };
  },
  mounted() {
    setTimeout(() => {
      this.initializeBoard();
    }, 1000);
  },
  computed: {
    currentPlayer() {
      return this.playerColors[this.currentPlayerIndex];
    },
    currentPlayerColor() {
      return this.currentPlayer;
    }
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
      // Set the flag to indicate resources are initialized
      this.resourcesInitialized = true;
    },
    getRandomResource() {
      const resources = ['desert', 'ore', 'brick', 'wheat', 'wood', 'sheep'];
      const availableResources = resources.filter(resource => this.resourceCounts[resource] > 0);

      const randomIndex = Math.floor(Math.random() * availableResources.length);
      const selectedResource = availableResources[randomIndex];
      this.resourceCounts[selectedResource]--;
      return selectedResource;
    },

    assignRandomNumber() {
      let randomNumber;
      do {
        randomNumber = Math.floor(Math.random() * 11) + 2;
      } while (randomNumber === 7);
      return randomNumber;
    },

    build(index) {
      // Store the owner of the settlement
      this.settlements[index] = this.currentPlayerIndex;

      // Add a CSS class to the settlement position
      const settlementElement = document.getElementById('s' + index);
      if (settlementElement) {
        settlementElement.classList.add(`has-settlement-${this.currentPlayerIndex}`);
      }
    },
    buildRoad(fromIndex, toIndex) {
      // Add the road to the list of roads
      this.roads.push({ from: fromIndex, to: toIndex, owner: this.currentPlayerIndex });

      // Add a CSS class to the road position
      const roadElement = document.querySelector(`.road.r${fromIndex}.r${toIndex}`);
      if (roadElement) {
        roadElement.classList.add(`build-${this.currentPlayerIndex}`);
      }
    },
    nextTurn() {
      // Increment the current player index
      this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.playerColors.length;
    },
    rollDice(){
      // Generate random number between 1 and 6
      function roll() {
        return 1 + Math.floor(6 * Math.random());
      }

      // Assign dice outcome
      let rightDiceOutcome = roll();
      let leftDiceOutcome = roll();
      this.dicesOutcome = rightDiceOutcome + leftDiceOutcome;

      //TODO Activate robber if outcome is 7

      // Update dice outcome images
      this.leftDiceImg = this.clearWhiteDiceImg[leftDiceOutcome];
      this.rightDiceImg = this.clearWhiteDiceImg[rightDiceOutcome];
    },
    continueNextPlayerTurn(){
      // Check if player rolled dice


      // Continue to next player's turn

    },
  }
};
</script>

<style scoped>
/* General */
.container {
  display: flex;
  justify-content: center;
}

/* ----------------------------------------- */

.current-player {
  margin-bottom: 10px;
}

.has-settlement-0 {
  background-color: red;
}
.has-settlement-1 {
  background-color: blue;
}
.has-settlement-2 {
  background-color: green;
}
.has-settlement-3 {
  background-color: orange;
}



.box {
  margin: 0 auto;
  /*margin-left: 31%;*/
  width: 1000px;
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

.build-0 {
  background-color: red;
}
.build-1 {
  background-color: blue;
}
.build-2 {
  background-color: green;
}
.build-3 {
  background-color: orange;
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

.hex .settlement.has-settlement::after {
  content: "";
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  height: 10px;
  background-color: brown; /* Adjust color as needed */
  border-radius: 50%;
}
.settlement-svg {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 40px; /* Adjust width as needed */
  height: 40px; /* Adjust height as needed */
  /* Add any additional styling properties here */
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

/* Dice container */
.dice-container {
  width: max-content;
  alignment: center;
  align-self: center;
}

/* Dice button */
#roll-dice-button {
  display: grid;
  justify-items: center;
  padding: 15px;
}

#roll-dice-button img {
  width: 120px;
}

/* Dice outcome */
.dice-outcome-container {
  text-align: center;
  width: fit-content;
}

.dice-outcome-container img {
  width: 120px;
  background-color: #000000;
  border-radius: 10px;
  margin: 5px;
}

.dice-outcome-container h2{
  font-weight: 400;
  margin: 0;
}

</style>

