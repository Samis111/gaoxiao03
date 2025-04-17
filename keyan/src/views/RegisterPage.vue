<template>
  <div class="register-container">
    <h1>注册</h1>
    <form @submit.prevent="registerUser">
      <div class="form-group">
        <label for="username">用户名:</label>
        <input type="text" id="username" v-model="registerForm.username" required>
      </div>
      <div class="form-group">
        <label for="password">密码:</label>
        <input type="password" id="password" v-model="registerForm.password" required>
      </div>
      <div class="form-group">
        <label for="confirmPassword">确认密码:</label>
        <input type="password" id="confirmPassword" v-model="registerForm.confirmPassword" required>
      </div>
      <button type="submit" @click="registerUser">注册</button>
    </form>
  </div>
</template>
<script>

import axios from 'axios'

export default {
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: ''
      }
    }
  },
  methods: {
    async registerUser() {
      // 在这里添加逻辑来检查密码和确认密码是否匹配  
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        alert('密码和确认密码不匹配！');
        return;
      }

      const fakeResponse = await axios.post('api/register', this.registerForm)

      if (fakeResponse.data.code === 200) {
        this.goLogin();
      }


    },
    goLogin() {
      this.$router.push('/');
    }
  }
}  
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #083795;
}
</style>