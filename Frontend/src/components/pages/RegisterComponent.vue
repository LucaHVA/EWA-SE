<template>
  <div class="container">
    <div class="title">
      <h1>Create Account</h1>
    </div>
    <div class="form-container">
      <form>
        <label for="email">Email</label>
        <input type="email" id="email" name="email" v-model="email" placeholder="E.g. example@mail.com" required>

        <label for="username">Username</label>
        <input type="text" id="username" name="username" v-model="username" placeholder="E.g. amongusinnator4000" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" v-model="password" placeholder="Password" required>
        <div class="alertMessage" role="alert" v-if="alert">
          your password should be between 8-16 characters
        </div>
        <div v-else>
        </div>
        <button type="submit" class="pos-button" @click.prevent='checkInfo'>Create account</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: "RegisterComponent",

  inject:['usersService'],

  created() {
  },

  data(){
    return{
      username:"",
      password:"",
      email:"",
      alert:false
    }
  },
  methods:{
    async checkInfo(event){
      event.preventDefault();
      if (this.password.length < 8 || this.password.length > 16) {
        this.alert = true;
        return;
      }


      console.log("email:", this.email, "username:", this.username, "password:", this.password);

      const id = Math.floor(Math.random() * 1000);


      const user = {
        id:id,
        email: this.email,
        username: this.username,
        password: this.password
      };
      // Attempt to save user
      const savedUser = await this.usersService.save(user);
      if (savedUser) {
        console.log('User saved successfully:', savedUser);
        this.$router.push({name: 'profilePage'})
      } else {
        console.error('Failed to save user.');
      }
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

/* Container */
.form-container {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  box-sizing: border-box;
}
.alertMessage{
  color: maroon;
  text-align: center;
}

/* Form */
form {
  display: flex;
  flex-direction: column;
}

/* Form button */
form button {
  margin-top: 20px;
  align-self: center;
}

/* Label */
form label {
  margin-bottom: 5px;
  display: block;
  font-size: 25px;
}

/* Input field */
form input {
  margin-bottom: 20px;
}

form input::placeholder {
  font-size: 15px;
}
</style>