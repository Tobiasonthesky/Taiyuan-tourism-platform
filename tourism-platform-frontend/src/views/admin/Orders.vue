<template>
  <div class="admin-orders">
    <el-card>
      <div slot="header">
        <span>订单管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回</el-button>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchOrderNo" placeholder="订单号" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchStatus" placeholder="选择状态" clearable>
            <el-option label="待支付" :value="0" />
            <el-option label="已支付" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已取消" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 订单列表 -->
      <el-table :data="orders" v-loading="loading" border>
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="orderType" label="订单类型" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.orderType === 'ticket'">门票</el-tag>
            <el-tag v-else-if="scope.row.orderType === 'hotel'" type="success">酒店</el-tag>
            <el-tag v-else-if="scope.row.orderType === 'experience'" type="warning">体验</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="orderStatus" label="订单状态" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.orderStatus === 0" type="warning">待支付</el-tag>
            <el-tag v-else-if="scope.row.orderStatus === 1" type="success">已支付</el-tag>
            <el-tag v-else-if="scope.row.orderStatus === 2" type="info">已完成</el-tag>
            <el-tag v-else-if="scope.row.orderStatus === 3" type="danger">已取消</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contactName" label="联系人" width="120" />
        <el-table-column prop="contactPhone" label="联系电话" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleViewDetail(scope.row)">查看详情</el-button>
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
    <el-dialog title="订单详情" :visible.sync="detailDialogVisible" width="800px">
      <div v-if="orderDetail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ orderDetail.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="订单类型">
            <el-tag v-if="orderDetail.orderType === 'ticket'">门票</el-tag>
            <el-tag v-else-if="orderDetail.orderType === 'hotel'" type="success">酒店</el-tag>
            <el-tag v-else-if="orderDetail.orderType === 'experience'" type="warning">体验</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="订单金额">¥{{ orderDetail.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag v-if="orderDetail.orderStatus === 0" type="warning">待支付</el-tag>
            <el-tag v-else-if="orderDetail.orderStatus === 1" type="success">已支付</el-tag>
            <el-tag v-else-if="orderDetail.orderStatus === 2" type="info">已完成</el-tag>
            <el-tag v-else-if="orderDetail.orderStatus === 3" type="danger">已取消</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="联系人">{{ orderDetail.contactName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ orderDetail.contactPhone }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ orderDetail.createTime }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ orderDetail.remark || '无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOrders } from '@/api/admin'
import { getOrderDetail } from '@/api/order'
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
        this.$message.error('加载订单列表失败')
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
        this.$message.error('加载订单详情失败')
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

