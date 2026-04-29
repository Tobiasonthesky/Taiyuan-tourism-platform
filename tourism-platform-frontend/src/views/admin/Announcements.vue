<template>
  <div class="admin-announcements">
    <el-card>
      <div slot="header" class="card-header">
        <span>{{ $t('admin.announcementManagement') }}</span>
        <div class="card-actions">
          <el-button type="primary" size="small" @click="handleAdd">{{ $t('admin.addAnnouncement') }}</el-button>
          <el-button size="small" @click="goBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item :label="$t('common.keyword')">
          <el-input v-model="searchKeyword" :placeholder="$t('admin.titleOrContent')" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item :label="$t('common.category')">
          <el-select v-model="searchCategory" :placeholder="$t('admin.selectCategory')" clearable>
            <el-option :label="$t('admin.festival')" value="festival" />
            <el-option :label="$t('admin.promotion')" value="promotion" />
            <el-option :label="$t('admin.news')" value="news" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('common.status')">
          <el-select v-model="searchStatus" :placeholder="$t('admin.selectStatus')" clearable>
            <el-option :label="$t('admin.online')" :value="1" />
            <el-option :label="$t('admin.offline')" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>

      <!-- 公告列表 -->
      <el-table :data="announcements" v-loading="loading" border>
        <el-table-column prop="id" :label="$t('common.id')" width="80" />
        <el-table-column prop="title" :label="$t('admin.title')" width="200" />
        <el-table-column prop="category" :label="$t('common.category')" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.category === 'festival'" type="success">{{ $t('admin.festival') }}</el-tag>
            <el-tag v-else-if="scope.row.category === 'promotion'" type="warning">{{ $t('admin.promotion') }}</el-tag>
            <el-tag v-else-if="scope.row.category === 'news'" type="info">{{ $t('admin.news') }}</el-tag>
            <span v-else>{{ scope.row.category }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="isTop" :label="$t('admin.isTop')" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isTop === 1 ? 'success' : 'info'">
              {{ scope.row.isTop === 1 ? $t('common.yes') : $t('common.no') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isBanner" :label="$t('admin.isBanner')" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isBanner === 1 ? 'success' : 'info'">
              {{ scope.row.isBanner === 1 ? $t('common.yes') : $t('common.no') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" :label="$t('admin.viewCount')" width="100" />
        <el-table-column prop="status" :label="$t('common.status')" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? $t('admin.online') : $t('admin.offline') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('admin.createTime')" width="180" />
        <el-table-column :label="$t('common.actions')" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
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

    <!-- 编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      @close="handleDialogClose"
    >
      <el-form :model="form" label-width="100px" ref="form">
        <el-form-item :label="$t('admin.title')" required>
          <el-input v-model="form.title" :placeholder="$t('admin.inputTitle')" />
        </el-form-item>
        <el-form-item :label="$t('common.category')" required>
          <el-select v-model="form.category" :placeholder="$t('admin.selectCategory')" style="width: 100%">
            <el-option :label="$t('admin.festival')" value="festival" />
            <el-option :label="$t('admin.promotion')" value="promotion" />
            <el-option :label="$t('admin.news')" value="news" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('admin.coverImage')">
          <ImageUpload v-model="form.coverImage" />
        </el-form-item>
        <el-form-item :label="$t('admin.content')" required>
          <el-input v-model="form.content" type="textarea" :rows="6" :placeholder="$t('admin.inputContent')" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('admin.startTime')">
              <el-date-picker
                v-model="form.startTime"
                type="datetime"
                :placeholder="$t('admin.selectStartTime')"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('admin.endTime')">
              <el-date-picker
                v-model="form.endTime"
                type="datetime"
                :placeholder="$t('admin.selectEndTime')"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('admin.isTop')">
              <el-radio-group v-model="form.isTop">
                <el-radio :label="1">{{ $t('common.yes') }}</el-radio>
                <el-radio :label="0">{{ $t('common.no') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('admin.isBanner')">
              <el-radio-group v-model="form.isBanner">
                <el-radio :label="1">{{ $t('common.yes') }}</el-radio>
                <el-radio :label="0">{{ $t('common.no') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('admin.sortOrder')">
              <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('common.status')">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">{{ $t('admin.online') }}</el-radio>
                <el-radio :label="0">{{ $t('admin.offline') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="handleCancel">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAnnouncements, createAnnouncement, updateAnnouncement, deleteAnnouncement } from '@/api/admin'
import Pagination from '@/components/common/Pagination'
import ImageUpload from '@/components/common/ImageUpload'

export default {
  name: 'AdminAnnouncements',
  components: {
    Pagination,
    ImageUpload
  },
  data() {
    return {
      announcements: [],
      searchKeyword: '',
      searchCategory: '',
      searchStatus: null,
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      dialogVisible: false,
      dialogTitle: this.$t('admin.addAnnouncement'),
      form: {
        id: null,
        title: '',
        category: '',
        coverImage: '',
        content: '',
        startTime: '',
        endTime: '',
        isTop: 0,
        isBanner: 0,
        sortOrder: 0,
        status: 1
      }
    }
  },
  mounted() {
    this.loadAnnouncements()
  },
  methods: {
    async loadAnnouncements() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword
        }
        if (this.searchCategory) {
          params.category = this.searchCategory
        }
        if (this.searchStatus !== null && this.searchStatus !== undefined) {
          params.status = this.searchStatus
        }

        const res = await getAnnouncements(params)
        if (res.code === 200) {
          this.announcements = res.data?.records || []
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
      this.loadAnnouncements()
    },
    handleReset() {
      this.searchKeyword = ''
      this.searchCategory = ''
      this.searchStatus = null
      this.page = 1
      this.loadAnnouncements()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadAnnouncements()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadAnnouncements()
    },
    handleAdd() {
      this.dialogTitle = this.$t('admin.addAnnouncement')
      this.form = {
        id: null,
        title: '',
        category: '',
        coverImage: '',
        content: '',
        startTime: '',
        endTime: '',
        isTop: 0,
        isBanner: 0,
        sortOrder: 0,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = this.$t('common.edit') + ' - ' + this.$t('admin.announcementManagement')
      this.form = {
        ...row,
        startTime: row.startTime ? this.formatDateTime(row.startTime) : '',
        endTime: row.endTime ? this.formatDateTime(row.endTime) : ''
      }
      this.dialogVisible = true
    },
    async handleSubmit() {
      if (!this.form.title || !this.form.category || !this.form.content) {
        this.$message.warning(this.$t('admin.pleaseFillTitleCategoryContent'))
        return
      }

      try {
        if (this.form.id) {
          const res = await updateAnnouncement(this.form.id, this.form)
          if (res.code === 200) {
            this.$message.success(this.$t('common.updateSuccess'))
            this.dialogVisible = false
            this.loadAnnouncements()
          }
        } else {
          const res = await createAnnouncement(this.form)
          if (res.code === 200) {
            this.$message.success(this.$t('common.createSuccess'))
            this.dialogVisible = false
            this.loadAnnouncements()
          }
        }
      } catch (error) {
        const errorMsg = (error.response && error.response.data && error.response.data.message) || error.message || this.$t('common.operateFailed')
        this.$message.error(this.$t('common.operateFailed') + ': ' + errorMsg)
      }
    },
    async handleDelete(row) {
      this.$confirm(this.$t('admin.confirmDeleteAnnouncement'), this.$t('common.tip'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteAnnouncement(row.id)
          if (res.code === 200) {
            this.$message.success(this.$t('common.deleteSuccess'))
            this.loadAnnouncements()
          }
        } catch (error) {
          this.$message.error(this.$t('common.deleteFailed'))
        }
      }).catch(() => {
        // 用户取消删除操作，不需要做任何处理
      })
    },
    handleCancel() {
      this.dialogVisible = false
    },
    handleDialogClose() {
      // 对话框关闭时重置表单
      this.$nextTick(() => {
        if (this.$refs.form) {
          try {
            this.$refs.form.resetFields()
          } catch (e) {
            // 忽略重置表单时的错误
            console.warn('重置表单失败:', e)
          }
        }
      })
    },
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      // 如果已经是字符串格式，直接返回
      if (typeof dateTime === 'string') {
        // 如果是ISO格式，转换为 yyyy-MM-dd HH:mm:ss
        if (dateTime.includes('T')) {
          return dateTime.replace('T', ' ').substring(0, 19)
        }
        return dateTime
      }
      // 如果是Date对象，格式化
      const date = new Date(dateTime)
      if (isNaN(date.getTime())) return ''
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    goBack() {
      this.$router.push('/admin')
    }
  }
}
</script>

<style scoped>
.admin-announcements {
  padding: 20px;
}

.admin-announcements .search-form {
  margin-bottom: 20px;
}
</style>

