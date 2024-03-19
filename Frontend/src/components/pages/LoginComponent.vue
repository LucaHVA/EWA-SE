<template>
    <div class="title">
      <h1>Log in</h1>
    </div>
    <div class="form-container">
      <form>
        <label for="username">Username</label>
        <input type="text" id="username" name="username" placeholder="Username" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="Password" required>

        <div class="other-logging-options">
          <p>Forgot password</p>
          <p>Create account</p>
        </div>

        <button type="submit" class="pos-button">Log in</button>
      </form>
    </div>
</template>

<script>
export default {
  name: "LoginComponent"

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