import request from '@/utils/request'

export function getSystemLogs(params) {
  return request({
    url: '/admin/system-logs',
    method: 'get',
    params
  })
}

export function getSystemLogDetail(id) {
  return request({
    url: `/admin/system-logs/${id}`,
    method: 'get'
  })
}

export function batchDeleteSystemLogs(ids) {
  return request({
    url: '/admin/system-logs/batch',
    method: 'delete',
    data: ids
  })
}

export function clearSystemLogs() {
  return request({
    url: '/admin/system-logs/clear',
    method: 'delete'
  })
}