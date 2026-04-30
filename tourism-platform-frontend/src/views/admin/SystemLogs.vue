<template>
  <div class="system-logs">
    <el-card>
      <div slot="header" class="card-header">
        <span>{{ $t('admin.systemLogManagement') }}</span>
        <div class="card-actions">
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="handleClearAll">{{ $t('systemLog.clearAll') }}</el-button>
          <el-button type="text" @click="goBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item :label="$t('systemLog.operationType')">
          <el-select v-model="searchForm.operationType" :placeholder="$t('common.pleaseSelect')" clearable>
            <el-option :label="$t('systemLog.login')" value="登录" />
            <el-option :label="$t('systemLog.query')" value="查询" />
            <el-option :label="$t('systemLog.create')" value="新增" />
            <el-option :label="$t('systemLog.update')" value="修改" />
            <el-option :label="$t('systemLog.delete')" value="删除" />
            <el-option :label="$t('systemLog.other')" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('systemLog.operationModule')">
          <el-input v-model="searchForm.operationModule" :placeholder="$t('common.pleaseInput')" clearable />
        </el-form-item>
        <el-form-item :label="$t('systemLog.operator')">
          <el-input v-model="searchForm.username" :placeholder="$t('common.pleaseInput')" clearable />
        </el-form-item>
        <el-form-item :label="$t('systemLog.result')">
          <el-select v-model="searchForm.success" :placeholder="$t('common.pleaseSelect')" clearable>
            <el-option :label="$t('systemLog.success')" :value="1" />
            <el-option :label="$t('systemLog.failed')" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('systemLog.timeRange')">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            :start-placeholder="$t('systemLog.startTime')"
            :end-placeholder="$t('systemLog.endTime')"
            value-format="yyyy-MM-dd"
            style="width: 240px;"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>

      <!-- 日志列表 -->
      <el-table :data="logs" v-loading="loading" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" :label="$t('common.id')" width="80" />
        <el-table-column prop="operationType" :label="$t('systemLog.operationType')" width="100" />
        <el-table-column prop="operationModule" :label="$t('systemLog.operationModule')" width="120" />
        <el-table-column prop="operationDesc" :label="$t('systemLog.operationDesc')" min-width="180" show-overflow-tooltip />
        <el-table-column prop="username" :label="$t('systemLog.operator')" width="100" />
        <el-table-column prop="ipAddress" :label="$t('systemLog.ipAddress')" width="130" />
        <el-table-column prop="success" :label="$t('systemLog.result')" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.success === 1 ? 'success' : 'danger'">
              {{ scope.row.success === 1 ? $t('systemLog.success') : $t('systemLog.failed') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="duration" :label="$t('systemLog.duration')" width="80">
          <template slot-scope="scope">
            {{ scope.row.duration }}ms
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('systemLog.createTime')" width="160" />
        <el-table-column :label="$t('common.actions')" width="100" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">{{ $t('common.detail') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 批量操作栏 -->
      <div v-if="selectedIds.length > 0" class="batch-actions">
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchDelete">
          {{ $t('systemLog.batchDelete') }} ({{ selectedIds.length }})
        </el-button>
      </div>

      <!-- 分页 -->
      <Pagination
        :current-page="page"
        :page-size="size"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog :title="$t('systemLog.detailTitle')" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item :label="$t('common.id')">{{ currentLog.id }}</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.operationType')">{{ currentLog.operationType }}</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.operationModule')">{{ currentLog.operationModule }}</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.operator')">{{ currentLog.username }}</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.ipAddress')">{{ currentLog.ipAddress }}</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.result')">
          <el-tag :type="currentLog.success === 1 ? 'success' : 'danger'">
            {{ currentLog.success === 1 ? $t('systemLog.success') : $t('systemLog.failed') }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.duration')">{{ currentLog.duration }}ms</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.createTime')">{{ currentLog.createTime }}</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.requestUrl')" :span="2">{{ currentLog.requestUrl }}</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.requestMethod')">{{ currentLog.requestMethod }}</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.userAgent')" :span="2">{{ currentLog.userAgent }}</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.operationDesc')" :span="2">{{ currentLog.operationDesc }}</el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.requestParams')" :span="2">
          <pre class="json-pre">{{ formatJson(currentLog.requestParams) }}</pre>
        </el-descriptions-item>
        <el-descriptions-item :label="$t('systemLog.resultContent')" :span="2">
          <pre class="json-pre">{{ formatJson(currentLog.result) }}</pre>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentLog.errorMsg" :label="$t('systemLog.errorMsg')" :span="2">
          <pre class="error-pre">{{ currentLog.errorMsg }}</pre>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { getSystemLogs, batchDeleteSystemLogs, clearSystemLogs } from '@/api/systemLog'
import Pagination from '@/components/common/Pagination'

export default {
  name: 'SystemLogs',
  components: { Pagination },
  data() {
    return {
      loading: false,
      logs: [],
      page: 1,
      size: 10,
      total: 0,
      selectedIds: [],
      searchForm: {
        operationType: '',
        operationModule: '',
        username: '',
        success: null
      },
      dateRange: [],
      detailDialogVisible: false,
      currentLog: {}
    }
  },
  created() {
    this.loadLogs()
  },
  methods: {
    async loadLogs() {
      this.loading = true
      try {
        const params = {
          current: this.page,
          size: this.size,
          ...this.searchForm
        }
        if (this.dateRange && this.dateRange.length === 2) {
          params.startTime = this.dateRange[0] + ' 00:00:00'
          params.endTime = this.dateRange[1] + ' 23:59:59'
        }
        const res = await getSystemLogs(params)
        if (res.code === 200) {
          this.logs = res.data.records
          this.total = res.data.total
        }
      } catch (error) {
        console.error('加载日志失败:', error)
        this.$message.error(this.$t('common.operationFailed'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadLogs()
    },
    handleReset() {
      this.searchForm = {
        operationType: '',
        operationModule: '',
        username: '',
        success: null
      }
      this.dateRange = []
      this.page = 1
      this.loadLogs()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadLogs()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadLogs()
    },
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.id)
    },
    handleDetail(row) {
      this.currentLog = row
      this.detailDialogVisible = true
    },
    async handleBatchDelete() {
      try {
        await this.$confirm(this.$t('systemLog.confirmBatchDelete'), this.$t('common.tip'), {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: 'warning'
        })
        await batchDeleteSystemLogs(this.selectedIds)
        this.$message.success(this.$t('systemLog.deleteSuccess'))
        this.selectedIds = []
        this.loadLogs()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
        }
      }
    },
    async handleClearAll() {
      try {
        await this.$confirm(this.$t('systemLog.confirmClearAll'), this.$t('common.tip'), {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: 'warning'
        })
        await clearSystemLogs()
        this.$message.success(this.$t('systemLog.clearSuccess'))
        this.loadLogs()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('清空失败:', error)
        }
      }
    },
    goBack() {
      this.$router.push('/admin')
    },
    formatJson(str) {
      if (!str) return this.$t('systemLog.noData')
      try {
        return JSON.stringify(JSON.parse(str), null, 2)
      } catch (e) {
        return str
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.system-logs {
  padding: 20px 0;
  min-height: calc(100vh - 140px);

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .search-form {
    margin-bottom: 20px;
  }

  .batch-actions {
    margin: 15px 0;
    padding: 10px;
    background: #fef0f0;
    border-radius: 4px;
  }

  .json-pre {
    white-space: pre-wrap;
    word-wrap: break-word;
    max-height: 300px;
    overflow-y: auto;
    margin: 0;
    font-size: 12px;
    background: #f5f7fa;
    padding: 10px;
    border-radius: 4px;
  }

  .error-pre {
    white-space: pre-wrap;
    word-wrap: break-word;
    max-height: 200px;
    overflow-y: auto;
    margin: 0;
    font-size: 12px;
    background: #fef0f0;
    padding: 10px;
    border-radius: 4px;
    color: #f56c6c;
  }
}
</style>