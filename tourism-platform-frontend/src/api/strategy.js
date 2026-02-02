import request from '@/utils/request'

// 获取攻略列表
export function getStrategies(params) {
  return request({
    url: '/strategies',
    method: 'get',
    params
  })
}

// 获取攻略详情
export function getStrategyDetail(id) {
  return request({
    url: `/strategies/${id}`,
    method: 'get'
  })
}

// 搜索攻略
export function searchStrategies(params) {
  return request({
    url: '/strategies/search',
    method: 'get',
    params
  })
}

// 获取攻略路线列表
export function getStrategyRoutes(strategyId) {
  return request({
    url: `/strategies/${strategyId}/routes`,
    method: 'get'
  })
}

// 获取攻略图片列表
export function getStrategyImages(strategyId) {
  return request({
    url: `/strategies/${strategyId}/images`,
    method: 'get'
  })
}

// AI生成攻略
export function generateStrategy(data) {
  return request({
    url: '/strategies/generate',
    method: 'post',
    data,
    timeout: 120000 // 120秒超时，AI生成需要较长时间
  })
}

// 提交攻略
export function submitStrategy(data) {
  return request({
    url: '/strategies/submit',
    method: 'post',
    data
  })
}

