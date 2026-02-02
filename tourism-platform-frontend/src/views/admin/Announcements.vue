<template>
  <div class="admin-announcements">
    <el-card>
      <div slot="header">
        <span>活动公告管理</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleAdd">新增公告</el-button>
          <el-button size="small" @click="goBack">返回</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchKeyword" placeholder="标题或内容" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchCategory" placeholder="请选择分类" clearable>
            <el-option label="节庆活动" value="festival" />
            <el-option label="优惠活动" value="promotion" />
            <el-option label="新闻资讯" value="news" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchStatus" placeholder="请选择状态" clearable>
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 公告列表 -->
      <el-table :data="announcements" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" width="200" />
        <el-table-column prop="category" label="分类" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.category === 'festival'" type="success">节庆活动</el-tag>
            <el-tag v-else-if="scope.row.category === 'promotion'" type="warning">优惠活动</el-tag>
            <el-tag v-else-if="scope.row.category === 'news'" type="info">新闻资讯</el-tag>
            <span v-else>{{ scope.row.category }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="isTop" label="置顶" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isTop === 1 ? 'success' : 'info'">
              {{ scope.row.isTop === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isBanner" label="轮播" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isBanner === 1 ? 'success' : 'info'">
              {{ scope.row.isBanner === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览次数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="分类" required>
          <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="节庆活动" value="festival" />
            <el-option label="优惠活动" value="promotion" />
            <el-option label="新闻资讯" value="news" />
          </el-select>
        </el-form-item>
        <el-form-item label="封面图片">
          <ImageUpload v-model="form.coverImage" />
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入公告内容" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间">
              <el-date-picker
                v-model="form.startTime"
                type="datetime"
                placeholder="选择开始时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间">
              <el-date-picker
                v-model="form.endTime"
                type="datetime"
                placeholder="选择结束时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否置顶">
              <el-radio-group v-model="form.isTop">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否轮播">
              <el-radio-group v-model="form.isBanner">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="排序顺序">
              <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">上架</el-radio>
                <el-radio :label="0">下架</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
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
      dialogTitle: '新增公告',
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
        this.$message.error('加载公告列表失败')
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
      this.dialogTitle = '新增公告'
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
      this.dialogTitle = '编辑公告'
      this.form = {
        ...row,
        startTime: row.startTime ? this.formatDateTime(row.startTime) : '',
        endTime: row.endTime ? this.formatDateTime(row.endTime) : ''
      }
      this.dialogVisible = true
    },
    async handleSubmit() {
      if (!this.form.title || !this.form.category || !this.form.content) {
        this.$message.warning('请填写标题、分类和内容')
        return
      }

      try {
        if (this.form.id) {
          const res = await updateAnnouncement(this.form.id, this.form)
          if (res.code === 200) {
            this.$message.success('更新成功')
            this.dialogVisible = false
            this.loadAnnouncements()
          }
        } else {
          const res = await createAnnouncement(this.form)
          if (res.code === 200) {
            this.$message.success('创建成功')
            this.dialogVisible = false
            this.loadAnnouncements()
          }
        }
      } catch (error) {
        const errorMsg = (error.response && error.response.data && error.response.data.message) || error.message || '操作失败'
        this.$message.error('操作失败：' + errorMsg)
      }
    },
    async handleDelete(row) {
      this.$confirm('确定要删除该公告吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteAnnouncement(row.id)
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadAnnouncements()
          }
        } catch (error) {
          this.$message.error('删除失败')
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

