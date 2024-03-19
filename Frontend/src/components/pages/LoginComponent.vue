<template>
    <div class="title">
      <h1>Log in</h1>
    </div>
    <div class="form-container">
      <form>
        <label for="username">Username</label>
        <input v-model="username" type="text" id="username" name="username" placeholder="Username" required>

        <label for="password">Password</label>
        <input v-model="password" type="password" id="password" name="password" placeholder="Password" required>

        <div class="other-logging-options">
          <p>Forgot password</p>
          <p>
            <router-link to="register">Create account</router-link></p>
        </div>
        <div class="alertMessage" role="alert" v-if="alert">
          Username and/or password is invalid
        </div>
        <div v-else>
        </div>
        <button type="submit" class="pos-button" @click.prevent="login">Log in</button>
      </form>
    </div>
</template>

<script>
export default {
  name: "LoginComponent",
  inject:['usersService'],
data(){
    return{
      username:'',
      password:'',
      alert:false
    }
},
  methods:{
    async login(){
      try {
        const response = await this.usersService.login(this.username, this.password);
        if (response !== null) {
          // Login successful
          console.log('Logged in successfully:', response);
          this.$router.push({name: 'home'})
        } else {
          // Login failed
          console.error('Login failed: Invalid username or password.');
          this.alert = true;
        }
      }
      catch (error){
        console.log('login failed', error);
        this.alert=true;
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
  font-size: 30px;
  font-family: "Inria Sans", sans-serif;
}

/* Container */
.form-container {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  box-sizing: border-box;
}

/* Form */
form {
  display: flex;
  flex-direction: column;
}

.alertMessage{
  color: maroon;
  text-align: center;
}

/* Label */
form label {
  margin-bottom: 5px;
  display: block;
  font-size: 25px;
}

/* Input field */
form input {
  margin-bottom: 25px;
}

form input::placeholder {
  font-size: 15px;
}

/* Other options */
.other-logging-options {
  display: flex;
  justify-content: center;
  text-decoration: none;
}

.other-logging-options p {
  padding: 0 7%;
  margin: 0;
}

/* Form button */
form button {
  margin-top: 30px;
  align-self: center;
}
</style>