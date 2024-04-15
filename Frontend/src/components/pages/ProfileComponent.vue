<template>
  <div class="container">
    <div class="title">
      <h1>Profile</h1>
    </div>

    <div class="profile-pic-container">
      <div @click="triggerFileInput">
        <img :src="imageUrl" class="profile-pic"  alt="Profile Picture"/>
        <input type="file" @change="onFileChange" accept="image/*" ref="fileInput" style="display: none;" />
      </div>
    </div>

    <div class="form-container">
      <form>
        <label for="email">Change Username</label>
        <input v-model="username" type="email" id="email" name="email" placeholder="Username">

        <label for="username">Change Email</label>
        <input v-model="email" type="text" id="username" name="username" placeholder="Email">

        <label for="password">Change Password</label>
        <input v-model="password" type="password" id="password" name="password" placeholder="Password">
        <div class="alertMessage" role="alert" v-if="alert">
          Your password should be between 8-16 characters.
        </div>
      </form>
    </div>

    <div class="button-container">
      <button type="submit" @click="cancel" class="cancel-button">Cancel</button>
      <button type="submit" @click="save" class="pos-button">Save</button>
    </div>
  </div>
</template>

<script>
import defaultImage from '@/assets/images/defaultpfp.png';
export default {
  name: "ProfileComponent",
  inject:['usersService'],

  data() {
    return {
      imageUrl: defaultImage,
      username: '',
      email: '',
      password: '',
      alert: false
    };
  },

  created() {
    this.fetchUserInfo();
  },

  methods: {
    cancel() {
      this.fetchUserInfo();
    },
    fetchUserInfo() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'));
      if (userInfo) {
        this.username = userInfo.username;
        this.email = userInfo.email;
        this.password = userInfo.password;
        if (userInfo.profilePicture != null){
          this.imageUrl = userInfo.profilePicture;
        }
      } else {
        // Redirect to login page if userInfo is not found
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
      const userInfo = JSON.parse(localStorage.getItem('userInfo'));
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
        localStorage.setItem('userInfo', JSON.stringify(userUpdate));
        alert('Profile saved successfully!');
      } catch (e) {
        console.error('Error during save:', e);
        alert('Failed to save profile. Please try again.');
      }
    },
  }
}
</script>

<style scoped>
/* Title */
.title {
  text-align: center;
  margin-top: 50px;
  margin-bottom: 10px;
  font-size: 30px;
  font-family: "Inria Sans", sans-serif;
}

.title h1 {
  margin: 0;
}

.form-container {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  box-sizing: border-box;
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
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
}

.button-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
}

.cancel-button{
  background-color: #FF7B4D;
  border: none;
  color: white;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  width: max-content;
  font-size: 20px;
  margin: 6px 2rem;
  cursor: pointer;
  border-radius: 8px;
  font-family: 'Raleway', sans-serif;
  font-weight: 600;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.1);
}

.pos-button {
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.1);
  margin: 4px 2rem!important;
}

input {
  font-size: 1.2rem;
}

.alertMessage{
  color: maroon;
  text-align: center;
}
</style>