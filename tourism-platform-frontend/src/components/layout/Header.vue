<template>
  <el-header class="header">
    <div class="container">
      <div class="header-content">
        <div class="logo" @click="goHome">
          <h1>{{ $t('common.brand') }}</h1>
        </div>
        <el-menu
          :default-active="activeIndex"
          class="nav-menu"
          mode="horizontal"
          router
        >
          <el-menu-item index="/">{{ $t('common.home') }}</el-menu-item>
          <el-menu-item index="/attraction">{{ $t('common.attraction') }}</el-menu-item>
          <el-menu-item index="/food">{{ $t('common.food') }}</el-menu-item>
          <el-menu-item index="/culture">{{ $t('common.culture') }}</el-menu-item>
          <el-menu-item index="/strategy">{{ $t('common.strategy') }}</el-menu-item>
          <el-menu-item index="/hotel">{{ $t('common.hotel') }}</el-menu-item>
          <el-menu-item index="/experience">{{ $t('common.experience') }}</el-menu-item>
          <el-menu-item index="/map">{{ $t('common.map') }}</el-menu-item>
        </el-menu>
        <div class="user-info">
          <!-- 语言切换 -->
          <el-dropdown @command="handleLanguageChange" trigger="click">
            <span class="language-selector">
              {{ currentLanguage === 'zh' ? '中文' : 'English' }}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="zh">中文</el-dropdown-item>
              <el-dropdown-item command="en">English</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          
          <template v-if="isLogin">
            <el-dropdown @command="handleCommand">
              <span class="user-name">
                <img 
                  :src="avatarUrl" 
                  :alt="userInfo?.username || '用户'"
                  class="user-avatar"
                  @error="handleAvatarError"
                />
                <span class="username-text">{{ userInfo?.username || '用户' }}</span>
                <el-tag v-if="isAdmin" type="danger" size="mini" class="admin-tag">{{ $t('common.admin') }}</el-tag>
                <i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-if="isAdmin" command="admin">
                  <i class="el-icon-setting"></i> {{ $t('common.adminPanel') }}
                </el-dropdown-item>
                <el-dropdown-item v-if="!isAdmin" command="submit">
                  <i class="el-icon-edit"></i> {{ $t('common.submitContent') }}
                </el-dropdown-item>
                <el-dropdown-item divided command="profile">{{ $t('common.profile') }}</el-dropdown-item>
                <el-dropdown-item command="order">{{ $t('common.order') }}</el-dropdown-item>
                <el-dropdown-item command="favorite">{{ $t('common.favorite') }}</el-dropdown-item>
                <el-dropdown-item divided command="logout">{{ $t('common.logout') }}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="text" @click="goToLogin">{{ $t('common.login') }}</el-button>
            <el-button type="primary" size="small" @click="goToRegister">{{ $t('common.register') }}</el-button>
          </template>
        </div>
      </div>
    </div>
  </el-header>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'AppHeader',
  computed: {
    ...mapGetters('user', ['isLogin', 'userInfo', 'isAdmin']),
    activeIndex() {
      return this.$route.path
    },
    currentLanguage() {
      return this.$i18n.locale
    },
    avatarUrl() {
      if (this.userInfo?.avatar) {
        // 处理头像 URL，确保是完整路径
        const url = this.userInfo.avatar
        if (url.startsWith('http') || url.startsWith('//')) {
          return url
        }
        const baseURL = process.env.VUE_APP_BASE_API || '/api'
        const safeBaseURL = baseURL || '/api'
        if (url.startsWith('/')) {
          return safeBaseURL + url
        }
        return safeBaseURL + '/' + url
      }
      // 默认头像：使用用户名的首字母或默认图标
      return 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  },
  methods: {
    handleLanguageChange(lang) {
      this.$i18n.locale = lang
      localStorage.setItem('language', lang)
      // 更新 Element UI 语言设置
      import('element-ui/lib/locale').then(module => {
        const locale = module.default
        if (lang === 'zh') {
          import('element-ui/lib/locale/lang/zh-CN').then(zhLocale => {
            locale.use(zhLocale.default)
          })
        } else {
          import('element-ui/lib/locale/lang/en').then(enLocale => {
            locale.use(enLocale.default)
          })
        }
      })
      this.$message.success(lang === 'zh' ? '语言已切换为中文' : 'Language switched to English')
    },
    goHome() {
      if (this.$route.path !== '/') {
        this.$router.push('/')
      }
    },
    goToLogin() {
      if (this.$route.path !== '/login') {
        this.$router.push('/login')
      }
    },
    goToRegister() {
      if (this.$route.path !== '/register') {
        this.$router.push('/register')
      }
    },
    handleAvatarError(event) {
      // 头像加载失败时，使用默认头像
      event.target.src = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    },
    handleCommand(command) {
      switch (command) {
        case 'admin':
          if (this.$route.path !== '/admin') {
            this.$router.push('/admin')
          }
          break
        case 'submit':
          if (this.$route.path !== '/submit') {
            this.$router.push('/submit')
          }
          break
        case 'profile':
          if (this.$route.path !== '/profile') {
            this.$router.push('/profile')
          }
          break
        case 'order':
          if (this.$route.path !== '/order') {
            this.$router.push('/order')
          }
          break
        case 'favorite':
          if (this.$route.path !== '/favorite') {
            this.$router.push('/favorite')
          }
          break
        case 'logout':
          this.$store.dispatch('user/logout').then(() => {
            this.$message.success('退出成功')
            if (this.$route.path !== '/') {
              this.$router.push('/')
            }
          })
          break
      }
    }
  }
}
</script>

<style scoped>
.header {
  background: #ffffff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 64px !important;
  line-height: 64px;
  position: sticky;
  top: 0;
  z-index: 1000;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.header .container {
  width: 1200px;
  max-width: 100%;
  margin: 0 auto;
  padding: 0 24px;
}

.header .header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  min-width: 0;
}

.header .header-content .nav-menu {
  overflow: hidden;
  flex-shrink: 1;
  min-width: 0;
}

.header .logo {
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: transform 0.3s ease;
  flex-shrink: 0;
  min-width: 140px;
}

.header .logo:hover {
  transform: scale(1.05);
}

.header .logo h1 {
  margin: 0;
  font-size: 20px;
  color: #1d1d1f;
  font-weight: 600;
  letter-spacing: -0.02em;
}

.header .nav-menu {
  flex: 1;
  border-bottom: none;
  margin-left: 20px;
  background: transparent;
  min-width: 0;
  overflow: hidden;
}

.header .nav-menu >>> .el-menu {
  display: flex !important;
  flex-wrap: nowrap !important;
  overflow: hidden !important;
}

.header .nav-menu >>> .el-menu--horizontal {
  border-bottom: none !important;
}

.header .nav-menu >>> .el-menu-item {
  color: #86868b;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.3s ease;
  border-bottom: 2px solid transparent;
  white-space: nowrap !important;
  padding: 0 12px !important;
  margin: 0 1px !important;
  min-width: auto !important;
  flex-shrink: 1 !important;
  display: inline-flex !important;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
}

.header .nav-menu >>> .el-menu-item:hover {
  color: #1d1d1f;
  background: rgba(0, 0, 0, 0.05);
  border-bottom-color: rgba(0, 113, 227, 0.5);
}

.header .nav-menu >>> .el-menu-item.is-active {
  color: #0071e3;
  background: rgba(0, 113, 227, 0.05);
  border-bottom-color: #0071e3;
  font-weight: 600;
}

.header .user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: 16px;
  flex-shrink: 0;
}

.header .user-info .language-selector {
  cursor: pointer;
  color: #86868b;
  font-size: 14px;
  padding: 6px 12px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.header .user-info .language-selector:hover {
  background: rgba(0, 0, 0, 0.05);
  color: #1d1d1f;
}

.header .user-info .user-name {
  cursor: pointer;
  color: #86868b;
  font-weight: 500;
  padding: 6px 16px;
  border-radius: 20px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  white-space: nowrap;
}

.header .user-info .user-name .username-text {
  flex-shrink: 0;
}

.header .user-info .user-name .admin-tag {
  flex-shrink: 0;
  vertical-align: middle;
  margin-left: 5px;
  line-height: 1.2;
  display: inline-flex;
  align-items: center;
}

.header .user-info .user-name .user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(0, 0, 0, 0.1);
  background: rgba(0, 0, 0, 0.05);
  flex-shrink: 0;
  display: block;
}

.header .user-info .user-name .el-icon-arrow-down {
  flex-shrink: 0;
  margin-left: 4px;
  font-size: 12px;
  vertical-align: middle;
}

.header .user-info .user-name:hover {
  background: rgba(0, 0, 0, 0.05);
  color: #1d1d1f;
}

.header .user-info >>> .el-button {
  color: #86868b;
}

.header .user-info >>> .el-button.el-button--text:hover {
  color: #1d1d1f;
}

.header .user-info >>> .el-button.el-button--primary {
  background: #0071e3;
  border-color: #0071e3;
}

.header .user-info >>> .el-button.el-button--primary:hover {
  background: #0077ed;
  border-color: #0077ed;
}

.header .user-info >>> .el-dropdown-menu {
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

@media (max-width: 1200px) {
  .header .container {
    width: 100%;
    padding: 0 16px;
  }
}

@media (max-width: 992px) {
  .header .nav-menu {
    margin-left: 20px;
  }
  
  .header .nav-menu >>> .el-menu-item {
    padding: 0 12px !important;
    font-size: 14px;
    margin: 0 1px;
  }
}

@media (max-width: 768px) {
  .header {
    height: 60px !important;
    line-height: 60px;
  }
  
  .header .logo h1 {
    font-size: 20px;
  }
  
  .header .nav-menu {
    margin-left: 10px;
  }
  
  .header .nav-menu >>> .el-menu-item {
    padding: 0 8px !important;
    font-size: 13px;
    margin: 0 1px;
  }
  
  .header .user-info {
    gap: 8px;
    margin-left: 10px;
  }
}
</style>

