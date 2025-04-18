<template>
  <div class="user-manager">
    <el-card class="box-card">
      <div slot="header" class="card-header">
        <h2>审批人管理</h2>
      </div>

      <el-table :data="users" stripe border style="width: 100%">
        <el-table-column prop="name" label="用户名" min-width="120">
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" min-width="120">
        </el-table-column>
        <el-table-column prop="college" label="学院" min-width="200">
        </el-table-column>
        <el-table-column prop="major" label="专业" min-width="200">
        </el-table-column>
        <el-table-column prop="role" label="权限" min-width="200">
          <template slot-scope="scope">
            <el-tag type="success">{{ scope.row.role }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button size="small" type="primary" @click="passwordupdate(scope.row)">
              重置密码
            </el-button>

          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 权限编辑对话框 -->
    <el-dialog title="密码重置" :visible.sync="isEditing" width="40%">
      <h3> 是否将密码重置为： 123123</h3>

      <span slot="footer" class="dialog-footer">
        <el-button @click="closeModal">取 消</el-button>
        <el-button type="primary" @click="savePermissions()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import axios from 'axios'

export default {
  data() {
    return {
      roles: ['管理员', '编辑者', '查看者'], // 角色列表
      users: [
        // 虚拟用户数据
        {
          name: 'Alice',
          duty: '前端开发',
          permissions: ['查看项目', '编辑代码'],
          selectedRoles: ['查看者', '编辑者'] // 初始选择的角色
        },
        {
          name: 'Bob',
          duty: '后端开发',
          permissions: ['查看项目', '部署服务器'],
          selectedRoles: ['管理员'] // 初始选择的角色
        },
        // ...更多用户数据
      ],
      allPermissions: ['查看报表', '编辑用户', '删除用户', '导出数据'], // 所有可能的权限列表
      isEditing: false,
      editingUser: {},
      selectedPermissions: [],
      thisid:"",
    };
  },
  methods: {
    updateUserRole(user, role) {
      // 这里可以添加逻辑来处理角色变更，比如发送请求到服务器
      console.log(`用户 ${user.name} 的角色已更新，现在包含：${user.selectedRoles.join(', ')}`);
    },
    passwordupdate(row) {
      this.isEditing = true
      this.thisid = row.id;
    },
    editPermissions(user) {
      this.isEditing = true;
      this.editingUser = { ...user }; // 深拷贝用户数据以避免直接修改原始数据
      this.selectedPermissions = [...user.permissions]; // 初始化选中的权限
    },
    closeModal() {
      this.isEditing = false;
      this.editingUser = {};
      this.selectedPermissions = [];
    },
    async savePermissions() {
      // 更新用户的权限

      const response = await axios.get(`/api/updatePassword/` + this.thisid)
      this.$message.success('重置密码成功')

      // 关闭模态框
      this.closeModal();

    },
    async info() {
      const response = await axios.get(`/api/list`)
      if (response.data.code === 200) {

        console.log(response.data.data)
        this.users = response.data.data;
      } else {
        this.$message.error(response.data.message || '项目更新失败')
      }
    },

  },
  mounted() {
    this.info();

  }
};
</script>

<style scoped>
.user-manager {
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

.el-tag {
  margin-right: 5px;
}

.el-checkbox {
  margin-right: 15px;
  margin-left: 0;
}
</style>
