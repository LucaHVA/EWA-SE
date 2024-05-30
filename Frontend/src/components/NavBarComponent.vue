<template>
  <nav class="navigation">
    <div class="navbar">
      <router-link to="/home" class="logo-link">
        <img alt="logo" class="navbar_logo" src="../assets/images/codeboard_black_logo.png">
      </router-link>
      <ul class="navbar-list">
        <li class="nav-item">
          <router-link to="/home" class="nav-link" active-class="active">Home</router-link>
        </li>
        <li class="nav-item" v-if="isLoggedIn">
          <router-link to="/leaderboard" class="nav-link" active-class="active">Leaderboard</router-link>
        </li>
<!--        <li class="nav-item" v-if="isLoggedIn">-->
<!--          <router-link to="/friends" class="nav-link" active-class="active">Friends List</router-link>-->
<!--        </li>-->
      </ul>
      <ul class="user-logging">
        <li class="nav-item" v-if="isLoggedIn">
          <router-link to="/profilePage" class="nav-link" active-class="active">Profile</router-link>
        </li>
        <li class="nav-item" v-else>
          <router-link to="/login" class="nav-link" active-class="active">Log in</router-link>
        </li>
        <li class="nav-item" v-if="isLoggedIn">
          <a href="#" @click="logout" class="nav-link">Logout</a>
        </li>
        <li class="nav-item" v-else>
          <router-link to="/register" class="nav-link" active-class="active">Register</router-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: "NavbarComponent",

  inject: ["usersService"],
  computed: {
    isLoggedIn() {
      // Check if user information exists in local storage
      return sessionStorage.getItem('undefined_ACC') !== null;
    }
  },
  methods: {
    async logout() {
      // Clear user info from local storage
      sessionStorage.removeItem('undefined_ACC');
      await this.usersService.signOut()
      this.$router.push('/home');
      window.location.reload();
    }
  }
}
</script>

<style scoped>
/* Logo */
.navbar_logo {
  width: 100%;
  margin: 0;
}

.logo-link {
  width: 15%; /* Set the width of the router-link */
  display: inline-block;
  margin: 20px;
}
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
  padding: 0;
  margin: 3.5rem;
}

/* Navigation */
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.navbar-list {
  display: flex;
  justify-content: center;
  flex-grow: 1;
}

.nav-link {
  font-weight: bold;
  transition: all 0.4s ease;
  text-align: center;
  text-decoration: none;
  cursor: pointer;
  color: black;
}

.nav-link:hover {
  color: var(--coral);
}

.nav-item {
  transition: all 0.5s ease;
  margin: 0 10px;
}

.nav-item:hover {
  transition: all 0.5s ease;
  top: 10%;
  transform: translateY(-25%);
  padding: 5px 10px;
  border-bottom: 1px solid var(--coral);
}

.active {
  font-weight: bold;
  color: var(--coral);
  border-bottom: 1px solid var(--coral);
}


/* User logging */
.user-logging {
  display: flex;
  justify-content: flex-end;
  margin-right: 5rem;
}
</style>