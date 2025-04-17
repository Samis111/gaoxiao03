<template>
    <div class="project-management-page">
      <el-card class="box-card">
        <div slot="header" class="card-header">
          <h2>项目管理</h2>
          <div class="header-operations">
            <el-input
              v-model="searchQuery"
              placeholder="搜索项目"
              prefix-icon="el-icon-search"
              style="width: 300px; margin-right: 20px;"
            />
            <el-button type="primary" @click="showAddModal">
              <i class="el-icon-plus"></i> 添加项目
            </el-button>
          </div>
        </div>

        <el-table
          :data="filteredProjects"
          stripe
          border
          style="width: 100%">
          <el-table-column
            prop="type"
            label="分类"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="subjectproject"
            label="研究内容"
            min-width="200">
          </el-table-column>
          <el-table-column
            prop="result"
            label="预期成果"
            min-width="200">
          </el-table-column>
          <el-table-column
            prop="thistime"
            label="发布时间"
            min-width="160">
          </el-table-column>
          <el-table-column
            label="操作"
            width="250"
            align="center">
            <template slot-scope="scope">
              <el-button
                size="small"
                type="primary"
                @click="showEditModal(scope.row)">
                修改
              </el-button>
              <el-button
                size="small"
                type="danger"
                @click="confirmDelete(scope.row.id)">
                删除
              </el-button>
              <el-button
                size="small"
                type="warning"
                @click="goToArchive(scope.row)">
                文档
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 添加/编辑项目对话框 -->
      <el-dialog
        :title="isEditing ? '修改项目' : '添加项目'"
        :visible.sync="showModal"
        width="50%">
        <el-form
          :model="currentProject"
          label-width="100px"
          :rules="rules"
          ref="projectForm">
          <el-form-item label="分类" prop="type">
            <el-input v-model="currentProject.type"></el-input>
          </el-form-item>
          <el-form-item label="研究内容" prop="subjectproject">
            <el-input
              type="textarea"
              :rows="4"
              v-model="currentProject.subjectproject">
            </el-input>
          </el-form-item>
          <el-form-item label="预期成果" prop="result">
            <el-input
              type="textarea"
              :rows="4"
              v-model="currentProject.result">
            </el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="closeModal">取 消</el-button>
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </template>

  <script>
  import axios from 'axios'

  export default {
    data() {
      return {
        showModal: false,
        isEditing: false,
        searchQuery: '',
        currentProject: {
          type: '',
          subjectproject: '',
          result: '',
          // 发布时间不需要在这里添加，因为它会自动生成
        },
        projects: [],
        rules: {
          type: [
            { required: true, message: '请输入分类', trigger: 'blur' }
          ],
          subjectproject: [
            { required: true, message: '请输入研究内容', trigger: 'blur' }
          ],
          result: [
            { required: true, message: '请输入预期成果', trigger: 'blur' }
          ]
        }
      };
    },
    computed: {
      filteredProjects() {
        return this.projects.filter(project =>
          project.type.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          project.subjectproject.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          project.result.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
      },
    },
    methods: {
      // 获取所有项目
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
          this.$message.error('获取项目列表失败')
        }
      },
      goToArchive(project) {
        // 设置当前选中的行数据
        this.$parent.setCurrentRow({
          id: project.id,
          projectName: project.type, // 使用 type 作为项目名称
          submitter: project.submitter || '未知', // 如果没有提交人信息则显示"未知"
          submissionDate: project.thistime // 使用发布时间作为提交时间
        });

        // 导航到归档页面
        this.$router.push({
          path: '/admin-home/archive-page',
          query: {
            id: project.id,
            projectName: project.type,
            submitter: project.submitter || '未知',
            submissionDate: project.thistime
          }
        });
      },
      // 显示添加模态框
      showAddModal() {
        this.isEditing = false
        this.currentProject = {
          type: '',
          subjectproject: '',
          result: ''
        }
        this.showModal = true
      },

      // 显示编辑模态框
      showEditModal(project) {
        this.isEditing = true
        this.currentProject = { ...project }
        this.showModal = true
      },

      // 关闭模态框
      closeModal() {
        this.showModal = false
        this.currentProject = {
         type: '',
          subjectproject: '',
          result: ''
        }
      },

      // 处理表单提交
      async handleSubmit() {
        try {
          if (this.isEditing) {
            // 更新项目
            const response = await axios.put(`/api/projects/${this.currentProject.id}`, this.currentProject)
            if (response.data.code === 200) {
              this.$message.success('项目更新成功')
              await this.fetchProjects() // 重新获取项目列表
            } else {
              this.$message.error(response.data.message || '项目更新失败')
            }
          } else {
            // 添加项目
            const response = await axios.post('/api/projects', this.currentProject)
            if (response.data.code === 200) {
              this.$message.success('项目添加成功')
              await this.fetchProjects() // 重新获取项目列表
            } else {
              this.$message.error(response.data.message || '项目添加失败')
            }
          }
          this.closeModal()
        } catch (error) {
          console.error('提交项目数据错误:', error)
          this.$message.error('操作失败，请稍后重试')
        }
      },

      // 确认删除
      confirmDelete(projectId) {
        this.$confirm('确认删除该项目?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteProject(projectId)
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      // 删除项目
      async deleteProject(projectId) {
        try {
          const response = await axios.delete(`/api/projects/${projectId}`)
          if (response.data.code === 200) {
            this.$message.success('删除成功')
            await this.fetchProjects() // 重新获取项目列表
          } else {
            this.$message.error(response.data.message || '删除失败')
          }
        } catch (error) {
          console.error('删除项目错误:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      },
      handleRowClick(row) {
        // 通过ref获取AdminHome组件实例并设置当前行
        this.$parent.$refs.adminHome.setCurrentRow(row);
      }
    },

    // 组件创建时获取项目列表
    created() {
      this.fetchProjects()
    }
  };
  </script>

  <style scoped>
  .project-management-page {
    padding: 20px;
    width: 100%;
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

  .header-operations {
    display: flex;
    align-items: center;
  }

  .el-button {
    margin: 0 5px;
  }

  .el-button + .el-button {
    margin-left: 5px;
  }
  </style>
