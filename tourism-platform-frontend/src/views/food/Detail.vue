<template>
  <div class="food-detail">
    <div class="container" v-if="food">
      <!-- 返回按钮 -->
      <el-button icon="el-icon-arrow-left" @click="goBack" style="margin-bottom: 20px;">{{ $t('food.back') }}</el-button>
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
                {{ $t('food.browserNotSupportVideo') }}
              </video>
            </div>
            <!-- 图片轮播 -->
            <el-carousel v-else-if="images.length > 0" height="400px">
              <el-carousel-item v-for="(img, index) in images" :key="index">
                <img :src="img.imageUrl" class="detail-image" />
              </el-carousel-item>
            </el-carousel>
            <!-- 封面图片 -->
            <div v-else-if="food.coverImage" class="image-section">
              <img :src="food.coverImage" class="detail-image" />
            </div>
            <!-- 占位图 -->
            <div v-else class="image-placeholder">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-col>
          <el-col :span="12">
            <h1>{{ food.name }}</h1>
            
            <!-- 评分 -->
            <div class="rating-section">
              <span class="rating">{{ food.rating || '0' }}</span>
              <span class="rating-label">{{ $t('food.rating') }}</span>
            </div>
            
            <div class="info-item">
              <span class="label">{{ $t('food.category') }}：</span>
              <span>{{ food.categoryName }}</span>
            </div>
            <div class="info-item" v-if="food.restaurant">
              <span class="label">{{ $t('food.restaurant') }}：</span>
              <span>{{ food.restaurant }}</span>
            </div>
            <div class="info-item" v-if="food.address">
              <span class="label">{{ $t('food.address') }}：</span>
              <span>{{ food.address }}</span>
            </div>
            
            <!-- 标签 -->
            <div class="tags-section" v-if="food.tags">
              <span class="label">{{ $t('food.tags') }}：</span>
              <el-tag
                v-for="tag in food.tags.split(',')"
                :key="tag"
                size="small"
                type="info"
              >
                {{ tag.trim() }}
              </el-tag>
            </div>
            
            <div class="actions">
              <el-button
                :type="isFavorite ? 'danger' : 'default'"
                size="medium"
                @click="handleFavorite"
                :disabled="!isLogin"
              >
                {{ isFavorite ? $t('food.favorited') : $t('food.favorite') }}
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 详细介绍 -->
      <el-card class="detail-card">
        <h2>{{ $t('food.description') }}</h2>
        <div class="content" v-html="food.content || food.description"></div>
      </el-card>

      <!-- 主要食材 -->
      <el-card class="detail-card" v-if="food.ingredients">
        <h2>{{ $t('food.ingredients') }}</h2>
        <div class="content">
          <span v-for="(ingredient, index) in food.ingredients.split(',')" :key="index" class="ingredient-tag">
            {{ ingredient.trim() }}
          </span>
        </div>
      </el-card>

      <!-- 制作方法 -->
      <el-card class="detail-card" v-if="food.cookingMethod">
        <h2>{{ $t('food.recipe') }}</h2>
        <div class="content" v-html="food.cookingMethod"></div>
      </el-card>

      <!-- 评论 -->
      <el-card class="detail-card">
        <h2>{{ $t('food.comments') }}</h2>
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
        this.$message.warning(this.$t('common.pleaseLogin'))
        return
      }

      try {
        if (this.isFavorite) {
          await removeFavorite('food', this.food.id)
          this.isFavorite = false
          this.$message.success(this.$t('favorite.removeSuccess'))
        } else {
          await addFavorite({
            targetType: 'food',
            targetId: this.food.id
          })
          this.isFavorite = true
          this.$message.success(this.$t('favorite.addSuccess'))
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

    .image-section {
      height: 400px;
    }

    .image-placeholder {
      height: 400px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f5f5;
      color: #ccc;
      font-size: 48px;
    }

    .info-item {
      margin-bottom: 15px;
      font-size: 16px;

      .label {
        color: #909399;
        margin-right: 10px;
      }
    }

    .rating-section {
      margin-bottom: 20px;
      display: flex;
      align-items: baseline;

      .rating {
        font-size: 36px;
        font-weight: bold;
        color: #f5a623;
        margin-right: 4px;
      }

      .rating-label {
        font-size: 14px;
        color: #909399;
      }
    }

    .tags-section {
      margin-bottom: 20px;
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

      .ingredient-tag {
        display: inline-block;
        background: #f0f5ff;
        color: #409eff;
        padding: 4px 12px;
        border-radius: 4px;
        margin-right: 8px;
        margin-bottom: 8px;
        font-size: 14px;
      }
    }
  }
}
</style>

