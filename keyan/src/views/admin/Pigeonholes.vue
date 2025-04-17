<template>
    <div class="archive-container">
      <h1 class="archive-title">科研归档详情</h1>
      <div class="archive-content">
        <!-- 提交时间 -->
        <div class="archive-item">
          <span class="label">提交时间：</span>
          <span class="value">{{ submissionDate }}</span>
        </div>
  
        <!-- 提交人 -->
        <div class="archive-item">
          <span class="label">提交人：</span>
          <span class="value">{{ submitter }}</span>
        </div>
  
        <!-- 上传文档 -->
        <div class="archive-item">
          <span class="label">上传文档：</span>
          <input type="file" @change="handleFileUpload" class="file-input" />
          <span v-if="uploadedFile" class="file-name">{{ uploadedFile.name }}</span>
        </div>
  
        <!-- 下载文档 -->
        <div class="archive-item">
          <span class="label">下载文档：</span>
          <button @click="downloadFile" class="download-button" :disabled="!uploadedFile">
            下载文档
          </button>
        </div>
  
        <!-- 科研描述 -->
        <div class="archive-item">
          <span class="label">科研描述：</span>
          <p class="research-description">{{ researchDescription }}</p>
        </div>

        <!-- 时间线 -->
        <div class="archive-item">
          <span class="label">项目进程：</span>
          <div class="timeline">
            <div v-for="(item, index) in timelineItems" :key="index" class="timeline-item">
              <div class="timeline-point"></div>
              <div class="timeline-content">
                <div class="timeline-date">{{ item.date }}</div>
                <div class="timeline-title">{{ item.title }}</div>
                <div class="timeline-description">{{ item.description }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
import axios from 'axios';

export default {
  props: {
    id: {
      type: [String, Number],
      default: ''
    },
    projectName: {
      type: String,
      default: ''
    },
    submitter: {
      type: String,
      default: ''
    },
    submissionDate: {
      type: String,
      default: ''
    }
  },

  data() {
    return {
      submissionDate: this.submissionDate || '2023-10-15',
      submitter: this.submitter || '张三',
      uploadedFile: null,
      researchDescription: '这是一个关于人工智能在医疗领域应用的科研项目，主要研究如何利用深度学习技术提高疾病诊断的准确性。',
      timelineItems: [
        {
          date: '2023-10-15',
          title: '项目启动',
          description: '项目正式启动，确定研究方向和目标'
        },
        {
          date: '2023-12-01',
          title: '初步研究',
          description: '完成文献综述和初步实验'
        },
        {
          date: '2024-02-15',
          title: '中期报告',
          description: '提交项目中期报告，展示阶段性成果'
        }
      ]
    };
  },

  created() {
    console.log('接收到的项目信息：', {
      id: this.id,
      projectName: this.projectName,
      submitter: this.submitter,
      submissionDate: this.submissionDate
    });
  },

  mounted() {
    this.initializeData();
  },

  methods: {
    async initializeData() {
      try {
        if (this.id) {
          const response = await axios.get(`/api/projects/${this.id}`);
          if (response.data.code === 200) {
            const projectData = response.data.data;
            
            this.researchDescription = projectData.subjectproject;
            
            await this.fetchProjectDocuments();
          } else {
            this.$message.error('获取项目信息失败');
          }
        }
      } catch (error) {
        console.error('初始化数据失败:', error);
        this.$message.error('加载项目信息失败，请稍后重试');
      }
    },

    async fetchProjectDocuments() {
      try {
        const response = await axios.get(`/api/projects/${this.id}/documents`);
        if (response.data.code === 200) {
          const documents = response.data.data;
          if (documents && documents.length > 0) {
            this.updateTimelineWithDocuments(documents);
          }
        }
      } catch (error) {
        console.error('获取项目文档失败:', error);
      }
    },

    updateTimelineWithDocuments(documents) {
      const documentTimeline = documents.map(doc => ({
        date: doc.uploadTime,
        title: '文档上传',
        description: doc.description || '上传了新的项目文档'
      }));

      this.timelineItems = [...this.timelineItems, ...documentTimeline]
        .sort((a, b) => new Date(a.date) - new Date(b.date));
    },

    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        this.uploadedFile = file;
        console.log('文件已上传：', file.name);
      }
    },
    
    downloadFile() {
      if (this.uploadedFile) {
        const url = URL.createObjectURL(this.uploadedFile);
        const link = document.createElement('a');
        link.href = url;
        link.download = this.uploadedFile.name;
        link.click();
        URL.revokeObjectURL(url);
        console.log('文件已下载：', this.uploadedFile.name);
      }
    }
  }
};
</script>
<style scoped lang="less">
.archive-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.archive-title {
  text-align: center;
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.archive-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.archive-item {
  display: flex;
  align-items: center;
  font-size: 16px;
}

.label {
  font-weight: bold;
  color: #555;
  min-width: 100px;
}

.value {
  color: #333;
}

.file-input {
  margin-left: 10px;
}

.file-name {
  margin-left: 10px;
  color: #007bff;
}

.download-button {
  margin-left: 10px;
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.download-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.research-description {
  margin: 0;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  color: #333;
  line-height: 1.6;
}

.timeline {
  position: relative;
  margin-left: 20px;
  padding-left: 30px;

  &::before {
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    width: 2px;
    background-color: #007bff;
  }

  .timeline-item {
    position: relative;
    margin-bottom: 30px;

    &:last-child {
      margin-bottom: 0;
    }

    .timeline-point {
      position: absolute;
      left: -34px;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background-color: #007bff;
      border: 2px solid #fff;
    }

    .timeline-content {
      background-color: #fff;
      padding: 15px;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

      .timeline-date {
        color: #007bff;
        font-weight: bold;
        margin-bottom: 5px;
      }

      .timeline-title {
        font-weight: bold;
        margin-bottom: 5px;
      }

      .timeline-description {
        color: #666;
        font-size: 14px;
      }
    }
  }
}
</style>