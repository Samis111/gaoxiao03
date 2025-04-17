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
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-s-comment"></i>
              <span slot="title">申报</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="1-1" @click="navigateTo('/student-home/research-declaration')">申报</el-menu-item>
              <el-menu-item index="1-2" @click="navigateTo('/student-home/review-status')">审核状态</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-s-tools"></i>
              <span slot="title">信息设置</span>
            </template>
            <el-menu-item-group>
              <el-menu-item
                index="2-1"
                @click="navigateTo('/student-home/password')"
                >密码修改</el-menu-item
              >
              <el-menu-item index="2-2" @click="navigateTo('/student-home/profile-form')">信息维护</el-menu-item>
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
  name: 'StudentHome',
  data() {
    return {
      isCollapse: true
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
    navigateTo(path) {
      this.$router.push(path);
    },
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