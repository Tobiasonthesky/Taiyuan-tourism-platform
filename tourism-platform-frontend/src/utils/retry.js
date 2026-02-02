/**
 * 带重试的请求函数
 * @param {Function} requestFn 请求函数
 * @param {Object} options 配置选项
 */
export async function retryRequest(requestFn, options = {}) {
  const {
    maxRetries = 3,
    delay = 1000,
    onRetry = null
  } = options

  let attempts = 0

  while (attempts < maxRetries) {
    try {
      return await requestFn()
    } catch (error) {
      attempts++
      
      if (attempts >= maxRetries) {
        throw error
      }
      
      if (onRetry) {
        onRetry(attempts, error)
      }
      
      await new Promise(resolve => setTimeout(resolve, delay * attempts))
    }
  }
}

