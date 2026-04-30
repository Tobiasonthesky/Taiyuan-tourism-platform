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

      <!-- 图表区域 -->
      <div class="charts-grid">
        <el-card class="chart-card">
          <h3 class="chart-title">{{ $t('admin.contentDistribution') }}</h3>
          <div ref="contentPieRef" class="chart-container"></div>
        </el-card>
        <el-card class="chart-card">
          <h3 class="chart-title">{{ $t('admin.hotAttractions') }}</h3>
          <div ref="hotAttractionsRef" class="chart-container"></div>
        </el-card>
        <el-card class="chart-card">
          <h3 class="chart-title">{{ $t('admin.orderDistribution') }}</h3>
          <div ref="orderPieRef" class="chart-container"></div>
        </el-card>
        <el-card class="chart-card">
          <h3 class="chart-title">{{ $t('admin.commentStatistics') }}</h3>
          <div ref="commentBarRef" class="chart-container"></div>
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
          <el-card class="menu-item" @click.native="goToManage('systemLogs')">
            <div class="menu-icon-wrapper">
              <i class="el-icon-document-copy"></i>
            </div>
            <h3>{{ $t('admin.systemLogManagement') }}</h3>
            <p>{{ $t('admin.systemLogManagementDesc') }}</p>
          </el-card>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getStatistics, getChartData } from '@/api/admin'

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
      },
      chartData: null,
      charts: []
    }
  },
  mounted() {
    this.loadStatistics()
    this.loadChartData()
    window.addEventListener('resize', this.handleResize)
    this.$i18n.on('localeChanged', this.handleLocaleChange)
  },
  activated() {
    this.loadStatistics()
    this.loadChartData()
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.$i18n.off('localeChanged', this.handleLocaleChange)
    this.disposeCharts()
  },
  watch: {
    '$route'(to, from) {
      if (from && from.path === '/admin/audit' && to.path === '/admin') {
        this.loadStatistics()
        this.loadChartData()
      }
    }
  },
  methods: {
    handleLocaleChange() {
      this.loadChartData()
    },
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
    async loadChartData() {
      try {
        const res = await getChartData()
        if (res.code === 200) {
          this.chartData = res.data
          this.initCharts()
        }
      } catch (error) {
        console.error('加载图表数据失败:', error)
      }
    },
    initCharts() {
      this.disposeCharts()
      
      if (!this.chartData) return
      
      const translateLabel = (label) => {
        const labelMap = {
          'attraction': this.$t('common.attraction'),
          'food': this.$t('common.food'),
          'culture': this.$t('common.culture'),
          'strategy': this.$t('common.strategy'),
          'hotel': this.$t('common.hotel'),
          'experience': this.$t('common.experience'),
          'ticket': this.$t('order.ticket')
        }
        return labelMap[label] || label
      }
      
      // 内容分布饼图
      const contentPieChart = echarts.init(this.$refs.contentPieRef)
      contentPieChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 8,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 14,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: this.chartData.contentPie.labels.map((label, index) => ({
            value: this.chartData.contentPie.values[index],
            name: translateLabel(label),
            itemStyle: {
              color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272'][index]
            }
          }))
        }]
      })
      this.charts.push(contentPieChart)
      
      // 热门景点柱状图
      const hotAttractionsChart = echarts.init(this.$refs.hotAttractionsRef)
      hotAttractionsChart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.chartData.hotAttractionsBar.names,
          axisLabel: {
            interval: 0,
            rotate: 30,
            fontSize: 11
          }
        },
        yAxis: {
          type: 'value',
          name: this.$t('common.viewCount')
        },
        series: [{
          type: 'bar',
          data: this.chartData.hotAttractionsBar.viewCounts,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ]),
            borderRadius: [4, 4, 0, 0]
          }
        }]
      })
      this.charts.push(hotAttractionsChart)
      
      // 订单分布饼图
      const orderPieChart = echarts.init(this.$refs.orderPieRef)
      orderPieChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 8,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 14,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: this.chartData.orderPie.labels.map((label, index) => ({
            value: this.chartData.orderPie.values[index],
            name: translateLabel(label),
            itemStyle: {
              color: ['#5470c6', '#91cc75', '#fac858'][index]
            }
          }))
        }]
      })
      this.charts.push(orderPieChart)
      
      // 评论统计柱状图
      const commentBarChart = echarts.init(this.$refs.commentBarRef)
      commentBarChart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.chartData.commentBar.labels.map(label => translateLabel(label))
        },
        yAxis: {
          type: 'value',
          name: this.$t('comment.comments')
        },
        series: [{
          type: 'bar',
          data: this.chartData.commentBar.values,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#ee6666' },
              { offset: 0.5, color: '#ff9999' },
              { offset: 1, color: '#ffcccc' }
            ]),
            borderRadius: [4, 4, 0, 0]
          }
        }]
      })
      this.charts.push(commentBarChart)
    },
    handleResize() {
      this.charts.forEach(chart => {
        chart.resize()
      })
    },
    disposeCharts() {
      this.charts.forEach(chart => {
        chart.dispose()
      })
      this.charts = []
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
        audit: '/admin/audit',
        systemLogs: '/admin/system-logs'
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
  padding: 20px;
  background: #f5f7fa;

  .container {
    max-width: 1400px;
    margin: 0 auto;
  }

  .page-title {
    font-size: 24px;
    font-weight: 600;
    margin-bottom: 24px;
    color: #303133;
    padding-left: 12px;
    border-left: 4px solid #409eff;
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 16px;
    margin-bottom: 24px;
  }

  .charts-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
    margin-bottom: 24px;
  }

  .chart-card {
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .chart-title {
      font-size: 15px;
      font-weight: 600;
      color: #303133;
      margin: 14px 16px 10px;
      padding-bottom: 8px;
      border-bottom: 1px solid #f0f0f0;
    }

    .chart-container {
      width: 100%;
      height: 300px;
    }
  }

  .pending-card {
    &:hover {
      border-color: #E6A23C;
    }
  }

  .stat-card {
    transition: all 0.3s;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
    }

    .stat-content {
      text-align: center;
      padding: 16px;

      .stat-value {
        font-size: 32px;
        font-weight: bold;
        color: #409eff;
        margin-bottom: 8px;
        line-height: 1.2;
      }

      .stat-label {
        font-size: 13px;
        color: #909399;
      }
    }
  }

  .menu-card {
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .menu-title {
      font-size: 20px;
      font-weight: 600;
      margin-bottom: 20px;
      color: #303133;
      padding-bottom: 12px;
      border-bottom: 1px solid #e0e0e0;
    }

    .menu-grid {
      display: grid;
      grid-template-columns: repeat(5, 1fr);
      gap: 16px;
    }

    .menu-item {
      cursor: pointer;
      text-align: center;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      border-radius: 8px;
      border: 1px solid #e8e8e8;
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      min-height: 160px;
      position: relative;

      ::v-deep .el-card__body {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding: 20px 12px;
      }

      &:hover {
        transform: translateY(-6px);
        box-shadow: 0 8px 20px rgba(64, 158, 255, 0.12);
        border-color: #409eff;
        background: linear-gradient(135deg, #f0f7ff 0%, #ffffff 100%);
      }

      .menu-icon-wrapper {
        width: 56px;
        height: 56px;
        min-width: 56px;
        min-height: 56px;
        max-width: 56px;
        max-height: 56px;
        border-radius: 12px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 12px auto;
        transition: all 0.3s;
        flex-shrink: 0;
        position: relative;
        box-sizing: border-box;

        i {
          font-size: 28px;
          color: #ffffff;
          line-height: 1;
          display: inline-flex;
          align-items: center;
          justify-content: center;
          width: 28px;
          height: 28px;
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          box-sizing: border-box;
        }
      }

      &:hover .menu-icon-wrapper {
        transform: scale(1.08) rotate(3deg);
        box-shadow: 0 6px 14px rgba(102, 126, 234, 0.25);
        
        i {
          transform: translate(-50%, -50%) scale(1.03);
        }
      }

      h3 {
        font-size: 15px;
        font-weight: 600;
        margin: 0 0 6px 0;
        color: #303133;
        line-height: 1.4;
        width: 100%;
        text-align: center;
        min-height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      p {
        color: #909399;
        font-size: 12px;
        line-height: 1.4;
        margin: 0;
        width: 100%;
        text-align: center;
        min-height: 34px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
  }
}

/* 响应式设计 - 平板 */
@media (max-width: 992px) {
  .admin-page .stats-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 12px;
  }

  .admin-page .charts-grid {
    grid-template-columns: 1fr;
  }

  .admin-page .menu-card .menu-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 12px;
  }
}

/* 响应式设计 - 手机 */
@media (max-width: 768px) {
  .admin-page {
    padding: 15px;
  }

  .admin-page .page-title {
    font-size: 20px;
    margin-bottom: 16px;
  }

  .admin-page .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
    margin-bottom: 16px;
  }

  .admin-page .stat-card .stat-content {
    padding: 12px;
  }

  .admin-page .stat-card .stat-content .stat-value {
    font-size: 24px;
  }

  .admin-page .menu-card .menu-title {
    font-size: 18px;
    margin-bottom: 16px;
  }

  .admin-page .menu-card .menu-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }

  .admin-page .menu-card .menu-item {
    min-height: 140px;

    ::v-deep .el-card__body {
      padding: 16px 10px;
    }
  }

  .admin-page .menu-card .menu-item .menu-icon-wrapper {
    width: 48px;
    height: 48px;
    min-width: 48px;
    min-height: 48px;
    max-width: 48px;
    max-height: 48px;
    margin: 0 auto 10px auto;

    i {
      font-size: 24px;
      width: 24px;
      height: 24px;
    }
  }

  .admin-page .menu-card .menu-item h3 {
    font-size: 14px;
    margin-bottom: 4px;
  }

  .admin-page .menu-card .menu-item p {
    font-size: 11px;
  }
}

/* 超小屏幕 */
@media (max-width: 480px) {
  .admin-page .stats-grid {
    grid-template-columns: 1fr;
    gap: 10px;
  }

  .admin-page .menu-card .menu-grid {
    grid-template-columns: 1fr;
    gap: 10px;
  }
}
</style>

