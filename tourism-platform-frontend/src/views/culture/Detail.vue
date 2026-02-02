<template>
  <div class="culture-detail">
    <div class="container" v-if="culture">
      <!-- 返回按钮 -->
      <el-button icon="el-icon-arrow-left" @click="goBack" style="margin-bottom: 20px;">返回</el-button>
      <el-card class="detail-card">
        <h1>{{ culture.name }}</h1>
        <!-- 视频展示 -->
        <div v-if="culture.videoUrl" class="video-section">
          <video
            :src="culture.videoUrl"
            controls
            class="detail-video"
            preload="metadata"
          >
            您的浏览器不支持视频播放
          </video>
        </div>
        <!-- 图片轮播 -->
        <el-carousel v-else-if="images.length > 0" height="500px" class="image-carousel">
          <el-carousel-item v-for="(img, index) in images" :key="index">
            <img :src="img.imageUrl" class="detail-image" />
          </el-carousel-item>
        </el-carousel>
        <!-- 单张图片 -->
        <img v-else-if="culture.coverImage" :src="culture.coverImage" class="main-image" />
        <div class="content" v-html="culture.description"></div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getCultureDetail, getCultureImages } from '@/api/culture'

export default {
  name: 'CultureDetail',
  data() {
    return {
      culture: null,
      images: []
    }
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
          // 如果没有图片但有封面图，使用封面图
          if (this.images.length === 0 && this.culture.coverImage) {
            this.images = [{ imageUrl: this.culture.coverImage }]
          }
        }
      } catch (error) {
        console.error('加载图片失败:', error)
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.culture-detail {
  padding: 20px 0;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .detail-card {
    h1 {
      font-size: 28px;
      margin-bottom: 20px;
    }

    .main-image {
      width: 100%;
      max-height: 500px;
      object-fit: cover;
      margin-bottom: 20px;
    }

    .image-carousel {
      margin-bottom: 20px;
      border-radius: 4px;
      overflow: hidden;
    }

    .detail-image {
      width: 100%;
      height: 500px;
      object-fit: cover;
    }

    .content {
      line-height: 1.8;
      color: #606266;
    }

    .video-section {
      margin-bottom: 20px;
    }

    .detail-video {
      width: 100%;
      max-height: 500px;
      border-radius: 4px;
    }
  }
}
</style>

