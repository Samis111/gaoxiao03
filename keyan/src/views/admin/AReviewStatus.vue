<template>
  <div class="review-status-page">
    <el-card class="box-card">
      <div slot="header" class="card-header">
        <h2>科研项目审核状态</h2>
      </div>

      <el-table
        v-loading="loading"
        :data="projects"
        stripe
        border
        style="width: 100%">
        <el-table-column
          prop="projectname"
          label="项目名称"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="applicantinfo"
          label="提交人"
          min-width="120">
        </el-table-column>
        <el-table-column
          prop="thistime"
          label="提交时间"
          min-width="160">
          <template slot-scope="scope">
            {{ scope.row.thistime | formatDate }}
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="审核状态"
          min-width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          min-width="200"
          align="center">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="small"
              @click="viewDetails(scope.row)">
              查看详情
            </el-button>
            <el-button
              v-if="scope.row.status === '待审核'"
              type="success"
              size="small"
              @click="approveProject(scope.row)">
              审核
            </el-button>
            <el-button
              v-if="scope.row.status === '已通过'"
              type="warning"
              size="small"
              @click="goToArchive(scope.row)">
              归档
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="!loading && projects.length === 0" class="empty-block">
        <el-empty description="暂无申报记录"></el-empty>
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      projects: [],
      loading: false
    };
  },
  methods: {
    // 获取项目列表
    async fetchProjects() {
      try {
        this.loading = true
        const response = await axios.get('/api/Returns')

        console.log('获取到的项目列表:', response.data.data)
        this.projects = response.data.data || []

      } catch (error) {
        console.error('获取项目列表失败:', error)
        this.$message.error('获取项目列表失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 查看详情
    viewDetails(project) {
      // 将项目数据存储到 sessionStorage
      sessionStorage.setItem('currentProject', JSON.stringify(project))
      this.$router.push('/admin-home/project-details')
    },

    // 审核项目
    async approveProject(project) {
      try {
        await this.$confirm('确定要审核这个项目吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await axios.post('/api/Returns/review', {
          id: project.id,
          status: '已通过' // 1表示审核通过
        })

        if (response.data) {
          this.$message.success('审核成功')
          // 重新获取列表
          await this.fetchProjects()
        } else {
          this.$message.error('审核失败，请稍后重试')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('审核项目失败:', error)
          this.$message.error('审核失败，请稍后重试')
        }
      }
    },
    navigateTo(path) {
      this.$router.push(path);
    },
    goToArchive(project) {

let query= {
     id: project.id,
     projectName: project.projectname,
     submitter: project.applicantinfo,
     submissionDate: project.thistime
   }

 console.log(project)

 this.$router.push({
   path: '/admin-home/archive-page',
   query: {
     id: project.id,
     projectName: project.projectname,
     submitter: project.applicantinfo,
     submissionDate: project.thistime
   }
 });
},
    getStatusType (status) {
      const statusMap = {
        '待审核': 'warning',
        '已通过': 'success',
        '已归档': 'info',
        '已驳回': 'danger',
        '已拒绝': 'danger'
      }
      return statusMap[status] || ''
    },
  },
  created() {
    this.fetchProjects()
  },
  filters: {
    formatDate(value) {
      // 格式化日期显示
      const date = new Date(value);
      return date.toLocaleString();
    }
  }
};
</script>

<style scoped>
.review-status-page {
  width: 100%;
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 60px);
}

.box-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.empty-block {
  margin: 40px 0;
}

.el-button {
  margin: 0 5px;
}

.el-button + .el-button {
  margin-left: 5px;
}
</style>
