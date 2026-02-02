<template>
  <div class="admin-attractions">
    <el-card>
      <div slot="header">
        <span>景点管理</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleAdd">新增景点</el-button>
          <el-button size="small" @click="goBack">返回</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchKeyword" placeholder="景点名称" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 景点列表 -->
      <el-table :data="attractions" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="景点名称" width="200" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="address" label="地址" width="200" />
        <el-table-column prop="ticketPrice" label="门票价格" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.ticketPrice }}
          </template>
        </el-table-column>
        <el-table-column prop="rating" label="评分" width="100" />
        <el-table-column prop="viewCount" label="浏览次数" width="100" />
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
        <el-form-item label="景点名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="分类" required>
          <el-select v-model="form.categoryId" placeholder="请选择分类" clearable style="width: 100%;">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="封面图片">
          <ImageUpload v-model="form.coverImage" />
        </el-form-item>
        <el-form-item label="多图片管理">
          <MultiImageUpload v-model="form.images" />
          <div style="margin-top: 8px; color: #909399; font-size: 12px;">
            <i class="el-icon-info"></i>
            提示：保存内容后，图片会自动关联到该景点
          </div>
        </el-form-item>
        <el-form-item label="地址">
          <el-input 
            v-model="form.address" 
            placeholder="请输入详细地址，系统将自动解析经纬度" 
            @blur="handleAddressBlur"
          >
            <template slot="suffix">
              <i v-if="searchingLocation" class="el-icon-loading"></i>
              <i v-else-if="form.longitude && form.latitude" class="el-icon-success" style="color: #67C23A;"></i>
            </template>
          </el-input>
          <div v-if="form.longitude && form.latitude" style="margin-top: 5px; color: #67C23A; font-size: 12px;">
            <i class="el-icon-success"></i> 已自动获取坐标：{{ form.longitude }}, {{ form.latitude }}
          </div>
        </el-form-item>
        <el-form-item label="经度" prop="longitude" style="display: none;">
          <el-input v-model="form.longitude" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude" style="display: none;">
          <el-input v-model="form.latitude" />
        </el-form-item>
        <el-form-item label="门票价格">
          <el-input-number v-model="form.ticketPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="开放时间">
          <el-input v-model="form.openingHours" />
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
import { getAttractions, getAttractionImages, getAttractionCategories } from '@/api/attraction'
import { createAttraction, updateAttraction, deleteAttraction, createAttractionImage, deleteAttractionImage } from '@/api/admin'
import Pagination from '@/components/common/Pagination'
import ImageUpload from '@/components/common/ImageUpload'
import MultiImageUpload from '@/components/common/MultiImageUpload'
import AMapLoader from '@amap/amap-jsapi-loader'
import { debounce } from '@/utils/debounce'

export default {
  name: 'AdminAttractions',
  components: {
    Pagination,
    ImageUpload,
    MultiImageUpload
  },
  data() {
    return {
      attractions: [],
      categories: [],
      searchKeyword: '',
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      dialogVisible: false,
      dialogTitle: '新增景点',
      AMap: null,
      searchingLocation: false,
      form: {
        id: null,
        name: '',
        categoryId: null,
        coverImage: '',
        address: '',
        longitude: '',
        latitude: '',
        ticketPrice: 0,
        openingHours: '',
        description: '',
        content: '',
        status: 1,
        images: []
      }
    }
  },
  mounted() {
    this.loadAttractions()
    this.loadCategories()
    // 创建防抖的地理编码函数
    this.debouncedGeocode = debounce(this.getLocationByAddress, 800)
  },
  watch: {
    // 监听地址变化，自动解析经纬度
    'form.address'(newVal) {
      // 如果地址不为空且长度大于2，且没有经纬度，则自动解析
      if (newVal && newVal.trim().length > 2 && (!this.form.longitude || !this.form.latitude)) {
        this.debouncedGeocode()
      } else if (!newVal || newVal.trim().length === 0) {
        // 如果地址被清空，也清空经纬度
        this.form.longitude = ''
        this.form.latitude = ''
      }
    }
  },
  methods: {
    async loadAttractions() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword
        }

        const res = await getAttractions(params)
        if (res.code === 200) {
          this.attractions = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        this.$message.error('加载景点列表失败')
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadAttractions()
    },
    handleReset() {
      this.searchKeyword = ''
      this.page = 1
      this.loadAttractions()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadAttractions()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadAttractions()
    },
    async loadCategories() {
      try {
        const res = await getAttractionCategories()
        if (res.code === 200) {
          this.categories = res.data || []
        }
      } catch (error) {
        console.error('加载分类失败:', error)
      }
    },
    handleAdd() {
      this.dialogTitle = '新增景点'
      this.form = {
        id: null,
        name: '',
        categoryId: null,
        coverImage: '',
        address: '',
        longitude: '',
        latitude: '',
        ticketPrice: 0,
        openingHours: '',
        description: '',
        content: '',
        status: 1,
        images: []
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = '编辑景点'
      this.form = { ...row }
      // 加载现有图片
      if (row.id) {
        try {
          const res = await getAttractionImages(row.id)
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
        let attractionId = this.form.id
        if (this.form.id) {
          const res = await updateAttraction(this.form.id, this.form)
          if (res.code === 200) {
            attractionId = this.form.id
          } else {
            this.$message.error('更新失败')
            return
          }
        } else {
          const res = await createAttraction(this.form)
          if (res.code === 200 && res.data) {
            attractionId = res.data.id
          } else {
            this.$message.error('创建失败')
            return
          }
        }

        // 保存多图片
        if (attractionId && this.form.images && this.form.images.length > 0) {
          try {
            // 先删除旧的图片（编辑时）
            if (this.form.id) {
              const oldImagesRes = await getAttractionImages(attractionId)
              if (oldImagesRes.code === 200 && oldImagesRes.data) {
                for (const oldImg of oldImagesRes.data) {
                  await deleteAttractionImage(oldImg.id)
                }
              }
            }

            // 创建新图片
            for (let i = 0; i < this.form.images.length; i++) {
              const img = this.form.images[i]
              await createAttractionImage({
                attractionId: attractionId,
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
        this.loadAttractions()
      } catch (error) {
        this.$message.error('操作失败')
      }
    },
    async handleDelete(row) {
      this.$confirm('确定要删除该景点吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteAttraction(row.id)
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadAttractions()
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
    },
    
    // 地址失焦时也尝试解析（如果还没有经纬度）
    handleAddressBlur() {
      if (this.form.address && this.form.address.trim().length > 2 && (!this.form.longitude || !this.form.latitude)) {
        this.getLocationByAddress()
      }
    },
    
    async getLocationByAddress() {
      const address = this.form.address ? this.form.address.trim() : ''
      if (!address || address.length < 2) {
        return
      }
      
      // 如果已经有经纬度了，不重复解析
      if (this.form.longitude && this.form.latitude) {
        return
      }
      
      this.searchingLocation = true
      try {
        const mapKey = process.env.VUE_APP_MAP_KEY
        const mapSecret = process.env.VUE_APP_MAP_SECRET
        
        if (!mapKey || !mapSecret) {
          console.warn('地图API密钥未配置')
          return
        }
        
        // 全局配置安全密钥
        window._AMapSecurityConfig = {
          securityJsCode: mapSecret
        }
        
        // 加载地理编码服务
        const AMap = await AMapLoader.load({
          key: mapKey,
          securityJsCode: mapSecret,
          version: '2.0',
          plugins: ['AMap.Geocoder']
        })
        
        // 创建地理编码器实例
        const geocoder = new AMap.Geocoder({
          city: '太原市', // 默认城市
          radius: 1000 // 范围，默认：500
        })
        
        // 进行地理编码
        geocoder.getLocation(address, (status, result) => {
          this.searchingLocation = false
          if (status === 'complete' && result.geocodes && result.geocodes.length) {
            const location = result.geocodes[0].location
            this.form.longitude = location.lng
            this.form.latitude = location.lat
            console.log('自动解析地址成功:', address, '坐标:', this.form.longitude, this.form.latitude)
            // 不显示成功消息，避免打扰用户输入
          } else {
            console.warn('地址解析失败:', address)
            // 解析失败时不显示错误消息，避免打扰用户
          }
        })
      } catch (error) {
        console.error('获取经纬度失败:', error)
        this.searchingLocation = false
        // 静默失败，不打扰用户
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-attractions {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }
}
</style>

