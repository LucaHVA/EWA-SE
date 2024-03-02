<template>
  <div class="container">
    <div class="title">
      <h1>Create Account</h1>
    </div>
    <div class="form-container">
      <form>
        <label for="email">Email</label>
        <input type="email" id="email" name="email" placeholder="E.g. example@mail.com" required>

        <label for="username">Username</label>
        <input type="text" id="username" name="username" placeholder="E.g. amongusinnator4000" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="Password" required>

        <button type="submit" class="pos-button">Create account</button>
      </form>
    </div>
  </div>

  <div id="background-blob">
    <div class="blob-top-left"></div>
    <div class="blob-bottom-left"></div>
    <div class="blob-bottom-Right"></div>
  </div>
</template>

<script>
export default {
  name: "RegisterComponent"
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

/* Form button */
form button {
  margin-top: 30px;
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
  margin-bottom: 25px;
}

form input::placeholder {
  font-size: 15px;
}
</style>