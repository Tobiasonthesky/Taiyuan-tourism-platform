import request from '@/utils/request'

// 添加评论
export function addComment(data) {
  return request({
    url: '/comments',
    method: 'post',
    data
  })
}

// 获取评论列表
export function getComments(params) {
  return request({
    url: '/comments',
    method: 'get',
    params
  })
}

// 删除评论
export function deleteComment(id) {
  return request({
    url: `/comments/${id}`,
    method: 'delete'
  })
}

// 添加评论回复
export function addCommentReply(commentId, data) {
  return request({
    url: `/comments/${commentId}/replies`,
    method: 'post',
    data
  })
}

// 获取评论回复列表
export function getCommentReplies(commentId) {
  return request({
    url: `/comments/${commentId}/replies`,
    method: 'get'
  })
}

// 删除评论回复
export function deleteCommentReply(commentId, replyId) {
  return request({
    url: `/comments/${commentId}/replies/${replyId}`,
    method: 'delete'
  })
}

