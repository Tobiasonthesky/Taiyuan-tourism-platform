<template>
  <div class="admin-audit">
    <el-card>
      <div slot="header">
        <span>内容审核</span>
        <div style="float: right;">
          <el-button size="small" @click="goBack">返回</el-button>
        </div>
      </div>

      <!-- 标签页 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="待审核景点" name="attractions">
          <el-table :data="attractions" v-loading="loading" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="景点名称" width="200" />
            <el-table-column prop="address" label="地址" width="200" />
            <el-table-column prop="description" label="描述" show-overflow-tooltip />
            <el-table-column prop="createTime" label="提交时间" width="180" />
            <el-table-column label="操作" width="250" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleViewDetail(scope.row, 'attraction')">查看详情</el-button>
                <el-button size="mini" type="success" @click="handleAudit(scope.row, 'attraction', 1)">通过</el-button>
                <el-button size="mini" type="danger" @click="handleAudit(scope.row, 'attraction', 2)">拒绝</el-button>
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

        <el-tab-pane label="待审核美食" name="foods">
          <el-table :data="foods" v-loading="loading" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="美食名称" width="200" />
            <el-table-column prop="description" label="描述" show-overflow-tooltip />
            <el-table-column prop="createTime" label="提交时间" width="180" />
            <el-table-column label="操作" width="250" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleViewDetail(scope.row, 'food')">查看详情</el-button>
                <el-button size="mini" type="success" @click="handleAudit(scope.row, 'food', 1)">通过</el-button>
                <el-button size="mini" type="danger" @click="handleAudit(scope.row, 'food', 2)">拒绝</el-button>
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

        <el-tab-pane label="待审核文化" name="cultures">
          <el-table :data="cultures" v-loading="loading" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="文化名称" width="200" />
            <el-table-column prop="description" label="描述" show-overflow-tooltip />
            <el-table-column prop="createTime" label="提交时间" width="180" />
            <el-table-column label="操作" width="250" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleViewDetail(scope.row, 'culture')">查看详情</el-button>
                <el-button size="mini" type="success" @click="handleAudit(scope.row, 'culture', 1)">通过</el-button>
                <el-button size="mini" type="danger" @click="handleAudit(scope.row, 'culture', 2)">拒绝</el-button>
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

        <el-tab-pane label="待审核攻略" name="strategies">
          <el-table :data="strategies" v-loading="loading" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="title" label="攻略标题" width="200" />
            <el-table-column prop="description" label="描述" show-overflow-tooltip />
            <el-table-column prop="createTime" label="提交时间" width="180" />
            <el-table-column label="操作" width="250" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleViewDetail(scope.row, 'strategy')">查看详情</el-button>
                <el-button size="mini" type="success" @click="handleAudit(scope.row, 'strategy', 1)">通过</el-button>
                <el-button size="mini" type="danger" @click="handleAudit(scope.row, 'strategy', 2)">拒绝</el-button>
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
            <el-descriptions-item label="景点名称">{{ detailData.name }}</el-descriptions-item>
            <el-descriptions-item label="地址">{{ detailData.address || '未填写' }}</el-descriptions-item>
            <el-descriptions-item label="门票价格">¥{{ detailData.ticketPrice || 0 }}</el-descriptions-item>
            <el-descriptions-item label="开放时间">{{ detailData.openingHours || '未填写' }}</el-descriptions-item>
            <el-descriptions-item label="评分">{{ detailData.rating || '未评分' }}</el-descriptions-item>
            <el-descriptions-item label="提交时间">{{ detailData.createTime }}</el-descriptions-item>
            <el-descriptions-item label="封面图片" :span="2">
              <img v-if="detailData.coverImage" :src="detailData.coverImage" alt="封面" style="max-width: 300px; max-height: 200px;" />
              <span v-else>无</span>
            </el-descriptions-item>
            <el-descriptions-item label="描述" :span="2">{{ detailData.description || '无' }}</el-descriptions-item>
            <el-descriptions-item label="详细内容" :span="2">
              <div v-html="detailData.content || '无'"></div>
            </el-descriptions-item>
          </el-descriptions>
        </template>

        <!-- 美食详情 -->
        <template v-if="currentType === 'food'">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="美食名称">{{ detailData.name }}</el-descriptions-item>
            <el-descriptions-item label="推荐餐厅">{{ detailData.restaurant || '未填写' }}</el-descriptions-item>
            <el-descriptions-item label="主要食材">{{ detailData.ingredients || '未填写' }}</el-descriptions-item>
            <el-descriptions-item label="提交时间">{{ detailData.createTime }}</el-descriptions-item>
            <el-descriptions-item label="封面图片" :span="2">
              <img v-if="detailData.coverImage" :src="detailData.coverImage" alt="封面" style="max-width: 300px; max-height: 200px;" />
              <span v-else>无</span>
            </el-descriptions-item>
            <el-descriptions-item label="描述" :span="2">{{ detailData.description || '无' }}</el-descriptions-item>
            <el-descriptions-item label="制作方法" :span="2">{{ detailData.cookingMethod || '无' }}</el-descriptions-item>
            <el-descriptions-item label="详细内容" :span="2">
              <div v-html="detailData.content || '无'"></div>
            </el-descriptions-item>
          </el-descriptions>
        </template>

        <!-- 文化详情 -->
        <template v-if="currentType === 'culture'">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="文化名称">{{ detailData.name }}</el-descriptions-item>
            <el-descriptions-item label="提交时间">{{ detailData.createTime }}</el-descriptions-item>
            <el-descriptions-item label="封面图片" :span="2">
              <img v-if="detailData.coverImage" :src="detailData.coverImage" alt="封面" style="max-width: 300px; max-height: 200px;" />
              <span v-else>无</span>
            </el-descriptions-item>
            <el-descriptions-item label="描述" :span="2">{{ detailData.description || '无' }}</el-descriptions-item>
            <el-descriptions-item label="详细内容" :span="2">
              <div v-html="detailData.content || '无'"></div>
            </el-descriptions-item>
          </el-descriptions>
        </template>

        <!-- 攻略详情 -->
        <template v-if="currentType === 'strategy'">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="攻略标题">{{ detailData.title }}</el-descriptions-item>
            <el-descriptions-item label="主题">{{ detailData.theme || '未填写' }}</el-descriptions-item>
            <el-descriptions-item label="推荐天数">{{ detailData.recommendedDays || '未填写' }}</el-descriptions-item>
            <el-descriptions-item label="提交时间">{{ detailData.createTime }}</el-descriptions-item>
            <el-descriptions-item label="封面图片" :span="2">
              <img v-if="detailData.coverImage" :src="detailData.coverImage" alt="封面" style="max-width: 300px; max-height: 200px;" />
              <span v-else>无</span>
            </el-descriptions-item>
            <el-descriptions-item label="描述" :span="2">{{ detailData.description || '无' }}</el-descriptions-item>
            <el-descriptions-item label="详细内容" :span="2">
              <div v-html="detailData.content || '无'"></div>
            </el-descriptions-item>
          </el-descriptions>
        </template>
      </div>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button type="danger" @click="handleAuditFromDetail(2)">拒绝</el-button>
        <el-button type="success" @click="handleAuditFromDetail(1)">通过</el-button>
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
      detailDialogTitle: '内容详情',
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
        this.$message.error('加载数据失败')
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
        'attraction': '景点详情',
        'food': '美食详情',
        'culture': '文化详情',
        'strategy': '攻略详情'
      }
      this.detailDialogTitle = typeMap[type] || '内容详情'
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
      const action = status === 1 ? '通过' : '拒绝'
      this.$confirm(`确定要${action}该内容吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
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
            this.$message.success(`审核${action}成功`)
            this.loadData()
          }
        } catch (error) {
          this.$message.error('审核失败')
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

