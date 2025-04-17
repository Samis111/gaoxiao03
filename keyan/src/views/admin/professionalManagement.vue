<template>
    <div class="user-manager">
        <el-card class="box-card">
            <div slot="header" class="card-header">
                <h2>专业管理</h2>
            </div>

            <el-row>
                <el-button type="primary" @click="add()">添加</el-button>
            </el-row>

            <el-table :data="users" stripe border style="width: 100%">
                <el-table-column prop="id" label="ID信息" min-width="120">
                </el-table-column>

                <el-table-column prop="type" label="学院名称" min-width="120">
                </el-table-column>
                <el-table-column prop="name" label="专业名称" min-width="120">
                </el-table-column>

                <el-table-column label="操作" width="120" align="center">
                    <template slot-scope="scope">
                        <el-button size="small" type="primary" @click="editPermissions(scope.row)">
                            修改
                        </el-button>
                        <el-button size="small" type="danger" @click="del(scope.row)">
                            删除
                        </el-button>

                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <!-- 权限编辑对话框 -->
        <el-dialog title="编辑权限" :visible.sync="isEditing" width="40%">

            <el-form :model="editingUser">
                <el-form-item label="学院" :label-width="formLabelWidth">

                    <el-select v-model="editingUser.type" placeholder="学院" :disabled="flag">
                        <el-option v-for="item in options" :key="item.speciality" :label="item.speciality"
                            :value="item.speciality">
                        </el-option>
                    </el-select>

                </el-form-item>

                <el-form-item label="专业" :label-width="formLabelWidth">
                    <el-input v-model="editingUser.name" autocomplete="off"></el-input>
                </el-form-item>

            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="closeModal">取 消</el-button>
                <el-button type="primary" @click="savePermissions">确 定</el-button>
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
            users: [],
            isEditing: false,
            editingUser: {},
            selectedPermissions: [],
            options: [],
            flag: true,
            formLabelWidth: '120px'
        };
    },
    methods: {
        updateUserRole(user, role) {
            // 这里可以添加逻辑来处理角色变更，比如发送请求到服务器
            console.log(`用户 ${user.name} 的角色已更新，现在包含：${user.selectedRoles.join(', ')}`);
        },
        editPermissions(user) {
            this.flag = true;
            this.isEditing = true;
            this.editingUser = { ...user }; // 深拷贝用户数据以避免直接修改原始数据
        },
        closeModal() {
            this.isEditing = false;
            this.editingUser = {};
            this.selectedPermissions = [];
        },
        savePermissions() {

            if (this.editingUser.id == null) {
                this.save(this.editingUser);
            } else {
                this.update(this.editingUser);
            }
            this.closeModal();

        },
        add() {
            this.isEditing = true;
            this.editingUser = {};
            this.flag = false;
        },
        async info() {
            const response = await axios.get(`/api/university/list`)
            if (response.data.code === 200) {


                console.log(response.data.data)
                this.users = response.data.data;
            } else {
                this.$message.error(response.data.message || '项目更新失败')
            }
        },
        async infooptions() {
            const response = await axios.get(`/api/speciality/list`)
            if (response.data.code === 200) {


                console.log(response.data.data)
                this.options = response.data.data;
            } else {
                this.$message.error(response.data.message || '项目更新失败')
            }
        },
        async del(row) {
            const response = await axios.post(`/api/university/delete/` + row.id)
            if (response.data.code === 200) {
                this.$message.success('删除成功')

                this.info();
            } else {
                this.$message.error(response.data.message || '删除失败')
            }
        },
        async save(row) {
            const response = await axios.post(`/api/university/save`, row)
            if (response.data.code === 200) {
                this.$message.success('添加成功')
                this.closeModal();
                this.info();

            } else {
                this.$message.error(response.data.message || '删除失败')
            }
        },
        async update(row) {
            const response = await axios.post(`/api/university/update`, row)
            if (response.data.code === 200) {
                this.$message.success('修改成功')
                this.closeModal();
                this.info();
            } else {
                this.$message.error(response.data.message || '删除失败')
            }

        },
    },
    mounted() {
        this.info();
        this.infooptions();
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