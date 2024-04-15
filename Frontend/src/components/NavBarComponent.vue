<template>
  <nav class="navigation">
    <div class="navbar">
      <ul class="navbar-list">
        <li>
          <ul>
            <li class="nav-item">
              <router-link to="/home" class="nav-link" active-class="active">Home</router-link>
            </li>
            <!--            <li class="nav-item">-->
            <!--              <router-link to="/leaderboard" class="nav-link" active-class="active">Leaderboard</router-link>-->
            <!--            </li>-->
            <!-- TODO: tutorial page -->
            <!-- <li class="nav-item">-->
            <!--    <router-link to="/tutorial" class="nav-link" active-class="active">Tutorial</router-link>-->
            <!-- </li>-->
          </ul>
        </li>

        <li>
          <!-- TODO: login and sign-up (not logged in) AND logout and profile (logged in)-->
          <ul class="user-logging">
            <li class="nav-item" v-if="isLoggedIn">
              <router-link to="/profilePage" class="nav-link" active-class="active">Profile</router-link>
            </li>
            <li class="nav-item" v-else>
              <router-link to="/login" class="nav-link" active-class="active">Log in</router-link>
            </li>
            <li class="nav-item" v-if="isLoggedIn">
              <router-link to="/home" @click.prevent="logout" class="nav-link" active-class="active">Logout</router-link>
            </li>
            <li class="nav-item" v-else>
              <router-link to="/register" class="nav-link" active-class="active">Register</router-link>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: "NavbarComponent",
  computed: {
    isLoggedIn() {
      // Check if user information exists in local storage
      return localStorage.getItem('userInfo') !== null;
    }
  },
  methods: {
    logout() {
      // Clear user info from local storage
      localStorage.removeItem('userInfo');
      window.location.reload();
    }
  }
}
</script>

<style scoped>
/* General */
nav {
  position: relative;
  z-index: 2;
  font-family: "Raleway", sans-serif;
  font-weight: 600;
  font-size: 20px;

}

nav ul {
  display: flex;
  list-style-type: none;
}

/* Logo */
#left-logo {
  margin: 20px;
}

.navbar_logo {
  width: 15%;
}

/* Navigation */
.navbar-list {
  display: flex;
  justify-content: center;
}

.nav-link {
  text-align: center;
  text-decoration: none;
  cursor: pointer;
  color: black;
}

/*.nav-link:hover::after {*/
/*  content: '';*/
/*  position: absolute;*/
/*  bottom: 0;*/
/*  left: 0;*/
/*  width: 100%;*/
/*  height: 2px;*/
/*  background-color: black; !* Add black line under link on hover *!*/
/*}*/

.nav-item {
  margin: 0 10px;
}

/* User logging */
.user-logging {
  justify-content: flex-end;
}

</style>