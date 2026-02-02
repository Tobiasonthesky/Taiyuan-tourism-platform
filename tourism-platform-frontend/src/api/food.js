import request from '@/utils/request'

// 获取美食列表
export function getFoods(params) {
  return request({
    url: '/foods',
    method: 'get',
    params
  }).then(res => {
    console.log('getFoods API完整响应:', JSON.stringify(res, null, 2))
    if (res.data && res.data.records) {
      console.log('返回的美食记录数量:', res.data.records.length)
      if (res.data.records.length > 0) {
        console.log('第一条美食记录:', JSON.stringify(res.data.records[0], null, 2))
        console.log('第一条美食记录的所有字段:', Object.keys(res.data.records[0]))
        console.log('第一条美食记录的餐厅字段:', res.data.records[0].restaurant)
        console.log('第一条美食记录的地址字段:', res.data.records[0].address)
      }
    } else {
      console.log('getFoods API响应中没有data或records字段')
    }
    return res
  })
}

// 获取美食详情
export function getFoodDetail(id) {
  return request({
    url: `/foods/${id}`,
    method: 'get'
  })
}

// 搜索美食
export function searchFoods(params) {
  return request({
    url: '/foods/search',
    method: 'get',
    params
  })
}

// 获取美食分类列表
export function getFoodCategories() {
  return request({
    url: '/foods/categories',
    method: 'get'
  })
}

// 获取美食图片列表
export function getFoodImages(foodId) {
  return request({
    url: `/foods/${foodId}/images`,
    method: 'get'
  })
}

