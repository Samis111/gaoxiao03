import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import { createStore } from 'vuex'
import document from './modules/document'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userInfo: JSON.parse(sessionStorage.getItem('userInfo') || '{}'),
    isLogin: !!sessionStorage.getItem('isLogin')
  },
  getters: {
  },
  mutations: {
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
    },
    SET_LOGIN_STATE(state, isLogin) {
      state.isLogin = isLogin
      if (isLogin) {
        sessionStorage.setItem('isLogin', 'true')
      } else {
        sessionStorage.removeItem('isLogin')
        sessionStorage.removeItem('userInfo')
      }
    }
  },
  actions: {
    // 登录
    async login({ commit }, userinfo) {
      try {
        const response = await axios.post('/api/login', userinfo)
        if (response.data.code === 200) {
          // 合并角色信息
          const userData = {
            ...response.data.data,
            role: userinfo.role
          }
          commit('SET_USER_INFO', userData)
          commit('SET_LOGIN_STATE', true)
        }
        return response
      } catch (error) {
        console.error('Login error:', error)
        throw error
      }
    },
    // 登出
    logout({ commit }) {
      commit('SET_USER_INFO', {})
      commit('SET_LOGIN_STATE', false)
    }
  },
  modules: {
    document
  }
})
