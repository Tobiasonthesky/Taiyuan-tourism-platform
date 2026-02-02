import request from '@/utils/request'

// 创建订单
export function createOrder(data) {
  return request({
    url: '/orders',
    method: 'post',
    data
  })
}

// 获取订单列表
export function getOrders(params) {
  return request({
    url: '/orders',
    method: 'get',
    params
  })
}

// 获取订单详情
export function getOrderDetail(id) {
  return request({
    url: `/orders/${id}`,
    method: 'get'
  })
}

// 获取订单项列表
export function getOrderItems(id) {
  return request({
    url: `/orders/${id}/items`,
    method: 'get'
  })
}

// 取消订单
export function cancelOrder(id) {
  return request({
    url: `/orders/${id}/cancel`,
    method: 'put'
  })
}

// 支付订单
export function payOrder(id, payMethod) {
  return request({
    url: `/orders/${id}/pay`,
    method: 'post',
    params: { payMethod }
  })
}

