import request from '@/utils/request'

// 添加收藏
export function addFavorite(data) {
  return request({
    url: '/favorites',
    method: 'post',
    params: {
      targetType: data.targetType,
      targetId: data.targetId
    }
  })
}

// 取消收藏
export function removeFavorite(targetType, targetId) {
  return request({
    url: '/favorites',
    method: 'delete',
    params: { targetType, targetId }
  })
}

// 获取收藏列表
export function getFavorites(params) {
  return request({
    url: '/favorites',
    method: 'get',
    params
  })
}

// 检查是否已收藏
export function checkFavorite(targetType, targetId) {
  return request({
    url: '/favorites/check',
    method: 'get',
    params: { targetType, targetId }
  })
}

