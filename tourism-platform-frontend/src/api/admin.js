import request from '@/utils/request'

// 获取统计数据
export function getStatistics() {
  return request({
    url: '/admin/statistics',
    method: 'get'
  })
}

// 获取用户列表
export function getUsers(params) {
  return request({
    url: '/admin/users',
    method: 'get',
    params
  })
}

// 修改用户角色
export function updateUserRole(id, role) {
  return request({
    url: `/admin/users/${id}/role`,
    method: 'put',
    params: { role }
  })
}

// 修改用户状态
export function updateUserStatus(id, status) {
  return request({
    url: `/admin/users/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/admin/users/${id}`,
    method: 'delete'
  })
}

// 获取订单列表
export function getOrders(params) {
  return request({
    url: '/admin/orders',
    method: 'get',
    params
  })
}

// ==================== 景点管理 ====================
export function createAttraction(data) {
  return request({
    url: '/admin/attractions',
    method: 'post',
    data
  })
}

export function updateAttraction(id, data) {
  return request({
    url: `/admin/attractions/${id}`,
    method: 'put',
    data
  })
}

export function deleteAttraction(id) {
  return request({
    url: `/admin/attractions/${id}`,
    method: 'delete'
  })
}

// ==================== 美食管理 ====================
export function getAdminFoods(params) {
  return request({
    url: '/admin/foods',
    method: 'get',
    params
  })
}

export function createFood(data) {
  console.log('创建美食发送的数据:', data)
  console.log('创建美食的餐厅字段:', data.restaurant)
  console.log('创建美食的地址字段:', data.address)
  return request({
    url: '/admin/foods',
    method: 'post',
    data
  })
}

export function updateFood(id, data) {
  console.log('更新美食发送的数据:', data)
  console.log('更新美食的餐厅字段:', data.restaurant)
  console.log('更新美食的地址字段:', data.address)
  return request({
    url: `/admin/foods/${id}`,
    method: 'put',
    data
  })
}

export function deleteFood(id) {
  return request({
    url: `/admin/foods/${id}`,
    method: 'delete'
  })
}

// ==================== 文化管理 ====================
export function createCulture(data) {
  return request({
    url: '/admin/cultures',
    method: 'post',
    data
  })
}

export function updateCulture(id, data) {
  return request({
    url: `/admin/cultures/${id}`,
    method: 'put',
    data
  })
}

export function deleteCulture(id) {
  return request({
    url: `/admin/cultures/${id}`,
    method: 'delete'
  })
}

// ==================== 酒店管理 ====================
export function createHotel(data) {
  return request({
    url: '/admin/hotels',
    method: 'post',
    data
  })
}

export function updateHotel(id, data) {
  return request({
    url: `/admin/hotels/${id}`,
    method: 'put',
    data
  })
}

export function deleteHotel(id) {
  return request({
    url: `/admin/hotels/${id}`,
    method: 'delete'
  })
}

// ==================== 攻略管理 ====================
export function createStrategy(data) {
  return request({
    url: '/admin/strategies',
    method: 'post',
    data
  })
}

export function updateStrategy(id, data) {
  return request({
    url: `/admin/strategies/${id}`,
    method: 'put',
    data
  })
}

export function deleteStrategy(id) {
  return request({
    url: `/admin/strategies/${id}`,
    method: 'delete'
  })
}

// 酒店房间管理
export function createHotelRoom(data) {
  return request({
    url: '/admin/hotel-rooms',
    method: 'post',
    data
  })
}

export function updateHotelRoom(id, data) {
  return request({
    url: `/admin/hotel-rooms/${id}`,
    method: 'put',
    data
  })
}

export function deleteHotelRoom(id) {
  return request({
    url: `/admin/hotel-rooms/${id}`,
    method: 'delete'
  })
}

// ==================== 活动公告管理 ====================
export function createAnnouncement(data) {
  return request({
    url: '/admin/announcements',
    method: 'post',
    data
  })
}

export function updateAnnouncement(id, data) {
  return request({
    url: `/admin/announcements/${id}`,
    method: 'put',
    data
  })
}

export function deleteAnnouncement(id) {
  return request({
    url: `/admin/announcements/${id}`,
    method: 'delete'
  })
}

export function getAnnouncements(params) {
  return request({
    url: '/admin/announcements',
    method: 'get',
    params
  })
}

// ==================== 内容审核 ====================
export function getPendingAttractions(params) {
  return request({
    url: '/admin/pending/attractions',
    method: 'get',
    params
  })
}

export function getPendingFoods(params) {
  return request({
    url: '/admin/pending/foods',
    method: 'get',
    params
  })
}

export function getPendingCultures(params) {
  return request({
    url: '/admin/pending/cultures',
    method: 'get',
    params
  })
}

export function getPendingStrategies(params) {
  return request({
    url: '/admin/pending/strategies',
    method: 'get',
    params
  })
}

export function auditAttraction(id, status) {
  return request({
    url: `/admin/attractions/${id}/audit`,
    method: 'put',
    params: { status }
  })
}

export function auditFood(id, status) {
  return request({
    url: `/admin/foods/${id}/audit`,
    method: 'put',
    params: { status }
  })
}

export function auditCulture(id, status) {
  return request({
    url: `/admin/cultures/${id}/audit`,
    method: 'put',
    params: { status }
  })
}

export function auditStrategy(id, status) {
  return request({
    url: `/admin/strategies/${id}/audit`,
    method: 'put',
    params: { status }
  })
}

// ==================== 图片管理 ====================
// 景点图片
export function createAttractionImage(data) {
  return request({
    url: '/admin/attraction-images',
    method: 'post',
    data
  })
}

export function updateAttractionImage(id, data) {
  return request({
    url: `/admin/attraction-images/${id}`,
    method: 'put',
    data
  })
}

export function deleteAttractionImage(id) {
  return request({
    url: `/admin/attraction-images/${id}`,
    method: 'delete'
  })
}

// 美食图片
export function createFoodImage(data) {
  return request({
    url: '/admin/food-images',
    method: 'post',
    data
  })
}

export function updateFoodImage(id, data) {
  return request({
    url: `/admin/food-images/${id}`,
    method: 'put',
    data
  })
}

export function deleteFoodImage(id) {
  return request({
    url: `/admin/food-images/${id}`,
    method: 'delete'
  })
}

// 文化图片
export function createCultureImage(data) {
  return request({
    url: '/admin/culture-images',
    method: 'post',
    data
  })
}

export function updateCultureImage(id, data) {
  return request({
    url: `/admin/culture-images/${id}`,
    method: 'put',
    data
  })
}

export function deleteCultureImage(id) {
  return request({
    url: `/admin/culture-images/${id}`,
    method: 'delete'
  })
}

// 攻略图片
export function createStrategyImage(data) {
  return request({
    url: '/admin/strategy-images',
    method: 'post',
    data
  })
}

export function updateStrategyImage(id, data) {
  return request({
    url: `/admin/strategy-images/${id}`,
    method: 'put',
    data
  })
}

export function deleteStrategyImage(id) {
  return request({
    url: `/admin/strategy-images/${id}`,
    method: 'delete'
  })
}

// 体验项目图片
export function createExperienceImage(data) {
  return request({
    url: '/admin/experience-images',
    method: 'post',
    data
  })
}

export function updateExperienceImage(id, data) {
  return request({
    url: `/admin/experience-images/${id}`,
    method: 'put',
    data
  })
}

export function deleteExperienceImage(id) {
  return request({
    url: `/admin/experience-images/${id}`,
    method: 'delete'
  })
}

// 酒店图片
export function createHotelImage(data) {
  return request({
    url: '/admin/hotel-images',
    method: 'post',
    data
  })
}

export function updateHotelImage(id, data) {
  return request({
    url: `/admin/hotel-images/${id}`,
    method: 'put',
    data
  })
}

export function deleteHotelImage(id) {
  return request({
    url: `/admin/hotel-images/${id}`,
    method: 'delete'
  })
}

// ==================== 体验项目管理 ====================
export function createExperience(data) {
  return request({
    url: '/admin/experiences',
    method: 'post',
    data
  })
}

export function updateExperience(id, data) {
  return request({
    url: `/admin/experiences/${id}`,
    method: 'put',
    data
  })
}

export function deleteExperience(id) {
  return request({
    url: `/admin/experiences/${id}`,
    method: 'delete'
  })
}

