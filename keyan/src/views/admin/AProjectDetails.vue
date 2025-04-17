<template>
    <div class="project-details-page">
      <h1>项目详情</h1>
      <div class="details-container">
        <h2>基本信息</h2>
        <p><strong>项目名称:</strong> {{ project.projectname }}</p>
        <p><strong>提交人:</strong> {{ project.applicantinfo }}</p>
        <p><strong>提交时间:</strong> {{ project.thistime }}</p>
        <p><strong>审核状态:</strong> {{ project.status }}</p>
        
        <!-- 项目详细信息 -->
        <h2>项目信息</h2>
        <p><strong>项目类型:</strong> {{ project.projecttype }}</p>
        <p><strong>所属学科:</strong> {{ project.subject }}</p>
        <p><strong>研究方向:</strong> {{ project.researchfield }}</p>
        <p><strong>项目来源:</strong> {{ project.source }}</p>
        
        <h2>项目内容</h2>
        <p><strong>项目简介:</strong></p>
        <div class="content-box">{{ project.introduction }}</div>
        
        <p><strong>研究内容:</strong></p>
        <div class="content-box">{{ project.researchcontent }}</div>
        
        <p><strong>预期成果:</strong></p>
        <div class="content-box">{{ project.expectedresults }}</div>

        <!-- 附件下载区域 -->
        <h2>附件材料</h2>
        <div class="attachments" v-if="project.attachments">
          <a 
            :href="project.attachments" 
            target="_blank" 
            class="download-btn"
          >
            下载附件
          </a>
        </div>

        <!-- 审核操作区域 -->
        <div class="review-actions" v-if="project.status === '待审核'">
          <button class="approve-btn" @click="handleApprove">通过审核</button>
          <button class="reject-btn" @click="handleReject">拒绝审核</button>
        </div>

        <!-- 返回按钮 -->
        <button class="back-btn" @click="goBack">返回列表</button>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
  
  export default {
    data() {
      return {
        project: {}
      }
    },
    methods: {
      // 获取项目详情
      loadProjectDetails() {
        const projectData = sessionStorage.getItem('currentProject')
        if (projectData) {
          this.project = JSON.parse(projectData)
        } else {
          this.$message.error('未找到项目信息')
          this.goBack()
        }
      },

      // 通过审核
      async handleApprove() {
        try {
          await this.$confirm('确定通过该项目的审核吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })

          const response = await axios.post('/api/Returns/review', {
            id: this.project.id,
            status: '已通过' // 1表示审核通过
          })

          if (response.data) {
            this.$message.success('审核通过成功')
            this.goBack()
          }
        } catch (error) {
          if (error !== 'cancel') {
            console.error('审核失败:', error)
            this.$message.error('审核失败，请稍后重试')
          }
        }
      },

      // 拒绝审核
      async handleReject() {
        try {
          await this.$confirm('确定拒绝该项目的审核吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })

          const response = await axios.post('/api/Returns/review', {
            id: this.project.id,
            status: '未通过' // 2表示审核拒绝
          })

          if (response.data) {
            this.$message.success('已拒绝该项目')
            this.goBack()
          }
        } catch (error) {
          if (error !== 'cancel') {
            console.error('操作失败:', error)
            this.$message.error('操作失败，请稍后重试')
          }
        }
      },

      // 返回列表页
      goBack() {
        this.$router.push('/admin-home/review-status')
      }
    },
    created() {
      this.loadProjectDetails()
    }
  };
  </script>
  
  <style scoped>
  .project-details-page {
    width: 80%;
    margin: 0 auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  }
  
  .details-container {
    margin-top: 20px;
  }
  
  h1 {
    color: #303133;
    margin-bottom: 30px;
  }
  
  h2 {
    color: #409EFF;
    margin: 25px 0 15px 0;
    padding-bottom: 10px;
    border-bottom: 1px solid #EBEEF5;
  }
  
  p {
    margin: 12px 0;
    line-height: 1.6;
  }
  
  strong {
    color: #606266;
    margin-right: 10px;
  }
  
  .content-box {
    padding: 15px;
    background-color: #F8F9FA;
    border-radius: 4px;
    margin: 10px 0;
    line-height: 1.6;
    white-space: pre-wrap;
  }
  
  .review-actions {
    margin: 30px 0;
    padding: 20px 0;
    border-top: 1px solid #EBEEF5;
  }
  
  button {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-right: 15px;
    font-size: 14px;
  }
  
  .approve-btn {
    background-color: #67C23A;
    color: white;
  }
  
  .reject-btn {
    background-color: #F56C6C;
    color: white;
  }
  
  .back-btn {
    background-color: #409EFF;
    color: white;
  }
  
  .download-btn {
    display: inline-block;
    padding: 10px 20px;
    background-color: #409EFF;
    color: white;
    text-decoration: none;
    border-radius: 4px;
  }
  
  button:hover, .download-btn:hover {
    opacity: 0.9;
  }
  </style>