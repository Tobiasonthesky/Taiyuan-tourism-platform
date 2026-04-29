<template>
  <el-footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-item">
          <h3>{{ $t('footer.about') }}</h3>
          <p>{{ $t('footer.platform') }}</p>
          <p>{{ $t('footer.description') }}</p>
        </div>
        <div class="footer-item">
          <h3>{{ $t('footer.quickLinks') }}</h3>
          <p><router-link to="/food">{{ $t('common.food') }} 🍜</router-link></p>
          <p><router-link to="/culture">{{ $t('common.culture') }} 📚</router-link></p>
          <p><router-link to="/strategy">{{ $t('common.strategy') }} 🗺️</router-link></p>
        </div>
        <div class="footer-item">
          <h3>{{ $t('footer.bookNow') }}</h3>
          <p><router-link to="/attraction">{{ $t('common.attraction') }} 🌄</router-link></p>
          <p><router-link to="/hotel">{{ $t('common.hotel') }} 🏨</router-link></p>
          <p><router-link to="/experience">{{ $t('common.experience') }} 🎪</router-link></p>
        </div>
        <div class="footer-item">
          <h3>{{ $t('footer.contact') }}</h3>
          <p>{{ $t('footer.email') }}: info@tourism.com</p>
          <p>{{ $t('footer.phone') }}: 400-123-4567</p>
        </div>
      </div>
      <div class="copyright">
        <p>© 2026 {{ $t('footer.copyright') }}</p>
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
  background: #f5f5f7;
  color: #1d1d1f;
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
  height: 1px;
  background: #d2d2d7;
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
  font-size: 16px;
  font-weight: 600;
  color: #1d1d1f;
  position: relative;
  padding-bottom: 12px;
}

.footer .footer-item h3::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 40px;
  height: 2px;
  background: #0071e3;
  border-radius: 2px;
}

.footer .footer-item p {
  margin: 12px 0;
  color: #86868b;
  line-height: 1.8;
  transition: color 0.3s ease;
}

.footer .footer-item p a {
  color: #86868b;
  text-decoration: none;
  transition: all 0.3s ease;
  display: inline-block;
}

.footer .footer-item p a:hover {
  color: #0071e3;
  transform: translateX(5px);
}

.footer .copyright {
  text-align: center;
  padding-top: 30px;
  border-top: 1px solid #d2d2d7;
  color: #86868b;
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
