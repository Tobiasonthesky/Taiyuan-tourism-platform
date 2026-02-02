<template>
  <div class="admin-experiences">
    <el-card>
      <div slot="header">
        <span>体验项目管理</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleAdd">新增体验项目</el-button>
          <el-button size="small" @click="goBack">返回</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchKeyword" placeholder="项目名称" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 体验项目列表 -->
      <el-table :data="experiences" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="项目名称" width="200" />
        <el-table-column prop="duration" label="时长" width="100">
          <template slot-scope="scope">
            {{ scope.row.duration }}分钟
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="address" label="地址" width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '上架' : '下架' }}
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
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="form" label-width="100px" ref="form">
        <el-form-item label="项目名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="封面图片">
          <ImageUpload v-model="form.coverImage" />
        </el-form-item>
        <el-form-item label="多图片管理">
          <MultiImageUpload v-model="form.images" />
          <div style="margin-top: 8px; color: #909399; font-size: 12px;">
            <i class="el-icon-info"></i>
            提示：保存内容后，图片会自动关联到该体验项目
          </div>
        </el-form-item>
        <el-form-item label="体验时长（分钟）">
          <el-input-number v-model="form.duration" :min="1" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="详细内容">
          <el-input v-model="form.content" type="textarea" :rows="5" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getExperiences, getExperienceImages } from '@/api/experience'
import { createExperience, updateExperience, deleteExperience, createExperienceImage, deleteExperienceImage } from '@/api/admin'
import Pagination from '@/components/common/Pagination'
import ImageUpload from '@/components/common/ImageUpload'
import MultiImageUpload from '@/components/common/MultiImageUpload'

export default {
  name: 'AdminExperiences',
  components: {
    Pagination,
    ImageUpload,
    MultiImageUpload
  },
  data() {
    return {
      experiences: [],
      searchKeyword: '',
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      dialogVisible: false,
      dialogTitle: '新增体验项目',
      form: {
        id: null,
        name: '',
        coverImage: '',
        duration: 60,
        price: 0,
        address: '',
        description: '',
        content: '',
        status: 1,
        images: []
      }
    }
  },
  mounted() {
    this.loadExperiences()
  },
  methods: {
    async loadExperiences() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword
        }

        const res = await getExperiences(params)
        if (res.code === 200) {
          this.experiences = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        this.$message.error('加载体验项目列表失败')
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadExperiences()
    },
    handleReset() {
      this.searchKeyword = ''
      this.page = 1
      this.loadExperiences()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadExperiences()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadExperiences()
    },
    handleAdd() {
      this.dialogTitle = '新增体验项目'
      this.form = {
        id: null,
        name: '',
        coverImage: '',
        duration: 60,
        price: 0,
        address: '',
        description: '',
        content: '',
        status: 1,
        images: []
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = '编辑体验项目'
      this.form = { ...row }
      // 加载现有图片
      if (row.id) {
        try {
          const res = await getExperienceImages(row.id)
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
        let experienceId = this.form.id
        if (this.form.id) {
          const res = await updateExperience(this.form.id, this.form)
          if (res.code === 200) {
            experienceId = this.form.id
          } else {
            this.$message.error('更新失败')
            return
          }
        } else {
          const res = await createExperience(this.form)
          if (res.code === 200 && res.data) {
            experienceId = res.data.id
          } else {
            this.$message.error('创建失败')
            return
          }
        }

        // 保存多图片
        if (experienceId && this.form.images && this.form.images.length > 0) {
          try {
            // 先删除旧的图片（编辑时）
            if (this.form.id) {
              const oldImagesRes = await getExperienceImages(experienceId)
              if (oldImagesRes.code === 200 && oldImagesRes.data) {
                for (const oldImg of oldImagesRes.data) {
                  await deleteExperienceImage(oldImg.id)
                }
              }
            }

            // 创建新图片
            for (let i = 0; i < this.form.images.length; i++) {
              const img = this.form.images[i]
              await createExperienceImage({
                experienceId: experienceId,
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
        this.loadExperiences()
      } catch (error) {
        this.$message.error('操作失败')
      }
    },
    async handleDelete(row) {
      this.$confirm('确定要删除该体验项目吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteExperience(row.id)
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadExperiences()
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      }).catch(() => {
        // 用户取消删除操作，不需要做任何处理
      })
    },
    handleDialogClose() {
      this.$refs.form?.resetFields()
    },
    goBack() {
      this.$router.push('/admin')
    }
  }
}
</script>

<style scoped>
.admin-experiences {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }
}
</style>

