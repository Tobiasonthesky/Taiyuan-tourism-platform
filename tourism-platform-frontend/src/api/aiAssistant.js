import request from '@/utils/request'

// AI助手对话
export function chatWithAI(message, conversationHistory = []) {
  return request({
    url: '/ai-assistant/chat',
    method: 'post',
    data: {
      message,
      conversationHistory
    },
    timeout: 60000 // 60秒超时
  })
}

// 获取网站数据摘要
export function getWebsiteDataSummary() {
  return request({
    url: '/ai-assistant/summary',
    method: 'get'
  })
}
