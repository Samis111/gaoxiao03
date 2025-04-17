<template>
  <div class="app">
    <div class="login-res">
      <router-link class="goto" v-show="$route.meta.show" to="/">登录</router-link>
      <router-link class="goto" v-show="$route.meta.show" to="/Register">注册</router-link>
    </div>
    <router-view></router-view>
  </div>
</template>

<style lang="less" scoped>
.login-res {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;

  .goto {
    font-size: 30px;
    margin-top: 30px;
    font-weight: bold;
    color: #2c3e50;
    text-decoration: none;
    margin-right: 30px;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
