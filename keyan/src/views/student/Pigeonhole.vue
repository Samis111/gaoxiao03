<template>
    <div class="archive-container">
      <div class="header-section">
        <h1 class="archive-title">科研归档详情</h1>
        <div class="status-badge">进行中</div>
      </div>
 <div class="description-section">
          <h2 class="section-title">科研描述</h2>
          <div class="research-description">
            {{ researchDescription }}
          </div>
        </div>
   <!-- 时间线 -->
        <div class="timeline-section">
          <h2 class="section-title">项目进程</h2>
          <div class="timeline">
            <div v-for="(item, index) in timelineItems" :key="index" class="timeline-item" :class="{ 'completed': index < currentStep }">
              <div class="timeline-point"></div>
              <div class="timeline-content">
                <div class="timeline-date">{{item.name }}</div>
                <div class="timeline-title">{{ item.node }}</div>
                <div class="timeline-description">{{ item.apptime }}</div>
              </div>
            </div>
          </div>
        </div>


      <div class="archive-content">
      
        <!-- 提交时间 -->
        <div class="info-section">
          <div class="info-grid">
            <div class="info-item">
              <i class="fas fa-calendar info-icon"></i>
              <div class="info-content">
                <div class="info-label">提交时间</div>
                <div class="info-value">{{ submissionDate }}</div>
              </div>
            </div>
  
            <!-- 提交人 -->
            <div class="info-item">
              <i class="fas fa-user info-icon"></i>
              <div class="info-content">
                <div class="info-label">提交人</div>
                <div class="info-value">{{ submitter }}</div>
              </div>
            </div>
          </div>
        </div>
  
        <!-- 上传文档 -->
        <div class="document-section">
          <h2 class="section-title">文档管理</h2>
          <div class="document-actions">
            <div class="upload-area" @click="triggerFileInput" :class="{ uploading: isUploading }">
              <i class="fas fa-cloud-upload-alt"></i>
              <span>点击上传文档</span>
              <input type="file" ref="fileInput" @change="handleFileUpload" class="file-input" />
            </div>
          </div>
          
          <!-- 新增文本框和提交按钮 -->
          <div class="document-form">
            <div class="form-group">
              <label>文档说明：</label>
              <textarea 
                v-model="documentDescription" 
                placeholder="请输入文档说明..."
                class="document-textarea"
              ></textarea>
            </div>
            <button 
              @click="submitDocument" 
              class="submit-button"
              :disabled="!canSubmit"
            >
              <i class="fas fa-paper-plane"></i>
              提交文档
            </button>
          </div>

          <div v-if="uploadedFile" class="uploaded-file">
            <i class="fas fa-file-alt"></i>
            <span>{{ uploadedFile.name }}</span>
          </div>

          <div v-if="isUploading" class="upload-progress">
            <div class="progress-bar"></div>
          </div>

          <div v-if="uploadStatus" class="upload-status" :class="uploadStatus">
            {{ uploadStatus === 'success' ? '上传成功' : uploadStatus }}
          </div>
        </div>
  
        
       
  
     
      </div>
    </div>
  </template>
  
  <script>
import { mapState } from 'vuex';
import axios from 'axios';

export default {
  data() {
    return {
      submissionDate: '2023-10-15', // 提交时间
      submitter: '张三', // 提交人
      uploadedFile: null, // 上传的文件
      currentStep: 2, // 当前进度，用于时间线显示
      researchDescription: '', // 科研描述
      timelineItems: [], // Initialize as empty array
      documentDescription: '', // Add this line to fix the previous error
      currentRow: null,
      id: null,
    };
  },

  computed: {
    ...mapState('document', ['uploadStatus', 'uploadProgress']),

    isUploading() {
      return this.uploadStatus === 'uploading';
    },

    canSubmit() {
      return this.uploadedFile && 
             this.documentDescription.trim() && 
             !this.isUploading;
    }
  },

  created() {
    // 从路由参数获取项目信息
    const { id, projectName, submitter, submissionDate } = this.$route.query;
    this.id = id;

    
    // 如果没有接收到项目信息，从 sessionStorage 获取
    if (!id) {
      const currentProject = JSON.parse(sessionStorage.getItem('currentProject') || '{}');
      if (currentProject.id) {
        this.currentRow = currentProject;
      } else {
        this.$message.warning('未找到项目信息');
      }
    }

    // 初始化项目描述
    this.researchDescription = `项目名称：${projectName || '未知项目'}\n提交人：${submitter || '未知'}\n提交时间：${submissionDate || '未知时间'}`;
    
    // 初始化页面数据
    this.initializeData();

console.log('接收到的项目信息：', {
      id,
      projectName,
      submitter,
      submissionDate
    });


  },

  methods: {
    // 初始化页面数据
    async initializeData() {
      try {
 
        if (this.id) {
          // 获取项目详细信息

    

          const response = await axios.get(`/api/Returns/${this.id}`);
          if (response.data.code === 200) {
            const projectData = response.data.data;
            
            // 更新项目描述
            this.researchDescription = projectData.subjectproject;
            
            // 获取项目文档和进度信息
            await this.fetchTimelineData();
          } else {
            this.$message.error('获取项目信息失败');
          }
        }
      } catch (error) {
        console.error('初始化数据失败:', error);
        this.$message.error('加载项目信息失败，请稍后重试');
      }
    },

    // 获取时间线数据
    async fetchTimelineData() {
      try {
        const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
        const response = await axios.get(`/api/Process/${this.id}/${userInfo.id}`);
        
        if (response.data && response.data.data) {
          // 转换API数据为时间线格式
          this.timelineItems = response.data.data;
          
          // 更新当前进度
          this.currentStep = this.timelineItems.length;
        }
      } catch (error) {
        console.error('获取时间线数据失败:', error);
        this.$message.error('获取项目进度信息失败');
      }
    },

    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        // 检查文件类型和大小
        const allowedTypes = ['application/pdf', 'application/msword', 
                            'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
        const maxSize = 10 * 1024 * 1024; // 10MB

        if (!allowedTypes.includes(file.type)) {
          this.$message.error('只支持 PDF 和 Word 文档格式');
          return;
        }

        if (file.size > maxSize) {
          this.$message.error('文件大小不能超过 10MB');
          return;
        }

        this.uploadedFile = file;
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
    },
    async submitDocument() {
      if (!this.canSubmit) return;

    const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
          if (!userInfo.id) {
            this.$message.error('未找到用户信息，请重新登录')
            return;
          }



      try {
        const result = await this.$store.dispatch('document/uploadDocument', {
          file: this.uploadedFile,
          description: this.documentDescription,
          uid:userInfo.id,
          id:this.id,
          submitter:this.submitter
        });





        if (result) {
          this.$message.success('文档上传成功！');
          // 清空表单
          this.uploadedFile = null;
          this.documentDescription = '';
          this.$refs.fileInput.value = ''; // 清空文件输入框
        }
      } catch (error) {
        console.error('上传文档失败：', error);
        this.$message.error(error.message || '文档上传失败，请重试');
      }
    }
  }
};
</script>
<style scoped lang="less">
.archive-container {
  max-width: 1000px;
  margin: 20px auto;
  padding: 30px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.header-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;

  .archive-title {
    font-size: 28px;
    color: #2c3e50;
    margin: 0;
  }

  .status-badge {
    padding: 6px 16px;
    background-color: #4CAF50;
    color: white;
    border-radius: 20px;
    font-size: 14px;
  }
}

.section-title {
  font-size: 20px;
  color: #2c3e50;
  margin-bottom: 20px;
  font-weight: 600;
}

.info-section {
  margin-bottom: 30px;
  
  .info-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
  }

  .info-item {
    display: flex;
    align-items: center;
    padding: 15px;
    background-color: #f8f9fa;
    border-radius: 8px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    }

    .info-icon {
      font-size: 24px;
      color: #007bff;
      margin-right: 15px;
    }

    .info-content {
      .info-label {
        font-size: 14px;
        color: #6c757d;
        margin-bottom: 4px;
      }

      .info-value {
        font-size: 16px;
        color: #2c3e50;
        font-weight: 500;
      }
    }
  }
}

.document-section {
  margin-bottom: 30px;
  
  .document-actions {
    display: flex;
    gap: 20px;
    margin-bottom: 15px;
  }

  .upload-area {
    flex: 1;
    padding: 20px;
    border: 2px dashed #007bff;
    border-radius: 8px;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      background-color: #f8f9fa;
      border-color: #0056b3;
    }

    &.uploading {
      pointer-events: none;
      opacity: 0.8;
    }

    i {
      font-size: 24px;
      color: #007bff;
      margin-bottom: 8px;
    }
  }

  .file-input {
    display: none;
  }

  .document-form {
    margin-top: 20px;
    
    .form-group {
      margin-bottom: 15px;
      
      label {
        display: block;
        margin-bottom: 8px;
        color: #2c3e50;
        font-weight: 500;
      }
    }

    .document-textarea {
      width: 100%;
      min-height: 100px;
      padding: 12px;
      border: 1px solid #ddd;
      border-radius: 8px;
      resize: vertical;
      font-family: inherit;
      font-size: 14px;
      line-height: 1.5;
      transition: all 0.3s ease;

      &:focus {
        outline: none;
        border-color: #007bff;
        box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.1);
      }

      &::placeholder {
        color: #adb5bd;
      }
    }

    .submit-button {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 10px 24px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 8px;
      font-size: 16px;
      cursor: pointer;
      transition: all 0.3s ease;

      i {
        margin-right: 8px;
      }

      &:hover:not(:disabled) {
        background-color: #0056b3;
      }

      &:disabled {
        background-color: #ccc;
        cursor: not-allowed;
      }
    }
  }

  .uploaded-file {
    display: flex;
    align-items: center;
    padding: 10px;
    background-color: #e9ecef;
    border-radius: 4px;

    i {
      margin-right: 8px;
      color: #6c757d;
    }
  }

  .upload-progress {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 4px;
    background-color: #e9ecef;
    border-radius: 0 0 8px 8px;
    overflow: hidden;

    .progress-bar {
      height: 100%;
      background-color: #007bff;
      transition: width 0.3s ease;
      width: v-bind('uploadProgress + "%"');
    }
  }

  .upload-status {
    margin-top: 10px;
    padding: 8px 12px;
    border-radius: 4px;
    text-align: center;
    font-size: 14px;

    &.success {
      background-color: #d4edda;
      color: #155724;
    }

    &.error {
      background-color: #f8d7da;
      color: #721c24;
    }
  }
}

.description-section {
  .research-description {
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 8px;
    line-height: 1.6;
    color: #2c3e50;
  }
}

.timeline-section {
  margin-bottom: 40px;
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
    background-color: #e9ecef;
  }

  .timeline-item {
    position: relative;
    margin-bottom: 30px;

    &:last-child {
      margin-bottom: 0;
    }

    &.completed {
      .timeline-point {
        background-color: #28a745;
        border-color: #28a745;
      }

      &::before {
        background-color: #28a745;
      }
    }

    .timeline-point {
      position: absolute;
      left: -34px;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background-color: #fff;
      border: 2px solid #007bff;
      transition: all 0.3s ease;
    }

    .timeline-content {
      background-color: #fff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }

      .timeline-date {
        color: #007bff;
        font-weight: 600;
        margin-bottom: 8px;
      }

      .timeline-title {
        font-weight: 600;
        color: #2c3e50;
        margin-bottom: 8px;
      }

      .timeline-description {
        color: #6c757d;
        font-size: 14px;
        line-height: 1.5;
      }
    }
  }
}
</style>