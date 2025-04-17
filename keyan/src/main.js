import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import 'element-ui/lib/theme-chalk/index.css';
import ElementUI from 'element-ui';
import axios from 'axios';


Vue.use(ElementUI);
Vue.config.productionTip = false

// 添加请求拦截器
axios.interceptors.request.use(config => {
  // 从 sessionStorage 获取用户信息
  const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
  if (userInfo.id) {
    config.headers['User-Id'] = userInfo.id
  }
  return config
}, error => {
  return Promise.reject(error)
})

// 添加响应拦截器
axios.interceptors.response.use(response => {
  return response
}, error => {
  console.error('请求错误:', error)
  if (error.response && error.response.status === 401) {
    // 未授权，可能是登录过期
    sessionStorage.removeItem('userInfo')
    sessionStorage.removeItem('isLogin')
    router.push('/')
  }
  return Promise.reject(error)
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
