<template>
  <div class="container">
    <div class="profile-page-title">
      <h1>Profile</h1>
    </div>
    <div class="profile-page-content">
      <!-- Profile Picture Section -->
      <div class="profile-pic-container">
        <div @click="triggerFileInput" class="profile-pic-wrapper">
          <img :src="imageUrl" class="profile-pic" alt="Profile Picture"/>
          <input type="file" @change="onFileChange" accept="image/*" ref="fileInput" style="display: none;"/>
          <div class="profile-pic-overlay">Change Profile Picture</div>
        </div>
      </div>
      <!-- User Info Section -->
      <div class="profile-info">
        <div class="form-container">
          <form>
            <label for="username">Change Username</label>
            <input v-model="username" type="text" id="username" name="username" placeholder="Username">

            <label for="email">Change Email</label>
            <input v-model="email" type="email" id="email" name="email" placeholder="Email">

            <label for="password">Change Password</label>
            <input v-model="password" type="password" id="password" name="password" placeholder="Password">
            <div class="alertMessage" role="alert" v-if="alert">
              Your password should be between 8-16 characters.
            </div>
          </form>
          <div class="button-container">
            <button type="submit" @click="cancel" class="cancel-button transition">Cancel</button>
            <button type="submit" @click="save" class="save-button transition">Save</button>
          </div>
        </div>
      </div>

      <!-- Match History Section -->
      <div class="match-history-container">
        <h1>Recent Games</h1>
        <ul>
          <li v-for="(game, index) in matchHistory" :key="index" class="pill transition" @click="showGameDetails(game)">
            {{ game.startTime }} - {{ game.placement }}
          </li>
        </ul>
        <button class="cancel-button transition" @click="showMoreHistory">See More</button>
      </div>
    </div>

    <!-- Modal Section -->
    <transition name="modal">
      <div v-if="isModalOpen" class="modal-mask" @click="closeModal">
        <div class="modal-wrapper">
          <div class="modal-container" @click.stop>
            <h1 v-if="selectedGameHistory">Game Details</h1>
            <h1 v-else>Match History</h1>
            <div v-if="selectedGameHistory">
              <p><strong>Placement:</strong> {{ selectedGameHistory.placement }}</p>
              <p><strong>Start Time:</strong> {{ selectedGameHistory.startTime }}</p>
              <p><strong>End Time:</strong> {{ selectedGameHistory.endTime }}</p>
            </div>
            <ul v-else>
              <li v-for="(game, index) in matchHistory" :key="index" class="pill transition"
                  @click="showGameDetails(game)">
                {{ game.startTime }} - {{ game.placement }}
              </li>
            </ul>
            <button class="cancel-button transition" @click="closeModal">Close</button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import defaultImage from '@/assets/images/defaultpfp.png';

export default {
  name: "ProfileComponent",
  inject: ['usersService'],

  data() {
    return {
      imageUrl: defaultImage,
      username: '',
      email: '',
      password: '',
      alert: false,
      recentGames: [],
      matchHistory: [],
      isModalOpen: false,
      selectedGameHistory: null
    };
  },

  created() {
    this.fetchUserInfo();
    this.fetchMatchHistory();
  },

  methods: {
    cancel() {
      this.fetchUserInfo();
    },
    async fetchUserInfo() {
      const userInfo = await this.usersService.getCurrentUser;
      if (userInfo) {
        this.username = userInfo.username;
        this.email = userInfo.email;
        this.password = userInfo.password;
        if (userInfo.profilePicture != null) {
          this.imageUrl = userInfo.profilePicture;
        }
      } else {
        this.$router.push({name: 'login'});
      }
    },
    onFileChange(e) {
      const file = e.target.files[0];
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.imageUrl = reader.result;
      };
      reader.onerror = function (error) {
        console.log('Error: ', error);
      };
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    async save() {
      if (this.password.length < 8 || this.password.length > 16) {
        this.alert = true;
        return;
      } else {
        this.alert = false;
      }
      const userInfo = JSON.parse(sessionStorage.getItem('undefined_ACC'));
      const userId = userInfo.id;
      const userUpdate = {
        id: userId,
        username: this.username,
        email: this.email,
        password: this.password,
        profilePicture: this.imageUrl
      };

      try {
        const response = await this.usersService.save(userUpdate);
        if (!response) {
          alert('Failed to save profile.');
          throw new Error('Failed to save profile.');
        }
        await this.usersService.asyncFindAll();
        sessionStorage.setItem('undefined_ACC', JSON.stringify(userUpdate));
        alert('Profile saved successfully!');
      } catch (e) {
        console.error('Error during save:', e);
        alert('Failed to save profile. Please try again.');
      }
    },
    async fetchMatchHistory() {
      try {
        const userId = JSON.parse(sessionStorage.getItem('undefined_ACC')).id;
        this.matchHistory = await this.usersService.fetchMatchHistory(userId);
        console.log("hello noobs " + this.matchHistory)
      } catch (error) {
        console.error('Failed to fetch match history:', error);
      }
    },
    showMoreHistory() {
      this.isModalOpen = true;
    },
    closeModal() {
      this.isModalOpen = false;
      this.selectedGameHistory = null;
    },
    showGameDetails(game) {
      this.selectedGameHistory = game;
      this.isModalOpen = true;
    }
  }
}
</script>

<style scoped>
/* Title */
.profile-page-title {
  text-align: center;
  margin-top: 50px;
  margin-bottom: 10px;
  font-size: 30px;
  font-family: "Inria Sans", sans-serif;
}

.profile-page-title h1 {
  margin: 0;
}

/*content*/
.profile-page-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

/*user info container*/
.form-container {
  max-width: 400px;
}

form {
  display: flex;
  flex-direction: column;
}

form label {
  margin-bottom: 5px;
  display: block;
  font-size: 25px;
}

form input {
  margin-bottom: 1rem;
}

form input::placeholder {
  font-size: 15px;
}

/* Profile picture container */
.profile-pic-container {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.profile-pic {
  width: 300px;
  height: 300px;
  border-radius: 50%;
  object-fit: cover;
}

.profile-pic-wrapper {
  position: relative;
}

.profile-pic-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(0, 0, 0, 0.9);
  color: white;
  padding: 70px;
  border-radius: 50%;
  font-size: 30px;
  opacity: 0;
  font-weight: bolder;
  transition: opacity 0.3s ease;
}

.profile-pic-wrapper:hover .profile-pic-overlay {
  opacity: 1;
}

/*buttons*/
.button-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
}

.cancel-button {
  font-family: 'Raleway', sans-serif;
  background-color: var(--coral);
  border: none;
  color: var(--white);
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  width: max-content;
  font-size: 20px;
  margin: 6px 2rem;
  cursor: pointer;
  border-radius: 8px;
  font-weight: bold;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.1);
}

.cancel-button:hover {
  background-color: var(--white);
  color: var(--coral);
}

.save-button {
  font-family: 'Raleway', sans-serif;
  background-color: var(--shade-of-tea);
  border: none;
  color: var(--white);
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  width: max-content;
  font-size: 20px;
  margin: 6px 2rem;
  cursor: pointer;
  border-radius: 8px;
  font-weight: bold;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.1);
}

.save-button:hover {
  background-color: var(--white);
  color: var(--shade-of-tea);
}

input {
  font-size: 1.2rem;
}

.alertMessage {
  color: maroon;
  text-align: center;
}

/*match history container*/
.match-history-container {
  width: 300px;
  margin-left: 20px;
}

.match-history-container h1 {
  display: flex;
  justify-content: center;
}

.match-history-container ul {
  list-style: none;
  padding: 0;
}

.match-history-container li {
  margin-bottom: 10px;
}

.match-history-container button {
  margin-top: 10px;
}

.pill {
  font-weight: bolder;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--white);
  padding: 0.5rem 1rem;
  border-radius: 10px;
  width: 250px;
  height: 30px;
}

.pill:hover {
  background-color: var(--black);
  color: var(--white);
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
  width: 450px;
  padding: 20px 30px;
  background-color: var(--white);
  border-radius: 2px;
  box-shadow: 0 15px 20px rgba(0, 0, 0, 0.5);
  transition: all 0.4s ease;
}

/* Transition for modal */
.modal-enter-active, .modal-leave-active {
  transition: opacity 0.5s, transform 0.5s;
}

.modal-enter-from, .modal-leave-to {
  opacity: 0;
  transform: scale(1.3);
}
</style>
