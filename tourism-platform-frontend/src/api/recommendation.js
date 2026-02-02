import request from '@/utils/request'

// 获取热门推荐
export function getHotRecommendations(type, limit = 10) {
  return request({
    url: '/recommendations/hot',
    method: 'get',
    params: { type, limit }
  })
}

// 获取个性化推荐
export function getPersonalizedRecommendations(limit = 10, forceRefresh = false) {
  const params = { limit }
  // 如果需要强制刷新，添加时间戳参数避免缓存
  if (forceRefresh) {
    params._t = Date.now()
  }
  return request({
    url: '/recommendations/personalized',
    method: 'get',
    params,
    // 强制刷新时禁用缓存
    forceRefresh: forceRefresh
  })
}

