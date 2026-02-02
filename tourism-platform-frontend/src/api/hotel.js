import request from '@/utils/request'

// 获取酒店列表
export function getHotels(params) {
  return request({
    url: '/hotels',
    method: 'get',
    params
  })
}

// 获取酒店详情
export function getHotelDetail(id) {
  return request({
    url: `/hotels/${id}`,
    method: 'get'
  })
}

// 获取酒店房间列表
export function getHotelRooms(hotelId) {
  return request({
    url: `/hotels/${hotelId}/rooms`,
    method: 'get'
  })
}

// 搜索酒店
export function searchHotels(params) {
  return request({
    url: '/hotels/search',
    method: 'get',
    params
  })
}

// 获取酒店图片列表
export function getHotelImages(hotelId) {
  return request({
    url: `/hotels/${hotelId}/images`,
    method: 'get'
  })
}

