<template>
  <div class="admin-orders">
    <el-card>
      <div slot="header" class="card-header">
        <span>{{ $t('admin.orderManagement') }}</span>
        <div class="card-actions">
          <el-button type="text" @click="goBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item :label="$t('order.orderNo')">
          <el-input v-model="searchOrderNo" :placeholder="$t('order.orderNo')" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item :label="$t('order.orderStatus')">
          <el-select v-model="searchStatus" :placeholder="$t('order.selectStatus')" clearable>
            <el-option :label="$t('order.pending')" :value="0" />
            <el-option :label="$t('order.paid')" :value="1" />
            <el-option :label="$t('order.completed')" :value="2" />
            <el-option :label="$t('order.cancelled')" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>

      <!-- 订单列表 -->
      <el-table :data="orders" v-loading="loading" border>
        <el-table-column prop="orderNo" :label="$t('order.orderNo')" width="180" />
        <el-table-column prop="orderType" :label="$t('order.orderType')" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.orderType === 'ticket'">{{ $t('order.ticket') }}</el-tag>
            <el-tag v-else-if="scope.row.orderType === 'hotel'" type="success">{{ $t('order.hotel') }}</el-tag>
            <el-tag v-else-if="scope.row.orderType === 'experience'" type="warning">{{ $t('order.experience') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" :label="$t('order.totalAmount')" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="orderStatus" :label="$t('order.orderStatus')" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.orderStatus === 0" type="warning">{{ $t('order.pending') }}</el-tag>
            <el-tag v-else-if="scope.row.orderStatus === 1" type="success">{{ $t('order.paid') }}</el-tag>
            <el-tag v-else-if="scope.row.orderStatus === 2" type="info">{{ $t('order.completed') }}</el-tag>
            <el-tag v-else-if="scope.row.orderStatus === 3" type="danger">{{ $t('order.cancelled') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contactName" :label="$t('order.contactName')" width="120" />
        <el-table-column prop="contactPhone" :label="$t('order.contactPhone')" width="120" />
        <el-table-column prop="createTime" :label="$t('common.createTime')" width="180" />
        <el-table-column :label="$t('common.actions')" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleViewDetail(scope.row)">{{ $t('order.viewDetail') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <Pagination
        :current-page="page"
        :page-size="size"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 订单详情对话框 -->
    <el-dialog :title="$t('order.orderDetail')" :visible.sync="detailDialogVisible" width="800px">
      <div v-if="orderDetail">
        <el-descriptions :column="2" border>
          <el-descriptions-item :label="$t('order.orderNo')">{{ orderDetail.orderNo }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.orderType')">
            <el-tag v-if="orderDetail.orderType === 'ticket'">{{ $t('order.ticket') }}</el-tag>
            <el-tag v-else-if="orderDetail.orderType === 'hotel'" type="success">{{ $t('order.hotel') }}</el-tag>
            <el-tag v-else-if="orderDetail.orderType === 'experience'" type="warning">{{ $t('order.experience') }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item :label="$t('order.totalAmount')">¥{{ orderDetail.totalAmount }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.orderStatus')">
            <el-tag v-if="orderDetail.orderStatus === 0" type="warning">{{ $t('order.pending') }}</el-tag>
            <el-tag v-else-if="orderDetail.orderStatus === 1" type="success">{{ $t('order.paid') }}</el-tag>
            <el-tag v-else-if="orderDetail.orderStatus === 2" type="info">{{ $t('order.completed') }}</el-tag>
            <el-tag v-else-if="orderDetail.orderStatus === 3" type="danger">{{ $t('order.cancelled') }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item :label="$t('order.contactName')">{{ orderDetail.contactName }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.contactPhone')">{{ orderDetail.contactPhone }}</el-descriptions-item>
          <el-descriptions-item :label="$t('common.createTime')" :span="2">{{ orderDetail.createTime }}</el-descriptions-item>
          <el-descriptions-item :label="$t('common.remark')" :span="2">{{ orderDetail.remark || $t('common.none') }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">{{ $t('common.close') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOrders, getOrderDetail } from '@/api/admin'
import Pagination from '@/components/common/Pagination'

export default {
  name: 'AdminOrders',
  components: {
    Pagination
  },
  data() {
    return {
      orders: [],
      searchOrderNo: '',
      searchStatus: null,
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      detailDialogVisible: false,
      orderDetail: null
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    async loadOrders() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.searchOrderNo) {
          params.orderNo = this.searchOrderNo
        }
        if (this.searchStatus !== null) {
          params.orderStatus = this.searchStatus
        }

        const res = await getOrders(params)
        if (res.code === 200) {
          this.orders = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        this.$message.error(this.$t('common.operateFailed'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadOrders()
    },
    handleReset() {
      this.searchOrderNo = ''
      this.searchStatus = null
      this.page = 1
      this.loadOrders()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadOrders()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadOrders()
    },
    async handleViewDetail(row) {
      try {
        const res = await getOrderDetail(row.id)
        if (res.code === 200) {
          this.orderDetail = res.data
          this.detailDialogVisible = true
        }
      } catch (error) {
        this.$message.error(this.$t('common.operateFailed'))
      }
    },
    goBack() {
      this.$router.push('/admin')
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-orders {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }
}
</style>

