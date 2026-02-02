<template>
  <div class="food-detail">
    <div class="container" v-if="food">
      <!-- 返回按钮 -->
      <el-button icon="el-icon-arrow-left" @click="goBack" style="margin-bottom: 20px;">返回</el-button>
      <el-card class="detail-card">
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 视频展示 -->
            <div v-if="food.videoUrl" class="video-section">
              <video
                :src="food.videoUrl"
                controls
                class="detail-video"
                preload="metadata"
              >
                您的浏览器不支持视频播放
              </video>
            </div>
            <!-- 图片轮播 -->
            <el-carousel v-else height="400px">
              <el-carousel-item v-for="(img, index) in images" :key="index">
                <img :src="img.imageUrl" class="detail-image" />
              </el-carousel-item>
            </el-carousel>
          </el-col>
          <el-col :span="12">
            <h1>{{ food.name }}</h1>
            <div class="info-item">
              <span class="label">分类：</span>
              <span>{{ food.categoryName }}</span>
            </div>
            <div class="info-item" v-if="food.restaurant">
              <span class="label">推荐餐厅：</span>
              <span>{{ food.restaurant }}</span>
            </div>
            <div class="info-item" v-if="food.address">
              <span class="label">地址：</span>
              <span>{{ food.address }}</span>
            </div>
            <div class="actions">
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
        <div class="content" v-html="food.description"></div>
      </el-card>

      <!-- 制作方法 -->
      <el-card class="detail-card" v-if="food.recipe">
        <h2>制作方法</h2>
        <div class="content" v-html="food.recipe"></div>
      </el-card>

      <!-- 评论 -->
      <el-card class="detail-card">
        <h2>用户评论</h2>
        <CommentList
          :target-type="'food'"
          :target-id="food.id"
        />
      </el-card>
    </div>
  </div>
</template>

<script>
import { getFoodDetail, getFoodImages } from '@/api/food'
import { checkFavorite, addFavorite, removeFavorite } from '@/api/favorite'
import { mapGetters } from 'vuex'
import CommentList from '@/components/common/CommentList'

export default {
  name: 'FoodDetail',
  components: {
    CommentList
  },
  data() {
    return {
      food: null,
      images: [],
      isFavorite: false
    }
  },
  computed: {
    ...mapGetters('user', ['isLogin'])
  },
  mounted() {
    this.loadDetail()
  },
  methods: {
    async loadDetail() {
      try {
        const id = this.$route.params.id
        const res = await getFoodDetail(id)
        if (res.code === 200) {
          this.food = res.data
          this.loadImages()
          // 数据加载完成后再检查收藏状态
          if (this.isLogin && this.food) {
            this.checkFavoriteStatus()
          }
        }
      } catch (error) {
        this.$message.error('加载详情失败')
      }
    },
    async loadImages() {
      try {
        const res = await getFoodImages(this.food.id)
        if (res.code === 200) {
          this.images = res.data || []
          if (this.images.length === 0 && this.food.image) {
            this.images = [{ imageUrl: this.food.image }]
          }
        }
      } catch (error) {
        console.error('加载图片失败:', error)
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    async checkFavoriteStatus() {
      // 确保 food 已加载
      if (!this.food || !this.food.id) {
        return
      }
      try {
        const res = await checkFavorite('food', this.food.id)
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
          await removeFavorite('food', this.food.id)
          this.isFavorite = false
          this.$message.success('取消收藏成功')
        } else {
          await addFavorite({
            targetType: 'food',
            targetId: this.food.id
          })
          this.isFavorite = true
          this.$message.success('收藏成功')
        }
      } catch (error) {
        this.$message.error('操作失败')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.food-detail {
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

