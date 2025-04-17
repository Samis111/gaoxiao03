<template>
  <div>
    <Header>
      <div class="name">
        <span>名字</span>
      </div>
      <!-- <el-row class="demo-avatar demo-basic">
        <el-col :span="12">
          <div class="demo-basic--circle">
            <div class="block">
              <el-avatar :size="50" :src="circleUrl"></el-avatar>
            </div>
          </div>
        </el-col>
      </el-row> -->
      <div class="out">
        <span @click="logout">退出</span>
      </div>
    </Header>
    <br />
    <div class="container">
      <div class="NavMenuLifet">
        <el-radio-group v-model="isCollapse" style="margin-bottom: 20px">
          <el-radio-button :label="false">展开</el-radio-button>
          <el-radio-button :label="true">收起</el-radio-button>
        </el-radio-group>
        <el-menu
          default-active="1-4-1"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
          :collapse="isCollapse"
        >
          <el-menu-item index="1" @click="navigateTo('/admin-home/user-management')">
            <i class="el-icon-user-solid"></i>
            <span slot="title">用户管理</span>
          </el-menu-item>
          <el-menu-item
            index="2"
            @click="navigateTo('/admin-home/project-management')"
          >
            <i class="el-icon-s-cooperation"></i>
            <span slot="title">项目管理</span>
          </el-menu-item>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-s-claim"></i>
              <span slot="title">结题管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="3-1" @click="navigateTo('/admin-home/review-status')">状态</el-menu-item>
              <!-- <el-menu-item index="3-2" @click="navigateTo('/admin-home/archive-page')">归档</el-menu-item> -->
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="4">
            <template slot="title">
              <i class="el-icon-s-claim"></i>
              <span slot="title">关键词管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="4-1" @click="navigateTo('/admin-home/instituteManagement')">学院管理</el-menu-item>
              <!-- <el-menu-item index="3-2" @click="navigateTo('/admin-home/archive-page')">归档</el-menu-item> -->
            </el-menu-item-group>
            <el-menu-item-group>
              <el-menu-item index="4-2" @click="navigateTo('/admin-home/professionalManagement')">专业管理</el-menu-item>
              <!-- <el-menu-item index="3-2" @click="navigateTo('/admin-home/archive-page')">归档</el-menu-item> -->
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </div>
      <router-view  class="router-view"></router-view>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isCollapse: true,
      currentRow: null
    };
  },

  methods: {
    logout() {
    // 清除 sessionStorage 中的登录状态
    sessionStorage.removeItem('isLogin');
    sessionStorage.removeItem('userInfo');

    // 重定向到登录页
    this.$router.push('/');

    // 提示用户已退出
    this.$message.success('已成功退出登录');
  },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    navigateTo(path, row = null) {
      if (row) {
        this.$router.push({
          path,
          query: {
            id: row.id,
            projectName: row.projectName,
            submitter: row.submitter,
            submissionDate: row.submissionDate
          }
        });
      } else {
        this.$router.push(path);
      }
    },
    setCurrentRow(row) {
      this.currentRow = row;
    }
  }
}
</script>

<style scoped lang="less">
Header {
  background-color: #409eff;
  display: flex;
  position: relative;
  .name,
  .out {
    line-height: 50px;
    color: white;
  }
  .name {
    margin-left: 20px;
    margin-right: 10px;
  }
  .out {
    position: absolute;
    right: 40px;
  }
}
.container {
  display: flex;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
