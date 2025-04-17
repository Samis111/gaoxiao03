<template>
  <div class="password-page">
    <el-form
      :model="ruleForm"
      status-icon
      :rules="rules"
      ref="ruleForm"
      label-width="100px"
      class="demo-ruleForm"
    >
      <el-form-item label="密码" prop="pass">
        <el-input
          type="password"
          v-model="ruleForm.pass"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input
          type="password"
          v-model="ruleForm.checkPass"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')" :loading="loading">
          {{ loading ? '提交中...' : '提交' }}
        </el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      loading: false,
      ruleForm: {
        pass: '',
        checkPass: '',
      },
      rules: {
        pass: [
          { validator: validatePass, trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ],
      }
    };
  },
  methods: {
    async submitForm(formName) {
      try {
        const valid = await this.$refs[formName].validate()
        if (valid) {
          this.loading = true
          
          // 从 session 获取用户信息
          const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
          if (!userInfo.id) {
            this.$message.error('未找到用户信息，请重新登录')
            return
          }

          // 准备更新数据
          const updateData = {
            id: userInfo.id,
            password: this.ruleForm.pass
          }

          // 发送更新请求
          const response = await axios.post('/api/updateById', updateData)

          if (response.data.code === 200) {
            this.$message.success('密码修改成功')
            this.resetForm(formName)
          } else {
            this.$message.error(response.data.message || '密码修改失败')
          }
        }
      } catch (error) {
        console.error('密码修改错误:', error)
        if (error.response) {
          console.log('错误响应:', error.response.data)
        }
        this.$message.error('密码修改失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },
    
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped lang="less">
.password-page {
  width: 80%;
  max-width: 600px;
  margin: 20px auto;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .el-form {
    max-width: 400px;
    margin: 0 auto;
  }

  .el-button {
    min-width: 100px;
    margin-right: 20px;
  }
}
</style>