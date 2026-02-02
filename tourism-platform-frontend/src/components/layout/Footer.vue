<template>
  <el-footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-item">
          <h3>关于我们</h3>
          <p>家乡文旅宣传平台</p>
          <p>致力于推广家乡丰富的文旅资源</p>
        </div>
        <div class="footer-item">
          <h3>快速链接</h3>
          <p><router-link to="/food">美食 🍜</router-link></p>
          <p><router-link to="/culture">文化 📚</router-link></p>
          <p><router-link to="/strategy">攻略 🗺️</router-link></p>
        </div>
        <div class="footer-item">
          <h3>即刻预订</h3>
          <p><router-link to="/attraction">景点 🌄</router-link></p>
          <p><router-link to="/hotel">酒店 🏨</router-link></p>
          <p><router-link to="/experience">体验 🎪</router-link></p>
        </div>
        <div class="footer-item">
          <h3>联系我们</h3>
          <p>邮箱: info@tourism.com</p>
          <p>电话: 400-123-4567</p>
        </div>
      </div>
      <div class="copyright">
        <p>© 2026 家乡文旅宣传平台 版权所有</p>
      </div>
    </div>
  </el-footer>
</template>

<script>
export default {
  name: "AppFooter",
};
</script>

<style scoped>
.footer {
  background: linear-gradient(135deg, #ff3232 0%, #8b0034 100%);
  color: #fff;
  padding: 60px 0 30px;
  margin-top: 0;
  position: relative;
  width: 100%;
  flex-shrink: 0;
  height: auto !important;
  line-height: normal;
}

.footer::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  z-index: 1;
}

.footer .container {
  width: 1200px;
  max-width: 100%;
  margin: 0 auto;
  padding: 0 24px;
  position: relative;
  z-index: 1;
}

.footer .footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 40px;
  margin-bottom: 40px;
}

.footer .footer-item h3 {
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: 600;
  color: #fff;
  position: relative;
  padding-bottom: 12px;
}

.footer .footer-item h3::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 40px;
  height: 3px;
  background: #409eff;
  border-radius: 2px;
}

.footer .footer-item p {
  margin: 12px 0;
  color: rgba(255, 255, 255, 0.7);
  line-height: 1.8;
  transition: color 0.3s ease;
}

.footer .footer-item p a {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  transition: all 0.3s ease;
  display: inline-block;
}

.footer .footer-item p a:hover {
  color: #409eff;
  transform: translateX(5px);
}

.footer .copyright {
  text-align: center;
  padding-top: 30px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
}

@media (max-width: 1200px) {
  .footer .container {
    width: 100%;
    padding: 0 16px;
  }
}

@media (max-width: 768px) {
  .footer {
    padding: 40px 0 20px;
    margin-top: 0;
  }

  .footer .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
    margin-bottom: 30px;
  }

  .footer .footer-item h3 {
    font-size: 18px;
  }
}
</style>
