import request from '@/utils/request'

// 获取体验项目列表
export function getExperiences(params) {
  return request({
    url: '/experiences',
    method: 'get',
    params
  })
}

// 获取体验项目详情
export function getExperienceDetail(id) {
  return request({
    url: `/experiences/${id}`,
    method: 'get'
  })
}

// 搜索体验项目
export function searchExperiences(params) {
  return request({
    url: '/experiences/search',
    method: 'get',
    params
  })
}

// 获取体验项目图片列表
export function getExperienceImages(experienceId) {
  return request({
    url: `/experiences/${experienceId}/images`,
    method: 'get'
  })
}

