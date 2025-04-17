<template>
    <div class="review-status-page">
      <h1>科研项目审核状态</h1>
      <table>
        <thead>
          <tr>
            <th>项目名称</th>
            <th>提交人</th>
            <th>提交时间</th>
            <th>审核状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(project, index) in projects" :key="index">
            <td>{{ project.projectname }}</td>
            <td>{{ project.applicantinfo }}</td>
            <td>{{ project.thistime }}</td>
            <td>{{ project.status }}</td>
            <td>
              <el-button 
                type="primary" 
                size="small" 
                @click="viewDetails(project)"
              >
                查看详情
              </el-button>
              <el-button 
                type="danger" 
                size="small" 
                v-if="project.status === '待审核'"
                @click="withdrawProject(project)"
              >
                撤回
              </el-button>
                   <el-button 
                type="success" 
                size="small" 
                v-if="project.status === '已通过'"
                @click="goToArchive(project)"
              >
                归档
              </el-button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading">
        <el-loading-text>加载中...</el-loading-text>
      </div>

      <!-- 无数据提示 -->
      <div v-if="!loading && projects.length === 0" class="no-data">
        暂无申报记录
      </div>
      <!-- <router-view  class="router-view"></router-view> -->
    </div>
    
  </template>
  
  <script>
  import axios from 'axios'

  export default {
    data() {
      return {
        projects: [],
        loading: false
      }
    },
    methods: {
      // 获取项目列表
      async fetchProjects() {
        try {
          this.loading = true
          // 从 session 获取用户信息
          const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
          if (!userInfo.id) {
            this.$message.error('未找到用户信息，请重新登录')
            return
          }

          // 获取当前用户的申报列表
          const response = await axios.get(`/api/Returns/list/${userInfo.id}`)

        console.log(response.data.data)

          this.projects = response.data.data || []

        } catch (error) {
          console.error('获取项目列表失败:', error)
          this.$message.error('获取项目列表失败，请稍后重试')
        } finally {
          this.loading = false
        }
        
      },
    // ... existing code ...
goToArchive(project) {

 let query= {
      id: project.id,
      projectName: project.projectname,
      submitter: project.applicantinfo,
      submissionDate: project.thistime
    }

  console.log(project)

  this.$router.push({
    path: '/student-home/pigeonhole',
    query: {
      id: project.id,
      projectName: project.projectname,
      submitter: project.applicantinfo,
      submissionDate: project.thistime
    }
  });
},
// ... existing code ...
      // 查看详情
      viewDetails(project) {
        // 将项目数据存储到 sessionStorage
        sessionStorage.setItem('currentProject', JSON.stringify(project))
        // 跳转到详情页
        this.$router.push('/student-home/project-details')
      },

      // 撤回项目
      async withdrawProject(project) {
        try {
          await this.$confirm('确定要撤回该项目吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })

          const response = await axios.post('/api/Returns/withdraw', {
            id: project.id,
            status: '已撤回'
          })

          if (response.data) {
            this.$message.success('项目已成功撤回')
            // 重新获取列表
            await this.fetchProjects()
          } else {
            this.$message.error('撤回失败，请稍后重试')
          }
        } catch (error) {
          if (error !== 'cancel') {
            console.error('撤回项目失败:', error)
            this.$message.error('撤回失败，请稍后重试')
          }
        }
      },
    },
    created() {
      this.fetchProjects()
    }
  };
  </script>
  
  <style scoped lang="less">
  .review-status-page {
    width: 90%;
    margin: 20px auto;
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    h1 {
      margin-bottom: 20px;
      color: #333;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;

      th, td {
        padding: 12px;
        border: 1px solid #ebeef5;
        text-align: center;
      }

      th {
        background-color: #f5f7fa;
        color: #606266;
        font-weight: bold;
      }

      tr:hover {
        background-color: #f5f7fa;
      }
    }

    .loading {
      text-align: center;
      margin: 20px 0;
    }

    .no-data {
      text-align: center;
      color: #909399;
      margin: 20px 0;
    }

    .el-button {
      margin: 0 5px;
    }

    .el-button--danger {
      margin-left: 10px;
    }
  }
  </style>