<template>
    <div class="research-declaration-page">
      <h1>科研项目申报页面</h1>
      <table>
        <thead>
          <tr>
            <th>分类</th>
            <th>研究内容</th>
            <th>预期成果</th>
            <th>发布时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(project, index) in projects" :key="index">
            <td>{{ project.type }}</td>
            <td>{{ project.subjectproject }}</td>
            <td>{{ project.result }}</td>
            <td>{{ project.thistime }}</td>
            <td>
              <el-button 
                type="primary" 
                size="small" 
                @click="handleApply(project)"
                :loading="loadingIndex === index"
              >
                {{ loadingIndex === index ? '申报中...' : '申报' }}
              </el-button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 申报确认对话框 -->
      <el-dialog
        title="项目申报确认"
        :visible.sync="dialogVisible"
        width="50%"
      >
        <div class="dialog-content" v-if="selectedProject">
          <h3>您确定要申报以下项目吗？</h3>
          <p><strong>分类：</strong>{{ selectedProject.type }}</p>
          <p><strong>研究内容：</strong>{{ selectedProject.subjectproject }}</p>
          <p><strong>预期成果：</strong>{{ selectedProject.result }}</p>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirmApply" :loading="submitting">
            {{ submitting ? '提交中...' : '确 定' }}
          </el-button>
        </span>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import axios from 'axios'

  export default {
    data() {
      return {
        projects: [],
        loadingIndex: -1,
        dialogVisible: false,
        selectedProject: null,
        submitting: false
      }
    },
    methods: {
      // 获取项目列表
      async fetchProjects() {
        try {
          const response = await axios.get('/api/projects')
          if (response.data.code === 200) {
            this.projects = response.data.data
          } else {
            this.$message.error('获取项目列表失败')
          }
        } catch (error) {
          console.error('获取项目列表错误:', error)
          this.$message.error('获取项目列表失败，请稍后重试')
        }
      },

      // 处理申报按钮点击
      handleApply(project) {
        this.selectedProject = project
        this.dialogVisible = true
      },

      // 确认申报
      async confirmApply() {
        try {
          this.submitting = true
          
          // 从 session 获取用户信息
          const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
          if (!userInfo.id) {
            this.$message.error('未找到用户信息，请重新登录')
            return
          }

          // 准备申报数据
          const applyData = {
            userId: userInfo.id,
            projectId: this.selectedProject.id,
            type: this.selectedProject.type,
            subjectproject: this.selectedProject.subjectproject,
            result: this.selectedProject.result,
            status: '待审核',  // 初始状态
            thistime: new Date().toISOString().split('T')[0]  // 当前日期
          }

          // 发送申报请求
          // const response = await axios.post('/api/Returns/save', applyData)

          this.$message.success('项目申报成功')
          this.dialogVisible = false
          
          // 使用 query 或 params 传递数据
          this.$router.push({
            path: '/student-home/research-declaration-form',
            query: {
              projectData: JSON.stringify(applyData)
            }
          })

        } catch (error) {
          console.error('项目申报错误:', error)
          if (error.response) {
            console.log('错误响应:', error.response.data)
          }
          this.$message.error('项目申报失败，请稍后重试')
        } finally {
          this.submitting = false
        }
      }
    },
    created() {
      this.fetchProjects()
    }
  };
  </script>
  
  <style scoped lang="less">
  .research-declaration-page {
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

    .dialog-content {
      padding: 20px;

      h3 {
        margin-bottom: 20px;
        color: #303133;
      }

      p {
        margin: 10px 0;
        color: #606266;

        strong {
          color: #303133;
        }
      }
    }
  }
  </style>