<template>
  <div class="container">
    <div class="centered-content">
      <div class="title">Play Catan Online</div>
      <div class="sub-title">Play for free</div>
      <p class="extra-info">Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed do eiusmod tempor incididunt
        ut labore et dolore magna.</p>
      <button>Play Singleplayer</button>
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
  position: relative;
  z-index: 2;
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
}

.extra-info {
  font-family: 'Baloo 2', cursive;
}

/* Background decoration */
.blob-top-left {
  position: fixed;
  top: -10vw;
  left: -10vw;
  width: 35vw;
  height: 35vw;
  background-image: url("data:image/svg+xml;utf8,%3Csvg viewBox=%220 0 1000 1000%22 xmlns=%22http:%2F%2Fwww.w3.org%2F2000%2Fsvg%22%3E%3Cdefs%3E%3CclipPath id=%22a%22%3E%3Cpath fill=%22currentColor%22 d=%22M786.5 599.5q-87.5 99.5-187 134T342 792q-158 24-179-134t4-312q25-154 179-226.5t304.5 3.5q150.5 76 187 226.5t-51 250Z%22%2F%3E%3C%2FclipPath%3E%3C%2Fdefs%3E%3Cg clip-path=%22url(%23a)%22%3E%3Cpath fill=%22%23FFB54A%22 d=%22M786.5 599.5q-87.5 99.5-187 134T342 792q-158 24-179-134t4-312q25-154 179-226.5t304.5 3.5q150.5 76 187 226.5t-51 250Z%22%2F%3E%3C%2Fg%3E%3C%2Fsvg%3E");
  background-repeat: no-repeat;
  background-position: center;
  background-size: contain;
}

.blob-bottom-left {
  position: fixed;
  top: 30vw;
  left: -10vw;
  width: 35vw;
  height: 35vw;
  background-image: url("data:image/svg+xml;utf8,%3Csvg viewBox=%220 0 1000 1000%22 xmlns=%22http:%2F%2Fwww.w3.org%2F2000%2Fsvg%22%3E%3Cdefs%3E%3CclipPath id=%22a%22%3E%3Cpath fill=%22currentColor%22 d=%22M831 637.5Q827 775 689 782t-214-49.5Q399 676 244 663T61.5 490.5Q34 331 168 244t253-29q119 58 288.5 12.5T857 341q-22 159-26 296.5Z%22%2F%3E%3C%2FclipPath%3E%3C%2Fdefs%3E%3Cg clip-path=%22url(%23a)%22%3E%3Cpath fill=%22%23FF7B4D%22 d=%22M831 637.5Q827 775 689 782t-214-49.5Q399 676 244 663T61.5 490.5Q34 331 168 244t253-29q119 58 288.5 12.5T857 341q-22 159-26 296.5Z%22%2F%3E%3C%2Fg%3E%3C%2Fsvg%3E");
  background-repeat: no-repeat;
  background-position: center;
  background-size: contain;
  overflow: hidden;

}

.blob-bottom-Right {
  position: fixed;
  top: 30vw;
  left: 80vw;
  width: 35vw;
  height: 35vw;
  background-image: url("data:image/svg+xml;utf8,%3Csvg viewBox=%220 0 1000 1000%22 xmlns=%22http:%2F%2Fwww.w3.org%2F2000%2Fsvg%22%3E%3Cdefs%3E%3CclipPath id=%22a%22%3E%3Cpath fill=%22currentColor%22 d=%22M810.5 646Q848 792 712 860.5T464 843q-112-86-203.5-154t-130-202.5q-38.5-134.5 45-269t246-163Q584 26 726 108.5t94.5 237Q773 500 810.5 646Z%22%2F%3E%3C%2FclipPath%3E%3C%2Fdefs%3E%3Cg clip-path=%22url(%23a)%22%3E%3Cpath fill=%22%2360bfb2%22 d=%22M810.5 646Q848 792 712 860.5T464 843q-112-86-203.5-154t-130-202.5q-38.5-134.5 45-269t246-163Q584 26 726 108.5t94.5 237Q773 500 810.5 646Z%22%2F%3E%3C%2Fg%3E%3C%2Fsvg%3E");
  background-repeat: no-repeat;
  background-position: center;
  background-size: contain;
}

</style>