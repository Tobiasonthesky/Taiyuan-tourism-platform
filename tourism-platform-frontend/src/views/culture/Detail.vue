<template>
  <div class="culture-detail">
    <div class="container" v-if="culture">
      <!-- 返回按钮 -->
      <el-button icon="el-icon-arrow-left" @click="goBack" style="margin-bottom: 20px;">{{ $t('common.back') }}</el-button>
      <!-- 基本信息 -->
      <el-card class="detail-card">
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 视频展示 -->
            <div v-if="culture.videoUrl" class="video-section">
              <video
                :src="culture.videoUrl"
                controls
                class="detail-video"
                preload="metadata"
              >
                {{ $t('common.browserNotSupportVideo') }}
              </video>
            </div>
            <!-- 图片轮播 -->
            <el-carousel v-else-if="images.length > 0" height="400px">
              <el-carousel-item v-for="(img, index) in images" :key="index">
                <img :src="img.imageUrl" class="detail-image" />
              </el-carousel-item>
            </el-carousel>
            <!-- 单张封面图 -->
            <img v-else-if="culture.coverImage" :src="culture.coverImage" class="detail-image" />
            <!-- 无图片占位符 -->
            <div v-else class="image-placeholder">
              <i class="el-icon-picture-outline"></i>
              <span>{{ $t('culture.noImage') }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <h1>{{ culture.name }}</h1>
            <div class="info-item" v-if="culture.categoryName">
              <span class="label">{{ $t('culture.category') }}：</span>
              <span>{{ culture.categoryName }}</span>
            </div>
            <div class="info-item" v-if="culture.rating">
              <span class="label">{{ $t('culture.rating') }}：</span>
              <el-rate
                :value="culture.rating"
                disabled
                show-score
                text-color="#ff9900"
              />
            </div>
            <div class="info-item" v-if="culture.activityTime">
              <span class="label">{{ $t('culture.activityTime') }}：</span>
              <span>{{ culture.activityTime }}</span>
            </div>
            <div class="info-item" v-if="culture.activityLocation">
              <span class="label">{{ $t('culture.activityLocation') }}：</span>
              <span>{{ culture.activityLocation }}</span>
            </div>
            <div class="info-item">
              <span class="label">{{ $t('culture.viewCount') }}：</span>
              <span>{{ culture.viewCount }}</span>
            </div>
            
            <!-- 标签 -->
            <div class="tags-section" v-if="culture.tags">
              <span class="label">{{ $t('culture.tags') }}：</span>
              <el-tag
                v-for="tag in culture.tags.split(',')"
                :key="tag"
                size="small"
                type="info"
              >
                {{ tag.trim() }}
              </el-tag>
            </div>
            
            <div class="actions">
              <el-button
                type="success"
                size="medium"
                icon="el-icon-location"
                @click="viewOnMap"
                v-if="culture.longitude && culture.latitude"
              >
                {{ $t('culture.viewOnMap') }}
              </el-button>
              <el-button
                :type="isFavorite ? 'danger' : 'default'"
                size="medium"
                @click="handleFavorite"
                :disabled="!isLogin"
              >
                {{ isFavorite ? $t('culture.favorited') : $t('culture.favorite') }}
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 历史背景 -->
      <el-card class="detail-card" v-if="culture.history">
        <h2>{{ $t('culture.history') }}</h2>
        <div class="content" v-html="culture.history"></div>
      </el-card>

      <!-- 详细介绍 -->
      <el-card class="detail-card" v-if="culture.description || culture.content">
        <h2>{{ $t('culture.description') }}</h2>
        <div class="content" v-html="culture.content || culture.description"></div>
      </el-card>

      <!-- 评论 -->
      <el-card class="detail-card">
        <h2>{{ $t('culture.comments') }}</h2>
        <CommentList
          :target-type="'culture'"
          :target-id="culture.id"
        />
      </el-card>
    </div>
  </div>
</template>

<script>
import { getCultureDetail, getCultureImages } from '@/api/culture'
import { checkFavorite, addFavorite, removeFavorite } from '@/api/favorite'
import { mapGetters } from 'vuex'
import CommentList from '@/components/common/CommentList'

export default {
  name: 'CultureDetail',
  components: {
    CommentList
  },
  data() {
    return {
      culture: null,
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
        const res = await getCultureDetail(id)
        if (res.code === 200) {
          this.culture = res.data
          this.loadImages()
          // 数据加载完成后再检查收藏状态
          if (this.isLogin && this.culture) {
            this.checkFavoriteStatus()
          }
        }
      } catch (error) {
        this.$message.error('加载详情失败')
      }
    },
    async loadImages() {
      try {
        const res = await getCultureImages(this.culture.id)
        if (res.code === 200) {
          this.images = res.data || []
          if (this.images.length === 0 && this.culture.coverImage) {
            this.images = [{ imageUrl: this.culture.coverImage }]
          }
        }
      } catch (error) {
        console.error('加载图片失败:', error)
      }
    },
    async checkFavoriteStatus() {
      if (!this.culture || !this.culture.id) {
        return
      }
      try {
        const res = await checkFavorite('culture', this.culture.id)
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
          await removeFavorite('culture', this.culture.id)
          this.isFavorite = false
          this.$message.success(this.$t('favorite.removeSuccess'))
        } else {
          await addFavorite({
            targetType: 'culture',
            targetId: this.culture.id
          })
          this.isFavorite = true
          this.$message.success(this.$t('favorite.addSuccess'))
        }
      } catch (error) {
        this.$message.error('操作失败')
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    viewOnMap() {
      if (this.culture.longitude && this.culture.latitude) {
        this.$router.push({
          path: '/map',
          query: {
            type: 'culture',
            id: this.culture.id,
            lng: this.culture.longitude,
            lat: this.culture.latitude,
            name: this.culture.name
          }
        })
      } else {
        this.$message.warning('该文化活动暂无位置信息')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.culture-detail {
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
    }
  }
}
</style>
