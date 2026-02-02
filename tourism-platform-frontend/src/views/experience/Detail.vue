<template>
  <div class="experience-detail">
    <div class="container" v-if="experience">
      <!-- 返回按钮 -->
      <el-button icon="el-icon-arrow-left" @click="goBack" style="margin-bottom: 20px;">返回</el-button>
      <!-- 基本信息 -->
      <el-card class="detail-card">
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 视频展示 -->
            <div v-if="experience.videoUrl" class="video-section">
              <video
                :src="experience.videoUrl"
                controls
                class="detail-video"
                preload="metadata"
              >
                您的浏览器不支持视频播放
              </video>
            </div>
            <!-- 图片轮播 -->
            <el-carousel v-else-if="images.length > 0" height="400px" class="image-carousel">
              <el-carousel-item v-for="(img, index) in images" :key="index">
                <img :src="img.imageUrl" class="detail-image" />
              </el-carousel-item>
            </el-carousel>
            <!-- 单张图片 -->
            <LazyImage
              v-else-if="experience.coverImage"
              :src="experience.coverImage"
              :alt="experience.name"
              width="100%"
              height="400px"
            />
          </el-col>
          <el-col :span="12">
            <h1>{{ experience.name }}</h1>
            <div class="info-item">
              <span class="label">体验时长：</span>
              <span>{{ experience.duration }}分钟</span>
            </div>
            <div class="info-item">
              <span class="label">价格：</span>
              <span class="price">¥{{ experience.price }}</span>
            </div>
            <div class="info-item">
              <span class="label">地址：</span>
              <span>{{ experience.address }}</span>
            </div>
            <div class="info-item">
              <span class="label">评分：</span>
              <el-rate
                :value="experience.rating"
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
        <div class="content" v-html="experience.content || experience.description"></div>
      </el-card>

      <!-- 评论 -->
      <el-card class="detail-card">
        <h2>用户评论</h2>
        <CommentList
          :target-type="'experience'"
          :target-id="experience.id"
        />
      </el-card>

      <!-- 预订对话框 -->
      <el-dialog
        title="体验项目预订"
        :visible.sync="bookingDialogVisible"
        width="600px"
        @close="resetBookingForm"
      >
        <el-form :model="bookingForm" label-width="100px">
          <el-form-item label="项目名称">
            <span>{{ experience?.name }}</span>
          </el-form-item>
          <el-form-item label="项目价格">
            <span class="price">¥{{ experience?.price }}/次</span>
          </el-form-item>
          <el-form-item label="体验时长">
            <span>{{ experience?.duration }}分钟</span>
          </el-form-item>
          <el-form-item label="预订数量" required>
            <el-input-number
              v-model="bookingForm.quantity"
              :min="1"
              :max="10"
              label="次"
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
          <el-form-item label="使用时间" required>
            <el-time-picker
              v-model="bookingForm.useTime"
              placeholder="选择使用时间"
              value-format="HH:mm"
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
              ¥{{ (experience?.price || 0) * (bookingForm.quantity || 1) }}
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
import { getExperienceDetail, getExperienceImages } from '@/api/experience'
import { checkFavorite, addFavorite, removeFavorite } from '@/api/favorite'
import { createOrder } from '@/api/order'
import { mapGetters } from 'vuex'
import CommentList from '@/components/common/CommentList'
import LazyImage from '@/components/common/LazyImage'

export default {
  name: 'ExperienceDetail',
  components: {
    CommentList,
    LazyImage
  },
  data() {
    return {
      experience: null,
      images: [],
      isFavorite: false,
      bookingDialogVisible: false,
      bookingForm: {
        quantity: 1,
        useDate: '',
        useTime: '',
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
        const res = await getExperienceDetail(id)
        if (res.code === 200) {
          this.experience = res.data
          this.loadImages()
          // 数据加载完成后再检查收藏状态
          if (this.isLogin && this.experience) {
            this.checkFavoriteStatus()
          }
        }
      } catch (error) {
        this.$message.error('加载详情失败')
      }
    },
    async loadImages() {
      try {
        const res = await getExperienceImages(this.experience.id)
        if (res.code === 200) {
          this.images = res.data || []
          // 如果没有图片但有封面图，使用封面图
          if (this.images.length === 0 && this.experience.coverImage) {
            this.images = [{ imageUrl: this.experience.coverImage }]
          }
        }
      } catch (error) {
        console.error('加载图片失败:', error)
      }
    },
    async checkFavoriteStatus() {
      // 确保 experience 已加载
      if (!this.experience || !this.experience.id) {
        return
      }
      try {
        const res = await checkFavorite('experience', this.experience.id)
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
          await removeFavorite('experience', this.experience.id)
          this.isFavorite = false
          this.$message.success('取消收藏成功')
        } else {
          await addFavorite({
            targetType: 'experience',
            targetId: this.experience.id
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
        useTime: '',
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
        const orderData = {
          orderType: 'experience',
          items: [{
            itemType: 'experience',
            itemId: this.experience.id,
            quantity: this.bookingForm.quantity,
            useDate: this.bookingForm.useDate,
            useTime: this.bookingForm.useTime
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
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.experience-detail {
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

    .video-section {
      margin-bottom: 20px;
    }

    .detail-video {
      width: 100%;
      max-height: 400px;
      border-radius: 4px;
    }

    .image-carousel {
      margin-bottom: 20px;
      border-radius: 4px;
      overflow: hidden;
    }

    .detail-image {
      width: 100%;
      height: 400px;
      object-fit: cover;
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

