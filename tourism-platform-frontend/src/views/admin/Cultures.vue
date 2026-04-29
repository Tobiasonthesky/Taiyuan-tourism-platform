<template>
  <div class="admin-cultures">
    <el-card>
      <div slot="header" class="card-header">
        <span>{{ $t('admin.cultureManagement') }}</span>
        <div class="card-actions">
          <el-button type="primary" size="small" @click="handleAdd">{{ $t('admin.addCulture') }}</el-button>
          <el-button size="small" @click="goBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item :label="$t('common.keyword')">
          <el-input v-model="searchKeyword" :placeholder="$t('culture.cultureName')" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>

      <!-- 文化列表 -->
      <el-table :data="cultures" v-loading="loading" border>
        <el-table-column prop="id" :label="$t('common.id')" width="80" />
        <el-table-column prop="name" :label="$t('culture.cultureName')" width="200" />
        <el-table-column prop="activityLocation" :label="$t('culture.activityLocation')" width="250" show-overflow-tooltip />
        <el-table-column prop="activityTime" :label="$t('culture.activityTime')" width="150" />
        <el-table-column prop="description" :label="$t('common.description')" width="250" show-overflow-tooltip />
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
        <el-form-item :label="$t('culture.cultureName')" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item :label="$t('culture.coverImage')">
          <ImageUpload v-model="form.coverImage" />
        </el-form-item>
        <el-form-item :label="$t('culture.multiImage')">
          <MultiImageUpload v-model="form.images" />
          <div style="margin-top: 8px; color: #909399; font-size: 12px;">
            <i class="el-icon-info"></i>
            {{ $t('culture.imageTip') }}
          </div>
        </el-form-item>
        <el-form-item :label="$t('culture.activityTime')">
          <el-input v-model="form.activityTime" :placeholder="$t('culture.activityTimeExample')" />
        </el-form-item>
        <el-form-item :label="$t('culture.activityLocation')">
          <el-input 
            v-model="form.activityLocation" 
            :placeholder="$t('culture.pleaseInputAddress')" 
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
        <el-form-item :label="$t('culture.history')">
          <el-input v-model="form.history" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item :label="$t('common.description')">
          <el-input v-model="form.description" type="textarea" :rows="3" />
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
import { getCultures, getCultureImages } from '@/api/culture'
import { createCulture, updateCulture, deleteCulture, createCultureImage, deleteCultureImage } from '@/api/admin'
import Pagination from '@/components/common/Pagination'
import ImageUpload from '@/components/common/ImageUpload'
import MultiImageUpload from '@/components/common/MultiImageUpload'
import AMapLoader from '@amap/amap-jsapi-loader'
import { debounce } from '@/utils/debounce'

export default {
  name: 'AdminCultures',
  components: {
    Pagination,
    ImageUpload,
    MultiImageUpload
  },
  data() {
    return {
      cultures: [],
      searchKeyword: '',
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      dialogVisible: false,
      dialogTitle: this.$t('admin.addCulture'),
      searchingLocation: false,
      form: {
        id: null,
        name: '',
        coverImage: '',
        description: '',
        content: '',
        history: '',
        activityTime: '',
        activityLocation: '',
        longitude: '',
        latitude: '',
        status: 1,
        images: []
      }
    }
  },
  mounted() {
    this.loadCultures()
    // 创建防抖的地理编码函数
    this.debouncedGeocode = debounce(this.getLocationByAddress, 800)
  },
  watch: {
    // 监听活动地点变化，自动解析经纬度
    'form.activityLocation'(newVal) {
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
    async loadCultures() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword
        }

        const res = await getCultures(params)
        if (res.code === 200) {
          this.cultures = res.data?.records || []
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
      this.loadCultures()
    },
    handleReset() {
      this.searchKeyword = ''
      this.page = 1
      this.loadCultures()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadCultures()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadCultures()
    },
    handleAdd() {
      this.dialogTitle = this.$t('admin.addCulture')
      this.form = {
        id: null,
        name: '',
        coverImage: '',
        description: '',
        content: '',
        history: '',
        activityTime: '',
        activityLocation: '',
        longitude: '',
        latitude: '',
        status: 1,
        images: []
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = this.$t('admin.editCulture')
      this.form = {
        ...row,
        activityLocation: row.activityLocation || '',
        activityTime: row.activityTime || '',
        longitude: row.longitude || '',
        latitude: row.latitude || '',
        history: row.history || ''
      }
      // 加载现有图片
      if (row.id) {
        try {
          const res = await getCultureImages(row.id)
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
        // 准备提交的数据，确保字段值正确
        const submitData = {
          ...this.form,
          activityLocation: this.form.activityLocation || '',
          activityTime: this.form.activityTime || '',
          longitude: this.form.longitude || '',
          latitude: this.form.latitude || '',
          history: this.form.history || ''
        }
        
        console.log('提交的文化数据:', JSON.stringify(submitData, null, 2))
        console.log('活动地点:', submitData.activityLocation)
        console.log('经度:', submitData.longitude)
        console.log('纬度:', submitData.latitude)
        
        // 保存基本信息
        let cultureId = this.form.id
        if (this.form.id) {
          const res = await updateCulture(this.form.id, submitData)
          if (res.code === 200) {
            cultureId = this.form.id
          } else {
            this.$message.error(this.$t('common.operateFailed'))
            return
          }
        } else {
          const res = await createCulture(submitData)
          if (res.code === 200 && res.data) {
            cultureId = res.data.id
          } else {
            this.$message.error(this.$t('common.operateFailed'))
            return
          }
        }

        // 保存多图片
        if (cultureId && this.form.images && this.form.images.length > 0) {
          try {
            // 先删除旧的图片（编辑时）
            if (this.form.id) {
              const oldImagesRes = await getCultureImages(cultureId)
              if (oldImagesRes.code === 200 && oldImagesRes.data) {
                for (const oldImg of oldImagesRes.data) {
                  await deleteCultureImage(oldImg.id)
                }
              }
            }

            // 创建新图片
            for (let i = 0; i < this.form.images.length; i++) {
              const img = this.form.images[i]
              await createCultureImage({
                cultureId: cultureId,
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
        this.loadCultures()
      } catch (error) {
        this.$message.error(this.$t('common.operateFailed'))
      }
    },
    async handleDelete(row) {
      this.$confirm(this.$t('admin.confirmDelete').replace('{name}', this.$t('culture.cultureName')), this.$t('admin.tip'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteCulture(row.id)
          if (res.code === 200) {
            this.$message.success(this.$t('common.deleteSuccess'))
            this.loadCultures()
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
      if (this.form.activityLocation && this.form.activityLocation.trim().length > 2 && (!this.form.longitude || !this.form.latitude)) {
        this.getLocationByAddress()
      }
    },
    
    async getLocationByAddress() {
      const address = this.form.activityLocation ? this.form.activityLocation.trim() : ''
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

<style scoped>
.admin-cultures {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }
}
</style>

