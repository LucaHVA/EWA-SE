<template>

  <div class="modal" v-if="canPlayyear_of_plentyCard">
    <div class="modal-content">
      <p>Choose two resources:</p>
      <div class="resource-cards">
        <div v-for="resource in resourceCardTypes" :key="resource">
          <img class="resource-cardmodal" :src="resourceCardImg[resource]" :alt="resource + ' card'">
          <div>
            <button @click="incrementResource(resource)">+</button>
            <span>{{ getSelectedCount(resource) }}</span>
            <button @click="decrementResource(resource)">-</button>
          </div>
        </div>
      </div>
      <button @click="confirmSelectedResources" :disabled="selectedResources.length < 2">Confirm</button>
    </div>
  </div>

  <div class="modal" v-if="canPlayMonopolyCard">
    <div class="modal-content">
      <p>Choose a resource to monopolize:</p>
      <div class="resource-cards">
        <div v-for="resource in resourceCardTypes" :key="resource" @click="selectResource(resource)">
          <img class="resource-cardmodal" :src="resourceCardImg[resource]" :alt="resource + ' card'">
          <p>{{ selectedResources.includes(resource) ? '+' : 'Click to select' }}</p>
        </div>
      </div>
      <button @click="confirmSelectedResource" :disabled="selectedResources.length !== 1">Confirm</button>
    </div>
  </div>



  <div class="modal" v-if="showTradingModal">
    <div class="modal-content">
      <h2>Select the resource you want to trade</h2>
      <div class="resource-selection">
        <div
            v-for="resourceType in simplifiedInventory"
            :key="resourceType"
            class="resource-cards"
            :class="{ 'selected': selectedResource === resourceType }"
            @click="selectTrade(resourceType)"
        >
          <img :src="resourceCardImg[resourceType]" :alt="resourceType">
        </div>
      </div>
      <h2>Trade for</h2>
      <div class="resource-selection">
        <div
            v-for="resourceType in resourceCardTypes"
            :key="resourceType"
            class="resource-cards"
            @click="tradeResources(resourceType)"
        >
          <img :src="resourceCardImg[resourceType]" :alt="resourceType">
        </div>
      </div>
      <button class="pos-button" @click="cancelTrade">Cancel</button>
    </div>
  </div>


  <div class="previous-page-container">
    <button class="previous-page-button" @click="confirmNavigation">
      <img src="../../assets/images/back_button.png" alt="back">
    </button>
  </div>

  <div v-if="resourcesInitialized" class="container">
    <div id="game-error-message" class="hidden">
      <div class="error-message-box">
        <h2 class="error-message">An error occurred</h2>
        <button id="confirm-error-message" class="pos-button">Continue</button>
      </div>
    </div>
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
          <li :class="'hex ' + row1[0].resource" id="h1">
            <div class="settlement target tl" @click="build(3)" id="s3"></div>
            <div class="settlement target t" @click="build(0)" id="s0"></div>
            <div class="road target tl r3 r0" @click="buildRoad(0,3)"></div>
            <div class="road target l r3 r7" @click="buildRoad(7,3)"></div>
            <div class="road target tr r0 r4" @click="buildRoad(0,4)"></div>
            <div class="number" @click="placeRobber(0)">{{ row1[0].number }}</div>
          </li>
          <li :class="'hex ' + row1[1].resource" id="h2">
            <div class="settlement target tl" @click="build(4)" id="s4"></div>
            <div class="settlement target t" @click="build(1)" id="s1"></div>
            <div class="road target tl r4 r1" @click="buildRoad(4,1)"></div>
            <div class="road target l r4 r8" @click="buildRoad(4,8)"></div>
            <div class="road target tr r1 r5" @click="buildRoad(1,5)"></div>
            <div class="number" @click="placeRobber(1)">{{ row1[1].number }}</div>
          </li>
          <li :class="'hex ' + row1[2].resource" id="h3">
            <div class="settlement target tl" @click="build(5)" id="s5"></div>
            <div class="settlement target t" @click="build(2)" id="s2"></div>
            <div class="road target tl r5 r2" @click="buildRoad(5,2)"></div>
            <div class="road target l r9 r5" @click="buildRoad(9,5)"></div>
            <div class="road target tr r2 r6" @click="buildRoad(2,6)"></div>
            <div class="number" @click="placeRobber(2)">{{ row1[2].number }}</div>
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
            <div class="number" @click="placeRobber(3)">{{ row2[0].number }}</div>
          </li>
          <li :class="'hex ' + row2[1].resource" id="h5">
            <div class="road target tl r12 r8" @click="buildRoad(12,8)"></div>
            <div class="road target l r12 r17" @click="buildRoad(12,17)"></div>
            <div class="road target tr r8 r13" @click="buildRoad(8,13)"></div>
            <div class="settlement target tl" @click="build(12)" id="s12"></div>
            <div class="settlement target t" @click="build(8)" id="s8"></div>
            <div class="number" @click="placeRobber(4)">{{ row2[1].number }}</div>
          </li>
          <li :class="'hex ' + row2[2].resource" id="h6">
            <div class="settlement target tl" @click="build(13)" id="s13"></div>
            <div class="settlement target t" @click="build(9)" id="s9"></div>
            <div class="road target tl r13 r9" @click="buildRoad(13,9)"></div>
            <div class="road target l r13 r18" @click="buildRoad(13,18)"></div>
            <div class="road target tr r9 r14" @click="buildRoad(9,14)"></div>
            <div class="number" @click="placeRobber(5)">{{ row2[2].number }}</div>
          </li>
          <li :class="'hex ' + row2[3].resource" id="h7">
            <div class="settlement target tl"></div>
            <div class="settlement target t"></div>
            <div class="road target tl r14 r10" @click="buildRoad(14,10)"></div>
            <div class="road target l r14 r19" @click="buildRoad(14,19)"></div>
            <div class="road target tr r10 r15" @click="buildRoad(10,15)"></div>
            <div class="settlement target tl" @click="build(14)" id="s14"></div>
            <div class="settlement target t" @click="build(10)" id="s10"></div>
            <div class="number" @click="placeRobber(6)">{{ row2[3].number }}</div>
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
            <div class="number" @click="placeRobber(7)">{{ row3[0].number }}</div>
          </li>
          <li :class="'hex '+ row3[1].resource" id="h9">
            <div class="settlement target tl" @click="build(22)" id="s22"></div>
            <div class="settlement target t" @click="build(17)" id="s17"></div>
            <div class="road target tl r22 r17" @click="buildRoad(22,17)"></div>
            <div class="road target l r22 r28" @click="buildRoad(22,28)"></div>
            <div class="road target tr r17 r23" @click="buildRoad(17,23)"></div>
            <div class="number" @click="placeRobber(8)">{{ row3[1].number }}</div>
          </li>
          <li :class="'hex '+ row3[2].resource" id="h10">
            <div class="settlement target tl" @click="build(23)" id="s23"></div>
            <div class="settlement target t" @click="build(18)" id="s18"></div>
            <div class="road target tl r23 r18" @click="buildRoad(23,18)"></div>
            <div class="road target l r29 r23" @click="buildRoad(29,23)"></div>
            <div class="road target tr r18 r24" @click="buildRoad(18,24)"></div>
            <div class="number" @click="placeRobber(9)">{{ row3[2].number }}</div>
          </li>
          <li :class="'hex '+ row3[3].resource" id="h11">
            <div class="settlement target tl" @click="build(24)" id="s24"></div>
            <div class="settlement target t" @click="build(19)" id="s19"></div>
            <div class="road target tl r24 r19" @click="buildRoad(24,19)"></div>
            <div class="road target l r24 r30" @click="buildRoad(24,30)"></div>
            <div class="road target tr r19 r25" @click="buildRoad(19,25)"></div>
            <div class="number" @click="placeRobber(10)">{{ row3[3].number }}</div>
          </li>
          <li :class="'hex '+ row3[4].resource" id="h12">
            <div class="settlement target tl" @click="build(25)" id="s25"></div>
            <div class="settlement target t" @click="build(20)" id="s20"></div>
            <div class="road target tl r25 r20" @click="buildRoad(25,20)"></div>
            <div class="road target l r25 r31" @click="buildRoad(25,31)"></div>
            <div class="road target tr r20 r26" @click="buildRoad(20,26)"></div>
            <div class="number" @click="placeRobber(11)">{{ row3[4].number }}</div>
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
            <div class="number" @click="placeRobber(12)">{{ row4[0].number }}</div>
          </li>
          <li :class="'hex '+ row4[1].resource" id="h14">
            <div class="settlement target tl" @click="build(34)" id="s34"></div>
            <div class="settlement target t" @click="build(29)" id="s29"></div>
            <div class="road target tl r34 r29" @click="buildRoad(34,29)"></div>
            <div class="road target l r34 r39" @click="buildRoad(34,39)"></div>
            <div class="road target tr r29 r35" @click="buildRoad(29,35)"></div>
            <div class="number" @click="placeRobber(13)">{{ row4[1].number }}</div>
          </li>
          <li :class="'hex '+ row4[2].resource" id="h15">
            <div class="settlement target tl" @click="build(35)" id="s35"></div>
            <div class="settlement target t" @click="build(30)" id="s30"></div>
            <div class="road target tl r35 r30" @click="buildRoad(35,30)"></div>
            <div class="road target l r35 r40" @click="buildRoad(35,40)"></div>
            <div class="road target tr r30 r36" @click="buildRoad(30,36)"></div>
            <div class="number" @click="placeRobber(14)">{{ row4[2].number }}</div>
          </li>
          <li :class="'hex '+ row4[3].resource" id="h16">
            <div class="road target tl r36 r31" @click="buildRoad(36,31)"></div>
            <div class="road target l r36 r41" @click="buildRoad(36,41)"></div>
            <div class="road target tr r31 r37" @click="buildRoad(31,37)"></div>
            <div class="settlement target tl" @click="build(36)" id="s36"></div>
            <div class="settlement target t" @click="build(31)" id="s31"></div>
            <div class="number" @click="placeRobber(15)">{{ row4[3].number }}</div>
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
            <div class="number" @click="placeRobber(16)">{{ row5[0].number }}</div>
          </li>
          <li :class="'hex '+ row5[1].resource" id="h18">
            <div class="settlement target tl" @click="build(44)" id="s44"></div>
            <div class="settlement target t" @click="build(40)" id="s40"></div>
            <div class="road target tl r44 r40" @click="buildRoad(44,40)"></div>
            <div class="road target l r44 r48" @click="buildRoad(44,48)"></div>
            <div class="road target tr r40 r45" @click="buildRoad(40,45)"></div>
            <div class="number" @click="placeRobber(17)">{{ row5[1].number }}</div>
          </li>
          <li :class="'hex '+ row5[2].resource" id="h19">
            <div class="settlement target tl" @click="build(45)" id="s45"></div>
            <div class="settlement target t" @click="build(41)" id="s41"></div>
            <div class="road target tl r45 r41" @click="buildRoad(45,41)"></div>
            <div class="road target l r45 r49" @click="buildRoad(45,49)"></div>
            <div class="road target tr r41 r46" @click="buildRoad(41,46)"></div>
            <div class="number" @click="placeRobber(18)">{{ row5[2].number }}</div>
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
      </div>
      <div class="game-buttons-container">


        <div class="dice-container">
          <button class="pos-button" id="roll-dice-button" @click="rollDice" :disabled="hasRolledDice">Roll the dices!
            <img alt="roll dice" src="../../assets/images/game/dices/clear_rolling-dices.png">
          </button>
          <div class="dice-outcome-container">
            <img :src="leftDiceImg" alt="left dice outcome">
            <img :src="rightDiceImg" alt="right dice outcome">
            <h2>Total outcome: {{ dicesOutcome }}</h2>
          </div>
        </div>
        <div>
          <!-- Modal for robber placement message -->
          <div class="modal" v-if="showRobberPlacementModal">
            <div class="modal-content">
              <p>Click one of the numbers to place the robber.</p>
            </div>
          </div>
          <button class="pos-button" id="next-turn-button" @click="nextTurn">Next turn</button>
          <div>Time remaining: {{ this.timeRemaining }}</div>
          <div>Points to win: {{this.game.pointsToWin}} </div>
          <div v-if="!hasRolledDice">Turn: {{ turn }}</div>
          <div class="current-player" :style="{ color: currentPlayerColor }">Current Player: {{ currentPlayer }}</div>
        </div>
        <!--        <div class="player-cards-container">-->
        <!--          <div v-for="player in players" :key="player" class="player-card">-->
        <!--            <div>Player: {{ player.playerId }}</div>-->
        <!--            <div>Points: {{ player.pointAmount }}</div>-->
        <!--            <div>Longest road: {{ player.longestRoad }}</div>-->
        <!--            <div>Settlement amount: {{ player.settlementAmount }}</div>-->
        <!--            <div>Knight cards used: {{ player.knightsUsed }}</div>-->
        <!--          </div>-->

        <!--          <div class="player-cards-container">-->
        <!--            <div v-for="player in players" :key="player" class="player-card">-->
        <!--              <div>Player: {{ player.playerId }}</div>-->
        <!--              <div>Points: {{ player.pointAmount }}</div>-->
        <!--              <div>Longest road: {{ player.longestRoad }}</div>-->
        <!--              <div>Settlement amount: {{ player.settlementAmount }}</div>-->
        <!--              <div>Knight cards used: {{ player.knightsUsed }}</div>-->
        <!--            </div>-->
        <!--          </div>-->
        <!--        </div>-->
      </div>

    </div>
    <div class="player-inventory-container">
      <div class="player-inventory-resources">
        <div v-for="resource in currentPlayerResourcesInventory"  :key="resource.id">
          <div class="inventory-resource-card"><img :src=this.resourceCardImg[resource] alt="resource card"></div>
        </div>
        <div class="game-buttons-container">
          <button class="pos-button" @click="acquireDevelopmentCard">Buy Development Card</button>
          <button class="pos-button" v-if="canPlayKnightCard" @click="playDevelopmentCard('knight')">Play Knight
            Card
          </button>
          <button class="pos-button" v-if="canPlayyear_of_plentyCard" @click="playDevelopmentCard('year_of_plenty')">
            Play year_of_plenty Card
          </button>
          <button class="pos-button" v-if="canPlayMonopolyCard" @click="playDevelopmentCard('monopoly')">Play Monopoly
            Card
          </button>
          <button class="pos-button" v-if="canPlayRoad_BuildingCard" @click="playDevelopmentCard('Road_Building')">
            Play Road_Building Card
          </button>
          <button class="pos-button" @click="openTradeModal">Trade Resources</button>
          <button class="pos-button" @click="upgradeSettlementToCity">Upgrade Settlement to City</button>

        </div>
      </div>
    </div>


  </div>
</template>

<script>
import Player from "@/models/player";



export default {
  name: "gameComponent",
  data() {
    return {
      selectedResource: null,
      selectedResources: [],
      showTradingModal: false,
      resourceCardTypes: ['brick', 'wheat', 'ore', 'wood', 'sheep'],
      availableResources: [],
      canPlayKnightCard: false,
      canPlayyear_of_plentyCard: false,
      showRobberPlacementModal: false,
      canPlayRoad_BuildingCard: false,
      canPlayMonopolyCard: false,
      canPlayVictory_PointCard: false,
      game: null,
      gameId: this.$route.params.id,
      previousPage: "/gameSettings",
      currentPlayerIndex: 0,
      playerColors: ["red", "blue", "green", "orange"],
      players: [],
      playerPoints: [0, 0, 0, 0],
      currentPlayerResources: [],
      robberHexIndex: null,
      hasRolledDice: false,
      timeRemaining: 0,
      turn: 1,
      row1: [],
      row2: [],
      row3: [],
      row4: [],
      row5: [],
      adjacents: [
        [0, 3, 4, 7, 8, 12],
        [1, 4, 5, 8, 9, 13],
        [2, 5, 6, 9, 10, 14],
        [7, 11, 12, 16, 17, 22],
        [8, 12, 13, 17, 18, 23],
        [9, 13, 14, 18, 19, 24],
        [10, 14, 15, 19, 20, 25],
        [16, 21, 22, 27, 28, 33],
        [17, 22, 23, 28, 29, 34],
        [18, 23, 24, 29, 30, 35],
        [19, 24, 25, 30, 31, 36],
        [20, 25, 26, 31, 32, 37],
        [28, 33, 34, 38, 39, 43],
        [29, 34, 35, 39, 40, 44],
        [30, 35, 36, 40, 41, 45],
        [31, 36, 37, 41, 42, 46],
        [39, 43, 44, 47, 48, 51],
        [40, 44, 45, 48, 49, 52],
        [41, 45, 46, 49, 50, 53]
      ],
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
      resourceCardImg: {
        brick: require("../../assets/images/game/resources/brick_card_v1.png"),
        wheat: require("../../assets/images/game/resources/wheat_card_v1.png"),
        ore: require("../../assets/images/game/resources/ore_card_v1.png"),
        wood: require("../../assets/images/game/resources/wood_card_v1.png"),
        sheep: require("../../assets/images/game/resources/sheep_card_v1.png")
      },
      developmentCards: ['Road_Building','year_of_plenty','monopoly','knight'],
      hasPlayedDevelopmentCard: false,

    };
  },
  inject:["gameService"],
 async created() {
    this.game = await this.gameService.asyncGetById(this.gameId)
    console.log(this.game)
  },
  mounted() {
    setTimeout(() => {
      this.initializeBoard();
      this.initializePlayers();
    }, 1000);
  },
  computed: {
    currentPlayer() {
      return this.playerColors[this.currentPlayerIndex];
    },
    currentPlayerColor() {
      return this.currentPlayer;
    },
    currentPlayerResourcesInventory() {
      return this.players[this.currentPlayerIndex].resources;
    },
    simplifiedInventory() {
      const simplified = {};
      // Iterate through each resource type
      this.resourceCardTypes.forEach(type => {
        // Count the number of cards of this type in inventory
        const count = this.currentPlayerResourcesInventory.filter(card => card === type).length;
        // If the count is 4 or more, add one card to simplified inventory
        if (count >= 4) {
          simplified[type] = type;
        }
      });
      // Return an array of simplified inventory
      return Object.values(simplified);
    },
  },
  methods: {

    checkConnectedRoads() {
      // Helper function to check if two roads are connected
      const areRoadsConnected = (road1, road2) => {
        const from1 = parseInt(road1.dataset.from);
        const to1 = parseInt(road1.dataset.to);
        const from2 = parseInt(road2.dataset.from);
        const to2 = parseInt(road2.dataset.to);

        return (from1 === from2 || from1 === to2 || to1 === from2 || to1 === to2);
      };

      // Helper function to find all connected roads starting from a specific road
      const findConnectedRoads = (startRoad, playerIndex) => {
        const stack = [startRoad];
        const connectedRoads = new Set();

        while (stack.length > 0) {
          const currentRoad = stack.pop();
          if (!connectedRoads.has(currentRoad)) {
            connectedRoads.add(currentRoad);
            const fromIndex = parseInt(currentRoad.dataset.from);
            const toIndex = parseInt(currentRoad.dataset.to);

            // Get all roads adjacent to the current road
            const roadsAdjacentToFrom = document.querySelectorAll(`.road.r${fromIndex}.build-${playerIndex}`);
            const roadsAdjacentToTo = document.querySelectorAll(`.road.r${toIndex}.build-${playerIndex}`);
            const adjacentRoads = Array.from(roadsAdjacentToFrom).concat(Array.from(roadsAdjacentToTo));

            // Add all connected roads to the stack
            for (const adjacentRoad of adjacentRoads) {
              if (!connectedRoads.has(adjacentRoad) && areRoadsConnected(currentRoad, adjacentRoad)) {
                stack.push(adjacentRoad);
              }
            }
          }
        }

        return connectedRoads;
      };

      // Iterate through each player
      this.players.forEach((player, playerIndex) => {
        const playerRoads = document.querySelectorAll(`.road.build-${playerIndex}`);
        let longestRoadLength = 0;
        let longestRoads = [];

        // Iterate through each road and find the connected roads
        playerRoads.forEach(road => {
          const connectedRoads = findConnectedRoads(road, playerIndex);
          if (connectedRoads.size > longestRoadLength) {
            longestRoadLength = connectedRoads.size;
            longestRoads = Array.from(connectedRoads);
          }
        });

        // Log the roads built by the player
        console.log(`Player ${playerIndex} (${player.name}) roads:`, Array.from(playerRoads).map(road => ({
          from: road.dataset.from,
          to: road.dataset.to
        })));

        // Log the longest connected road for the player
        console.log(`Player ${playerIndex} (${player.name}) longest connected road length:`, longestRoadLength);
        console.log(`Player ${playerIndex} (${player.name}) longest connected road IDs:`,
            longestRoads.map(road => ({
              from: road.dataset.from,
              to: road.dataset.to
            }))
        );
      });
    },




    upgradeSettlementToCity() {
      // Set a flag to indicate that the player is in the process of upgrading a settlement to a city
      this.isUpgradingToCity = true;

      // Check if the current player has enough resources to upgrade
      const currentPlayer = this.players[this.currentPlayerIndex];
      const hasEnoughResources = currentPlayer.resources.filter(resource => resource === 'wheat').length >= 2 &&
          currentPlayer.resources.filter(resource => resource === 'ore').length >= 3;

      // Check if the player has enough resources to upgrade
      if (!hasEnoughResources) {
        this.displayError("You don't have enough resources to upgrade to a city.");
        return;
      }

      // Deduct the required resources from the player's inventory
      currentPlayer.resources.splice(currentPlayer.resources.indexOf('ore'), 3);
      currentPlayer.resources.splice(currentPlayer.resources.indexOf('wheat'), 2);

      // Display a message to prompt the player to select a settlement to upgrade
      this.displayError("Click on a settlement to upgrade it to a city.");

      // Add an event listener to listen for clicks on settlements
      const settlementElements = document.querySelectorAll('.settlement');
      settlementElements.forEach(settlementElement => {
        settlementElement.addEventListener('click', this.handleSettlementClickForUpgrade);
      });
    },

    handleSettlementClickForUpgrade(event) {
      // Remove the event listener from all settlements
      const settlementElements = document.querySelectorAll('.settlement');
      settlementElements.forEach(settlementElement => {
        settlementElement.removeEventListener('click', this.handleSettlementClickForUpgrade);
      });

      // Get the clicked settlement element
      const clickedSettlement = event.currentTarget;



      // Add the 'city' class along with the player's ID to the clicked settlement element
      clickedSettlement.classList.add(`city-${this.currentPlayerIndex}`);

      this.playerPoints[this.currentPlayerIndex] += 1;

      // Reset the flag to indicate that the upgrade process is complete
      this.isUpgradingToCity = false;

      // Display a message to indicate the successful upgrade
      this.displayError("Settlement upgraded to a city successfully.");
    },

    openTradeModal() {
      // Show the trading modal
      this.showTradingModal = true;
    },
    selectTrade(selectedResource) {
      // Select the resource for trade
      this.selectedResource = selectedResource;
    },
    tradeResources(newResourceType) {
      // Perform trade only if a resource is selected
      if (this.selectedResource) {
        // Deduct four cards of the selected resource from currentPlayerResourcesInventory
        for (let i = 0; i < 4; i++) {
          const selectedIndex = this.currentPlayerResourcesInventory.indexOf(this.selectedResource);
          if (selectedIndex !== -1) {
            this.currentPlayerResourcesInventory.splice(selectedIndex, 1);
          }
        }
        // Add newResourceType to currentPlayerResourcesInventory
        this.currentPlayerResourcesInventory.push(newResourceType);
        // Log the trade
        console.log(`Traded 4 ${this.selectedResource} for 1 ${newResourceType}`);
        // Reset selectedResource
        this.selectedResource = null;
        // Close the trading modal
        this.showTradingModal = false;
      }
    },
    cancelTrade() {
      // Close the trading modal without performing any action
      this.showTradingModal = false;
    },

    confirmNavigation() {
      if (confirm('Are you sure you want to go to the previous page?')) {
        this.$router.push(this.previousPage);
      }
    },
    initializeBoard() {
      for (let i = 0; i < 3; i++) {
        const resource = this.getRandomResource();
        const number = this.assignRandomNumber();
        this.row1.push({resource, number});
      }
      for (let i = 0; i < 4; i++) {
        const resource = this.getRandomResource();
        const number = this.assignRandomNumber();
        this.row2.push({resource, number});
      }
      for (let i = 0; i < 5; i++) {
        const resource = this.getRandomResource();
        const number = this.assignRandomNumber();
        this.row3.push({resource, number});
      }
      for (let i = 0; i < 4; i++) {
        const resource = this.getRandomResource();
        const number = this.assignRandomNumber();
        this.row4.push({resource, number});
      }
      for (let i = 0; i < 3; i++) {
        const resource = this.getRandomResource();
        const number = this.assignRandomNumber();
        this.row5.push({resource, number});
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
      // Check if it's the first turn
      const isFirstTurn = this.turn === 1;
      const isSecondTurn = this.turn === 2;

      // Check if the current player has the required resources (1 wood, 1 brick, 1 sheep, and 1 wheat) on non-first turn
      if ((!isFirstTurn && !isSecondTurn) || (isFirstTurn && !this.players[this.currentPlayerIndex].hasBuiltFirstTurn) || (isSecondTurn && !this.players[this.currentPlayerIndex].hasBuiltSecondTurn)) {
        const currentPlayer = this.players[this.currentPlayerIndex];
        const hasWood = currentPlayer.resources.includes('wood');
        const hasBrick = currentPlayer.resources.includes('brick');
        const hasSheep = currentPlayer.resources.includes('sheep');
        const hasWheat = currentPlayer.resources.includes('wheat');

        if (!(isFirstTurn || isSecondTurn) && (!hasWood || !hasBrick || !hasSheep || !hasWheat)) {
          // Display an error message if the player doesn't have the required resources on non-first turn
          this.displayError("You don't have enough resources to build a settlement.");
          return;
        }

        // Check if there are settlements adjacent to the road
        const adjacentSettlements = this.getAdjacentSettlements(index);

        // Check if any adjacent settlement is occupied
        if (adjacentSettlements.some(settlement => settlement.player !== null && settlement.player !== this.currentPlayerIndex)) {
          this.displayError("You cannot build a settlement adjacent to another player's settlement.");
          return;
        }

        // Check if there are at least 2 roads between settlements
        const roadsBetweenSettlements = this.getRoadsBetweenSettlements(index);
        if (roadsBetweenSettlements < 2) {
          this.displayError("There should be at least 2 roads between settlements.");
          return;
        }

        // Deduct the resources from the player's inventory on non-first turn
        if (!(isFirstTurn || isSecondTurn)) {
          currentPlayer.resources.splice(currentPlayer.resources.indexOf('wood'), 1);
          currentPlayer.resources.splice(currentPlayer.resources.indexOf('brick'), 1);
          currentPlayer.resources.splice(currentPlayer.resources.indexOf('sheep'), 1);
          currentPlayer.resources.splice(currentPlayer.resources.indexOf('wheat'), 1);
        }

        // Store the owner of the settlement
        this.settlements[index] = this.currentPlayerIndex;

        // Add a CSS class to the settlement position
        const settlementElement = document.getElementById('s' + index);
        if (settlementElement) {
          settlementElement.classList.add(`has-settlement-${this.currentPlayerIndex}`);
        }

        this.playerPoints[this.currentPlayerIndex] += 1;


        // Set the flag to indicate that the player has built on their first or second turn
        if (isFirstTurn) {
          currentPlayer.hasBuiltFirstTurn = true;
        } else if (isSecondTurn) {
          currentPlayer.hasBuiltSecondTurn = true;
        }
      } else {
        this.displayError("You have already built on your turn.");
        return;
      }
    },

    getAdjacentSettlements(index) {
      const connectedSettlements = [];
      const roadElements = document.querySelectorAll(`.road[class*="r${index}"]`);

      roadElements.forEach(roadElement => {
        // Extract the settlement IDs from the class attribute
        const settlements = roadElement.className.match(/r\d+/g);
        // Remove the current settlement ID from the list
        const connected = settlements.filter(settlement => settlement !== `r${index}`);
        // Remove the 'r' prefix from each settlement ID
        const connectedWithoutPrefix = connected.map(settlement => settlement.substring(1));
        // Check if the connected settlements have a settlement built on them
        connectedWithoutPrefix.forEach(settlementId => {
          for (let i = 0; i < 4; i++) { // Assuming there are 4 players
            const settlementElement = document.querySelector(`#s${settlementId}.has-settlement-${i}`);
            if (settlementElement) {
              connectedSettlements.push({id: settlementId, player: i});
              break; // Exit loop once settlement is found for the player
            }
          }
        });
      });

      return connectedSettlements;
    },

    getRoadsBetweenSettlements(index) {
      const roadElements = document.querySelectorAll(`.road[class*="r${index}"]`);
      let count = 0;
      roadElements.forEach(roadElement => {
        // Extract the settlement IDs from the class attribute
        const settlements = roadElement.className.match(/r\d+/g);
        // Count the number of roads that are connected to the settlement
        count += settlements.length - 1;
      });
      return count;
    },


    buildRoad(fromIndex, toIndex) {
      // Check if the player can play the Road Building card and has roads left to build with the card
      if (this.canPlayRoad_BuildingCard && this.roadsLeftToBuild > 0) {
        // Check if the road is adjacent to a settlement or an owned road
        if (this.isAdjacentToSettlement(fromIndex, toIndex) || this.isAdjacentToOwnRoad(fromIndex, toIndex)) {
          // Build the road for free
          this.buildRoadForFree(fromIndex, toIndex);
          // Decrement the counter for roads left to build with the card
          this.roadsLeftToBuild--;

          // Check if the player has placed all roads allowed by the card
          if (this.roadsLeftToBuild < 1) {
            // Disable the ability to play the Road Building card after using all available roads
            this.canPlayRoad_BuildingCard = false;
          }

          // Set the flag to indicate that the player has built on their first or second turn
          const isFirstTurn = this.turn === 1;
          const isSecondTurn = this.turn === 2;

          if (isFirstTurn) {
            this.players[this.currentPlayerIndex].hasBuiltFirstTurn2 = true;
          } else if (isSecondTurn) {
            this.players[this.currentPlayerIndex].hasBuiltSecondTurn2 = true;
          }
        } else {
          // Display a warning message if the road is not adjacent to the player's settlement or road
          this.displayError("Road must be adjacent to your own settlement or road.");
        }
      } else {
        // Perform the regular road building logic if the player didn't use the Road Building card
        const isFirstTurn = this.turn === 1;
        const isSecondTurn = this.turn === 2;


        if (isFirstTurn) {
          // Check if the player has already built on their first turn
          const hasBuiltFirstTurn2 = this.players[this.currentPlayerIndex].hasBuiltFirstTurn2 || false;

          if (hasBuiltFirstTurn2) {
            this.displayError("You have already built on your first turn.");
            return;
          }
        } else if (isSecondTurn) {
          // Check if the player has already built on their second turn
          const hasBuiltSecondTurn2 = this.players[this.currentPlayerIndex].hasBuiltSecondTurn2 || false;

          if (hasBuiltSecondTurn2) {
            this.displayError("You have already built on your second turn.");
            return;
          }
        } else {
          // Check if the player has enough resources (brick and wood) to build a road
          const currentPlayer = this.players[this.currentPlayerIndex];
          const hasBrick = currentPlayer.resources.includes('brick');
          const hasWood = currentPlayer.resources.includes('wood');

          if (!hasBrick || !hasWood) {
            this.displayError("You don't have enough resources to build a road.");
            return;
          }

        }

        // Build the road only if it's adjacent to the player's settlement or road
        if (this.isAdjacentToSettlement(fromIndex, toIndex) || this.isAdjacentToOwnRoad(fromIndex, toIndex)) {
          // Add the road to the list of roads
          this.roads.push({ from: fromIndex, to: toIndex, owner: this.currentPlayerIndex });

          // Add a CSS class to the road position
          const roadElement = document.querySelector(`.road.r${fromIndex}.r${toIndex}`);
          if (roadElement) {
            roadElement.classList.add(`build-${this.currentPlayerIndex}`);
            roadElement.dataset.from = fromIndex;
            roadElement.dataset.to = toIndex;
            console.log(`Created road with data-from=${roadElement.dataset.from} and data-to=${roadElement.dataset.to}`);
          }

          this.checkConnectedRoads();




          // Set the flag to indicate that the player has built on their first or second turn
          const isFirstTurn = this.turn === 1;
          const isSecondTurn = this.turn === 2;

          if (isFirstTurn) {
            this.players[this.currentPlayerIndex].hasBuiltFirstTurn2 = true;
          } else if (isSecondTurn) {
            this.players[this.currentPlayerIndex].hasBuiltSecondTurn2 = true;
          }else {
            const currentPlayer = this.players[this.currentPlayerIndex];
            // Deduct the required resources from the player's inventory
            currentPlayer.resources.splice(currentPlayer.resources.indexOf('brick'), 1);
            currentPlayer.resources.splice(currentPlayer.resources.indexOf('wood'), 1);
          }

        } else {
          // Display a warning message if the road is not adjacent to the player's settlement or road
          this.displayError("Road must be adjacent to your own settlement or road.");
        }
      }
    },

    buildRoadForFree(fromIndex, toIndex) {
      // Add the road to the list of roads
      this.roads.push({from: fromIndex, to: toIndex, owner: this.currentPlayerIndex});
      // Add a CSS class to the road position
      const roadElement = document.querySelector(`.road.r${fromIndex}.r${toIndex}`);
      if (roadElement) {
        roadElement.classList.add(`build-${this.currentPlayerIndex}`);
      }
    },

    isAdjacentToOwnRoad(fromIndex, toIndex) {
      // Check if there is a road built adjacent to either of the specified indices by the current player
      const roadsAdjacentToFrom = document.querySelectorAll(`.road.r${fromIndex}`);
      const roadsAdjacentToTo = document.querySelectorAll(`.road.r${toIndex}`);

      // Check if any of the adjacent roads belong to the current player
      const adjacentRoads = Array.from(roadsAdjacentToFrom).concat(Array.from(roadsAdjacentToTo));


      for (const road of adjacentRoads) {
        const roadClasses = road.classList;
        for (const roadClass of roadClasses) {
          if (roadClass.startsWith('build-') && roadClass.endsWith(this.currentPlayerIndex.toString())) {

            return true;
          }
        }
      }
      return false;
    },

    isAdjacentToSettlement(fromIndex, toIndex) {
      // Check if there is a settlement built on either of the specified indices
      const settlement1 = document.getElementById(`s${fromIndex}`);
      const settlement2 = document.getElementById(`s${toIndex}`);

      // Check if either settlement has the class indicating it has a settlement built on it for the current player index
      const currentPlayerIndex = this.currentPlayerIndex;
      const hasSettlement1 = settlement1 ? settlement1.classList.contains(`has-settlement-${currentPlayerIndex}`) : false;
      const hasSettlement2 = settlement2 ? settlement2.classList.contains(`has-settlement-${currentPlayerIndex}`) : false;

      // Return true if either settlement has a settlement built on it
      return hasSettlement1 || hasSettlement2;
    },


    // Method to acquire a development card
    acquireDevelopmentCard() {
      const currentPlayer = this.players[this.currentPlayerIndex];

      // Check if the player has resources to acquire a development card (wheat, ore, sheep)
      if (currentPlayer.resources.includes('wheat') && currentPlayer.resources.includes('ore') && currentPlayer.resources.includes('sheep')) {
        // Deduct resources from the player's inventory
        currentPlayer.resources.splice(currentPlayer.resources.indexOf('wheat'), 1);
        currentPlayer.resources.splice(currentPlayer.resources.indexOf('ore'), 1);
        currentPlayer.resources.splice(currentPlayer.resources.indexOf('sheep'), 1);

        // Add a random development card (e.g., "knight" or "year_of_plenty") to the player's development card list
        const randomCard = this.developmentCards[Math.floor(Math.random() * this.developmentCards.length)];

        // Handle different types of development cards
        switch (randomCard) {
          case 'year_of_plenty':
            this.canPlayyear_of_plentyCard = true;
            break;
          case 'knight':
            this.canPlayKnightCard = true;
            break;
          case 'Road_Building':
            this.canPlayRoad_BuildingCard = true;
            break;
          case 'monopoly':
            this.canPlayMonopolyCard = true;
            break;
          case 'victory_point':
            this.canPlayVictory_PointCard = true;
            break;
        }

        // Push the acquired card to the player's development cards
        currentPlayer.developmentCards.push(randomCard);
      } else {
        this.displayError("You don't have enough resources to acquire a development card.");
      }
    },


    playDevelopmentCard(cardType) {
      const currentPlayer = this.players[this.currentPlayerIndex];

      // Check if the player has the specified development card type
      if (currentPlayer.developmentCards.includes(cardType)) {
        // Remove the card from the player's development card list
        const cardIndex = currentPlayer.developmentCards.indexOf(cardType);
        currentPlayer.developmentCards.splice(cardIndex, 1);

        // Perform the action based on the development card type
        switch (cardType) {
          case 'knight':
            this.playKnightCardLogic();
            break;
          case 'year_of_plenty':
            this.playYearOfPlentyCardLogic();
            break;
          case 'Road_Building':
            this.playRoadBuildingCardLogic();
            break;
          case 'monopoly':
            this.playMonopolyCardLogic();
            break;
          case 'victory_point':
            this.playVictoryPointCardLogic();
            break;
          default:
            this.displayError("Unknown development card type.");
            break;
        }
      } else {
        this.displayError("You don't have the specified development card to play.");
      }
    },


    playKnightCardLogic() {
      // Perform the action for the "Knight" card
      this.hasPlayedDevelopmentCard = true;
      this.canPlayKnightCard = false; // Disable the "Play Knight Card" button
      this.placeRobberMode = true; // Set flag to activate robber placement
    },

    playYearOfPlentyCardLogic() {
      // Perform the action for the "Year of Plenty" card
      this.canPlayyear_of_plentyCard = true; // Enable the "Play Year of Plenty Card" button
      // Set available resources to all resource types
      this.availableResources = ['brick', 'wheat', 'ore', 'wood', 'sheep'];
    },

    incrementResource(resource) {
      if (this.selectedResources.length < 2) {
        this.selectedResources.push(resource);
      }
    },
    decrementResource(resource) {
      // Remove the resource from the selected resources
      const index = this.selectedResources.indexOf(resource);
      if (index !== -1) {
        this.selectedResources.splice(index, 1);
      }
    },

    getSelectedCount(resource) {
      return this.selectedResources.filter(res => res === resource).length;
    },

    // Method to confirm the selected resources for the Year of Plenty card
    confirmSelectedResources() {
      // Perform the action for the Year of Plenty card using the selected resources
      this.addSelectedResourcesToInventory();

      // Disable the "Play Year of Plenty Card" button
      this.canPlayyear_of_plentyCard = false;

      // Clear the selected resources array
      this.selectedResources = [];
    },

    // Method to add the selected resources to the player's inventory
    addSelectedResourcesToInventory() {
      const currentPlayer = this.players[this.currentPlayerIndex];
      this.selectedResources.forEach(resource => {
        currentPlayer.resources.push(resource);
      });
    },


    playRoadBuildingCardLogic() {
      // Perform the action for the "Road Building" card
      this.canPlayRoad_BuildingCard = true;
      this.roadsLeftToBuild = 2; // Initialize the counter for roads left to build
    },

    // Method to handle playing the Monopoly card
    playMonopolyCardLogic() {
      // Enable the "Play Monopoly Card" button
      this.canPlayMonopolyCard = true;
      // Set available resources to all resource types
      this.availableResources = ['brick', 'wheat', 'ore', 'wood', 'sheep'];
    },

    // Method to handle resource selection for the Monopoly card
    selectResource(resource) {
      // Clear the selected resources array if it contains more than one resource
      if (this.selectedResources.length >= 1) {
        this.selectedResources = [];
      }
      // Add the selected resource
      this.selectedResources.push(resource);
    },

// Method to confirm the selected resource for the Monopoly card
    confirmSelectedResource() {
      // Check if a resource is selected
      if (this.selectedResources.length === 1) {
        // Perform the action for the Monopoly card using the selected resource
        this.collectMonopolyResources();
        // Disable the "Play Monopoly Card" button
        this.canPlayMonopolyCard = false;
        // Clear the selected resources array
        this.selectedResources = [];
      } else {
        // Inform the player to select exactly one resource
        this.displayError("Please select exactly one resource.");
      }
    },

// Method to collect resources of the selected type from all players
    collectMonopolyResources() {
      const selectedResource = this.selectedResources[0];
      const currentPlayer = this.players[this.currentPlayerIndex];
      // Loop through all players except the current player
      this.players.forEach(player => {
        // Skip the current player
        if (player !== currentPlayer) {
          // Find the count of the selected resource in the player's resources
          const count = player.resources.filter(resource => resource === selectedResource).length;
          // Remove the selected resource from the other player's inventory
          player.resources = player.resources.filter(resource => resource !== selectedResource);
          // Add the count of the selected resource to the current player's inventory
          for (let i = 0; i < count; i++) {
            currentPlayer.resources.push(selectedResource);
          }
        }
      });
    },

    playVictoryPointCardLogic() {
      // Perform the action for the "Victory Point" card
      this.canPlayVictoryPointCard = true; // Enable the "Play Victory Point Card" button
    },


    placeRobber(hexIndex) {
      if (this.placeRobberMode) {
        const adjustedHexIndex = hexIndex + 1;
        if (!isNaN(adjustedHexIndex)) {
          if (adjustedHexIndex !== this.robberHexIndex) {
            if (this.robberHexIndex !== null) {
              this.deactivateRobber(this.robberHexIndex);
            }
            this.activateRobber(adjustedHexIndex);

            const hexId = `h${adjustedHexIndex}`;
            const numberElement = document.getElementById(hexId)?.querySelector('.number');
            if (numberElement) {
              numberElement.classList.add('red-number');
            }

            // Call the method to steal a resource from adjacent settlements
            this.stealResourceFromAdjacentSettlement();
          } else {
            console.error(`Same hexIndex as current robber position: ${adjustedHexIndex}`);
          }
        } else {
          console.error(`Invalid hexIndex: ${adjustedHexIndex}`);
        }

        this.placeRobberMode = false;
      } else {
        this.displayError("You can only place the robber after playing the knight card.");
      }
      this.showRobberPlacementModal = false;
    },

    stealResourceFromAdjacentSettlement() {
      // Check if there is a robber placed on any tile
      if (this.robberHexIndex !== null) {
        // Get the adjacent settlement indexes for the robber's tile
        const adjacentIndexes = this.adjacents[this.robberHexIndex - 1];

        // Check if there are adjacent settlement indexes
        if (Array.isArray(adjacentIndexes) && adjacentIndexes.length > 0) {
          // Find all player settlements adjacent to the robber's tile
          const playerSettlements = this.findPlayerSettlements(adjacentIndexes);

          // Check if there are valid player settlements adjacent to the robber hex
          if (playerSettlements.length > 0) {
            // Randomly choose a player settlement to steal from
            const randomPlayerId = playerSettlements[Math.floor(Math.random() * playerSettlements.length)];

            // Get the corresponding player object
            const player = this.players[randomPlayerId];

            // Check if the player has resources to steal
            if (Array.isArray(player.resources) && player.resources.length > 0) {
              // Randomly choose a resource to steal
              const randomResourceIndex = Math.floor(Math.random() * player.resources.length);


              // Remove the stolen resource from the player's inventory
              const stolenResource = player.resources.splice(randomResourceIndex, 1)[0];

              // Add the stolen resource to the player who placed the robber
              const robberPlayer = this.players.find(player => player.id === this.currentPlayerId);
              if (robberPlayer) {
                robberPlayer.resources.push(stolenResource);

                // Log the theft
                console.log(`Stole ${stolenResource} from ${player.name}'s settlement.`);
              } else {
                console.log(`Error: Current player not found.`);
              }
            } else {
              console.log(`Player ${player.name} (ID: ${randomPlayerId}) has no resources to steal.`);
            }
          } else {
            console.log('No valid player settlements adjacent to the robber.');
          }
        } else {
          console.log('No adjacent settlement indexes found for the robber hex.');
        }
      } else {
        console.log('Robber has not been placed on any tile.');
      }
    },

    findPlayerSettlements(adjacentIndexes) {
      const playerSettlements = [];

      // Iterate over each adjacent settlement index
      for (const index of adjacentIndexes) {
        const settlementId = `s${index}`;

        // Get the settlement element by ID
        const settlementElement = document.getElementById(settlementId);

        // Check if there's a settlement at the given index and if it belongs to any player
        if (settlementElement) {
          // Iterate over each player to check ownership of the settlement
          for (let i = 0; i < this.players.length; i++) {
            const playerClass = `has-settlement-${i}`;

            // Check if the settlement belongs to the current player
            if (settlementElement.classList.contains(playerClass)) {
              // Extract player ID from the settlement class
              const playerId = i;

              // Ensure playerId is a valid number and corresponds to a player in the players array
              if (!isNaN(playerId) && playerId >= 0 && playerId < this.players.length) {
                playerSettlements.push(playerId);
              }
            }
          }
        }
      }

      return playerSettlements;
    },


    activateRobber(hexIndex) {
      // Set the robber's hex index
      this.robberHexIndex = hexIndex;
    },

    deactivateRobber(hexIndex) {
      // Reset the UI of the previous robber's position
      const previousHexId = `h${hexIndex}`;
      const previousNumberElement = document.getElementById(previousHexId)?.querySelector('.number');
      if (previousNumberElement) {
        previousNumberElement.classList.remove('red-number');
      }
    },


    initializePlayers() {
      // Dummy player data
      const player1 = new Player("red", "NaN", [], []);
      const player2 = new Player("blue", "NaN", [], []);
      const player3 = new Player("green", "NaN", [], []);
      const player4 = new Player("orange", "NaN", [], []);

      // Assign initial resources to the red player
      player1.resources = [];
      player2.resources = [];
      player3.resources = [];
      player4.resources = [];

      // Add players to the players array
      this.players.push(player1);
      this.players.push(player2);
      this.players.push(player3);
      this.players.push(player4);

      // Start the countdown timer after initializing players
      this.startCountdown();
    },

    startCountdown() {
      // Clear any existing timer
      clearInterval(this.timerId);

      // Reset the timer
      this.timeRemaining = this.game.turnDuration; // Set the time remaining to 120 seconds

      // Start a new timer
      this.timerId = setInterval(() => {
        if (this.timeRemaining > 0) {
          this.timeRemaining--;
        } else {
          this.timeUp();
        }
      }, 1000);
    },

    timeUp() {
      clearInterval(this.timerId);
      this.rollDice();
      this.nextTurn();
      this.startCountdown(); // Start the countdown for the next player
    },

    nextTurn() {
      // Check if current player has rolled the dice
      if (!this.hasRolledDice) {
        this.displayError("You have not rolled the dice yet.");
        return;
      }

      if (this.turn === 1) {
        // Increment the current player index
        if (this.currentPlayerIndex === this.players.length - 1) {
          // If it's the last player's turn, reset to the first player and increment the turn counter
          this.currentPlayerIndex = 3;

          // Increment the turn counter
          this.turn++;
        } else {
          // Otherwise, move to the next player
          this.currentPlayerIndex++;
        }
      } else {
        // Decrement the current player index
        if (this.currentPlayerIndex === 0) {
          // If it's the first player's turn, move to the last player and increment the turn counter
          this.currentPlayerIndex = this.players.length - 1;

          // Increment the turn counter
          this.turn++;
        } else {
          // Otherwise, move to the previous player
          this.currentPlayerIndex--;
        }
      }

      // Reset next button
      this.hasRolledDice = false;

      // Reset roadsLeftToBuild to 0
      this.roadsLeftToBuild = 0;

      this.startCountdown();
    },
    rollDice() {
      // Generate random number between 1 and 6
      function roll() {
        return 1 + Math.floor(6 * Math.random());
      }

      // Assign dice outcome
      let rightDiceOutcome = roll();
      let leftDiceOutcome = roll();
      this.dicesOutcome = rightDiceOutcome + leftDiceOutcome;

      // Update dice outcome images
      this.leftDiceImg = this.clearWhiteDiceImg[leftDiceOutcome];
      this.rightDiceImg = this.clearWhiteDiceImg[rightDiceOutcome];

      // Log the hex ID with the rolled number
      let rolledNumber = rightDiceOutcome + leftDiceOutcome;
      let hexIdList = [];
      let hexes = document.querySelectorAll('.hex');
      hexes.forEach(hex => {
        let numberElement = hex.querySelector('.number');
        if (numberElement) {
          let number = parseInt(numberElement.textContent);
          if (number === rolledNumber) {
            hexIdList.push(hex.id);
          }
        }
      });
      console.log(`Rolled: ${rolledNumber}, Hex IDs: ${hexIdList.join(', ')}`);

      // Assign resources to players based on the rolled number
      this.assignResourcesToPlayers(rolledNumber);


      // User can end their turn after rolling the dice
      this.hasRolledDice = true;
    },


    assignResourcesToPlayers(rolledNumber) {
      // Iterate over each hex on the board
      let hexes = document.querySelectorAll('.hex');
      hexes.forEach(hex => {
        // Get the number element within the hex
        let numberElement = hex.querySelector('.number');
        if (numberElement) {
          // Get the number value from the number element
          let number = parseInt(numberElement.textContent);
          // Check if the number matches the rolled number
          if (number === rolledNumber) {
            // Find the index of the hex (from 1 to 19)
            let hexIndex = parseInt(hex.id.substr(1));

            // Check if the hex has the robber
            if (hexIndex === this.robberHexIndex) {
              console.log(`Robber is on hex ${hex.id}. No resources assigned.`);
              return; // Skip assigning resources if there's a robber on this hex
            }

            // Get the adjacent settlement indexes for the given hex index
            let adjacentIndexes = this.adjacents[hexIndex - 1]; // Adjust for zero-based index

            // Ensure adjacentIndexes is an array and is not empty
            if (Array.isArray(adjacentIndexes) && adjacentIndexes.length > 0) {
              // Iterate over each adjacent index and check if there's a settlement
              for (const adjacentIndex of adjacentIndexes) {
                // Get the settlement ID for this adjacent index
                let settlementId = `s${adjacentIndex}`; // Keep it as one-based index

                // Get the settlement element by ID
                let settlementElement = document.getElementById(settlementId);

                // Check if there's a settlement and if it belongs to any player
                if (settlementElement) {
                  // Iterate over each player
                  for (let i = 0; i < this.players.length; i++) {
                    // Check if the settlement belongs to the current player
                    let playerClass = `has-settlement-${i}`;
                    if (settlementElement.classList.contains(playerClass)) {
                      // Check if the settlement is a city
                      const city = `city-${i}`;
                      if (settlementElement.classList.contains(city)) {
                        // Assign twice as many resources if it's a city
                        let resourceType = hex.classList[1]; // Assuming the resource type is the second class
                        if (resourceType) {
                          // Update the player's inventory with the assigned resource
                          this.giveResourcesToPlayer(i, [resourceType, resourceType]);
                          // Log the assignment of resources
                          console.log(`Player ${i} received 2 ${resourceType} from hex ${hex.id} with number ${number}`);
                        }
                      } else {
                        // Assign normal amount of resources if it's not a city
                        let resourceType = hex.classList[1]; // Assuming the resource type is the second class
                        if (resourceType) {
                          // Update the player's inventory with the assigned resource
                          this.giveResourcesToPlayer(i, [resourceType]);
                          // Log the assignment of resources
                          console.log(`Player ${i} received ${resourceType} from hex ${hex.id} with number ${number}`);
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      });
    },


    giveResourcesToPlayer(playerIndex, resources) {
      // Find the player by their index
      const player = this.players[playerIndex];

      // Check if the player is found
      if (player) {
        // Add the resources to the player's inventory
        player.resources.push(...resources);
      } else {
        console.error(`Player at index ${playerIndex} not found.`);
      }
    },

    displayError(message) {
      // Display error message
      document.getElementById('game-error-message').classList.remove('hidden');
      let errorMessage = document.querySelector('.error-message');
      errorMessage.textContent = message;

      // Close error message
      document.getElementById('confirm-error-message').addEventListener('click', function () {
        document.getElementById('game-error-message').classList.add('hidden');
      });
    }
  }
};
</script>


<style scoped>



.resource-card {
  cursor: pointer;
}

.resource-cards.selected {
  border: 2px solid blue; /* Add your preferred styling for selected cards */
}

.modal {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  pointer-events: none;
  z-index: 2;
}

.modal-content {
  background-color: #ebfcf7;
  border: solid 5px #60BFB2;
  padding: 16px;
  border-radius: 15px;
  width: auto;
  pointer-events: auto; /* Enable clicks inside modal content */

}

.inventory-resource-card {
  display: inline-flex;
  margin-right: 10px;
  cursor: pointer;
  position: relative;
  justify-content: center;
  align-items: center;
}
.resource-cards{
  display: inline-flex;
  margin: 5px;
}
.resource-cardmodal{
  margin: 5px;
}
.resource-selection{
  display: inline-flex;
  margin: 5px;
}

/* Styles for the resource count */
.resource-count {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: #000;
  color: #fff;
  border-radius: 50%;
  padding: 4px;
  font-size: 12px;
}

.game-buttons-container {
  position: relative; /* Ensure modal is positioned relative to this container */
}

.robber-placed {
  background-color: #c0392b;
  border: 2px solid #fff;
}

.red-number {
  color: #c0392b;
}

.robber-number {
  color: #c0392b;
}

/* General */
.container {
  justify-content: center;
}

.mainContent {
  display: inline-flex;
  width: 90vw;
  margin-left: 5vw;
  margin-right: 5vw;
}

/* Previous page button */
.previous-page-container {
  margin-left: 30px;
  display: flex;
}

.previous-page-button {
  border: none;
  background: none;
}

.previous-page-container img {
  width: 40px;
}

/* Error message */
.hidden {
  display: none !important;
}

#game-error-message {
  z-index: 3;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.error-message-box {
  background-color: #ebfcf7;
  border: solid 5px #60BFB2;
  padding: 16px;
  border-radius: 15px;
  width: 30vw;
  display: grid;
}

.error-message {
  margin: 20px 20px 50px 20px;
  font-weight: normal;
}

#confirm-error-message {
  justify-self: right;
}

/* ----------------------------------------- */

.current-player {
  margin-bottom: 10px;
  text-align: center;
}

.has-settlement-0 {
  background-image: url("~@/assets/images/Settlement.png");
  background-repeat: no-repeat;
  background-size: contain; /* or cover depending on your needs */
  transform: rotate(90deg); /* Rotate 90 degrees clockwise */

}

.has-settlement-1 {
  background-repeat: no-repeat;
  background-size: contain; /* or cover depending on your needs */
  transform: rotate(90deg); /* Rotate 90 degrees clockwise */
  background-image: url("~@/assets/images/Settlement(3).png");
}

.has-settlement-2 {
  background-repeat: no-repeat;
  background-size: contain; /* or cover depending on your needs */
  transform: rotate(90deg); /* Rotate 90 degrees clockwise */
  background-image: url("~@/assets/images/Settlement (1).png");
}

.has-settlement-3 {
  background-repeat: no-repeat;
  background-size: contain; /* or cover depending on your needs */
  transform: rotate(90deg); /* Rotate 90 degrees clockwise */
  background-image: url("~@/assets/images/Settlement (2).png");
}

.city-0 {
  background-image: url("~@/assets/images/City.png");
  background-repeat: no-repeat;
  background-size: contain; /* or cover depending on your needs */
  transform: rotate(90deg); /* Rotate 90 degrees clockwise */

}

.city-1 {
  background-repeat: no-repeat;
  background-size: contain; /* or cover depending on your needs */
  transform: rotate(90deg); /* Rotate 90 degrees clockwise */
  background-image: url("~@/assets/images/City (1).png");
}

.city-2 {
  background-repeat: no-repeat;
  background-size: contain; /* or cover depending on your needs */
  transform: rotate(90deg); /* Rotate 90 degrees clockwise */
  background-image: url("~@/assets/images/City (2).png");
}

.city-3 {
  background-repeat: no-repeat;
  background-size: contain; /* or cover depending on your needs */
  transform: rotate(90deg); /* Rotate 90 degrees clockwise */
  background-image: url("~@/assets/images/City (3).png");
}



.box {
  margin: 0 auto;
  /*margin-left: 31%;*/
  width: 1100px;
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

  height: 40px;
  width: 40px;
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
  top: 45px;
  left: 95px;
}

.hex .settlement.tl {
  top: -25px;
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
  top: 45px;
  left: 95px;
}

.hex .city.tl {
  top: -25px;
  left: 65px;
}

/* end cities */
/* ----------------------------------------- */

/* Game buttons */
.game-buttons-container {

}

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

/* Player resources */
.player-inventory-container {
  background-color: #5c86ae;
  border: solid #2c3e50 ;
  padding: 10px;
  min-height: 160px;
}

.player-inventory-container img {
  height: 150px;
  margin: 0 5px;
}

.player-inventory-resources {
  display: inline-flex;
}

/* Player cards */
.player-cards-container {
  display: flex;
  flex-direction: column;
}

.player-card {
  background-color: #5c86ae;
  border: solid #2c3e50;
  border-radius: 10px;
  padding: 10px;
  margin: 5px;
  color: white;
}

</style>


