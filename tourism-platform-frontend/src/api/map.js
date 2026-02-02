import request from '@/utils/request'

// 获取景点地图数据
export function getAttractionMapData() {
  return request({
    url: '/map/attractions',
    method: 'get'
  })
}

// 获取餐厅地图数据
export function getRestaurantMapData() {
  return request({
    url: '/map/restaurants',
    method: 'get'
  })
}

// 获取酒店地图数据
export function getHotelMapData() {
  return request({
    url: '/map/hotels',
    method: 'get'
  })
}

// 获取文化地图数据
export function getCultureMapData() {
  return request({
    url: '/map/cultures',
    method: 'get'
  })
}

// 获取所有地图数据（景点、餐厅、酒店、文化）
export function getAllMapData() {
  return request({
    url: '/map/all',
    method: 'get'
  })
}

// 路线规划
export function planRoute(originLng, originLat, destLng, destLat, mode = 'driving') {
  // 后端使用@RequestParam接收查询参数
  // 对于POST请求，axios的params会作为查询参数附加到URL
  // 构建查询字符串
  const params = new URLSearchParams({
    originLng: originLng.toString(),
    originLat: originLat.toString(),
    destLng: destLng.toString(),
    destLat: destLat.toString(),
    mode: mode || 'driving'
  })
  
  return request({
    url: `/map/route?${params.toString()}`,
    method: 'post'
  })
}

// 地点搜索
export function searchPlace(keyword, city) {
  return request({
    url: '/map/search',
    method: 'get',
    params: {
      keyword,
      city
    }
  })
}

// 兼容旧接口
export function getMapData() {
  return getAttractionMapData()
}

