import request from '@/utils/request'

// 获取景点列表
export function getAttractions(params) {
  return request({
    url: '/attractions',
    method: 'get',
    params
  })
}

// 获取景点详情
export function getAttractionDetail(id) {
  return request({
    url: `/attractions/${id}`,
    method: 'get'
  })
}

// 搜索景点
export function searchAttractions(params) {
  return request({
    url: '/attractions/search',
    method: 'get',
    params
  })
}

// 获取景点分类列表
export function getAttractionCategories() {
  return request({
    url: '/attractions/categories',
    method: 'get'
  })
}

// 获取景点图片列表
export function getAttractionImages(attractionId) {
  return request({
    url: `/attractions/${attractionId}/images`,
    method: 'get'
  })
}

