<template>
  <div class="admin-page">
    <div class="container">
      <h1 class="page-title">{{ $t('admin.dashboard') }}</h1>
      
      <!-- 统计卡片 -->
      <div class="stats-grid">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ statistics.userCount }}</div>
            <div class="stat-label">{{ $t('admin.totalUsers') }}</div>
          </div>
        </el-card>
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ statistics.adminCount }}</div>
            <div class="stat-label">{{ $t('admin.adminCount') }}</div>
          </div>
        </el-card>
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ statistics.attractionCount }}</div>
            <div class="stat-label">{{ $t('admin.attractionCount') }}</div>
          </div>
        </el-card>
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ statistics.orderCount }}</div>
            <div class="stat-label">{{ $t('admin.orderCount') }}</div>
          </div>
        </el-card>
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ statistics.foodCount || 0 }}</div>
            <div class="stat-label">{{ $t('admin.foodCount') }}</div>
          </div>
        </el-card>
        <el-card class="stat-card pending-card" @click.native="goToManage('audit')" style="cursor: pointer;">
          <div class="stat-content">
            <div class="stat-value" style="color: #E6A23C;">{{ statistics.pendingCount || 0 }}</div>
            <div class="stat-label">{{ $t('admin.pendingCount') }}</div>
          </div>
        </el-card>
      </div>

      <!-- 功能菜单 -->
      <el-card class="menu-card">
        <h2 class="menu-title">{{ $t('admin.functionManagement') }}</h2>
        <div class="menu-grid">
          <el-card class="menu-item" @click.native="goToManage('users')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-user"></i>
            </div>
            <h3>{{ $t('admin.userManagement') }}</h3>
            <p>{{ $t('admin.userManagementDesc') }}</p>
          </el-card>
          <el-card class="menu-item" @click.native="goToManage('attractions')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-picture-outline"></i>
            </div>
            <h3>{{ $t('admin.attractionManagement') }}</h3>
            <p>{{ $t('admin.attractionManagementDesc') }}</p>
          </el-card>
          <el-card class="menu-item" @click.native="goToManage('foods')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-food"></i>
            </div>
            <h3>{{ $t('admin.foodManagement') }}</h3>
            <p>{{ $t('admin.foodManagementDesc') }}</p>
          </el-card>
          <el-card class="menu-item" @click.native="goToManage('cultures')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-trophy"></i>
            </div>
            <h3>{{ $t('admin.cultureManagement') }}</h3>
            <p>{{ $t('admin.cultureManagementDesc') }}</p>
          </el-card>
          <el-card class="menu-item" @click.native="goToManage('strategies')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-document"></i>
            </div>
            <h3>{{ $t('admin.strategyManagement') }}</h3>
            <p>{{ $t('admin.strategyManagementDesc') }}</p>
          </el-card>
          <el-card class="menu-item" @click.native="goToManage('hotels')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-office-building"></i>
            </div>
            <h3>{{ $t('admin.hotelManagement') }}</h3>
            <p>{{ $t('admin.hotelManagementDesc') }}</p>
          </el-card>
          <el-card class="menu-item" @click.native="goToManage('experiences')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-star-on"></i>
            </div>
            <h3>{{ $t('admin.experienceManagement') }}</h3>
            <p>{{ $t('admin.experienceManagementDesc') }}</p>
          </el-card>
          <el-card class="menu-item" @click.native="goToManage('orders')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-s-order"></i>
            </div>
            <h3>{{ $t('admin.orderManagement') }}</h3>
            <p>{{ $t('admin.orderManagementDesc') }}</p>
          </el-card>
          <el-card class="menu-item" @click.native="goToManage('announcements')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-bell"></i>
            </div>
            <h3>{{ $t('admin.announcementManagement') }}</h3>
            <p>{{ $t('admin.announcementManagementDesc') }}</p>
          </el-card>
          <el-card class="menu-item" @click.native="goToManage('audit')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-check"></i>
            </div>
            <h3>{{ $t('admin.contentAudit') }}</h3>
            <p>{{ $t('admin.contentAuditDesc') }}</p>
          </el-card>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getStatistics } from '@/api/admin'

export default {
  name: 'AdminIndex',
  data() {
    return {
      statistics: {
        userCount: 0,
        adminCount: 0,
        attractionCount: 0,
        orderCount: 0,
        foodCount: 0,
        pendingCount: 0
      }
    }
  },
  mounted() {
    this.loadStatistics()
  },
  activated() {
    // 当从其他页面返回时刷新统计数据（keep-alive 组件）
    this.loadStatistics()
  },
  watch: {
    // 监听路由变化，当从审核页面返回时刷新统计数据
    '$route'(to, from) {
      if (from && from.path === '/admin/audit' && to.path === '/admin') {
        this.loadStatistics()
      }
    }
  },
  methods: {
    async loadStatistics() {
      try {
        const res = await getStatistics()
        if (res.code === 200) {
          this.statistics = res.data
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    goToManage(type) {
      const routes = {
        users: '/admin/users',
        attractions: '/admin/attractions',
        foods: '/admin/foods',
        cultures: '/admin/cultures',
        strategies: '/admin/strategies',
        hotels: '/admin/hotels',
        experiences: '/admin/experiences',
        orders: '/admin/orders',
        announcements: '/admin/announcements',
        audit: '/admin/audit'
      }
      if (routes[type]) {
        this.$router.push(routes[type])
      } else {
        this.$message.info(`${type}管理功能开发中...`)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-page {
  padding: 20px 0;
  min-height: calc(100vh - 140px);

  .container {
    width: 1200px;
    max-width: 100%;
    margin: 0 auto;
    padding: 0 20px;
  }

  .page-title {
    font-size: 28px;
    font-weight: 700;
    margin-bottom: 30px;
    color: #303133;
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 20px;
    margin-bottom: 30px;
  }

  .pending-card {
    &:hover {
      border-color: #E6A23C;
    }
  }

  .stat-card {
    transition: all 0.3s;
    border-radius: 12px;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
    }

    .stat-content {
      text-align: center;
      padding: 20px;

      .stat-value {
        font-size: 36px;
        font-weight: bold;
        color: #409eff;
        margin-bottom: 10px;
        line-height: 1;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }

  .menu-card {
    border-radius: 16px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);

    .menu-title {
      font-size: 24px;
      font-weight: 700;
      margin-bottom: 30px;
      color: #303133;
      padding-bottom: 15px;
      border-bottom: 2px solid #f0f0f0;
    }

    .menu-grid {
      display: grid;
      grid-template-columns: repeat(5, 1fr);
      gap: 20px;
    }

    .menu-item {
      cursor: pointer;
      text-align: center;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      border-radius: 12px;
      border: 1px solid #EBEEF5;
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: flex-start;
      min-height: 180px;
      position: relative;

      ::v-deep .el-card__body {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start;
        padding: 24px 16px;
      }

      &:hover {
        transform: translateY(-8px);
        box-shadow: 0 12px 24px rgba(64, 158, 255, 0.15);
        border-color: #409eff;
        background: linear-gradient(135deg, #f5f7fa 0%, #ffffff 100%);
      }

      .menu-icon-wrapper {
        width: 64px;
        height: 64px;
        min-width: 64px;
        min-height: 64px;
        max-width: 64px;
        max-height: 64px;
        border-radius: 16px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 16px auto;
        transition: all 0.3s;
        flex-shrink: 0;
        position: relative;
        box-sizing: border-box;

        i {
          font-size: 32px;
          color: #ffffff;
          line-height: 1;
          display: inline-flex;
          align-items: center;
          justify-content: center;
          width: 32px;
          height: 32px;
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          box-sizing: border-box;
        }
      }

      &:hover .menu-icon-wrapper {
        transform: scale(1.1) rotate(5deg);
        box-shadow: 0 8px 16px rgba(102, 126, 234, 0.3);
        
        i {
          transform: translate(-50%, -50%) scale(1.05);
        }
      }

      h3 {
        font-size: 16px;
        font-weight: 600;
        margin: 0 0 8px 0;
        color: #303133;
        line-height: 1.4;
        width: 100%;
        text-align: center;
        min-height: 44px;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      p {
        color: #909399;
        font-size: 13px;
        line-height: 1.5;
        margin: 0;
        width: 100%;
        text-align: center;
        min-height: 39px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
  }
}

/* 响应式设计 - 平板 */
@media (max-width: 992px) {
  .admin-page .container {
    padding: 0 16px;
  }

  .admin-page .stats-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;
  }

  .admin-page .menu-card .menu-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;
  }
}

/* 响应式设计 - 手机 */
@media (max-width: 768px) {
  .admin-page {
    padding: 15px 0;
  }

  .admin-page .container {
    padding: 0 12px;
  }

  .admin-page .page-title {
    font-size: 22px;
    margin-bottom: 20px;
  }

  .admin-page .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
    margin-bottom: 20px;
  }

  .admin-page .stat-card .stat-content {
    padding: 15px;
  }

  .admin-page .stat-card .stat-content .stat-value {
    font-size: 28px;
  }

  .admin-page .menu-card {
    padding: 16px;
  }

  .admin-page .menu-card .menu-title {
    font-size: 20px;
    margin-bottom: 20px;
    padding-bottom: 12px;
  }

  .admin-page .menu-card .menu-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
  }

  .admin-page .menu-card .menu-item {
    min-height: 160px;

    ::v-deep .el-card__body {
      padding: 20px 12px;
    }
  }

  .admin-page .menu-card .menu-item .menu-icon-wrapper {
    width: 56px;
    height: 56px;
    min-width: 56px;
    min-height: 56px;
    max-width: 56px;
    max-height: 56px;
    margin: 0 auto 12px auto;

    i {
      font-size: 28px;
      width: 28px;
      height: 28px;
    }
  }

  .admin-page .menu-card .menu-item h3 {
    min-height: 40px;
    font-size: 15px;
  }

  .admin-page .menu-card .menu-item p {
    min-height: 36px;
    font-size: 12px;
  }

  .admin-page .menu-card .menu-item h3 {
    font-size: 15px;
    margin-bottom: 6px;
  }

  .admin-page .menu-card .menu-item p {
    font-size: 12px;
  }
}

/* 超小屏幕 */
@media (max-width: 480px) {
  .admin-page .stats-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .admin-page .menu-card .menu-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .admin-page .menu-card .menu-item {
    min-height: 140px;
  }
}
</style>

