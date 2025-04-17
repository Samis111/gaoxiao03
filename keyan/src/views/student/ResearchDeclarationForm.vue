<template>
    <div class="research-declaration-form">
      <h1>科研项目申报页面</h1>
      <form @submit.prevent="submitDeclaration">
        <!-- 基础信息简表 -->
        <div class="section">
          <h2>基础信息简表</h2>
          <div>
            <label for="projectName">项目名称：</label>
            <input type="text" id="projectName" v-model="declaration.projectname" required />
          </div>
          <div>
            <label for="applicantName">申请人信息：</label>
            <input type="text" id="applicantName" v-model="declaration.applicantinfo" required />
          </div>
          <div>
            <label for="teamMembers">项目组成员：</label>
            <textarea id="teamMembers" v-model="declaration.teamMembers" required></textarea>
          </div>
        </div>
  
        <!-- 其他部分 -->
        <div class="section">
          <h2>研究目的和意义</h2>
          <textarea v-model="declaration.purposeandsignificance" required></textarea>
        </div>
        <div class="section">
          <h2>已有研究综述</h2>
          <textarea v-model="declaration.existingresearchreview" required></textarea>
        </div>
        <div class="section">
          <h2>研究内容</h2>
          <textarea v-model="declaration.researchcontent" required></textarea>
        </div>
        <div class="section">
          <h2>技术路线</h2>
          <textarea v-model="declaration.technicalroute" required></textarea>
        </div>
        <div class="section">
          <h2>预期成果</h2>
          <textarea v-model="declaration.expectedoutcomes" required></textarea>
        </div>
        <div class="section">
          <h2>经费预算</h2>
          <textarea v-model="declaration.budget" required></textarea>
        </div>
        <div class="section">
          <h2>成员分工</h2>
          <textarea v-model="declaration.memberdivision" required></textarea>
        </div>
        <div class="section">
          <h2>参考文献</h2>
          <textarea v-model="declaration.myreferences" required></textarea>
        </div>
        <div class="section">
          <h2>研究基础</h2>
          <textarea v-model="declaration.researchbasis" required></textarea>
        </div>
  
        <!-- 提交按钮 -->
        <div class="actions">
          <button type="submit" :disabled="submitting">{{ submitting ? '提交中...' : '提交' }}</button>
          <button type="button" @click="resetForm" class="reset-btn">重置</button>
        </div>
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
  
  export default {
    data() {
      return {
        submitting: false,
        declaration: {
          projectname: '',
          applicantinfo: '',
          teamMembers: '',
          purposeandsignificance: '',
          existingresearchreview: '',
          researchcontent: '',
          technicalroute: '',
          expectedoutcomes: '',
          budget: '',
          memberdivision: '',
          myreferences: '',
          researchbasis: '',
          type: '',
          projectid: null,
          userid: null,
          status: '',
          thistime: ''
        }
      };
    },
    methods: {
      async submitDeclaration() {
        try {
          this.submitting = true
          
          // 获取用户信息
          const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
          if (!userInfo.id) {
            this.$message.error('未找到用户信息，请重新登录')
            return
          }

          // 准备提交的数据
          const submitData = {
            ...this.declaration,
            userid: userInfo.id,
            status: '待审核',
            thistime: new Date().toISOString().split('T')[0]
          }

          // 发送申报信息到后端
          const response = await axios.post('/api/Returns/save', submitData)
          
          if (response.data) {
            this.$message.success('申报信息提交成功！')
            // 提交成功后返回列表页面
            this.$router.push('/student-home/research-declaration')
          } else {
            this.$message.error('提交失败，请稍后重试')
          }
        } catch (error) {
          console.error('提交申报信息错误:', error)
          this.$message.error('提交失败，请检查网络连接')
        } finally {
          this.submitting = false
        }
      },

      resetForm() {
        // 重置所有字段为空
        Object.keys(this.declaration).forEach(key => {
          this.declaration[key] = ''
        })
      },

      // 获取已保存的草稿（如果需要）
      async fetchDraft() {
        try {
          const response = await axios.get('/api/declarations/draft')
          if (response.data.code === 200 && response.data.data) {
            this.declaration = response.data.data
          }
        } catch (error) {
          console.error('获取草稿失败:', error)
        }
      },

      // 保存草稿（如果需要）
      async saveDraft() {
        try {
          const response = await axios.post('/api/declarations/draft', this.declaration)
          if (response.data.code === 200) {
            this.$message.success('草稿保存成功')
          }
        } catch (error) {
          console.error('保存草稿失败:', error)
          this.$message.error('保存草稿失败')
        }
      }
    },

    // 组件创建时获取草稿（如果需要）
    created() {
      // 获取路由传递的数据
      const projectData = this.$route.query.projectData
      if (projectData) {
        try {
          const data = JSON.parse(projectData)
          // 将数据映射到表单
          this.declaration = {
            ...this.declaration,
            projectname: data.subjectproject,
            type: data.type,
            expectedoutcomes: data.result,
            projectid: data.projectId,
            userid: data.userId,
            status: data.status,
            thistime: data.thistime,
            researchcontent: data.subjectproject
          }
        } catch (error) {
          console.error('解析项目数据失败:', error)
          this.$message.error('加载项目数据失败')
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .research-declaration-form {
    width: 80%;
    margin: 0 auto;
    text-align: left;
    padding: 20px;
  }
  
  .section {
    margin-bottom: 20px;
    background: #fff;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .section h2 {
    margin-top: 0;
    color: #333;
    border-bottom: 2px solid #eee;
    padding-bottom: 10px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
    color: #666;
  }
  
  input[type="text"],
  textarea {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    margin-bottom: 10px;
  }
  
  textarea {
    height: 100px;
    resize: vertical;
  }
  
  .actions {
    margin-top: 20px;
    text-align: center;
  }
  
  button {
    padding: 10px 20px;
    margin: 0 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
  }
  
  button[type="submit"] {
    background-color: #409EFF;
    color: white;
  }
  
  button[type="submit"]:hover {
    background-color: #66b1ff;
  }
  
  button[type="submit"]:disabled {
    background-color: #a0cfff;
    cursor: not-allowed;
  }
  
  .reset-btn {
    background-color: #909399;
    color: white;
  }
  
  .reset-btn:hover {
    background-color: #a6a9ad;
  }
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    .research-declaration-form {
      width: 95%;
    }
  }
  </style>