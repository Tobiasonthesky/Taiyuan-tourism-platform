<template>
  <div class="hotel-detail">
    <div class="container" v-if="hotel">
      <!-- 返回按钮 -->
      <el-button icon="el-icon-arrow-left" @click="goBack" style="margin-bottom: 20px;">返回</el-button>
      <!-- 基本信息 -->
      <el-card class="detail-card">
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 图片轮播 -->
            <el-carousel v-if="images.length > 0" height="400px" class="image-carousel">
              <el-carousel-item v-for="(img, index) in images" :key="index">
                <img :src="img.imageUrl" class="detail-image" />
              </el-carousel-item>
            </el-carousel>
            <!-- 单张图片 -->
            <LazyImage
              v-else-if="hotel.coverImage"
              :src="hotel.coverImage"
              :alt="hotel.name"
              width="100%"
              height="400px"
            />
          </el-col>
          <el-col :span="12">
            <h1>{{ hotel.name }}</h1>
            <div class="info-item">
              <el-rate
                :value="hotel.starLevel"
                disabled
                show-score
                text-color="#ff9900"
              />
              <span class="rating">评分：{{ hotel.rating }}</span>
            </div>
            <div class="info-item">
              <span class="label">地址：</span>
              <span>{{ hotel.address }}</span>
            </div>
            <div class="info-item">
              <span class="label">联系电话：</span>
              <span>{{ hotel.phone }}</span>
            </div>
            <div class="info-item">
              <span class="label">最低价格：</span>
              <span class="price">¥{{ hotel.minPrice }}/晚起</span>
            </div>
            <div class="actions">
              <el-button
                type="success"
                size="medium"
                icon="el-icon-location"
                @click="viewOnMap"
                v-if="hotel.longitude && hotel.latitude"
              >
                查看地图
              </el-button>
              <el-button
                :type="isFavorite ? 'danger' : 'default'"
                size="medium"
                @click="handleFavorite"
                :disabled="!isLogin"
              >
                {{ isFavorite ? '已收藏' : '收藏' }}
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 房间列表 -->
      <el-card class="detail-card" v-if="rooms.length > 0">
        <h2>房间类型</h2>
        <el-row :gutter="20">
          <el-col :span="8" v-for="room in rooms" :key="room.id">
            <el-card class="room-card">
              <LazyImage
                :src="room.image || ''"
                :alt="room.roomName"
                width="100%"
                height="150px"
              />
              <div class="room-info">
                <h3>{{ room.roomName }}</h3>
                <p class="room-type">{{ room.roomType }}</p>
                <p class="room-desc">{{ room.description }}</p>
                <div class="room-meta">
                  <span>面积：{{ room.area }}㎡</span>
                  <span>床型：{{ room.bedType }}</span>
                  <span>可住：{{ room.maxOccupancy }}人</span>
                </div>
                <div class="room-price">
                  <span class="price">¥{{ room.price }}/晚</span>
                  <el-button
                    type="primary"
                    size="small"
                    @click.stop="bookRoom(room)"
                  >
                    预订
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- 无房间提示 -->
      <el-card class="detail-card" v-else-if="hotel && roomsLoaded">
        <el-empty description="该酒店暂无可用房间">
          <template slot="image">
            <i class="el-icon-box" style="font-size: 80px; color: #ddd;"></i>
          </template>
          <div style="margin-top: 20px;">
            <p style="color: #999; margin-bottom: 10px;">请联系酒店获取更多信息</p>
            <el-button type="primary" @click="contactHotel">联系酒店</el-button>
          </div>
        </el-empty>
      </el-card>

      <!-- 详细介绍 -->
      <el-card class="detail-card">
        <h2>详细介绍</h2>
        <div class="content" v-html="hotel.description"></div>
      </el-card>

      <!-- 评论 -->
      <el-card class="detail-card">
        <h2>用户评论</h2>
        <CommentList
          :target-type="'hotel'"
          :target-id="hotel.id"
        />
      </el-card>

      <!-- 预订对话框 -->
      <el-dialog
        title="预订房间"
        :visible.sync="bookingDialogVisible"
        width="600px"
        @close="resetBookingForm"
      >
        <el-form :model="bookingForm" label-width="100px">
          <el-form-item label="酒店名称">
            <el-input :value="hotel?.name" disabled />
          </el-form-item>
              <el-option
                v-for="room in rooms"
                :key="room.id"
                :label="`${room.roomName} - ¥${room.price}/晚`"
                :value="room"
              >
                <div style="display: flex; justify-content: space-between; align-items: center;">
                  <div>
                    <div style="font-weight: bold;">{{ room.roomName }}</div>
                    <div style="font-size: 12px; color: #999;">
                      {{ room.roomType }} | {{ room.area }}㎡ | {{ room.bedType }} | 可住{{ room.maxOccupancy }}人
                    </div>
                  </div>
                  <div style="color: #f56c6c; font-weight: bold;">
                    ¥{{ room.price }}/晚
                  </div>
                </div>
              </el-option>
          <!-- 选中房间的详细信息 -->
          <el-form-item v-if="selectedRoom" label="房间信息">
            <el-card shadow="never" style="background-color: #f5f7fa;">
              <div style="display: flex; gap: 20px;">
                <div v-if="selectedRoom.image" style="flex-shrink: 0;">
                  <img 
                    :src="selectedRoom.image" 
                    :alt="selectedRoom.roomName"
                    style="width: 120px; height: 90px; object-fit: cover; border-radius: 4px;"
                  />
                </div>
                <div style="flex: 1;">
                  <div style="font-size: 16px; font-weight: bold; margin-bottom: 8px;">
                    {{ selectedRoom.roomName }}
                  </div>
                  <div style="font-size: 14px; color: #666; margin-bottom: 4px;">
                    <span>类型：{{ selectedRoom.roomType }}</span>
                    <span style="margin-left: 15px;">面积：{{ selectedRoom.area }}㎡</span>
                  </div>
                  <div style="font-size: 14px; color: #666; margin-bottom: 4px;">
                    <span>床型：{{ selectedRoom.bedType }}</span>
                    <span style="margin-left: 15px;">最大入住：{{ selectedRoom.maxOccupancy }}人</span>
                    <span style="margin-left: 15px;">库存：{{ selectedRoom.stock || 0 }}间</span>
                  </div>
                  <div v-if="selectedRoom.description" style="font-size: 13px; color: #999; margin-top: 8px;">
                    {{ selectedRoom.description }}
                  </div>
                  <div style="font-size: 18px; color: #f56c6c; font-weight: bold; margin-top: 8px;">
                    ¥{{ selectedRoom.price }}/晚
                  </div>
                </div>
              </div>
            </el-card>
          </el-form-item>
          <el-form-item label="入住日期" required>
            <el-date-picker
              v-model="bookingForm.checkInDate"
              type="date"
              placeholder="选择入住日期"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
          <el-form-item label="退房日期" required>
            <el-date-picker
              v-model="bookingForm.checkOutDate"
              type="date"
              placeholder="选择退房日期"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
          <el-form-item label="房间数量" required>
            <el-input-number
              v-model="bookingForm.quantity"
              :min="1"
              :max="selectedRoom?.stock || selectedRoom?.roomCount || 10"
            />
            <span v-if="selectedRoom" style="margin-left: 10px; color: #999; font-size: 12px;">
              （当前库存：{{ selectedRoom.stock || 0 }}间）
            </span>
          </el-form-item>
          <el-form-item label="订单总额" v-if="selectedRoom">
            <span class="total-price">
              ¥{{ calculateTotalPrice() }}
            </span>
          </el-form-item>
          <el-form-item label="联系人" required>
            <el-input v-model="bookingForm.contactName" />
          </el-form-item>
          <el-form-item label="联系电话" required>
            <el-input v-model="bookingForm.contactPhone" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input
              v-model="bookingForm.remark"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="bookingDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleBooking">确认预订</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { getHotelDetail, getHotelRooms, getHotelImages } from '@/api/hotel'
import { checkFavorite, addFavorite, removeFavorite } from '@/api/favorite'
import { createOrder } from '@/api/order'
import { mapGetters } from 'vuex'
import CommentList from '@/components/common/CommentList'
import LazyImage from '@/components/common/LazyImage'

export default {
  name: 'HotelDetail',
  components: {
    CommentList,
    LazyImage
  },
  data() {
    return {
      hotel: null,
      rooms: [],
      images: [],
      roomsLoaded: false,
      isFavorite: false,
      bookingDialogVisible: false,
      selectedRoom: null,
      bookingForm: {
        checkInDate: '',
        checkOutDate: '',
        quantity: 1,
        contactName: '',
        contactPhone: '',
        remark: ''
      }
    }
  },
  computed: {
    ...mapGetters('user', ['isLogin', 'userInfo'])
  },
  watch: {
    userInfo: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.bookingForm.contactName = newVal.nickname || newVal.username || ''
          this.bookingForm.contactPhone = newVal.phone || ''
        }
      }
    }
  },
  mounted() {
    this.loadDetail()
  },
  methods: {
    async loadDetail() {
      try {
        const id = this.$route.params.id
        this.roomsLoaded = false
        const [hotelRes, roomsRes] = await Promise.all([
          getHotelDetail(id),
          getHotelRooms(id)
        ])
        if (hotelRes.code === 200) {
          this.hotel = hotelRes.data
          this.loadImages()
        }
        if (roomsRes.code === 200) {
          this.rooms = roomsRes.data || []
          // 如果有房间，默认选择第一个
          if (this.rooms.length > 0 && !this.selectedRoom) {
            this.selectedRoom = this.rooms[0]
          }
        } else {
          console.warn('加载房间列表失败:', roomsRes)
          this.rooms = []
        }
        this.roomsLoaded = true
        // 数据加载完成后再检查收藏状态
        if (this.isLogin && this.hotel) {
          this.checkFavoriteStatus()
        }
      } catch (error) {
        console.error('加载详情失败:', error)
        this.roomsLoaded = true
        this.$message.error('加载详情失败：' + (error.response?.data?.message || error.message))
      }
    },
    async loadImages() {
      try {
        const res = await getHotelImages(this.hotel.id)
        if (res.code === 200) {
          this.images = res.data || []
          // 如果没有图片但有封面图，使用封面图
          if (this.images.length === 0 && this.hotel.coverImage) {
            this.images = [{ imageUrl: this.hotel.coverImage }]
          }
        }
      } catch (error) {
        console.error('加载图片失败:', error)
      }
    },
    contactHotel() {
      if (this.hotel && this.hotel.phone) {
        // 可以复制电话号码或打开电话应用
        const phone = this.hotel.phone
        if (navigator.clipboard) {
          navigator.clipboard.writeText(phone).then(() => {
            this.$message.success('联系电话已复制到剪贴板：' + phone)
          })
        } else {
          this.$message.info('联系电话：' + phone)
        }
      } else {
        this.$message.warning('该酒店未提供联系电话')
      }
    },
    async checkFavoriteStatus() {
      // 确保 hotel 已加载
      if (!this.hotel || !this.hotel.id) {
        return
      }
      try {
        const res = await checkFavorite('hotel', this.hotel.id)
        if (res.code === 200) {
          this.isFavorite = res.data || false
        }
      } catch (error) {
        console.error('检查收藏状态失败:', error)
      }
    },
    async handleFavorite() {
      if (!this.isLogin) {
        this.$message.warning('请先登录')
        return
      }

      try {
        if (this.isFavorite) {
          await removeFavorite('hotel', this.hotel.id)
          this.isFavorite = false
          this.$message.success('取消收藏成功')
        } else {
          await addFavorite({
            targetType: 'hotel',
            targetId: this.hotel.id
          })
          this.isFavorite = true
          this.$message.success('收藏成功')
        }
      } catch (error) {
        this.$message.error('操作失败')
      }
    },
    showBookingDialog() {
      if (!this.isLogin) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }
      // 如果没有房间列表，提示用户
      if (this.rooms.length === 0) {
        this.$message.warning('该酒店暂无可用房间，请联系酒店')
        return
      }
      // 自动选择第一个房间
      if (!this.selectedRoom) {
        this.selectedRoom = this.rooms[0]
      }
      // 初始化表单数据
      if (this.userInfo) {
        this.bookingForm.contactName = this.userInfo.nickname || this.userInfo.username || ''
        this.bookingForm.contactPhone = this.userInfo.phone || ''
      }
      this.bookingDialogVisible = true
    },
    bookRoom(room) {
      if (!this.isLogin) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }
      this.selectedRoom = room
      // 初始化表单数据
      if (this.userInfo) {
        this.bookingForm.contactName = this.userInfo.nickname || this.userInfo.username || ''
        this.bookingForm.contactPhone = this.userInfo.phone || ''
      }
      this.bookingDialogVisible = true
    },
    resetBookingForm() {
      this.bookingForm = {
        checkInDate: '',
        checkOutDate: '',
        quantity: 1,
        contactName: this.userInfo?.nickname || this.userInfo?.username || '',
        contactPhone: this.userInfo?.phone || '',
        remark: ''
      }
    },
    calculateTotalPrice() {
      if (!this.selectedRoom || !this.selectedRoom.price) {
        return 0
      }
      const nights = this.calculateNights()
      return (this.selectedRoom.price * this.bookingForm.quantity * nights).toFixed(2)
    },
    calculateNights() {
      if (!this.bookingForm.checkInDate || !this.bookingForm.checkOutDate) {
        return 1
      }
      const checkIn = new Date(this.bookingForm.checkInDate)
      const checkOut = new Date(this.bookingForm.checkOutDate)
      const diffTime = Math.abs(checkOut - checkIn)
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      return diffDays > 0 ? diffDays : 1
    },
    onRoomChange(room) {
      // 房间变化时，更新库存限制
      if (room && room.stock !== undefined && room.stock !== null) {
        if (this.bookingForm.quantity > room.stock) {
          this.bookingForm.quantity = room.stock
        }
      }
    },
    async handleBooking() {
      if (!this.selectedRoom) {
        this.$message.warning('请选择房间')
        return
      }
      if (!this.bookingForm.checkInDate || !this.bookingForm.checkOutDate) {
        this.$message.warning('请选择入住和退房日期')
        return
      }
      // 验证日期
      const checkIn = new Date(this.bookingForm.checkInDate)
      const checkOut = new Date(this.bookingForm.checkOutDate)
      if (checkOut <= checkIn) {
        this.$message.warning('退房日期必须晚于入住日期')
        return
      }
      if (!this.bookingForm.contactName || !this.bookingForm.contactPhone) {
        this.$message.warning('请填写联系信息')
        return
      }

      try {
        const orderData = {
          orderType: 'hotel',
          items: [{
            itemType: 'hotel',
            itemId: this.selectedRoom.id,
            quantity: this.bookingForm.quantity,
            useDate: this.bookingForm.checkInDate,
            // 添加房间相关信息
            roomType: this.selectedRoom.roomType,
            roomName: this.selectedRoom.roomName,
            roomPrice: this.selectedRoom.price,
            checkInDate: this.bookingForm.checkInDate,
            checkOutDate: this.bookingForm.checkOutDate
          }],
          contactName: this.bookingForm.contactName,
          contactPhone: this.bookingForm.contactPhone,
          remark: `入住：${this.bookingForm.checkInDate}，退房：${this.bookingForm.checkOutDate}。${this.bookingForm.remark || ''}`
        }

        const res = await createOrder(orderData)
        if (res.code === 200) {
          this.$message.success('预订成功')
          this.bookingDialogVisible = false
          this.$router.push(`/order/${res.data.id}`)
        }
      } catch (error) {
        this.$message.error('预订失败：' + (error.response?.data?.message || error.message || '未知错误'))
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    viewOnMap() {
      // 跳转到地图页面，并传递定位参数
      if (this.hotel.longitude && this.hotel.latitude) {
        this.$router.push({
          path: '/map',
          query: {
            type: 'hotel',
            id: this.hotel.id,
            lng: this.hotel.longitude,
            lat: this.hotel.latitude,
            name: this.hotel.name
          }
        })
      } else {
        this.$message.warning('该酒店暂无位置信息')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.hotel-detail {
  padding: 20px 0;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .detail-card {
    margin-bottom: 20px;

    h1 {
      font-size: 28px;
      margin-bottom: 20px;
      color: #303133;
    }

    h2 {
      font-size: 20px;
      margin-bottom: 15px;
      color: #303133;
    }

    .info-item {
      margin-bottom: 15px;
      font-size: 16px;
      display: flex;
      align-items: center;
      gap: 10px;

      .label {
        color: #909399;
        margin-right: 10px;
      }

      .price {
        color: #f56c6c;
        font-size: 24px;
        font-weight: bold;
      }

      .rating {
        color: #606266;
      }
    }

    .actions {
      margin-top: 30px;
    }

    .content {
      line-height: 1.8;
      color: #606266;
    }

    .room-card {
      margin-bottom: 20px;
      cursor: pointer;
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .room-info {
        padding: 15px 0 0;

        h3 {
          font-size: 18px;
          margin-bottom: 5px;
        }

        .room-type {
          color: #909399;
          font-size: 14px;
          margin-bottom: 10px;
        }

        .room-desc {
          color: #606266;
          font-size: 14px;
          margin-bottom: 10px;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }

        .room-meta {
          display: flex;
          flex-direction: column;
          gap: 5px;
          color: #909399;
          font-size: 12px;
          margin-bottom: 10px;
        }

        .room-price {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .price {
            color: #f56c6c;
            font-size: 20px;
            font-weight: bold;
          }
        }
      }
    }
  }
}
</style>

