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
      <div v-if="isAdminModalOpen" class="modal-mask" @click="isAdminModalOpen = false">
        <div class="modal-wrapper">
          <div class="modal-container" @click.stop>
            <h2>Admin Section</h2>
            <ul class="user-list">
              <li v-for="user in users" :key="user.id">
                {{ user.name }} - {{ user.email }}
                <button @click="removeUser(user.id)">Remove</button>
              </li>
            </ul>
            <button class="close-button transition" @click="isAdminModalOpen = false">Close</button>
          </div>
        </div>
      </div>
    </transition>
  </nav>
</template>




<script>
export default {
  name: "NavbarComponent",

  inject: ["usersService"],
  data(){
    return{
      isAdminModalOpen: false,
      users: []
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

/* Modal */
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
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
  background: #ebeef1;
  padding: 20px 20px 0 20px;
  border-radius: 5px;
  width: 75%;
  max-width: 700px;
  max-height: 85%;
}

.modal-container h2 {
  margin: 1rem;
}

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

.close-button-box {
  display: flex;
  justify-content: center;
}

/* User List in Modal */
.user-list {
  list-style-type: none;
  padding: 0;
  margin: 1rem 0;
}

.user-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.user-list button {
  background-color: var(--coral);
  border: none;
  color: var(--white);
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 4px;
}

.user-list button:hover {
  background-color: var(--white);
  color: var(--coral);
}
</style>

