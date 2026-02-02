import request from '@/utils/request'

// 获取公告列表
export function getAnnouncements(params) {
  return request({
    url: '/announcements',
    method: 'get',
    params
  })
}

// 获取公告详情
export function getAnnouncementDetail(id) {
  return request({
    url: `/announcements/${id}`,
    method: 'get'
  })
}

// 获取轮播图
export function getBanners() {
  return request({
    url: '/announcements/banners',
    method: 'get'
  })
}

