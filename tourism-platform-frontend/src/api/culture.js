import request from '@/utils/request'

// 获取文化列表
export function getCultures(params) {
  return request({
    url: '/cultures',
    method: 'get',
    params
  })
}

// 获取文化详情
export function getCultureDetail(id) {
  return request({
    url: `/cultures/${id}`,
    method: 'get'
  })
}

// 搜索文化
export function searchCultures(params) {
  return request({
    url: '/cultures/search',
    method: 'get',
    params
  })
}

// 获取文化分类列表
export function getCultureCategories() {
  return request({
    url: '/cultures/categories',
    method: 'get'
  })
}

// 获取文化图片列表
export function getCultureImages(cultureId) {
  return request({
    url: `/cultures/${cultureId}/images`,
    method: 'get'
  })
}

