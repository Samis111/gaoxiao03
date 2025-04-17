<template>
  <div class="profile-form">
    <!-- 表单部分，只在编辑模式下显示 -->
    <form v-if="isEdit" @submit.prevent="saveProfile">
      <h1>修改信息</h1>
      <div>
        <label for="name">名字:</label>
        <input type="text" v-model="profile.name" id="name" required />
      </div>
      <div>
        <label for="gender">性别:</label>
        <select v-model="profile.gender" id="gender" required>
          <option v-for="gender in genders" :key="gender" :value="gender">
            {{ gender }}
          </option>
        </select>
      </div>
      <div>
        <label for="phone">电话号:</label>
        <input type="tel" v-model="profile.phone" id="phone" required 
               pattern="^1[3-9]\d{9}$" title="请输入正确的手机号码"/>
      </div>
      <div>
        <label for="juese">职责:</label>
        <input type="text" v-model="profile.juese" id="juese" required />
      </div>
      <div>
        <label for="college">二级院校:</label>
        <input type="text" v-model="profile.college" id="college" required />
      </div>
      <div>
        <label for="major">专业:</label>
        <input type="text" v-model="profile.major" id="major" required />
      </div>
      <div class="button-group">
        <button type="submit" :disabled="submitting">
          {{ submitting ? '保存中...' : '保存' }}
        </button>
        <button type="button" class="reset-btn" @click="cancelEdit">取消</button>
      </div>
    </form>

    <!-- 信息展示部分 -->
    <div class="xinxi" v-if="!isEdit">
      <h2>个人信息</h2>
      <div v-if="showProfile">
        <p>名字: {{ profile.name }}</p>
        <p>性别: {{ profile.gender }}</p>
        <p>电话号: {{ profile.phone }}</p>
        <p>职责: {{ profile.juese }}</p>
        <p>二级院校: {{ profile.college }}</p>
        <p>年级: {{ profile.grade }}</p>
        <p>专业: {{ profile.major }}</p>
        <div class="button-group">
          <button class="edit-btn" @click="startEdit">修改信息</button>
        </div>
      </div>
      <div v-else>
        <p class="no-data">暂无个人信息，请点击添加</p>
        <div class="button-group">
          <button class="edit-btn" @click="startEdit">添加信息</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      isEdit: false,
      profile: {
        id: null,
        name: '',
        gender: '',
        phone: '',
        juese: '',
        college: '',
        grade: '',
        major: ''
      },
      genders: ['男', '女'],
      showProfile: false,
      submitting: false
    };
  },
  methods: {
    async saveProfile() {
      try {
        this.submitting = true
        // 从 session 获取用户 ID
        const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
        this.profile.userId = userInfo.id // 添加用户ID到个人信息中
        
        // 使用 /api/updateById 作为更新地址
        const response = await axios.post('/api/updateById', this.profile)
        
        if (response.data.code === 200) {
          this.$message.success('信息保存成功！')
          this.showProfile = true
          this.isEdit = false
          // 重新获取最新数据
          await this.fetchProfile()
        } else {
          this.$message.error(response.data.message || '操作失败，请稍后重试')
        }
      } catch (error) {
        console.error('保存信息错误:', error)
        if (error.response) {
          console.log('错误响应:', error.response.data)
        }
        this.$message.error('操作失败，请检查网络连接')
      } finally {
        this.submitting = false
      }
    },

    startEdit() {
      this.isEdit = true
    },

    cancelEdit() {
      this.isEdit = false
      // 如果是新增，重置表单
      if (!this.profile.id) {
        this.resetForm()
      }
    },

    resetForm() {
      this.profile = {
        id: null,
        name: '',
        gender: '',
        phone: '',
        juese: '',
        college: '',
        grade: '',
        major: ''
      }
      this.showProfile = false
      this.isEdit = false
    },

    async fetchProfile() {
      try {
        // 从 session 获取用户 ID
        const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
        if (!userInfo.id) {
          console.log('当前用户信息:', userInfo) // 调试输出
          this.$message.error('未找到用户信息，请重新登录')
          return
        }

        // 使用用户ID获取个人信息
        const response = await axios.get(`/api/profile/${userInfo.id}`)
        console.log('获取个人信息响应:', response.data) // 调试输出

        if (response.data.code === 200 && response.data.data) {
          this.profile = response.data.data
          this.showProfile = true
        } else if (response.data.code === 404) {
          // 如果没有找到个人信息，显示空表单
          console.log('未找到个人信息，显示空表单') // 调试输出
          this.showProfile = false
          this.resetForm()
        } else {
          this.$message.error(response.data.message || '获取个人信息失败')
        }
      } catch (error) {
        console.error('获取个人信息失败:', error)
        if (error.response) {
          console.log('错误响应:', error.response.data) // 调试输出
        }
        this.$message.error('获取个人信息失败，请稍后重试')
      }
    },

    // 检查是否已登录
    checkLogin() {
      const isLogin = sessionStorage.getItem('isLogin')
      const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
      
      if (!isLogin || !userInfo.id) {
        this.$message.error('请先登录')
        this.$router.push('/login')
        return false
      }
      return true
    }
  },
  created() {
    if (this.checkLogin()) {
      this.fetchProfile()
    }
  }
};
</script>

<style scoped lang="less">
.profile-form {
  width: 80%; // 调整为百分比宽度，以适应不同屏幕尺寸
  max-width: 800px; // 设置最大宽度，防止在超大屏幕上显得过小
  margin: 20px auto; // 增加上下外边距，使页面布局更加均衡
  padding: 30px; // 增加内边距，使内容区域更加宽敞
  border: 1px solid #ddd; // 使用更浅的边框颜色
  border-radius: 8px; // 增加圆角半径，使边框更加柔和
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); // 添加轻微的阴影效果，增加立体感
  background-color: #fff; // 确保背景为白色，提高可读性

  h1 {
    text-align: center;
    margin-bottom: 20px; // 增加底部外边距，与表单内容分隔开
  }

  div {
    margin-bottom: 20px; // 增加底部外边距，使每个表单项之间有明显的分隔
  }

  label {
    display: block;
    margin-bottom: 8px; // 稍微减少底部外边距，使标签与输入框更加紧凑
    font-weight: bold; // 加粗标签文本，提高可读性
    color: #333; // 使用更深的字体颜色，与背景形成对比
  }

  input,
  select {
    width: 100%;
    padding: 10px; // 增加内边距，使输入框更加宽敞
    box-sizing: border-box;
    border: 1px solid #ccc; // 添加边框，使输入框更加明显
    border-radius: 4px; // 增加圆角半径，使输入框更加柔和
    outline: none; // 移除焦点时的默认轮廓线
    transition: border-color 0.3s ease; // 添加过渡效果，使边框颜色变化更加平滑
  }

  input:focus,
  select:focus {
    border-color: #409eff; // 焦点时改变边框颜色，提供视觉反馈
  }

  button {
    width: 100%;
    padding: 12px; // 增加内边距，使按钮更加宽敞
    background-color: #409eff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px; // 增加字体大小，使按钮文本更加明显
    transition: background-color 0.3s ease; // 添加过渡效果，使背景颜色变化更加平滑
  }

  button:hover {
    background-color: #66b1ff; // 悬停时改变背景颜色，提供交互反馈
  }

  .xinxi {
    margin: 20px auto; // 重新设置外边距，使信息区域居中显示
    padding: 20px; // 增加内边距，使信息内容更加宽敞
    background-color: #f9f9f9; // 使用浅灰色背景，与信息内容形成对比
    border-radius: 8px; // 增加圆角半径，使信息区域更加柔和
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); // 添加轻微的阴影效果，增加立体感
    max-width: 800px; // 设置最大宽度，与表单区域保持一致

    h2 {
      text-align: center;
      margin-bottom: 20px; // 增加底部外边距，与信息内容分隔开
    }

    p {
      text-align: left; // 文本左对齐，提高可读性
      color: #333; // 使用更深的字体颜色，与背景形成对比
      padding-bottom: 10px; // 增加底部内边距，使每条信息之间有明显的分隔
    }
  }

  .no-data {
    text-align: center;
    color: #909399;
    margin: 20px 0;
  }

  .button-group {
    display: flex;
    gap: 10px;
    margin-top: 20px;
    justify-content: center;

    button {
      flex: 0 1 200px; // 修改按钮宽度
      padding: 12px;
      border-radius: 5px;
      cursor: pointer;
      font-size: 16px;
      transition: all 0.3s ease;
    }

    .reset-btn, .cancel-btn {
      background-color: #909399;
      color: white;
      &:hover {
        background-color: #a6a9ad;
      }
    }

    .edit-btn {
      background-color: #67c23a;
      color: white;
      &:hover {
        background-color: #85ce61;
      }
    }
  }

  // 添加表单验证样式
  input:invalid {
    border-color: #f56c6c;
  }

  input:invalid:focus {
    border-color: #f56c6c;
    box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.2);
  }
}
</style>