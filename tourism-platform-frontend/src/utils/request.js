import axios from 'axios'
import { Message } from 'element-ui'
import router from '@/router'
import store from '@/store'
import cache from './cache'
import { retryRequest } from './retry'

// 创建 axios 实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API || '/api',
  timeout: 10000
})

// 临时禁用缓存功能，避免 toUpperCase 错误
const ENABLE_CACHE = false

// 需要缓存的请求方法
const cacheableMethods = ['get']
// 不需要缓存的请求路径
const noCachePaths = ['/user/info', '/orders']

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = store.getters['user/token']
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    
    // 如果设置了强制刷新，添加缓存控制头
    if (config.forceRefresh) {
      config.headers['Cache-Control'] = 'no-cache'
      config.headers['Pragma'] = 'no-cache'
    }
    
    // 检查缓存 - 确保 method 存在（临时禁用）
    if (ENABLE_CACHE && config.method && cacheableMethods.includes(config.method.toLowerCase())) {
      // 检查是否需要缓存（排除某些路径）
      const shouldCache = !noCachePaths.some(path => config.url && config.url.includes(path))
      
      if (shouldCache && !config.forceRefresh) {
        const cacheKey = `${config.method}_${config.url}_${JSON.stringify(config.params || {})}`
        const cachedData = cache.get(cacheKey)
        
        if (cachedData) {
          // 暂时禁用缓存返回，避免 toUpperCase 错误
          // TODO: 修复缓存逻辑后重新启用
        }
        
        // 保存缓存键到配置中，用于响应拦截器
        config.cacheKey = cacheKey
      }
      
      config.shouldCache = shouldCache
    }
    
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data || response
    
    // 如果返回的状态码不是200，则视为错误
    if (res.code !== 200) {
      // AI助手接口的错误由组件自己处理，不在这里显示错误提示
      const isAiAssistantRequest = response.config && response.config.url && 
                                   (response.config.url.includes('/ai-assistant/chat') || 
                                    response.config.url.includes('/ai-assistant/summary'))
      
      if (!isAiAssistantRequest) {
        Message.error(res.message || '请求失败')
      }
      
      // 401: Token 过期或未登录
      if (res.code === 401) {
        store.dispatch('user/logout').then(() => {
          router.push('/login')
        })
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    } else {
      // 缓存成功的GET请求
      if (response.config && response.config.shouldCache && response.config.cacheKey) {
        cache.set(response.config.cacheKey, res, 5 * 60 * 1000) // 缓存5分钟
      }
      return res
    }
  },
  error => {
    // 404错误不显示错误提示（可能是图片资源不存在等）
    if (error.response && error.response.status === 404) {
      // 静默处理404错误，不显示错误提示
      return Promise.reject(error)
    }
    
    console.error('响应错误:', error)
    
    // 网络错误时尝试重试 - 确保 error.config 存在
    if (!error.response && error.message === 'Network Error' && error.config) {
      return retryRequest(() => {
        // 确保 config 有 method
        if (!error.config.method) {
          error.config.method = error.config.method || 'get'
        }
        return service.request(error.config)
      }, {
        maxRetries: 3,
        delay: 1000,
        onRetry: (attempts) => {
          console.log(`请求重试 ${attempts}/3`)
        }
      }).catch(err => {
        Message.error('网络连接失败，请检查网络')
        return Promise.reject(err)
      })
    }
    
    if (error.response) {
      switch (error.response.status) {
        case 401:
          Message.error('未登录或登录已过期')
          store.dispatch('user/logout').then(() => {
            router.push('/login')
          })
          break
        case 403:
          Message.error('没有权限访问')
          break
        case 404:
          Message.error('请求的资源不存在')
          break
        case 405:
          // 405错误（Method Not Allowed）通常是因为请求方法不正确，静默处理，不显示错误提示
          // 因为这可能是浏览器自动发送的请求，不是用户主动操作
          break
        case 500:
          Message.error('服务器错误')
          break
        default:
          Message.error(error.response.data?.message || '网络错误')
      }
    } else {
      Message.error('网络连接失败，请检查网络')
    }
    
    return Promise.reject(error)
  }
)

export default service

