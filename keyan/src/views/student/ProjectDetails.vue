<template>
  <div class="project-details-page">
    <h1>项目详情</h1>
    <div class="details-container" v-if="project">
      <h2>基本信息</h2>
      <div class="info-section">
        <p><strong>项目名称：</strong>{{ project.projectname }}</p>
        <p><strong>申请人：</strong>{{ project.applicantinfo }}</p>
        <p><strong>提交时间：</strong>{{ project.thistime }}</p>
        <p><strong>项目类型：</strong>{{ project.type }}</p>
        <p><strong>审核状态：</strong>{{ project.status }}</p>
      </div>

      <h2>项目内容</h2>
      <div class="info-section">
        <p><strong>项目组成员：</strong>{{ project.teamMembers }}</p>
        <p><strong>研究目的和意义：</strong>{{ project.purposeandsignificance }}</p>
        <p><strong>已有研究综述：</strong>{{ project.existingresearchreview }}</p>
        <p><strong>研究内容：</strong>{{ project.researchcontent }}</p>
        <p><strong>技术路线：</strong>{{ project.technicalroute }}</p>
        <p><strong>预期成果：</strong>{{ project.expectedoutcomes }}</p>
        <p><strong>经费预算：</strong>{{ project.budget }}</p>
        <p><strong>成员分工：</strong>{{ project.memberdivision }}</p>
        <p><strong>参考文献：</strong>{{ project.myreferences }}</p>
        <p><strong>研究基础：</strong>{{ project.researchbasis }}</p>
      </div>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button type="primary" @click="goBack">返回</el-button>
        <el-button 
          type="danger" 
          v-if="project.status === '待审核'"
          @click="withdrawProject"
        >
          撤回申请
        </el-button>
      </div>
    </div>

    <!-- 加载提示 -->
    <div v-else class="loading">
      <el-loading-text>加载中...</el-loading-text>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      project: null
    }
  },
  methods: {
    // 获取项目详情
    loadProjectDetails() {
      const projectData = sessionStorage.getItem('currentProject')
      if (projectData) {
        try {
          this.project = JSON.parse(projectData)
        } catch (error) {
          console.error('解析项目数据失败:', error)
          this.$message.error('加载项目数据失败')
        }
      } else {
        this.$message.error('未找到项目数据')
        this.goBack()
      }
    },

    // 返回上一页
    goBack() {
      this.$router.push('/student-home/review-status')
    },

    // 撤回项目
    async withdrawProject() {
      try {
        await this.$confirm('确定要撤回该项目吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await axios.post('/api/Returns/withdraw', {
          id: this.project.id,
          status: '已撤回'
        })

        if (response.data) {
          this.$message.success('项目已成功撤回')
          this.goBack()
        } else {
          this.$message.error('撤回失败，请稍后重试')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('撤回项目失败:', error)
          this.$message.error('撤回失败，请稍后重试')
        }
      }
    }
  },
  created() {
    this.loadProjectDetails()
  }
}
</script>

<style scoped lang="less">
.project-details-page {
  width: 90%;
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  h1 {
    color: #303133;
    margin-bottom: 30px;
    text-align: center;
  }

  h2 {
    color: #409EFF;
    margin: 20px 0;
    padding-bottom: 10px;
    border-bottom: 2px solid #EBEEF5;
  }

  .info-section {
    padding: 20px;
    background-color: #F8F9FA;
    border-radius: 4px;
    margin-bottom: 20px;

    p {
      margin: 10px 0;
      line-height: 1.6;
      color: #606266;

      strong {
        color: #303133;
        margin-right: 10px;
        min-width: 120px;
        display: inline-block;
      }
    }
  }

  .action-buttons {
    margin-top: 30px;
    text-align: center;

    .el-button {
      margin: 0 10px;
      padding: 12px 25px;
    }
  }

  .loading {
    text-align: center;
    margin: 50px 0;
    color: #909399;
  }
}

@media screen and (max-width: 768px) {
  .project-details-page {
    width: 95%;
    padding: 15px;

    .info-section {
      padding: 15px;

      p strong {
        min-width: 100px;
      }
    }
  }
}
</style>