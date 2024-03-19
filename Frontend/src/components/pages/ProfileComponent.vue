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
        <input type="email" id="email" name="email" placeholder="Username">

        <label for="username">Change Email</label>
        <input type="text" id="username" name="username" placeholder="Email">

        <label for="password">Change Password</label>
        <input type="password" id="password" name="password" placeholder="Password">
      </form>
    </div>

    <div class="button-container">
      <button type="submit" class="cancel-button">Cancel</button>
      <button type="submit" @click="findUser" class="pos-button">Save</button>
    </div>
  </div>
</template>

<script>
import defaultImage from '@/assets/images/defaultpfp.png'
export default {
  name: "ProfileComponent",
  inject:['usersService'],
  data() {
    return {
      imageUrl: defaultImage
    };
  },
  methods: {
    onFileChange(e) {
      const file = e.target.files[0];
      this.imageUrl = URL.createObjectURL(file);
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    findUser() {
      console.log(this.usersService.asyncFindById(30000))
    }
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
</style>