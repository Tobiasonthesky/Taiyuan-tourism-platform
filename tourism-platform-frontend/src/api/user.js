import request from '@/utils/request'

// 用户注册
export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

// 用户登录
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

// 更新用户信息
export function updateUserInfo(data) {
  return request({
    url: '/user/info',
    method: 'put',
    data
  })
}

// ==================== 用户提交内容 ====================
export function submitAttraction(data) {
  return request({
    url: '/attractions/submit',
    method: 'post',
    data
  })
}

export function submitFood(data) {
  return request({
    url: '/foods/submit',
    method: 'post',
    data
  })
}

export function submitCulture(data) {
  return request({
    url: '/cultures/submit',
    method: 'post',
    data
  })
}

export function submitStrategy(data) {
  return request({
    url: '/strategies/submit',
    method: 'post',
    data
  })
}

