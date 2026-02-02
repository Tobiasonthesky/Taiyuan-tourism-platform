const { defineConfig } = require('@vue/cli-service')
const path = require('path')

module.exports = defineConfig({
  transpileDependencies: true,
  
  // 开发服务器配置
  devServer: {
    port: 8081,
    open: true,
    proxy: {
      // 配置 /uploads 路径代理，指向后端静态资源（放在 /api 之前，确保优先匹配）
      '/uploads': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // 后端配置了 context-path: /api，所以需要添加 /api 前缀
        pathRewrite: {
          '^/uploads': '/api/uploads'
        }
      },
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        ws: true,
        // 后端已经配置了 context-path: /api，所以不需要 pathRewrite
        // pathRewrite: {
        //   '^/api': ''
        // }
      }
    }
  },
  
  // 生产环境配置
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
  outputDir: 'dist',
  assetsDir: 'static',
  productionSourceMap: false
})
