<template>
  <div class="admin-strategies">
    <el-card>
      <div slot="header">
        <span>攻略管理</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleAdd">新增攻略</el-button>
          <el-button size="small" @click="goBack">返回</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchKeyword" placeholder="攻略标题" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchCategory" placeholder="请选择" clearable>
            <el-option label="1日游" value="1day" />
            <el-option label="2日游" value="2day" />
            <el-option label="主题游" value="theme" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 攻略列表 -->
      <el-table :data="strategies" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="攻略标题" width="200" />
        <el-table-column prop="category" label="分类" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.category === '1day'" type="info">1日游</el-tag>
            <el-tag v-else-if="scope.row.category === '2day'" type="warning">2日游</el-tag>
            <el-tag v-else-if="scope.row.category === 'theme'" type="success">主题游</el-tag>
            <span v-else>{{ scope.row.category }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="theme" label="主题" width="120" />
        <el-table-column prop="description" label="描述" width="250" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : scope.row.status === 0 ? 'warning' : 'danger'">
              {{ scope.row.status === 1 ? '已通过' : scope.row.status === 0 ? '待审核' : '已拒绝' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isRecommend" label="推荐" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isRecommend === 1 ? 'success' : 'info'">
              {{ scope.row.isRecommend === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
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
      <el-form :model="form" label-width="120px" ref="form">
        <el-form-item label="攻略标题" required>
          <el-input v-model="form.title" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类" required>
              <el-select v-model="form.category" placeholder="请选择分类">
                <el-option label="1日游" value="1day" />
                <el-option label="2日游" value="2day" />
                <el-option label="主题游" value="theme" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主题">
              <el-input v-model="form.theme" placeholder="如：亲子、情侣、摄影等" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图片">
          <ImageUpload v-model="form.coverImage" />
        </el-form-item>
        <el-form-item label="多图片管理">
          <MultiImageUpload v-model="form.images" />
          <div style="margin-top: 8px; color: #909399; font-size: 12px;">
            <i class="el-icon-info"></i>
            提示：保存内容后，图片会自动关联到该攻略
          </div>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="详细内容">
          <el-input v-model="form.content" type="textarea" :rows="6" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="游玩时长（天）">
              <el-input-number v-model="form.duration" :min="1" :max="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预算（元）">
              <el-input-number v-model="form.budget" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最佳季节">
              <el-input v-model="form.bestSeason" placeholder="如：春季、夏季等" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="注意事项">
          <el-input v-model="form.tips" type="textarea" :rows="3" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio :label="0">待审核</el-radio>
                <el-radio :label="1">已通过</el-radio>
                <el-radio :label="2">已拒绝</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否推荐">
              <el-radio-group v-model="form.isRecommend">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getStrategies, getStrategyImages } from '@/api/strategy'
import { createStrategy, updateStrategy, deleteStrategy, createStrategyImage, deleteStrategyImage } from '@/api/admin'
import Pagination from '@/components/common/Pagination'
import ImageUpload from '@/components/common/ImageUpload'
import MultiImageUpload from '@/components/common/MultiImageUpload'

export default {
  name: 'AdminStrategies',
  components: {
    Pagination,
    ImageUpload,
    MultiImageUpload
  },
  data() {
    return {
      strategies: [],
      searchKeyword: '',
      searchCategory: '',
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      dialogVisible: false,
      dialogTitle: '新增攻略',
      form: {
        id: null,
        title: '',
        category: '',
        theme: '',
        coverImage: '',
        description: '',
        content: '',
        duration: null,
        budget: null,
        bestSeason: '',
        tips: '',
        status: 1,
        isRecommend: 0,
        images: []
      }
    }
  },
  mounted() {
    this.loadStrategies()
  },
  methods: {
    async loadStrategies() {
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

        const res = await getStrategies(params)
        if (res.code === 200) {
          this.strategies = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        this.$message.error('加载攻略列表失败')
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadStrategies()
    },
    handleReset() {
      this.searchKeyword = ''
      this.searchCategory = ''
      this.page = 1
      this.loadStrategies()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadStrategies()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadStrategies()
    },
    handleAdd() {
      this.dialogTitle = '新增攻略'
      this.form = {
        id: null,
        title: '',
        category: '',
        theme: '',
        coverImage: '',
        description: '',
        content: '',
        duration: null,
        budget: null,
        bestSeason: '',
        tips: '',
        status: 1,
        isRecommend: 0,
        images: []
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = '编辑攻略'
      this.form = { ...row }
      // 加载现有图片
      if (row.id) {
        try {
          const res = await getStrategyImages(row.id)
          if (res.code === 200 && res.data) {
            this.form.images = res.data.map(img => ({
              id: img.id,
              imageUrl: img.imageUrl,
              imageType: img.imageType,
              sortOrder: img.sortOrder
            }))
          }
        } catch (error) {
          console.error('加载图片失败:', error)
          this.form.images = []
        }
      }
      this.dialogVisible = true
    },
    async handleSubmit() {
      try {
        // 保存基本信息
        let strategyId = this.form.id
        if (this.form.id) {
          const res = await updateStrategy(this.form.id, this.form)
          if (res.code === 200) {
            strategyId = this.form.id
          } else {
            this.$message.error('更新失败')
            return
          }
        } else {
          const res = await createStrategy(this.form)
          if (res.code === 200 && res.data) {
            strategyId = res.data.id
          } else {
            this.$message.error('创建失败')
            return
          }
        }

        // 保存多图片
        if (strategyId && this.form.images && this.form.images.length > 0) {
          try {
            // 先删除旧的图片（编辑时）
            if (this.form.id) {
              const oldImagesRes = await getStrategyImages(strategyId)
              if (oldImagesRes.code === 200 && oldImagesRes.data) {
                for (const oldImg of oldImagesRes.data) {
                  await deleteStrategyImage(oldImg.id)
                }
              }
            }

            // 创建新图片
            for (let i = 0; i < this.form.images.length; i++) {
              const img = this.form.images[i]
              await createStrategyImage({
                strategyId: strategyId,
                imageUrl: img.imageUrl,
                imageType: i === 0 ? 'cover' : 'detail',
                sortOrder: i + 1
              })
            }
          } catch (error) {
            console.error('保存图片失败:', error)
            this.$message.warning('内容保存成功，但图片保存失败')
          }
        }

        this.$message.success(this.form.id ? '更新成功' : '创建成功')
        this.dialogVisible = false
        this.loadStrategies()
      } catch (error) {
        this.$message.error('操作失败')
      }
    },
    async handleDelete(row) {
      this.$confirm('确定要删除该攻略吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteStrategy(row.id)
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadStrategies()
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    handleDialogClose() {
      this.$nextTick(() => {
        if (this.$refs.form) {
          try {
            this.$refs.form.resetFields()
          } catch (e) {
            console.warn('重置表单失败:', e)
          }
        }
      })
    },
    goBack() {
      this.$router.push('/admin')
    }
  }
}
</script>

<style scoped>
.admin-strategies {
  padding: 20px;
}

.admin-strategies .search-form {
  margin-bottom: 20px;
}
</style>

