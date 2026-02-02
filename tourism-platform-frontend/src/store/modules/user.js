import { login, register, getUserInfo, updateUserInfo } from '@/api/user'
import { getToken, setToken, removeToken, getUserInfo as getStoredUserInfo, setUserInfo, removeUserInfo } from '@/utils/auth'

const state = {
  token: getToken(), // 从localStorage恢复token
  userInfo: getStoredUserInfo() // 从localStorage恢复用户信息
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
  }
}

const actions = {
  // 登录
  login({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      login(userInfo).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        if (data.userInfo) {
          commit('SET_USER_INFO', data.userInfo)
          setUserInfo(data.userInfo)
        }
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  // 注册
  register(context, userInfo) {
    return new Promise((resolve, reject) => {
      register(userInfo).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  // 获取用户信息
  getUserInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getUserInfo().then(response => {
        const { data } = response
        commit('SET_USER_INFO', data)
        setUserInfo(data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  // 更新用户信息
  updateUserInfo({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      updateUserInfo(userInfo).then(response => {
        const { data } = response
        commit('SET_USER_INFO', data)
        setUserInfo(data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  // 退出登录
  logout({ commit }) {
    return new Promise((resolve) => {
      commit('SET_TOKEN', '')
      commit('SET_USER_INFO', null)
      removeToken()
      removeUserInfo()
      resolve()
    })
  }
}

const getters = {
  token: state => state.token,
  userInfo: state => state.userInfo,
  isLogin: state => !!state.token,
  isAdmin: state => {
    return state.userInfo && state.userInfo.role === 'admin'
  },
  role: state => {
    return state.userInfo ? state.userInfo.role : null
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}

