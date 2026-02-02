<template>
  <div class="admin-hotels">
    <el-card>
      <div slot="header">
        <span>酒店管理</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleAdd">新增酒店</el-button>
          <el-button size="small" @click="goBack">返回</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchKeyword" placeholder="酒店名称" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 酒店列表 -->
      <el-table :data="hotels" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="酒店名称" width="200" />
        <el-table-column prop="address" label="地址" width="200" />
        <el-table-column prop="starLevel" label="星级" width="100">
          <template slot-scope="scope">
            <el-rate :value="scope.row.starLevel" disabled />
          </template>
        </el-table-column>
        <el-table-column prop="minPrice" label="最低价格" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.minPrice }}/晚
          </template>
        </el-table-column>
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
      width="1000px"
      @close="handleDialogClose"
    >
      <el-form :model="form" label-width="100px" ref="form">
        <el-form-item label="酒店名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="封面图片">
          <ImageUpload v-model="form.coverImage" />
        </el-form-item>
        <el-form-item label="多图片管理">
          <MultiImageUpload v-model="form.images" />
          <div style="margin-top: 8px; color: #909399; font-size: 12px;">
            <i class="el-icon-info"></i>
            提示：保存内容后，图片会自动关联到该酒店
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
        <el-form-item label="星级">
          <el-rate v-model="form.starLevel" :max="5" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <!-- 房间信息 -->
        <el-divider>房间信息</el-divider>
        <el-form-item>
          <el-button type="primary" size="small" @click="handleAddRoom">添加房间</el-button>
        </el-form-item>
        <el-table :data="form.rooms" border style="width: 100%" v-if="form.rooms && form.rooms.length > 0">
          <el-table-column prop="roomName" label="房间名称" width="150" />
          <el-table-column prop="roomType" label="房间类型" width="120" />
          <el-table-column prop="price" label="价格" width="100">
            <template slot-scope="scope">
              ¥{{ scope.row.price }}/晚
            </template>
          </el-table-column>
          <el-table-column prop="area" label="面积(㎡)" width="100" />
          <el-table-column prop="bedType" label="床型" width="120" />
          <el-table-column prop="maxOccupancy" label="最大入住" width="100" />
          <el-table-column prop="stock" label="库存" width="80" />
          <el-table-column label="操作" width="150" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleEditRoom(scope.$index)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDeleteRoom(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="暂无房间信息" :image-size="80" />
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 房间编辑对话框 -->
    <el-dialog
      :title="roomDialogTitle"
      :visible.sync="roomDialogVisible"
      width="700px"
      @close="handleRoomDialogClose"
    >
      <el-form :model="roomForm" label-width="100px" ref="roomForm">
        <el-form-item label="房间名称" required>
          <el-input v-model="roomForm.roomName" placeholder="如：豪华大床房" />
        </el-form-item>
        <el-form-item label="房间类型" required>
          <el-select v-model="roomForm.roomType" placeholder="请选择房间类型" style="width: 100%">
            <el-option label="标准间" value="标准间" />
            <el-option label="大床房" value="大床房" />
            <el-option label="双床房" value="双床房" />
            <el-option label="套房" value="套房" />
            <el-option label="豪华间" value="豪华间" />
            <el-option label="行政间" value="行政间" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格(元/晚)" required>
          <el-input-number v-model="roomForm.price" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="面积(㎡)">
          <el-input-number v-model="roomForm.area" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="床型">
          <el-input v-model="roomForm.bedType" placeholder="如：1.8米大床" />
        </el-form-item>
        <el-form-item label="最大入住人数">
          <el-input-number v-model="roomForm.maxOccupancy" :min="1" :max="10" style="width: 100%" />
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input-number v-model="roomForm.stock" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="房间图片">
          <ImageUpload v-model="roomForm.image" />
        </el-form-item>
        <el-form-item label="房间描述">
          <el-input v-model="roomForm.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="roomForm.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="roomDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRoomSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getHotels, getHotelImages } from '@/api/hotel'
import { createHotel, updateHotel, deleteHotel, createHotelImage, deleteHotelImage } from '@/api/admin'
import Pagination from '@/components/common/Pagination'
import ImageUpload from '@/components/common/ImageUpload'
import MultiImageUpload from '@/components/common/MultiImageUpload'
import AMapLoader from '@amap/amap-jsapi-loader'
import { debounce } from '@/utils/debounce'

export default {
  name: 'AdminHotels',
  components: {
    Pagination,
    ImageUpload,
    MultiImageUpload
  },
  data() {
    return {
      hotels: [],
      searchKeyword: '',
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      dialogVisible: false,
      dialogTitle: '新增酒店',
      form: {
        id: null,
        name: '',
        address: '',
        longitude: '',
        latitude: '',
        starLevel: 3,
        minPrice: 0,
        phone: '',
        description: '',
        status: 1,
        rooms: []
      },
      roomDialogVisible: false,
      roomDialogTitle: '添加房间',
      roomForm: {
        roomName: '',
        roomType: '',
        price: 0,
        area: 0,
        bedType: '',
        maxOccupancy: 2,
        stock: 0,
        image: '',
        description: '',
        status: 1
      },
      editingRoomIndex: -1,
      searchingLocation: false
    }
  },
  mounted() {
    this.loadHotels()
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
    async loadHotels() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword
        }

        const res = await getHotels(params)
        if (res.code === 200) {
          this.hotels = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        this.$message.error('加载酒店列表失败')
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadHotels()
    },
    handleReset() {
      this.searchKeyword = ''
      this.page = 1
      this.loadHotels()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadHotels()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadHotels()
    },
    handleAdd() {
      this.dialogTitle = '新增酒店'
      this.form = {
        id: null,
        name: '',
        coverImage: '',
        address: '',
        longitude: '',
        latitude: '',
        starLevel: 3,
        minPrice: 0,
        phone: '',
        description: '',
        status: 1,
        rooms: [],
        images: []
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = '编辑酒店'
      this.form = { ...row }
      // 加载现有图片
      if (row.id) {
        try {
          const res = await getHotelImages(row.id)
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
      // 加载房间信息
      if (this.form.id) {
        try {
          const { getHotelRooms } = await import('@/api/hotel')
          const res = await getHotelRooms(this.form.id)
          if (res.code === 200) {
            this.form.rooms = res.data || []
          }
        } catch (error) {
          console.error('加载房间信息失败:', error)
          this.form.rooms = []
        }
      } else {
        this.form.rooms = []
      }
      this.dialogVisible = true
    },
    handleAddRoom() {
      this.roomDialogTitle = '添加房间'
      this.editingRoomIndex = -1
      this.roomForm = {
        roomName: '',
        roomType: '',
        price: 0,
        area: 0,
        bedType: '',
        maxOccupancy: 2,
        stock: 0,
        image: '',
        description: '',
        status: 1
      }
      this.roomDialogVisible = true
    },
    handleEditRoom(index) {
      this.roomDialogTitle = '编辑房间'
      this.editingRoomIndex = index
      this.roomForm = { ...this.form.rooms[index] }
      this.roomDialogVisible = true
    },
    handleDeleteRoom(index) {
      this.$confirm('确定要删除该房间吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.form.rooms.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {
        // 用户点击取消，不需要处理
      })
    },
    handleRoomSubmit() {
      if (!this.roomForm.roomName || !this.roomForm.roomType || !this.roomForm.price) {
        this.$message.warning('请填写房间名称、类型和价格')
        return
      }
      if (!this.form.rooms) {
        this.form.rooms = []
      }
      if (this.editingRoomIndex >= 0) {
        // 编辑
        this.$set(this.form.rooms, this.editingRoomIndex, { ...this.roomForm })
      } else {
        // 新增
        this.form.rooms.push({ ...this.roomForm })
      }
      this.roomDialogVisible = false
      this.$message.success(this.editingRoomIndex >= 0 ? '更新成功' : '添加成功')
    },
    handleRoomDialogClose() {
      this.$refs.roomForm?.resetFields()
      this.editingRoomIndex = -1
    },
    async handleSubmit() {
      try {
        // 准备提交数据
        const submitData = {
          ...this.form
        }
        // 移除 images 字段，不提交到基本信息
        delete submitData.images
        
        let hotelId = this.form.id
        if (this.form.id) {
          const res = await updateHotel(this.form.id, submitData)
          if (res.code === 200) {
            hotelId = this.form.id
          } else {
            this.$message.error('更新失败')
            return
          }
        } else {
          const res = await createHotel(submitData)
          if (res.code === 200 && res.data && res.data.id) {
            hotelId = res.data.id
          } else {
            this.$message.error('创建失败')
            return
          }
        }

        // 保存房间信息
        if (hotelId && this.form.rooms && this.form.rooms.length > 0) {
          await this.saveRooms(hotelId, this.form.rooms)
        }

        // 保存多图片
        if (hotelId && this.form.images && this.form.images.length > 0) {
          try {
            // 先删除旧的图片（编辑时）
            if (this.form.id) {
              const oldImagesRes = await getHotelImages(hotelId)
              if (oldImagesRes.code === 200 && oldImagesRes.data) {
                for (const oldImg of oldImagesRes.data) {
                  await deleteHotelImage(oldImg.id)
                }
              }
            }

            // 创建新图片
            for (let i = 0; i < this.form.images.length; i++) {
              const img = this.form.images[i]
              await createHotelImage({
                hotelId: hotelId,
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
        this.loadHotels()
      } catch (error) {
        this.$message.error('操作失败：' + (error.response?.data?.message || error.message))
      }
    },
    async saveRooms(hotelId, rooms) {
      try {
        const { createHotelRoom, updateHotelRoom, deleteHotelRoom } = await import('@/api/admin')
        // 获取现有房间列表
        const { getHotelRooms } = await import('@/api/hotel')
        const res = await getHotelRooms(hotelId)
        const existingRooms = res.code === 200 ? (res.data || []) : []
        const existingRoomIds = existingRooms.map(r => r.id)
        
        // 处理房间：新增、更新、删除
        for (const room of rooms) {
          if (room.id && existingRoomIds.includes(room.id)) {
            // 更新现有房间
            await updateHotelRoom(room.id, { ...room, hotelId })
          } else {
            // 新增房间
            await createHotelRoom({ ...room, hotelId })
          }
        }
        
        // 删除不在新列表中的房间
        const newRoomIds = rooms.filter(r => r.id).map(r => r.id)
        for (const existingRoom of existingRooms) {
          if (!newRoomIds.includes(existingRoom.id)) {
            await deleteHotelRoom(existingRoom.id)
          }
        }
      } catch (error) {
        console.error('保存房间信息失败:', error)
        throw error
      }
    },
    async handleDelete(row) {
      this.$confirm('确定要删除该酒店吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteHotel(row.id)
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadHotels()
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      }).catch(() => {
        // 用户点击取消，不需要处理
      })
    },
    handleDialogClose() {
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
    },
    goBack() {
      this.$router.push('/admin')
    }
  }
}
</script>

<style scoped>
.admin-hotels {
  padding: 20px;
}

.admin-hotels .search-form {
  margin-bottom: 20px;
}
</style>

