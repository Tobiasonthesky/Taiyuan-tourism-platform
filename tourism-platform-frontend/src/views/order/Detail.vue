<template>
  <div class="order-detail-page">
    <div class="container" v-if="order">
      <h1 class="page-title">订单详情</h1>
      
      <el-card class="order-info">
        <h2>订单信息</h2>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="订单类型">{{ order.orderType }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(order.orderStatus)">
              {{ getStatusText(order.orderStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="总金额">¥{{ order.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ order.contactName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ order.contactPhone }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ order.createTime }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ order.remark || '无' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="order-items">
        <h2>订单项</h2>
        <el-table :data="orderItems" style="width: 100%">
          <el-table-column prop="itemName" label="项目名称" width="200" />
          <el-table-column prop="itemType" label="类型" width="100">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.itemType === 'ticket'" type="success">门票</el-tag>
              <el-tag v-else-if="scope.row.itemType === 'hotel'" type="warning">酒店</el-tag>
              <el-tag v-else-if="scope.row.itemType === 'experience'" type="info">体验</el-tag>
              <span v-else>{{ scope.row.itemType }}</span>
            </template>
          </el-table-column>
          <el-table-column label="详细信息" width="350">
            <template slot-scope="scope">
              <!-- 酒店订单显示房间信息 -->
              <div v-if="scope.row.itemType === 'hotel' && (scope.row.roomType || scope.row.roomName)">
                <div><strong>房间类型：</strong>{{ scope.row.roomType || '-' }}</div>
                <div><strong>房间名称：</strong>{{ scope.row.roomName || '-' }}</div>
                <div v-if="scope.row.checkInDate || scope.row.checkOutDate">
                  <strong>入住日期：</strong>{{ scope.row.checkInDate || '-' }}<br>
                  <strong>退房日期：</strong>{{ scope.row.checkOutDate || '-' }}
                </div>
              </div>
              <!-- 门票订单显示使用日期 -->
              <div v-else-if="scope.row.itemType === 'ticket' && scope.row.useDate">
                <div><strong>使用日期：</strong>{{ formatDate(scope.row.useDate) }}</div>
              </div>
              <!-- 体验项目显示使用日期和时间 -->
              <div v-else-if="scope.row.itemType === 'experience'">
                <div v-if="scope.row.useDate">
                  <strong>使用日期：</strong>{{ formatDate(scope.row.useDate) }}
                </div>
                <div v-if="scope.row.useTime">
                  <strong>使用时间：</strong>{{ scope.row.useTime }}
                </div>
              </div>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="80" align="center" />
          <el-table-column label="单价" width="120" align="right">
            <template slot-scope="scope">
              ¥{{ scope.row.unitPrice || scope.row.roomPrice || scope.row.price || 0 }}
            </template>
          </el-table-column>
          <el-table-column label="小计" width="120" align="right">
            <template slot-scope="scope">
              ¥{{ scope.row.totalPrice || scope.row.subtotal || 0 }}
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <div class="actions">
        <el-button
          v-if="order.orderStatus === 0"
          type="primary"
          @click="handlePay"
        >
          立即支付
        </el-button>
        <el-button
          v-if="order.orderStatus === 0"
          @click="handleCancel"
        >
          取消订单
        </el-button>
        <el-button @click="$router.back()">返回</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getOrderDetail, getOrderItems, cancelOrder, payOrder } from '@/api/order'

export default {
  name: 'OrderDetail',
  data() {
    return {
      order: null,
      orderItems: []
    }
  },
  computed: {
    isHotelOrder() {
      return this.order && this.order.orderType === 'hotel'
    }
  },
  mounted() {
    this.loadDetail()
  },
  methods: {
    async loadDetail() {
      try {
        const id = this.$route.params.id
        const orderRes = await getOrderDetail(id)
        
        if (orderRes.code === 200) {
          const data = orderRes.data
          if (data.order && data.items) {
            // 新格式：返回OrderDetailVO
            this.order = data.order
            this.orderItems = data.items || []
          } else {
            // 兼容旧格式
            this.order = data.order || data
            // 如果items不在data中，单独获取
            if (!data.items) {
              const itemsRes = await getOrderItems(id)
              if (itemsRes.code === 200) {
                this.orderItems = itemsRes.data || []
              }
            }
          }
        }
      } catch (error) {
        this.$message.error('加载订单详情失败')
      }
    },
    async handlePay() {
      try {
        await payOrder(this.order.id, 'alipay')
        this.$message.success('支付成功')
        this.loadDetail()
      } catch (error) {
        this.$message.error('支付失败')
      }
    },
    async handleCancel() {
      try {
        await this.$confirm('确定要取消订单吗？', '提示', {
          type: 'warning'
        })
        await cancelOrder(this.order.id)
        this.$message.success('订单已取消')
        this.loadDetail()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('取消订单失败')
        }
      }
    },
    getStatusType(status) {
      const types = {
        0: 'warning',
        1: 'success',
        2: 'info'
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
    },
    formatDate(date) {
      if (!date) return '-'
      if (typeof date === 'string') return date
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>

<style lang="scss" scoped>
.order-detail-page {
  padding: 20px 0;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .order-info,
  .order-items {
    margin-bottom: 20px;

    h2 {
      font-size: 20px;
      margin-bottom: 15px;
      color: #303133;
    }
  }

  .actions {
    text-align: center;
    margin-top: 30px;

    .el-button {
      margin: 0 10px;
    }
  }
}
</style>

