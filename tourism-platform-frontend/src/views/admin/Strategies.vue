<template>
  <div class="admin-strategies">
    <el-card>
      <div slot="header" class="card-header">
        <span>{{ $t('admin.strategyManagement') }}</span>
        <div class="card-actions">
          <el-button type="primary" size="small" @click="handleAdd">{{ $t('admin.addStrategy') }}</el-button>
          <el-button size="small" @click="goBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item :label="$t('common.keyword')">
          <el-input v-model="searchKeyword" :placeholder="$t('strategy.title')" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item :label="$t('common.category')">
          <el-select v-model="searchCategory" :placeholder="$t('common.pleaseSelect')" clearable>
            <el-option :label="$t('strategy.oneDay')" value="1day" />
            <el-option :label="$t('strategy.twoDay')" value="2day" />
            <el-option :label="$t('strategy.theme')" value="theme" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>

      <!-- 攻略列表 -->
      <el-table :data="strategies" v-loading="loading" border>
        <el-table-column prop="id" :label="$t('common.id')" width="80" />
        <el-table-column prop="title" :label="$t('strategy.title')" width="200" />
        <el-table-column prop="category" :label="$t('common.category')" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.category === '1day'" type="info">{{ $t('strategy.oneDay') }}</el-tag>
            <el-tag v-else-if="scope.row.category === '2day'" type="warning">{{ $t('strategy.twoDay') }}</el-tag>
            <el-tag v-else-if="scope.row.category === 'theme'" type="success">{{ $t('strategy.theme') }}</el-tag>
            <span v-else>{{ scope.row.category }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="theme" :label="$t('strategy.theme')" width="120" />
        <el-table-column prop="description" :label="$t('common.description')" width="250" show-overflow-tooltip />
        <el-table-column prop="status" :label="$t('common.status')" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : scope.row.status === 0 ? 'warning' : 'danger'">
              {{ scope.row.status === 1 ? $t('strategy.approved') : scope.row.status === 0 ? $t('strategy.pending') : $t('strategy.rejected') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isRecommend" :label="$t('strategy.recommend')" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isRecommend === 1 ? 'success' : 'info'">
              {{ scope.row.isRecommend === 1 ? $t('common.yes') : $t('common.no') }}
            </el-tag>
          </template>
        </el-table-column>
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
      <el-form :model="form" label-width="120px" ref="form">
        <el-form-item :label="$t('strategy.title')" required>
          <el-input v-model="form.title" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('common.category')" required>
              <el-select v-model="form.category" :placeholder="$t('strategy.selectCategory')">
                <el-option :label="$t('strategy.oneDay')" value="1day" />
                <el-option :label="$t('strategy.twoDay')" value="2day" />
                <el-option :label="$t('strategy.theme')" value="theme" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('strategy.theme')">
              <el-input v-model="form.theme" :placeholder="$t('strategy.themePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('strategy.coverImage')">
          <ImageUpload v-model="form.coverImage" />
        </el-form-item>
        <el-form-item :label="$t('strategy.multiImage')">
          <MultiImageUpload v-model="form.images" />
          <div style="margin-top: 8px; color: #909399; font-size: 12px;">
            <i class="el-icon-info"></i>
            {{ $t('strategy.imageTip') }}
          </div>
        </el-form-item>
        <el-form-item :label="$t('common.description')">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item :label="$t('common.content')">
          <el-input v-model="form.content" type="textarea" :rows="6" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('strategy.duration')">
              <el-input-number v-model="form.duration" :min="1" :max="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('strategy.budget')">
              <el-input-number v-model="form.budget" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('strategy.bestSeason')">
              <el-input v-model="form.bestSeason" :placeholder="$t('strategy.bestSeasonPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('strategy.tips')">
          <el-input v-model="form.tips" type="textarea" :rows="3" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('common.status')">
              <el-radio-group v-model="form.status">
                <el-radio :label="0">{{ $t('strategy.pending') }}</el-radio>
                <el-radio :label="1">{{ $t('strategy.approved') }}</el-radio>
                <el-radio :label="2">{{ $t('strategy.rejected') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('strategy.isRecommend')">
              <el-radio-group v-model="form.isRecommend">
                <el-radio :label="1">{{ $t('common.yes') }}</el-radio>
                <el-radio :label="0">{{ $t('common.no') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
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
      dialogTitle: this.$t('admin.addStrategy'),
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
        this.$message.error(this.$t('common.operateFailed'))
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
      this.dialogTitle = this.$t('admin.addStrategy')
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
      this.dialogTitle = this.$t('admin.editStrategy')
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
            this.$message.error(this.$t('common.operateFailed'))
            return
          }
        } else {
          const res = await createStrategy(this.form)
          if (res.code === 200 && res.data) {
            strategyId = res.data.id
          } else {
            this.$message.error(this.$t('common.operateFailed'))
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
            this.$message.warning(this.$t('common.saveSuccess') + '，' + this.$t('common.image') + this.$t('common.operateFailed'))
          }
        }

        this.$message.success(this.form.id ? this.$t('common.updateSuccess') : this.$t('common.createSuccess'))
        this.dialogVisible = false
        this.loadStrategies()
      } catch (error) {
        this.$message.error(this.$t('common.operateFailed'))
      }
    },
    async handleDelete(row) {
      this.$confirm(this.$t('admin.confirmDeleteStrategy'), this.$t('common.tip'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteStrategy(row.id)
          if (res.code === 200) {
            this.$message.success(this.$t('common.deleteSuccess'))
            this.loadStrategies()
          }
        } catch (error) {
          this.$message.error(this.$t('common.deleteFailed'))
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

