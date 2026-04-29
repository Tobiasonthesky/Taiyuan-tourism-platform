<template>
  <div class="admin-audit">
    <el-card>
      <div slot="header" class="card-header">
        <span>{{ $t('admin.auditManagement') }}</span>
        <div class="card-actions">
          <el-button size="small" @click="goBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <!-- 标签页 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane :label="$t('audit.pendingAttractions')" name="attractions">
          <el-table :data="attractions" v-loading="loading" border>
            <el-table-column prop="id" :label="$t('common.id')" width="80" />
            <el-table-column prop="name" :label="$t('attraction.name')" width="200" />
            <el-table-column prop="address" :label="$t('common.address')" width="200" />
            <el-table-column prop="description" :label="$t('common.description')" show-overflow-tooltip />
            <el-table-column prop="createTime" :label="$t('audit.submitTime')" width="180" />
            <el-table-column :label="$t('common.actions')" width="320" fixed="right">
              <template slot-scope="scope">
                <div class="audit-actions">
                  <el-button size="mini" type="primary" @click="handleViewDetail(scope.row, 'attraction')">{{ $t('audit.viewDetail') }}</el-button>
                  <el-button size="mini" type="success" @click="handleAudit(scope.row, 'attraction', 1)">{{ $t('audit.approve') }}</el-button>
                  <el-button size="mini" type="danger" @click="handleAudit(scope.row, 'attraction', 2)">{{ $t('audit.reject') }}</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <Pagination
            :current-page="page"
            :page-size="size"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-tab-pane>

        <el-tab-pane :label="$t('audit.pendingFoods')" name="foods">
          <el-table :data="foods" v-loading="loading" border>
            <el-table-column prop="id" :label="$t('common.id')" width="80" />
            <el-table-column prop="name" :label="$t('food.name')" width="200" />
            <el-table-column prop="description" :label="$t('common.description')" show-overflow-tooltip />
            <el-table-column prop="createTime" :label="$t('audit.submitTime')" width="180" />
            <el-table-column :label="$t('common.actions')" width="320" fixed="right">
              <template slot-scope="scope">
                <div class="audit-actions">
                  <el-button size="mini" type="primary" @click="handleViewDetail(scope.row, 'food')">{{ $t('audit.viewDetail') }}</el-button>
                  <el-button size="mini" type="success" @click="handleAudit(scope.row, 'food', 1)">{{ $t('audit.approve') }}</el-button>
                  <el-button size="mini" type="danger" @click="handleAudit(scope.row, 'food', 2)">{{ $t('audit.reject') }}</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <Pagination
            :current-page="page"
            :page-size="size"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-tab-pane>

        <el-tab-pane :label="$t('audit.pendingCultures')" name="cultures">
          <el-table :data="cultures" v-loading="loading" border>
            <el-table-column prop="id" :label="$t('common.id')" width="80" />
            <el-table-column prop="name" :label="$t('culture.name')" width="200" />
            <el-table-column prop="description" :label="$t('common.description')" show-overflow-tooltip />
            <el-table-column prop="createTime" :label="$t('audit.submitTime')" width="180" />
            <el-table-column :label="$t('common.actions')" width="320" fixed="right">
              <template slot-scope="scope">
                <div class="audit-actions">
                  <el-button size="mini" type="primary" @click="handleViewDetail(scope.row, 'culture')">{{ $t('audit.viewDetail') }}</el-button>
                  <el-button size="mini" type="success" @click="handleAudit(scope.row, 'culture', 1)">{{ $t('audit.approve') }}</el-button>
                  <el-button size="mini" type="danger" @click="handleAudit(scope.row, 'culture', 2)">{{ $t('audit.reject') }}</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <Pagination
            :current-page="page"
            :page-size="size"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-tab-pane>

        <el-tab-pane :label="$t('audit.pendingStrategies')" name="strategies">
          <el-table :data="strategies" v-loading="loading" border>
            <el-table-column prop="id" :label="$t('common.id')" width="80" />
            <el-table-column prop="title" :label="$t('strategy.title')" width="200" />
            <el-table-column prop="description" :label="$t('common.description')" show-overflow-tooltip />
            <el-table-column prop="createTime" :label="$t('audit.submitTime')" width="180" />
            <el-table-column :label="$t('common.actions')" width="320" fixed="right">
              <template slot-scope="scope">
                <div class="audit-actions">
                  <el-button size="mini" type="primary" @click="handleViewDetail(scope.row, 'strategy')">{{ $t('audit.viewDetail') }}</el-button>
                  <el-button size="mini" type="success" @click="handleAudit(scope.row, 'strategy', 1)">{{ $t('audit.approve') }}</el-button>
                  <el-button size="mini" type="danger" @click="handleAudit(scope.row, 'strategy', 2)">{{ $t('audit.reject') }}</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <Pagination
            :current-page="page"
            :page-size="size"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog
      :title="detailDialogTitle"
      :visible.sync="detailDialogVisible"
      width="800px"
      @close="handleDetailDialogClose"
    >
      <div v-if="detailData" class="detail-content">
        <!-- 景点详情 -->
        <template v-if="currentType === 'attraction'">
          <el-descriptions :column="2" border>
            <el-descriptions-item :label="$t('attraction.name')">{{ detailData.name }}</el-descriptions-item>
            <el-descriptions-item :label="$t('common.address')">{{ detailData.address || $t('common.notFilled') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('attraction.ticketPrice')">¥{{ detailData.ticketPrice || 0 }}</el-descriptions-item>
            <el-descriptions-item :label="$t('attraction.openingHours')">{{ detailData.openingHours || $t('common.notFilled') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('common.rating')">{{ detailData.rating || $t('common.notRated') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('audit.submitTime')">{{ detailData.createTime }}</el-descriptions-item>
            <el-descriptions-item :label="$t('common.coverImage')" :span="2">
              <img v-if="detailData.coverImage" :src="detailData.coverImage" :alt="$t('common.cover')" style="max-width: 300px; max-height: 200px;" />
              <span v-else>{{ $t('common.none') }}</span>
            </el-descriptions-item>
            <el-descriptions-item :label="$t('common.description')" :span="2">{{ detailData.description || $t('common.none') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('common.content')" :span="2">
              <div v-html="detailData.content || $t('common.none')"></div>
            </el-descriptions-item>
          </el-descriptions>
        </template>

        <!-- 美食详情 -->
        <template v-if="currentType === 'food'">
          <el-descriptions :column="2" border>
            <el-descriptions-item :label="$t('food.name')">{{ detailData.name }}</el-descriptions-item>
            <el-descriptions-item :label="$t('food.restaurant')">{{ detailData.restaurant || $t('common.notFilled') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('food.ingredients')">{{ detailData.ingredients || $t('common.notFilled') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('audit.submitTime')">{{ detailData.createTime }}</el-descriptions-item>
            <el-descriptions-item :label="$t('common.coverImage')" :span="2">
              <img v-if="detailData.coverImage" :src="detailData.coverImage" :alt="$t('common.cover')" style="max-width: 300px; max-height: 200px;" />
              <span v-else>{{ $t('common.none') }}</span>
            </el-descriptions-item>
            <el-descriptions-item :label="$t('common.description')" :span="2">{{ detailData.description || $t('common.none') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('food.cookingMethod')" :span="2">{{ detailData.cookingMethod || $t('common.none') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('common.content')" :span="2">
              <div v-html="detailData.content || $t('common.none')"></div>
            </el-descriptions-item>
          </el-descriptions>
        </template>

        <!-- 文化详情 -->
        <template v-if="currentType === 'culture'">
          <el-descriptions :column="2" border>
            <el-descriptions-item :label="$t('culture.name')">{{ detailData.name }}</el-descriptions-item>
            <el-descriptions-item :label="$t('audit.submitTime')">{{ detailData.createTime }}</el-descriptions-item>
            <el-descriptions-item :label="$t('common.coverImage')" :span="2">
              <img v-if="detailData.coverImage" :src="detailData.coverImage" :alt="$t('common.cover')" style="max-width: 300px; max-height: 200px;" />
              <span v-else>{{ $t('common.none') }}</span>
            </el-descriptions-item>
            <el-descriptions-item :label="$t('common.description')" :span="2">{{ detailData.description || $t('common.none') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('common.content')" :span="2">
              <div v-html="detailData.content || $t('common.none')"></div>
            </el-descriptions-item>
          </el-descriptions>
        </template>

        <!-- 攻略详情 -->
        <template v-if="currentType === 'strategy'">
          <el-descriptions :column="2" border>
            <el-descriptions-item :label="$t('strategy.title')">{{ detailData.title }}</el-descriptions-item>
            <el-descriptions-item :label="$t('strategy.theme')">{{ detailData.theme || $t('common.notFilled') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('strategy.duration')">{{ detailData.recommendedDays || $t('common.notFilled') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('audit.submitTime')">{{ detailData.createTime }}</el-descriptions-item>
            <el-descriptions-item :label="$t('common.coverImage')" :span="2">
              <img v-if="detailData.coverImage" :src="detailData.coverImage" :alt="$t('common.cover')" style="max-width: 300px; max-height: 200px;" />
              <span v-else>{{ $t('common.none') }}</span>
            </el-descriptions-item>
            <el-descriptions-item :label="$t('common.description')" :span="2">{{ detailData.description || $t('common.none') }}</el-descriptions-item>
            <el-descriptions-item :label="$t('common.content')" :span="2">
              <div v-html="detailData.content || $t('common.none')"></div>
            </el-descriptions-item>
          </el-descriptions>
        </template>
      </div>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">{{ $t('common.close') }}</el-button>
        <el-button type="danger" @click="handleAuditFromDetail(2)">{{ $t('audit.reject') }}</el-button>
        <el-button type="success" @click="handleAuditFromDetail(1)">{{ $t('audit.approve') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPendingAttractions, getPendingFoods, getPendingCultures, getPendingStrategies, auditAttraction, auditFood, auditCulture, auditStrategy } from '@/api/admin'
import Pagination from '@/components/common/Pagination'

export default {
  name: 'AdminAudit',
  components: {
    Pagination
  },
  data() {
    return {
      activeTab: 'attractions',
      attractions: [],
      foods: [],
      cultures: [],
      strategies: [],
      loading: false,
      page: 1,
      size: 10,
      total: 0,
      detailDialogVisible: false,
      detailDialogTitle: this.$t('audit.contentDetail'),
      detailData: null,
      currentType: '',
      currentRow: null
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        let res
        const params = { page: this.page, size: this.size }
        
        switch (this.activeTab) {
          case 'attractions':
            res = await getPendingAttractions(params)
            if (res.code === 200) {
              this.attractions = res.data?.records || []
              this.total = res.data?.total || 0
            }
            break
          case 'foods':
            res = await getPendingFoods(params)
            if (res.code === 200) {
              this.foods = res.data?.records || []
              this.total = res.data?.total || 0
            }
            break
          case 'cultures':
            res = await getPendingCultures(params)
            if (res.code === 200) {
              this.cultures = res.data?.records || []
              this.total = res.data?.total || 0
            }
            break
          case 'strategies':
            res = await getPendingStrategies(params)
            if (res.code === 200) {
              this.strategies = res.data?.records || []
              this.total = res.data?.total || 0
            }
            break
        }
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      } finally {
        this.loading = false
      }
    },
    handleTabClick() {
      this.page = 1
      this.loadData()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadData()
    },
    handleViewDetail(row, type) {
      this.currentType = type
      this.currentRow = row
      this.detailData = { ...row }
      
      // 设置对话框标题
      const typeMap = {
        'attraction': this.$t('audit.attractionDetail'),
        'food': this.$t('audit.foodDetail'),
        'culture': this.$t('audit.cultureDetail'),
        'strategy': this.$t('audit.strategyDetail')
      }
      this.detailDialogTitle = typeMap[type] || this.$t('audit.contentDetail')
      this.detailDialogVisible = true
    },
    handleDetailDialogClose() {
      this.detailData = null
      this.currentType = ''
      this.currentRow = null
    },
    async handleAuditFromDetail(status) {
      if (!this.currentRow || !this.currentType) {
        return
      }
      await this.handleAudit(this.currentRow, this.currentType, status)
      this.detailDialogVisible = false
    },
    async handleAudit(row, type, status) {
      const message = status === 1 ? this.$t('audit.confirmApprove') : this.$t('audit.confirmReject')
      this.$confirm(message, this.$t('admin.tip'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(async () => {
        try {
          let res
          switch (type) {
            case 'attraction':
              res = await auditAttraction(row.id, status)
              break
            case 'food':
              res = await auditFood(row.id, status)
              break
            case 'culture':
              res = await auditCulture(row.id, status)
              break
            case 'strategy':
              res = await auditStrategy(row.id, status)
              break
          }
          if (res.code === 200) {
            const successMsg = status === 1 ? this.$t('audit.approveSuccess') : this.$t('audit.rejectSuccess')
            this.$message.success(successMsg)
            this.loadData()
          }
        } catch (error) {
          this.$message.error(this.$t('audit.auditFailed'))
        }
      }).catch(() => {
        // 用户点击取消
      })
    },
    goBack() {
      this.$router.push('/admin')
    }
  }
}
</script>

<style scoped>
.admin-audit {
  padding: 20px;
}

.audit-actions {
  display: flex;
  flex-wrap: nowrap;
  gap: 5px;
}

.audit-actions .el-button {
  padding: 7px 10px;
  font-size: 12px;
}

.detail-content {
  max-height: 600px;
  overflow-y: auto;
}

.detail-content img {
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.detail-content >>> .el-descriptions__label {
  font-weight: 600;
  width: 120px;
}

.detail-content >>> .el-descriptions__content {
  word-break: break-word;
}
</style>

