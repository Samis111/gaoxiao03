<template>
  <div class="login-container">
    <div class="login-form">
      <h2 class="text-center">登录</h2>
      <div class="form-group">
        <label class="radio-inline">
          <input type="radio" name="role" value="student" v-model="role" />
          申请人
        </label>
        <label class="radio-inline">
          <input type="radio" name="role" value="admin" v-model="role" /> 审批人
        </label>
        <label class="radio-inline">
          <input type="radio" name="role" value="superadmin" v-model="role" /> 管理员
        </label>
      </div>
      <div class="form-group">
        <input type="text" class="form-control" v-model="username" placeholder="Username" />
      </div>
      <div class="form-group">
        <input type="password" class="form-control" v-model="password" placeholder="Password" />
      </div>
      <button class="btn btn-primary btn-block" @click="login">登录</button>
    </div>
  </div>
</template>

<script>


export default {
  data() {
    return {
      username: '',
      password: '',
      role: 'student'
    }
  },
  methods: {
    async login() {
      try {
        // 模拟登录请求
        // const fakeResponse = {
        //   data: {
        //     code: 200,
        //     message: '登录成功',
        //     data: {
        //       id: 1,
        //       name: this.username,
        //       role: this.role
        //     }
        //   }
        // }

        // console.log('Login response:', fakeResponse) // 调试日志

        let userinfo = {
          username: this.username,
          password: this.password,
          role: this.role
        }

        const fakeResponse = await this.$store.dispatch('login', userinfo)
        console.log('Login response:', fakeResponse) // 调试日志



        if (fakeResponse.data.code === 200) {
          // 保存完整的用户信息到 sessionStorage
          const userData = {
            ...fakeResponse.data.data,
            role: this.role
          }
          sessionStorage.setItem('userInfo', JSON.stringify(userData))
          sessionStorage.setItem('isLogin', 'true')

          this.$message.success('登录成功')

          // 根据角色跳转
          if (this.role === 'student') {
            await this.$router.push('/student-home')
          } else if (this.role === 'admin') {
            await this.$router.push('/admin-home')
          } else if (this.role === 'superadmin') {
            await this.$router.push('/superadmin-home')
          }

          console.log('Current route:', this.$route.path) // 调试日志
        } else {
          this.$message.error(fakeResponse.data.message || '登录失败，请检查用户名和密码')
        }
      } catch (error) {
        console.error('Login error:', error)
        this.$message.error('登录失败，请稍后重试')
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 100px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.login-form h2 {
  margin-bottom: 20px;
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

.btn-block {
  margin-top: 20px;
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

.btn {
  color: #fff;
}

.radio-inline {
  margin-right: 10px;
}

.form-group .form-control {
  width: 100%;
}
</style>