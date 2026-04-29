<template>
  <div class="admin-foods">
    <el-card>
      <div slot="header" class="card-header">
        <span>{{ $t('admin.foodManagement') }}</span>
        <div class="card-actions">
          <el-button type="primary" size="small" @click="handleAdd">{{ $t('admin.addFood') }}</el-button>
          <el-button size="small" @click="goBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item :label="$t('common.keyword')">
          <el-input v-model="searchKeyword" :placeholder="$t('food.name')" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>

      <!-- 美食列表 -->
      <el-table :data="foods" v-loading="loading" border>
        <el-table-column prop="id" :label="$t('common.id')" width="80" />
        <el-table-column prop="name" :label="$t('food.name')" width="200" />
        <el-table-column prop="categoryName" :label="$t('common.category')" width="120" />
        <el-table-column prop="restaurant" :label="$t('food.restaurant')" width="200" />
        <el-table-column prop="address" :label="$t('common.address')" width="300" show-overflow-tooltip />
        <el-table-column prop="description" :label="$t('common.description')" width="300" show-overflow-tooltip />
        <el-table-column prop="status" :label="$t('common.status')" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? $t('admin.online') : $t('admin.offline') }}
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
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="form" label-width="100px" ref="form">
        <el-form-item :label="$t('food.name')" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item :label="$t('common.category')" required>
          <el-select v-model="form.categoryId" :placeholder="$t('food.selectCategory')" clearable style="width: 100%;">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('food.coverImage')">
          <ImageUpload v-model="form.coverImage" />
        </el-form-item>
        <el-form-item :label="$t('food.multiImage')">
          <MultiImageUpload v-model="form.images" />
          <div style="margin-top: 8px; color: #909399; font-size: 12px;">
            <i class="el-icon-info"></i>
            {{ $t('food.imageTip') }}
          </div>
        </el-form-item>
        <el-form-item :label="$t('food.restaurant')">
          <el-input v-model="form.restaurant" />
        </el-form-item>
        <el-form-item :label="$t('common.address')">
          <el-input 
            v-model="form.address" 
            :placeholder="$t('food.pleaseInputAddress')" 
            @blur="handleAddressBlur"
          >
            <template slot="suffix">
              <i v-if="searchingLocation" class="el-icon-loading"></i>
              <i v-else-if="form.longitude && form.latitude" class="el-icon-success" style="color: #67C23A;"></i>
            </template>
          </el-input>
          <div v-if="form.longitude && form.latitude" style="margin-top: 5px; color: #67C23A; font-size: 12px;">
            <i class="el-icon-success"></i> {{ $t('common.autoLocated') }}：{{ form.longitude }}, {{ form.latitude }}
          </div>
        </el-form-item>
        <el-form-item :label="$t('common.longitude')" prop="longitude" style="display: none;">
          <el-input v-model="form.longitude" />
        </el-form-item>
        <el-form-item :label="$t('common.latitude')" prop="latitude" style="display: none;">
          <el-input v-model="form.latitude" />
        </el-form-item>
        <el-form-item :label="$t('common.description')">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item :label="$t('food.ingredients')">
          <el-input v-model="form.ingredients" />
        </el-form-item>
        <el-form-item :label="$t('food.cookingMethod')">
          <el-input v-model="form.cookingMethod" type="textarea" :rows="5" />
        </el-form-item>
        <el-form-item :label="$t('common.content')">
          <el-input v-model="form.content" type="textarea" :rows="5" />
        </el-form-item>
        <el-form-item :label="$t('common.status')">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">{{ $t('admin.online') }}</el-radio>
            <el-radio :label="0">{{ $t('admin.offline') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFoods, getFoodImages, getFoodCategories } from '@/api/food'
import { createFood, updateFood, deleteFood, createFoodImage, deleteFoodImage } from '@/api/admin'
import Pagination from '@/components/common/Pagination'
import ImageUpload from '@/components/common/ImageUpload'
import MultiImageUpload from '@/components/common/MultiImageUpload'
import AMapLoader from '@amap/amap-jsapi-loader'
import { debounce } from '@/utils/debounce'

export default {
  name: 'AdminFoods',
  components: {
    Pagination,
    ImageUpload,
    MultiImageUpload
  },
  data() {
    return {
      foods: [],
      categories: [],
      searchKeyword: '',
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      dialogVisible: false,
      dialogTitle: this.$t('admin.addFood'),
      searchingLocation: false,
      form: {
        id: null,
        name: '',
        categoryId: null,
        coverImage: '',
        restaurant: '',
        address: '',
        longitude: '',
        latitude: '',
        description: '',
        ingredients: '',
        cookingMethod: '',
        content: '',
        status: 1,
        images: []
      }
    }
  },
  mounted() {
    this.loadFoods()
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
    async loadFoods() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword
        }

        const res = await getFoods(params)
        if (res.code === 200) {
          this.foods = res.data?.records || []
          this.total = res.data?.total || 0
          console.log('获取的美食列表数据:', this.foods)
          if (this.foods.length > 0) {
            console.log('第一条美食数据:', this.foods[0])
            console.log('第一条美食的餐厅字段:', this.foods[0].restaurant)
            console.log('第一条美食的地址字段:', this.foods[0].address)
            console.log('第一条美食的所有字段:', Object.keys(this.foods[0]))
          }
        }
      } catch (error) {
        this.$message.error(this.$t('common.operateFailed'))
        console.error('加载美食列表错误:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadFoods()
    },
    handleReset() {
      this.searchKeyword = ''
      this.page = 1
      this.loadFoods()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadFoods()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadFoods()
    },
    async loadCategories() {
      try {
        const res = await getFoodCategories()
        if (res.code === 200) {
          this.categories = res.data || []
        }
      } catch (error) {
        console.error('加载分类失败:', error)
      }
    },
    handleAdd() {
      this.dialogTitle = this.$t('admin.addFood')
      this.form = {
        id: null,
        name: '',
        categoryId: null,
        coverImage: '',
        restaurant: '',
        address: '',
        longitude: '',
        latitude: '',
        description: '',
        ingredients: '',
        cookingMethod: '',
        content: '',
        status: 1,
        images: []
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = this.$t('admin.editFood')
      console.log('编辑美食数据:', row)
      console.log('编辑时的餐厅字段:', row.restaurant)
      console.log('编辑时的地址字段:', row.address)
      this.form = { ...row }
      // 加载现有图片
      if (row.id) {
        try {
          const res = await getFoodImages(row.id)
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
      console.log('编辑表单初始化后:', this.form)
      this.dialogVisible = true
    },
    async handleSubmit() {
      try {
        // 验证：如果有地址但没有经纬度，提示用户（但不阻止提交，因为地址本身也是有用的）
        if (this.form.address && (!this.form.longitude || !this.form.latitude)) {
          const confirmed = await this.$confirm(this.$t('common.address') + this.$t('common.no') + this.$t('common.longitude') + this.$t('common.latitude') + '，' + this.$t('common.map') + this.$t('common.no') + this.$t('common.show') + this.$t('food.restaurant') + '。' + this.$t('common.continue') + this.$t('common.save') + '？', this.$t('common.tip'), {
            confirmButtonText: this.$t('common.continue') + this.$t('common.save'),
            cancelButtonText: this.$t('common.cancel'),
            type: 'warning'
          }).catch(() => false)
          if (!confirmed) {
            return
          }
        }
        
        // 准备提交的数据，确保字段值正确
        const submitData = {
          ...this.form,
          // 确保空字符串转换为null或保留为空字符串（后端会处理）
          restaurant: this.form.restaurant || '',
          address: this.form.address || '',
          longitude: this.form.longitude || '',
          latitude: this.form.latitude || ''
        }
        
        console.log('提交的美食数据:', JSON.stringify(submitData, null, 2))
        console.log('餐厅字段:', submitData.restaurant, '(类型:', typeof submitData.restaurant, ')')
        console.log('地址字段:', submitData.address, '(类型:', typeof submitData.address, ')')
        console.log('经度字段:', submitData.longitude, '(类型:', typeof submitData.longitude, ')')
        console.log('纬度字段:', submitData.latitude, '(类型:', typeof submitData.latitude, ')')
        
        // 保存基本信息
        let foodId = this.form.id
        if (this.form.id) {
          const res = await updateFood(this.form.id, submitData)
          if (res.code === 200) {
            foodId = this.form.id
            console.log('更新成功响应:', res)
            console.log('更新返回的数据 - recommendedRestaurants:', res.data?.recommendedRestaurants)
            console.log('更新返回的数据 - restaurant:', res.data?.restaurant)
            console.log('更新返回的数据 - address:', res.data?.address)
          } else {
            this.$message.error(this.$t('common.operateFailed'))
            console.log('更新失败响应:', res)
            return
          }
        } else {
          const res = await createFood(submitData)
          if (res.code === 200 && res.data) {
            foodId = res.data.id
            console.log('创建成功响应:', res)
            console.log('创建返回的美食数据:', res.data)
            console.log('创建返回的数据 - recommendedRestaurants:', res.data?.recommendedRestaurants)
            console.log('创建返回的数据 - restaurant:', res.data?.restaurant)
            console.log('创建返回的数据 - address:', res.data?.address)
          } else {
            this.$message.error(this.$t('common.operateFailed'))
            console.log('创建失败响应:', res)
            return
          }
        }

        // 保存多图片
        if (foodId && this.form.images && this.form.images.length > 0) {
          try {
            // 先删除旧的图片（编辑时）
            if (this.form.id) {
              const oldImagesRes = await getFoodImages(foodId)
              if (oldImagesRes.code === 200 && oldImagesRes.data) {
                for (const oldImg of oldImagesRes.data) {
                  await deleteFoodImage(oldImg.id)
                }
              }
            }

            // 创建新图片
            for (let i = 0; i < this.form.images.length; i++) {
              const img = this.form.images[i]
              await createFoodImage({
                foodId: foodId,
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
        this.loadFoods()
      } catch (error) {
        this.$message.error(this.$t('common.operateFailed'))
        console.error('操作失败错误:', error)
      }
    },
    async handleDelete(row) {
      this.$confirm(this.$t('admin.confirmDelete').replace('{name}', this.$t('food.name')), this.$t('admin.tip'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteFood(row.id)
          if (res.code === 200) {
            this.$message.success(this.$t('common.deleteSuccess'))
            this.loadFoods()
          }
        } catch (error) {
          this.$message.error(this.$t('common.deleteFailed'))
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
.admin-foods {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }
}
</style>