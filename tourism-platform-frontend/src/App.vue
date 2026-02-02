<template>
  <div id="app">
    <Header />
    <main class="main-content">
    <router-view />
    </main>
    <Footer />
    <AiAssistant />
  </div>
</template>

<script>
import Header from '@/components/layout/Header'
import Footer from '@/components/layout/Footer'
import AiAssistant from '@/components/common/AiAssistant'

export default {
  name: 'App',
  components: {
    Header,
    Footer,
    AiAssistant
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html {
  scroll-behavior: smooth;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-size: 16px;
  line-height: 1.6;
  color: #303133;
  background-color: #F5F7FA;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
}

.main-content {
  flex: 1;
}

/* 全局滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #FAFAFA;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #DCDFE6;
  border-radius: 4px;
  transition: background 0.3s ease;
}
  
::-webkit-scrollbar-thumb:hover {
  background: #909399;
}

/* 链接样式 */
a {
  color: #409EFF;
  text-decoration: none;
  transition: color 0.3s ease;
}
  
a:hover {
  color: #337ecc;
}

/* 选中文本样式 */
::selection {
  background: rgba(64, 158, 255, 0.2);
  color: #303133;
}

/* 清除浮动 */
.clearfix::after {
  content: '';
  display: table;
  clear: both;
}
</style>
