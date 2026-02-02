<template>
  <div class="attraction-detail">
    <div class="container" v-if="attraction">
      <!-- 返回按钮 -->
      <el-button icon="el-icon-arrow-left" @click="goBack" style="margin-bottom: 20px;">返回</el-button>
      <!-- 基本信息 -->
      <el-card class="detail-card">
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 视频展示 -->
            <div v-if="attraction.videoUrl" class="video-section">
              <video
                :src="attraction.videoUrl"
                controls
                class="detail-video"
                preload="metadata"
              >
                您的浏览器不支持视频播放
              </video>
            </div>
            <!-- 图片轮播 -->
            <el-carousel v-else-if="images.length > 0" height="400px">
              <el-carousel-item v-for="(img, index) in images" :key="index">
                <img :src="img.imageUrl" class="detail-image" />
              </el-carousel-item>
            </el-carousel>
            <!-- 单张封面图 -->
            <img v-else-if="attraction.coverImage" :src="attraction.coverImage" class="detail-image" />
            <!-- 无图片占位符 -->
            <div v-else class="image-placeholder">
              <i class="el-icon-picture-outline"></i>
              <span>暂无图片</span>
            </div>
          </el-col>
          <el-col :span="12">
            <h1>{{ attraction.name }}</h1>
            <div class="info-item" v-if="attraction.categoryName">
              <span class="label">分类：</span>
              <span>{{ attraction.categoryName }}</span>
            </div>
            <div class="info-item">
              <span class="label">门票价格：</span>
              <span class="price">¥{{ attraction.ticketPrice }}</span>
            </div>
            <div class="info-item" v-if="attraction.openingHours">
              <span class="label">开放时间：</span>
              <span>{{ attraction.openingHours }}</span>
            </div>
            <div class="info-item" v-if="attraction.address">
              <span class="label">地址：</span>
              <span>{{ attraction.address }}</span>
            </div>
            <div class="info-item">
              <span class="label">浏览次数：</span>
              <span>{{ attraction.viewCount }}</span>
            </div>
            <div class="info-item" v-if="attraction.rating">
              <span class="label">评分：</span>
              <el-rate
                :value="attraction.rating"
                disabled
                show-score
                text-color="#ff9900"
              />
            </div>
            <div class="actions">
              <el-button type="primary" size="medium" @click="showBookingDialog">
                立即预订
              </el-button>
              <el-button
                type="success"
                size="medium"
                icon="el-icon-location"
                @click="viewOnMap"
                v-if="attraction.longitude && attraction.latitude"
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

      <!-- 详细介绍 -->
      <el-card class="detail-card">
        <h2>详细介绍</h2>
        <div class="content" v-html="attraction.description"></div>
      </el-card>

      <!-- 评论 -->
      <el-card class="detail-card">
        <h2>用户评论</h2>
        <CommentList
          :target-type="'attraction'"
          :target-id="attraction.id"
          @refresh="loadComments"
        />
      </el-card>

      <!-- 预订对话框 -->
      <el-dialog
        title="门票预订"
        :visible.sync="bookingDialogVisible"
        width="600px"
        @close="resetBookingForm"
      >
        <el-form :model="bookingForm" label-width="100px">
          <el-form-item label="景点名称">
            <span>{{ attraction?.name }}</span>
          </el-form-item>
          <el-form-item label="门票价格">
            <span class="price">¥{{ attraction?.ticketPrice }}/张</span>
          </el-form-item>
          <el-form-item label="预订数量" required>
            <el-input-number
              v-model="bookingForm.quantity"
              :min="1"
              :max="10"
              label="张"
            />
          </el-form-item>
          <el-form-item label="使用日期" required>
            <el-date-picker
              v-model="bookingForm.useDate"
              type="date"
              placeholder="选择使用日期"
              value-format="yyyy-MM-dd"
              :picker-options="datePickerOptions"
            />
          </el-form-item>
          <el-form-item label="联系人" required>
            <el-input v-model="bookingForm.contactName" placeholder="请输入联系人姓名" />
          </el-form-item>
          <el-form-item label="联系电话" required>
            <el-input v-model="bookingForm.contactPhone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input
              v-model="bookingForm.remark"
              type="textarea"
              :rows="3"
              placeholder="选填"
            />
          </el-form-item>
          <el-form-item label="订单总额">
            <span class="total-price">
              ¥{{ (attraction?.ticketPrice || 0) * (bookingForm.quantity || 1) }}
            </span>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="bookingDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleBooking">确认预订</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { getAttractionDetail, getAttractionImages } from '@/api/attraction'
import { checkFavorite, addFavorite, removeFavorite } from '@/api/favorite'
import { mapGetters } from 'vuex'
import CommentList from '@/components/common/CommentList'

export default {
  name: 'AttractionDetail',
  components: {
    CommentList
  },
  data() {
    return {
      attraction: null,
      images: [],
      isFavorite: false,
      bookingDialogVisible: false,
      bookingForm: {
        quantity: 1,
        useDate: '',
        contactName: '',
        contactPhone: '',
        remark: ''
      },
      datePickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7 // 不能选择今天之前的日期
        }
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
        const res = await getAttractionDetail(id)
        if (res.code === 200) {
          this.attraction = res.data
          this.loadImages()
          // 数据加载完成后再检查收藏状态
          if (this.isLogin && this.attraction) {
            this.checkFavoriteStatus()
          }
        }
      } catch (error) {
        this.$message.error('加载详情失败')
      }
    },
    async loadImages() {
      try {
        const res = await getAttractionImages(this.attraction.id)
        if (res.code === 200) {
          this.images = res.data || []
          if (this.images.length === 0 && this.attraction.coverImage) {
            this.images = [{ imageUrl: this.attraction.coverImage }]
          }
        }
      } catch (error) {
        console.error('加载图片失败:', error)
      }
    },
    async checkFavoriteStatus() {
      // 确保 attraction 已加载
      if (!this.attraction || !this.attraction.id) {
        return
      }
      try {
        const res = await checkFavorite('attraction', this.attraction.id)
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
          await removeFavorite('attraction', this.attraction.id)
          this.isFavorite = false
          this.$message.success('取消收藏成功')
        } else {
          await addFavorite({
            targetType: 'attraction',
            targetId: this.attraction.id
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
      this.bookingDialogVisible = true
    },
    resetBookingForm() {
      this.bookingForm = {
        quantity: 1,
        useDate: '',
        contactName: this.userInfo?.nickname || this.userInfo?.username || '',
        contactPhone: this.userInfo?.phone || '',
        remark: ''
      }
    },
    async handleBooking() {
      if (!this.bookingForm.useDate) {
        this.$message.warning('请选择使用日期')
        return
      }
      if (!this.bookingForm.contactName || !this.bookingForm.contactPhone) {
        this.$message.warning('请填写联系信息')
        return
      }

      try {
        const { createOrder } = await import('@/api/order')
        const orderData = {
          orderType: 'ticket',
          items: [{
            itemType: 'ticket',
            itemId: this.attraction.id,
            quantity: this.bookingForm.quantity,
            useDate: this.bookingForm.useDate
          }],
          contactName: this.bookingForm.contactName,
          contactPhone: this.bookingForm.contactPhone,
          remark: this.bookingForm.remark
        }

        const res = await createOrder(orderData)
        if (res.code === 200) {
          this.$message.success('预订成功')
          this.bookingDialogVisible = false
          this.$router.push(`/order/${res.data.id}`)
        }
      } catch (error) {
        this.$message.error('预订失败：' + (error.response?.data?.message || error.message))
      }
    },
    loadComments() {
      // 评论列表刷新
    },
    goBack() {
      this.$router.go(-1)
    },
    viewOnMap() {
      // 跳转到地图页面，并传递定位参数
      if (this.attraction.longitude && this.attraction.latitude) {
        this.$router.push({
          path: '/map',
          query: {
            type: 'attraction',
            id: this.attraction.id,
            lng: this.attraction.longitude,
            lat: this.attraction.latitude,
            name: this.attraction.name
          }
        })
      } else {
        this.$message.warning('该景点暂无位置信息')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.attraction-detail {
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

    .detail-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .image-placeholder {
      width: 100%;
      height: 400px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background-color: #f5f7fa;
      color: #909399;
      font-size: 16px;
      border-radius: 4px;
    }

    .image-placeholder i {
      font-size: 64px;
      margin-bottom: 12px;
    }

    .video-section {
      margin-bottom: 20px;
    }

    .detail-video {
      width: 100%;
      max-height: 400px;
      border-radius: 4px;
    }

    .info-item {
      margin-bottom: 15px;
      font-size: 16px;

      .label {
        color: #909399;
        margin-right: 10px;
      }

      .price {
        color: #f56c6c;
        font-size: 24px;
        font-weight: bold;
      }
    }

    .actions {
      margin-top: 30px;
    }

    .content {
      line-height: 1.8;
      color: #606266;
    }
  }
}
</style>

