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
        <li class="nav-item" v-if="isLoggedIn">
          <router-link to="/friends" class="nav-link" active-class="active">Friends List</router-link>
        </li>
        <li class="nav-item" v-if="isAdmin">
          <router-link to="#" class="nav-link" @click.prevent="openAdminModal">Admin Only</router-link>
        </li>
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

    <transition name="modal">
      <div v-if="isAdminModalOpen" class="modal-mask" @click="closeAdminModal">
        <div class="modal-wrapper">
          <div class="modal-container" @click.stop>
            <div class="input_box">
              <input type="text" v-model="searchQuery" ref="searchInput" @blur="keepFocus"/>
              <span>Search users by name</span>
            </div>
            <h2 class="admin-section-title">Admin Section</h2>
            <div class="modal-scrollable-content-admin">
              <ul class="user-list">
                <li class="user-pill transition" v-for="user in filteredUsers" :key="user.id">
                  <span>{{ user.username }} - {{ user.email }}</span>
                  <button class="remove-button transition" @click="removeUser(user.id)">Remove</button>
                </li>
              </ul>
            </div>
            <div class="close-button-box">
              <button class="close-button transition" @click="closeAdminModal">Close</button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </nav>
</template>

<script>
import {nextTick} from "vue";

export default {
  name: "NavbarComponent",

  inject: ["usersService"],
  data() {
    return {
      isAdminModalOpen: false,
      users: [],
      searchQuery: ''
    };
  },

  computed: {
    isLoggedIn() {
      // Check if user information exists in local storage
      return sessionStorage.getItem('undefined_ACC') !== null;
    },
    isAdmin() {
      const user = JSON.parse(sessionStorage.getItem('undefined_ACC'));
      return user && user.roles && user.roles.includes('ADMIN');
    },
    filteredUsers() {
      return this.users.filter(user => user.username.toLowerCase().includes(this.searchQuery.toLowerCase()));
    }
  },
  methods: {
    async logout() {
      // Clear user info from local storage
      sessionStorage.removeItem('undefined_ACC');
      await this.usersService.signOut();
      this.$router.push('/home');
      window.location.reload();
    },
    async fetchUsers() {
      try {
        this.users = await this.usersService.asyncFindAll();
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    },
    async removeUser(userId) {
      try {
        await this.usersService.deleteUser(userId);
        this.users = this.users.filter(user => user.id !== userId);
      } catch (error) {
        console.error('Error removing user:', error);
      }
    },
    openAdminModal() {
      this.isAdminModalOpen = true;
      this.fetchUsers();
      document.body.style.overflow = 'hidden';
    },
    closeAdminModal() {
      this.isAdminModalOpen = false;
      this.searchQuery = '';
      document.body.style.overflow = '';
    },
    keepFocus() {
      if (this.searchQuery) {
        //ensures that the DOM updates are applied before running the code
        nextTick(() => {
          this.$refs.searchInput && this.$refs.searchInput.focus();
        });
      }
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
  width: 15%;
  display: inline-block;
  margin: 20px;
}

/*Navigation*/
.navigation {
  position: relative;
  z-index: 2;
  font-family: "Raleway", sans-serif;
  font-weight: 600;
  font-size: 20px;
}

.navbar-list {
  display: flex;
  list-style-type: none;
  padding: 0;
  margin: 3.5rem;
  justify-content: center;
  flex-grow: 1;
}

.user-logging {
  display: flex;
  list-style-type: none;
  justify-content: flex-end;
  margin-right: 5rem;
}

/* Navigation */
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
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

/* Modal */
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  transition: opacity 0.5s ease;
}

.modal-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.modal-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: var(--white);
  padding: 20px 20px 0 20px;
  border-radius: 5px;
  width: 80%;
  height: 80%;
  transition: all 0.4s ease;
  overflow: hidden;
}

.admin-section-title {
  margin-bottom: 20px;
}

.modal-scrollable-content-admin {
  max-height: none;
  overflow-y: auto;
  width: 100%;
}

.user-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%
}

.modal-enter-active, .modal-leave-active {
  transition: opacity 0.5s, transform 0.5s;
}

.modal-enter-from, .modal-leave-to {
  opacity: 0;
  transform: scale(1.3);
}

.close-button-box {
  display: flex;
  justify-content: center;
}

.user-pill {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: var(--white);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  margin: 1rem;
  width: 80%;
  height: 50px;
}

.user-pill:hover {
  background-color: var(--black);
  color: var(--white);
}

/*buttons*/
.close-button {
  font-family: 'Raleway', sans-serif;
  background-color: var(--coral);
  border: none;
  color: var(--white);
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  width: max-content;
  font-size: 20px;
  margin: 2rem;
  cursor: pointer;
  border-radius: 8px;
  font-weight: bold;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.1);
}

.close-button:hover {
  background-color: var(--white);
  color: var(--coral);
}

.remove-button {
  font-family: 'Raleway', sans-serif;
  background-color: var(--red);
  border: none;
  color: var(--white);
  padding: 10px 18px;
  text-align: center;
  text-decoration: none;
  width: max-content;
  font-size: 20px;
  margin: 2rem;
  cursor: pointer;
  font-weight: bold;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.1);
}

.remove-button:hover {
  background-color: var(--white);
  color: var(--red);
  border-radius: 8px;
}

/*search bar*/
.input_box {
  display: flex;
  position: relative;
  width: 80%;
  margin: 1rem 0;
}

.input_box input {
  width: 100%;
  padding: 10px;
  border: 2px solid var(--black);
  background: var(--white);
  border-radius: 5px;
  outline: none;
  color: var(--red);
  font-size: 1em;
  font-weight: bold;
}

.input_box span {
  position: absolute;
  left: 0;
  padding: 10px;
  pointer-events: none;
  font-size: 0.8em;
  color: var(--black);
  text-transform: uppercase;
  transition: 0.5s;
}

.input_box input:focus ~ span {
  color: var(--red);
  transform: translateX(10px) translateY(-7px);
  font-size: 0.55em;
  padding: 0 10px;
  background: var(--white);
  border-left: 2px solid var(--red);
  border-right: 2px solid var(--red);
  letter-spacing: 0.2em;
}

.input_box input:focus {
  border: 2px solid var(--red);
}

</style>