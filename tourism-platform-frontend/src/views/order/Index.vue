<template>
  <div class="order-page">
    <div class="container">
      <h1 class="page-title">我的订单</h1>
      
      <el-tabs 
        v-model="activeTab" 
        @tab-click="handleTabClick"
      >
        <el-tab-pane label="全部" name="all"></el-tab-pane>
        <el-tab-pane label="待支付" name="0"></el-tab-pane>
        <el-tab-pane label="已支付" name="1"></el-tab-pane>
        <el-tab-pane label="已使用" name="2"></el-tab-pane>
        <el-tab-pane label="已取消" name="3"></el-tab-pane>
      </el-tabs>

      <el-table :data="orders" style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column prop="orderType" label="订单类型" width="120" />
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="orderStatus" label="状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.orderStatus)">
              {{ getStatusText(scope.row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="viewDetail(scope.row.id)"
            >
              查看详情
            </el-button>
            <el-button
              v-if="scope.row.orderStatus === 0"
              type="text"
              @click="payOrder(scope.row.id)"
            >
              支付
            </el-button>
            <el-button
              v-if="scope.row.orderStatus === 0"
              type="text"
              @click="cancelOrder(scope.row.id)"
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <Pagination
        :current-page="page"
        :page-size="size"
        :total="total"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
import { getOrders, cancelOrder, payOrder } from '@/api/order'
import Pagination from '@/components/common/Pagination'

export default {
  name: 'OrderIndex',
  components: {
    Pagination
  },
  data() {
    return {
      orders: [],
      activeTab: 'all', // 使用 'all' 表示"全部"
      page: 1,
      size: 10,
      total: 0
    }
  },
  mounted() {
    // 检查路由参数，如果有orderStatus参数，设置对应的标签
    const orderStatus = this.$route.query.orderStatus
    if (orderStatus !== undefined && orderStatus !== null && orderStatus !== '') {
      this.activeTab = String(orderStatus)
    } else {
      // 确保初始值为 'all'，表示"全部"
      this.activeTab = 'all'
    }
    // 延迟加载，确保 activeTab 已设置
    this.$nextTick(() => {
      this.loadOrders()
    })
  },
  watch: {
    // 监听 activeTab 变化，确保切换标签时加载数据
    activeTab(newVal, oldVal) {
      // 只在值真正改变时重新加载（排除初始化）
      if (oldVal !== undefined && newVal !== oldVal) {
        this.page = 1
        this.loadOrders()
      }
    }
  },
  methods: {
    async loadOrders() {
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        // 只有当 activeTab 不为 'all' 时才添加 orderStatus 参数
        // 'all' 表示"全部"，不传 orderStatus 参数，后端会返回所有订单
        if (this.activeTab && this.activeTab !== 'all' && this.activeTab !== '' && this.activeTab !== null && this.activeTab !== undefined) {
          const status = parseInt(this.activeTab)
          if (!isNaN(status)) {
            params.orderStatus = status
          }
        }
        // 调试信息
        console.log('加载订单参数:', params, 'activeTab:', this.activeTab)

        const res = await getOrders(params)
        console.log('订单列表响应:', res)
        if (res.code === 200) {
          this.orders = res.data?.records || []
          this.total = res.data?.total || 0
          console.log('订单数量:', this.orders.length, '总数:', this.total)
        } else {
          console.warn('加载订单失败:', res)
          this.$message.warning(res.message || '加载订单失败')
        }
      } catch (error) {
        console.error('加载订单失败:', error)
        this.$message.error('加载订单失败：' + (error.response?.data?.message || error.message || '未知错误'))
      }
    },
    handleTabClick(tab) {
      // v-model 会自动更新 activeTab，这里只需要处理数据加载
      // 确保 activeTab 的值正确
      const newTab = tab.name !== undefined ? String(tab.name) : 'all'
      if (this.activeTab !== newTab) {
        this.$nextTick(() => {
          this.activeTab = newTab
        })
      }
      this.page = 1
      // 直接调用 loadOrders，不依赖 watch
      this.loadOrders()
    },
    handlePageChange(val) {
      this.page = val
      this.loadOrders()
    },
    viewDetail(id) {
      this.$router.push(`/order/${id}`)
    },
    async cancelOrder(id) {
      try {
        await this.$confirm('确定要取消订单吗？', '提示', {
          type: 'warning'
        })
        await cancelOrder(id)
        this.$message.success('订单已取消')
        this.loadOrders()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('取消订单失败')
        }
      }
    },
    async payOrder(id) {
      try {
        await payOrder(id, 'alipay')
        this.$message.success('支付成功')
        this.loadOrders()
      } catch (error) {
        this.$message.error('支付失败')
      }
    },
    getStatusType(status) {
      const types = {
        0: 'warning',  // 待支付
        1: 'success',  // 已支付
        2: 'info',     // 已使用
        3: 'danger',   // 已取消
        4: 'info'      // 已退款
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = {
        0: '待支付',
        1: '已支付',
        2: '已使用',
        3: '已取消',
        4: '已退款'
      }
      return texts[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.order-page {
  padding: 20px 0;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .page-title {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 20px;
  }

  // 确保标签页可以正常点击
  ::v-deep .el-tabs__item {
    cursor: pointer;
    user-select: none;
    
    &:hover {
      color: #409eff;
    }
    
    &.is-active {
      color: #409eff;
      font-weight: 500;
    }
  }

  // 确保标签页下划线正确显示
  ::v-deep .el-tabs__header {
    margin-bottom: 0;
  }

  ::v-deep .el-tabs__nav-wrap {
    position: relative;
  }

  ::v-deep .el-tabs__active-bar {
    background-color: #409eff;
    height: 3px;
    transition: transform 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
    position: absolute;
    bottom: 0;
    left: 0;
    z-index: 1;
  }

  ::v-deep .el-tabs__item {
    position: relative;
    padding: 0 20px;
    height: 40px;
    line-height: 40px;
    box-sizing: border-box;
  }

  // 确保标签页内容区域正确显示
  ::v-deep .el-tabs__content {
    overflow: visible;
  }
}
</style>

