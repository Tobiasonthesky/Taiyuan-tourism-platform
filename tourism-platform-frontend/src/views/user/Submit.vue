<template>
  <div class="submit-page">
    <el-card>
      <div slot="header">
        <span>提交内容</span>
        <div style="float: right;">
          <el-button size="small" @click="goBack">返回</el-button>
        </div>
      </div>

      <!-- 类型选择 -->
      <el-form :inline="true" style="margin-bottom: 20px;">
        <el-form-item label="内容类型">
          <el-select v-model="contentType" @change="handleTypeChange" style="width: 200px;">
            <el-option label="景点" value="attraction" />
            <el-option label="美食" value="food" />
            <el-option label="文化" value="culture" />
            <el-option label="攻略" value="strategy" />
          </el-select>
        </el-form-item>
      </el-form>

      <!-- 景点表单 -->
      <el-form v-if="contentType === 'attraction'" :model="attractionForm" label-width="120px" ref="attractionForm">
        <el-form-item label="景点名称" required>
          <el-input v-model="attractionForm.name" placeholder="请输入景点名称" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input 
            v-model="attractionForm.address" 
            placeholder="请输入详细地址，系统将自动解析经纬度" 
            @blur="handleAttractionAddressBlur"
          >
            <template slot="suffix">
              <i v-if="searchingAttractionLocation" class="el-icon-loading"></i>
              <i v-else-if="attractionForm.longitude && attractionForm.latitude" class="el-icon-success" style="color: #67C23A;"></i>
            </template>
          </el-input>
          <div v-if="attractionForm.longitude && attractionForm.latitude" style="margin-top: 5px; color: #67C23A; font-size: 12px;">
            <i class="el-icon-success"></i> 已自动获取坐标：{{ attractionForm.longitude }}, {{ attractionForm.latitude }}
          </div>
        </el-form-item>
        <el-form-item label="经度" prop="longitude" style="display: none;">
          <el-input v-model="attractionForm.longitude" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude" style="display: none;">
          <el-input v-model="attractionForm.latitude" />
        </el-form-item>
        <el-form-item label="门票价格">
          <el-input-number v-model="attractionForm.ticketPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="开放时间">
          <el-input v-model="attractionForm.openingHours" placeholder="如：8:00-18:00" />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-input v-model="attractionForm.coverImage" placeholder="图片URL" />
        </el-form-item>
        <el-form-item label="多图片（每行一个URL）">
          <el-input
            v-model="attractionForm.imageUrls"
            type="textarea"
            :rows="4"
            placeholder="每行输入一个图片URL，支持多张图片"
          />
        </el-form-item>
        <el-form-item label="描述" required>
          <el-input v-model="attractionForm.description" type="textarea" :rows="4" placeholder="请输入景点描述" />
        </el-form-item>
        <el-form-item label="详细内容">
          <el-input v-model="attractionForm.content" type="textarea" :rows="6" placeholder="请输入详细内容" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAttraction">提交审核</el-button>
          <el-button @click="resetAttractionForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 美食表单 -->
      <el-form v-if="contentType === 'food'" :model="foodForm" label-width="120px" ref="foodForm">
        <el-form-item label="美食名称" required>
          <el-input v-model="foodForm.name" placeholder="请输入美食名称" />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-input v-model="foodForm.coverImage" placeholder="图片URL" />
        </el-form-item>
        <el-form-item label="多图片（每行一个URL）">
          <el-input
            v-model="foodForm.imageUrls"
            type="textarea"
            :rows="4"
            placeholder="每行输入一个图片URL，支持多张图片"
          />
        </el-form-item>
        <el-form-item label="描述" required>
          <el-input v-model="foodForm.description" type="textarea" :rows="4" placeholder="请输入美食描述" />
        </el-form-item>
        <el-form-item label="主要食材">
          <el-input v-model="foodForm.ingredients" placeholder="请输入主要食材" />
        </el-form-item>
        <el-form-item label="推荐餐厅名称">
          <el-input v-model="foodForm.restaurant" placeholder="请输入餐厅名称" />
        </el-form-item>
        <el-form-item label="餐厅地址">
          <el-input 
            v-model="foodForm.address" 
            placeholder="请输入餐厅详细地址，系统将自动解析经纬度" 
            @blur="handleFoodAddressBlur"
          >
            <template slot="suffix">
              <i v-if="searchingFoodLocation" class="el-icon-loading"></i>
              <i v-else-if="foodForm.longitude && foodForm.latitude" class="el-icon-success" style="color: #67C23A;"></i>
            </template>
          </el-input>
          <div v-if="foodForm.longitude && foodForm.latitude" style="margin-top: 5px; color: #67C23A; font-size: 12px;">
            <i class="el-icon-success"></i> 已自动获取坐标：{{ foodForm.longitude }}, {{ foodForm.latitude }}
          </div>
        </el-form-item>
        <el-form-item label="经度" prop="longitude" style="display: none;">
          <el-input v-model="foodForm.longitude" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude" style="display: none;">
          <el-input v-model="foodForm.latitude" />
        </el-form-item>
        <el-form-item label="详细内容">
          <el-input v-model="foodForm.content" type="textarea" :rows="6" placeholder="请输入详细内容" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitFood">提交审核</el-button>
          <el-button @click="resetFoodForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 文化表单 -->
      <el-form v-if="contentType === 'culture'" :model="cultureForm" label-width="120px" ref="cultureForm">
        <el-form-item label="文化名称" required>
          <el-input v-model="cultureForm.name" placeholder="请输入文化名称" />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-input v-model="cultureForm.coverImage" placeholder="图片URL" />
        </el-form-item>
        <el-form-item label="多图片（每行一个URL）">
          <el-input
            v-model="cultureForm.imageUrls"
            type="textarea"
            :rows="4"
            placeholder="每行输入一个图片URL，支持多张图片"
          />
        </el-form-item>
        <el-form-item label="描述" required>
          <el-input v-model="cultureForm.description" type="textarea" :rows="4" placeholder="请输入文化描述" />
        </el-form-item>
        <el-form-item label="历史背景">
          <el-input v-model="cultureForm.history" type="textarea" :rows="4" placeholder="请输入历史背景" />
        </el-form-item>
        <el-form-item label="活动时间">
          <el-input v-model="cultureForm.activityTime" placeholder="请输入活动时间" />
        </el-form-item>
        <el-form-item label="活动地点">
          <el-input 
            v-model="cultureForm.activityLocation" 
            placeholder="请输入活动地点详细地址，系统将自动解析经纬度" 
            @blur="handleCultureAddressBlur"
          >
            <template slot="suffix">
              <i v-if="searchingCultureLocation" class="el-icon-loading"></i>
              <i v-else-if="cultureForm.longitude && cultureForm.latitude" class="el-icon-success" style="color: #67C23A;"></i>
            </template>
          </el-input>
          <div v-if="cultureForm.longitude && cultureForm.latitude" style="margin-top: 5px; color: #67C23A; font-size: 12px;">
            <i class="el-icon-success"></i> 已自动获取坐标：{{ cultureForm.longitude }}, {{ cultureForm.latitude }}
          </div>
        </el-form-item>
        <el-form-item label="经度" prop="longitude" style="display: none;">
          <el-input v-model="cultureForm.longitude" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude" style="display: none;">
          <el-input v-model="cultureForm.latitude" />
        </el-form-item>
        <el-form-item label="详细内容">
          <el-input v-model="cultureForm.content" type="textarea" :rows="6" placeholder="请输入详细内容" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitCulture">提交审核</el-button>
          <el-button @click="resetCultureForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 攻略表单 -->
      <el-form v-if="contentType === 'strategy'" :model="strategyForm" label-width="120px" ref="strategyForm">
        <el-form-item label="攻略标题" required>
          <el-input v-model="strategyForm.title" placeholder="请输入攻略标题" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="strategyForm.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="1日游" value="1day" />
            <el-option label="2日游" value="2day" />
            <el-option label="主题游" value="theme" />
          </el-select>
        </el-form-item>
        <el-form-item label="主题">
          <el-input v-model="strategyForm.theme" placeholder="如：亲子、情侣、摄影等" />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-input v-model="strategyForm.coverImage" placeholder="图片URL" />
        </el-form-item>
        <el-form-item label="多图片（每行一个URL）">
          <el-input
            v-model="strategyForm.imageUrls"
            type="textarea"
            :rows="4"
            placeholder="每行输入一个图片URL，支持多张图片"
          />
        </el-form-item>
        <el-form-item label="描述" required>
          <el-input v-model="strategyForm.description" type="textarea" :rows="4" placeholder="请输入攻略描述" />
        </el-form-item>
        <el-form-item label="游玩时长（天）">
          <el-input-number v-model="strategyForm.duration" :min="1" />
        </el-form-item>
        <el-form-item label="预算（元）">
          <el-input-number v-model="strategyForm.budget" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="最佳季节">
          <el-input v-model="strategyForm.bestSeason" placeholder="如：春季、夏季等" />
        </el-form-item>
        <el-form-item label="详细内容">
          <el-input v-model="strategyForm.content" type="textarea" :rows="8" placeholder="请输入详细攻略内容" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitStrategy">提交审核</el-button>
          <el-button @click="resetStrategyForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { submitAttraction, submitFood, submitCulture, submitStrategy } from '@/api/user'
import { createAttractionImage, createFoodImage, createCultureImage, createStrategyImage } from '@/api/admin'
import AMapLoader from '@amap/amap-jsapi-loader'
import { debounce } from '@/utils/debounce'

export default {
  name: 'UserSubmit',
  data() {
    return {
      contentType: 'attraction',
      searchingAttractionLocation: false,
      searchingFoodLocation: false,
      searchingCultureLocation: false,
      attractionForm: {
        name: '',
        address: '',
        longitude: '',
        latitude: '',
        ticketPrice: 0,
        openingHours: '',
        coverImage: '',
        imageUrls: '',
        description: '',
        content: ''
      },
      foodForm: {
        name: '',
        restaurant: '',
        address: '',
        longitude: '',
        latitude: '',
        coverImage: '',
        imageUrls: '',
        description: '',
        ingredients: '',
        content: ''
      },
      cultureForm: {
        name: '',
        coverImage: '',
        imageUrls: '',
        description: '',
        history: '',
        activityTime: '',
        activityLocation: '',
        longitude: '',
        latitude: '',
        content: ''
      },
      strategyForm: {
        title: '',
        category: '',
        theme: '',
        coverImage: '',
        imageUrls: '',
        description: '',
        duration: 1,
        budget: 0,
        bestSeason: '',
        content: ''
      }
    }
  },
  mounted() {
    // 创建防抖的地理编码函数
    this.debouncedAttractionGeocode = debounce(() => this.getAttractionLocationByAddress(), 800)
    this.debouncedFoodGeocode = debounce(() => this.getFoodLocationByAddress(), 800)
    this.debouncedCultureGeocode = debounce(() => this.getCultureLocationByAddress(), 800)
  },
  watch: {
    // 监听景点地址变化，自动解析经纬度
    'attractionForm.address'(newVal) {
      if (newVal && newVal.trim().length > 2 && (!this.attractionForm.longitude || !this.attractionForm.latitude)) {
        this.debouncedAttractionGeocode()
      } else if (!newVal || newVal.trim().length === 0) {
        this.attractionForm.longitude = ''
        this.attractionForm.latitude = ''
      }
    },
    // 监听美食地址变化，自动解析经纬度
    'foodForm.address'(newVal) {
      if (newVal && newVal.trim().length > 2 && (!this.foodForm.longitude || !this.foodForm.latitude)) {
        this.debouncedFoodGeocode()
      } else if (!newVal || newVal.trim().length === 0) {
        this.foodForm.longitude = ''
        this.foodForm.latitude = ''
      }
    },
    // 监听文化地址变化，自动解析经纬度
    'cultureForm.activityLocation'(newVal) {
      if (newVal && newVal.trim().length > 2 && (!this.cultureForm.longitude || !this.cultureForm.latitude)) {
        this.debouncedCultureGeocode()
      } else if (!newVal || newVal.trim().length === 0) {
        this.cultureForm.longitude = ''
        this.cultureForm.latitude = ''
      }
    }
  },
  methods: {
    handleTypeChange() {
      // 切换类型时重置表单
    },
    async submitAttraction() {
      if (!this.attractionForm.name || !this.attractionForm.description) {
        this.$message.warning('请填写景点名称和描述')
        return
      }
      try {
        const formData = { ...this.attractionForm }
        const res = await submitAttraction(formData)
        if (res.code === 200) {
          // 如果有图片URL，创建图片记录（图片创建失败不影响提交成功）
          if (formData.imageUrls && res.data && res.data.id) {
            try {
              await this.createImages('attraction', res.data.id, formData.imageUrls)
            } catch (imageError) {
              console.error('创建图片失败，但景点已提交成功:', imageError)
            }
          }
          this.$message.success(res.message || '提交成功，等待审核')
          this.resetAttractionForm()
        } else {
          this.$message.error(res.message || '提交失败')
        }
      } catch (error) {
        console.error('提交景点失败:', error)
        let errorMessage = '提交失败'
        if (error.response && error.response.data) {
          if (error.response.data.message) {
            errorMessage = String(error.response.data.message)
          } else if (error.response.data.error) {
            errorMessage = String(error.response.data.error)
          }
        } else if (error.message) {
          errorMessage = '提交失败：' + String(error.message)
        } else {
          errorMessage = '提交失败：网络错误'
        }
        this.$message.error(errorMessage)
      }
    },
    async submitFood() {
      if (!this.foodForm.name || !this.foodForm.description) {
        this.$message.warning('请填写美食名称和描述')
        return
      }
      try {
        // 清理空字符串字段，将空字符串转为 null 或删除
        const formData = { ...this.foodForm }
        // 将空字符串字段转为 null，避免后端处理时出错
        if (formData.imageUrls === '') formData.imageUrls = null
        
        const res = await submitFood(formData)
        if (res.code === 200) {
          // 如果有图片URL，创建图片记录（图片创建失败不影响提交成功）
          if (formData.imageUrls && res.data && res.data.id) {
            try {
              await this.createImages('food', res.data.id, formData.imageUrls)
            } catch (imageError) {
              console.error('创建图片失败，但美食已提交成功:', imageError)
            }
          }
          this.$message.success(res.message || '提交成功，等待审核')
          this.resetFoodForm()
        } else {
          this.$message.error(res.message || '提交失败')
        }
      } catch (error) {
        console.error('提交美食失败:', error)
        let errorMessage = '提交失败'
        if (error.response && error.response.data) {
          if (error.response.data.message) {
            errorMessage = String(error.response.data.message)
          } else if (error.response.data.error) {
            errorMessage = String(error.response.data.error)
          }
        } else if (error.message) {
          errorMessage = '提交失败：' + String(error.message)
        } else {
          errorMessage = '提交失败：网络错误'
        }
        this.$message.error(errorMessage)
      }
    },
    async submitCulture() {
      if (!this.cultureForm.name || !this.cultureForm.description) {
        this.$message.warning('请填写文化名称和描述')
        return
      }
      try {
        // 清理空字符串字段，将空字符串转为 null 或删除
        const formData = { ...this.cultureForm }
        // 将空字符串字段转为 null，避免后端处理时出错
        if (formData.imageUrls === '') formData.imageUrls = null
        
        const res = await submitCulture(formData)
        if (res.code === 200) {
          // 如果有图片URL，创建图片记录（图片创建失败不影响提交成功）
          if (formData.imageUrls && res.data && res.data.id) {
            try {
              await this.createImages('culture', res.data.id, formData.imageUrls)
            } catch (imageError) {
              console.error('创建图片失败，但文化已提交成功:', imageError)
            }
          }
          this.$message.success(res.message || '提交成功，等待审核')
          this.resetCultureForm()
        } else {
          this.$message.error(res.message || '提交失败')
        }
      } catch (error) {
        console.error('提交文化失败:', error)
        let errorMessage = '提交失败'
        if (error.response && error.response.data) {
          if (error.response.data.message) {
            errorMessage = String(error.response.data.message)
          } else if (error.response.data.error) {
            errorMessage = String(error.response.data.error)
          }
        } else if (error.message) {
          errorMessage = '提交失败：' + String(error.message)
        } else {
          errorMessage = '提交失败：网络错误'
        }
        this.$message.error(errorMessage)
      }
    },
    async submitStrategy() {
      if (!this.strategyForm.title || !this.strategyForm.description) {
        this.$message.warning('请填写攻略标题和描述')
        return
      }
      try {
        // 清理空字符串字段，将空字符串转为 null 或删除
        const formData = { ...this.strategyForm }
        // 将空字符串字段转为 null，避免后端处理时出错
        if (formData.category === '') formData.category = null
        if (formData.theme === '') formData.theme = null
        if (formData.coverImage === '') formData.coverImage = null
        if (formData.bestSeason === '') formData.bestSeason = null
        if (formData.content === '') formData.content = null
        if (formData.imageUrls === '') formData.imageUrls = null
        
        const res = await submitStrategy(formData)
        if (res.code === 200) {
          // 如果有图片URL，创建图片记录（图片创建失败不影响提交成功）
          if (formData.imageUrls && res.data && res.data.id) {
            try {
              await this.createImages('strategy', res.data.id, formData.imageUrls)
            } catch (imageError) {
              console.error('创建图片失败，但攻略已提交成功:', imageError)
              // 图片创建失败不影响提交成功，只记录错误
            }
          }
          this.$message.success(res.message || '提交成功，等待审核')
          this.resetStrategyForm()
        } else {
          // 如果返回码不是200，显示后端返回的错误信息
          this.$message.error(res.message || '提交失败')
        }
      } catch (error) {
        console.error('提交攻略失败:', error)
        let errorMessage = '提交失败'
        if (error.response && error.response.data) {
          if (error.response.data.message) {
            errorMessage = String(error.response.data.message)
          } else if (error.response.data.error) {
            errorMessage = String(error.response.data.error)
          }
        } else if (error.message) {
          errorMessage = '提交失败：' + String(error.message)
        } else {
          errorMessage = '提交失败：网络错误'
        }
        this.$message.error(errorMessage)
      }
    },
    async createImages(type, id, imageUrls) {
      // 严格检查参数
      if (!id || id === null || id === undefined) {
        console.warn('createImages: id 无效', id)
        return
      }
      
      if (!imageUrls || imageUrls === null || imageUrls === undefined) {
        console.warn('createImages: imageUrls 无效', imageUrls)
        return
      }
      
      // 确保 imageUrls 是字符串类型
      let imageUrlsStr = ''
      try {
        imageUrlsStr = String(imageUrls)
      } catch (e) {
        console.error('createImages: 转换 imageUrls 为字符串失败', e)
        return
      }
      
      if (!imageUrlsStr || imageUrlsStr.trim() === '' || imageUrlsStr === 'null' || imageUrlsStr === 'undefined') {
        return
      }
      
      // 分割 URL
      let urls = []
      try {
        urls = imageUrlsStr.split('\n').filter(url => url && typeof url === 'string' && url.trim() !== '')
      } catch (e) {
        console.error('createImages: 分割 URL 失败', e)
        return
      }
      
      if (!urls || urls.length === 0) {
        return
      }
      
      try {
        const imageTypeMap = {
          attraction: { field: 'attractionId', api: createAttractionImage },
          food: { field: 'foodId', api: createFoodImage },
          culture: { field: 'cultureId', api: createCultureImage },
          strategy: { field: 'strategyId', api: createStrategyImage }
        }
        
        const config = imageTypeMap[type]
        if (!config || !config.api) {
          console.warn('createImages: 无效的类型', type)
          return
        }
        
        for (let i = 0; i < urls.length; i++) {
          const url = urls[i]
          if (!url || typeof url !== 'string') {
            continue
          }
          
          const trimmedUrl = url.trim()
          if (!trimmedUrl) {
            continue
          }
          
          const imageData = {
            [config.field]: id,
            imageUrl: trimmedUrl,
            imageType: i === 0 ? 'cover' : 'detail',
            sortOrder: i + 1
          }
          
          try {
            await config.api(imageData)
          } catch (apiError) {
            console.error(`createImages: 创建第 ${i + 1} 张图片失败`, apiError)
            // 继续处理下一张图片
          }
        }
      } catch (error) {
        console.error('createImages: 创建图片失败', error)
        // 不阻止提交，只记录错误
      }
    },
    // 景点地址失焦处理
    handleAttractionAddressBlur() {
      if (this.attractionForm.address && this.attractionForm.address.trim().length > 2 && (!this.attractionForm.longitude || !this.attractionForm.latitude)) {
        this.getAttractionLocationByAddress()
      }
    },
    
    // 美食地址失焦处理
    handleFoodAddressBlur() {
      if (this.foodForm.address && this.foodForm.address.trim().length > 2 && (!this.foodForm.longitude || !this.foodForm.latitude)) {
        this.getFoodLocationByAddress()
      }
    },
    
    // 文化地址失焦处理
    handleCultureAddressBlur() {
      if (this.cultureForm.activityLocation && this.cultureForm.activityLocation.trim().length > 2 && (!this.cultureForm.longitude || !this.cultureForm.latitude)) {
        this.getCultureLocationByAddress()
      }
    },
    
    // 获取景点经纬度
    async getAttractionLocationByAddress() {
      const address = this.attractionForm.address ? this.attractionForm.address.trim() : ''
      if (!address || address.length < 2 || (this.attractionForm.longitude && this.attractionForm.latitude)) {
        return
      }
      
      this.searchingAttractionLocation = true
      try {
        const mapKey = process.env.VUE_APP_MAP_KEY
        const mapSecret = process.env.VUE_APP_MAP_SECRET
        
        if (!mapKey || !mapSecret) {
          console.warn('地图API密钥未配置')
          return
        }
        
        window._AMapSecurityConfig = { securityJsCode: mapSecret }
        
        const AMap = await AMapLoader.load({
          key: mapKey,
          securityJsCode: mapSecret,
          version: '2.0',
          plugins: ['AMap.Geocoder']
        })
        
        const geocoder = new AMap.Geocoder({ city: '太原市', radius: 1000 })
        
        geocoder.getLocation(address, (status, result) => {
          this.searchingAttractionLocation = false
          if (status === 'complete' && result.geocodes && result.geocodes.length) {
            const location = result.geocodes[0].location
            this.attractionForm.longitude = location.lng
            this.attractionForm.latitude = location.lat
            console.log('景点地址自动解析成功:', address, '坐标:', this.attractionForm.longitude, this.attractionForm.latitude)
          }
        })
      } catch (error) {
        console.error('获取景点经纬度失败:', error)
        this.searchingAttractionLocation = false
      }
    },
    
    // 获取美食经纬度
    async getFoodLocationByAddress() {
      const address = this.foodForm.address ? this.foodForm.address.trim() : ''
      if (!address || address.length < 2 || (this.foodForm.longitude && this.foodForm.latitude)) {
        return
      }
      
      this.searchingFoodLocation = true
      try {
        const mapKey = process.env.VUE_APP_MAP_KEY
        const mapSecret = process.env.VUE_APP_MAP_SECRET
        
        if (!mapKey || !mapSecret) {
          console.warn('地图API密钥未配置')
          return
        }
        
        window._AMapSecurityConfig = { securityJsCode: mapSecret }
        
        const AMap = await AMapLoader.load({
          key: mapKey,
          securityJsCode: mapSecret,
          version: '2.0',
          plugins: ['AMap.Geocoder']
        })
        
        const geocoder = new AMap.Geocoder({ city: '太原市', radius: 1000 })
        
        geocoder.getLocation(address, (status, result) => {
          this.searchingFoodLocation = false
          if (status === 'complete' && result.geocodes && result.geocodes.length) {
            const location = result.geocodes[0].location
            this.foodForm.longitude = location.lng
            this.foodForm.latitude = location.lat
            console.log('美食地址自动解析成功:', address, '坐标:', this.foodForm.longitude, this.foodForm.latitude)
          }
        })
      } catch (error) {
        console.error('获取美食经纬度失败:', error)
        this.searchingFoodLocation = false
      }
    },
    
    // 获取文化经纬度
    async getCultureLocationByAddress() {
      const address = this.cultureForm.activityLocation ? this.cultureForm.activityLocation.trim() : ''
      if (!address || address.length < 2 || (this.cultureForm.longitude && this.cultureForm.latitude)) {
        return
      }
      
      this.searchingCultureLocation = true
      try {
        const mapKey = process.env.VUE_APP_MAP_KEY
        const mapSecret = process.env.VUE_APP_MAP_SECRET
        
        if (!mapKey || !mapSecret) {
          console.warn('地图API密钥未配置')
          return
        }
        
        window._AMapSecurityConfig = { securityJsCode: mapSecret }
        
        const AMap = await AMapLoader.load({
          key: mapKey,
          securityJsCode: mapSecret,
          version: '2.0',
          plugins: ['AMap.Geocoder']
        })
        
        const geocoder = new AMap.Geocoder({ city: '太原市', radius: 1000 })
        
        geocoder.getLocation(address, (status, result) => {
          this.searchingCultureLocation = false
          if (status === 'complete' && result.geocodes && result.geocodes.length) {
            const location = result.geocodes[0].location
            this.cultureForm.longitude = location.lng
            this.cultureForm.latitude = location.lat
            console.log('文化地址自动解析成功:', address, '坐标:', this.cultureForm.longitude, this.cultureForm.latitude)
          }
        })
      } catch (error) {
        console.error('获取文化经纬度失败:', error)
        this.searchingCultureLocation = false
      }
    },
    
    resetAttractionForm() {
      this.attractionForm = {
        name: '',
        address: '',
        longitude: '',
        latitude: '',
        ticketPrice: 0,
        openingHours: '',
        coverImage: '',
        imageUrls: '',
        description: '',
        content: ''
      }
      // 使用 nextTick 确保表单引用存在后再重置
      this.$nextTick(() => {
        if (this.$refs.attractionForm) {
          this.$refs.attractionForm.resetFields()
        }
      })
    },
    resetFoodForm() {
      this.foodForm = {
        name: '',
        restaurant: '',
        address: '',
        longitude: '',
        latitude: '',
        coverImage: '',
        imageUrls: '',
        description: '',
        ingredients: '',
        content: ''
      }
      // 使用 nextTick 确保表单引用存在后再重置
      this.$nextTick(() => {
        if (this.$refs.foodForm) {
          this.$refs.foodForm.resetFields()
        }
      })
    },
    resetCultureForm() {
      this.cultureForm = {
        name: '',
        coverImage: '',
        imageUrls: '',
        description: '',
        history: '',
        activityTime: '',
        activityLocation: '',
        longitude: '',
        latitude: '',
        content: ''
      }
      // 使用 nextTick 确保表单引用存在后再重置
      this.$nextTick(() => {
        if (this.$refs.cultureForm) {
          this.$refs.cultureForm.resetFields()
        }
      })
    },
    resetStrategyForm() {
      this.strategyForm = {
        title: '',
        category: '',
        theme: '',
        coverImage: '',
        imageUrls: '',
        description: '',
        duration: 1,
        budget: 0,
        bestSeason: '',
        content: ''
      }
      // 使用 nextTick 确保表单引用存在后再重置
      this.$nextTick(() => {
        if (this.$refs.strategyForm) {
          this.$refs.strategyForm.resetFields()
        }
      })
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.submit-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
</style>

