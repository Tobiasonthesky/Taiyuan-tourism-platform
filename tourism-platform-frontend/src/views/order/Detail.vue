<template>
  <div class="order-detail-page">
    <div class="container" v-if="order">
      <h1 class="page-title">{{ $t('order.orderDetail') }}</h1>
      
      <el-card class="order-info">
        <h2>{{ $t('order.orderInfo') }}</h2>
        <el-descriptions :column="2" border>
          <el-descriptions-item :label="$t('order.orderNo')">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.orderType')">
            <el-tag v-if="order.orderType === 'ticket'">{{ $t('order.ticket') }}</el-tag>
            <el-tag v-else-if="order.orderType === 'hotel'" type="success">{{ $t('order.hotel') }}</el-tag>
            <el-tag v-else-if="order.orderType === 'experience'" type="warning">{{ $t('order.experience') }}</el-tag>
            <span v-else>{{ order.orderType }}</span>
          </el-descriptions-item>
          <el-descriptions-item :label="$t('order.status')">
            <el-tag :type="getStatusType(order.orderStatus)">
              {{ getStatusText(order.orderStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item :label="$t('order.totalAmount')">¥{{ order.totalAmount }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.contactName')">{{ order.contactName }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.contactPhone')">{{ order.contactPhone }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.createTime')">{{ order.createTime }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.remark')" :span="2">{{ order.remark || $t('common.none') }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="order-items">
        <h2>{{ $t('order.items') }}</h2>
        <el-table :data="orderItems" style="width: 100%">
          <el-table-column prop="itemName" :label="$t('order.itemName')" width="200" />
          <el-table-column prop="itemType" :label="$t('common.category')" width="100">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.itemType === 'ticket'" type="success">{{ $t('order.ticket') }}</el-tag>
              <el-tag v-else-if="scope.row.itemType === 'hotel'" type="warning">{{ $t('order.hotel') }}</el-tag>
              <el-tag v-else-if="scope.row.itemType === 'experience'" type="info">{{ $t('order.experience') }}</el-tag>
              <span v-else>{{ scope.row.itemType }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('order.detailInfo')" width="350">
            <template slot-scope="scope">
              <!-- 酒店订单显示房间信息 -->
              <div v-if="scope.row.itemType === 'hotel' && (scope.row.roomType || scope.row.roomName)">
                <div><strong>{{ $t('hotel.roomType') }}：</strong>{{ scope.row.roomType || '-' }}</div>
                <div><strong>{{ $t('hotel.roomName') }}：</strong>{{ scope.row.roomName || '-' }}</div>
                <div v-if="scope.row.checkInDate || scope.row.checkOutDate">
                  <strong>{{ $t('hotel.checkIn') }}：</strong>{{ scope.row.checkInDate || '-' }}<br>
                  <strong>{{ $t('hotel.checkOut') }}：</strong>{{ scope.row.checkOutDate || '-' }}
                </div>
              </div>
              <!-- 门票订单显示使用日期 -->
              <div v-else-if="scope.row.itemType === 'ticket' && scope.row.useDate">
                <div><strong>{{ $t('attraction.useDate') }}：</strong>{{ formatDate(scope.row.useDate) }}</div>
              </div>
              <!-- 体验项目显示使用日期和时间 -->
              <div v-else-if="scope.row.itemType === 'experience'">
                <div v-if="scope.row.useDate">
                  <strong>{{ $t('experience.useDate') }}：</strong>{{ formatDate(scope.row.useDate) }}
                </div>
                <div v-if="scope.row.useTime">
                  <strong>{{ $t('experience.useTime') }}：</strong>{{ scope.row.useTime }}
                </div>
              </div>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="quantity" :label="$t('order.quantity')" width="80" align="center" />
          <el-table-column :label="$t('order.unitPrice')" width="120" align="right">
            <template slot-scope="scope">
              ¥{{ scope.row.unitPrice || scope.row.roomPrice || scope.row.price || 0 }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('order.subtotal')" width="120" align="right">
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
          {{ $t('order.payNow') }}
        </el-button>
        <el-button
          v-if="order.orderStatus === 0"
          @click="handleCancel"
        >
          {{ $t('order.cancel') }}
        </el-button>
        <el-button @click="$router.back()">{{ $t('common.back') }}</el-button>
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
        await this.$confirm(this.$t('order.confirmCancel'), this.$t('admin.tip'), {
          type: 'warning'
        })
        await cancelOrder(this.order.id)
        this.$message.success(this.$t('order.cancelSuccess'))
        this.loadDetail()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error(this.$t('order.cancelFailed'))
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
        0: this.$t('order.pending'),
        1: this.$t('order.paid'),
        2: this.$t('order.used'),
        3: this.$t('order.cancelled'),
        4: this.$t('order.refunded')
      }
      return texts[status] || this.$t('common.unknown')
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

