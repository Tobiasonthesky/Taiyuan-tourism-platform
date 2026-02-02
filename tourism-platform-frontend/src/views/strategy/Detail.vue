<template>
  <div class="strategy-detail">
    <div class="container" v-if="strategy">
      <!-- 返回按钮 -->
      <el-button icon="el-icon-arrow-left" @click="goBack" style="margin-bottom: 20px;">返回</el-button>
      <el-card class="detail-card">
        <h1>{{ strategy.title }}</h1>
        <div class="meta">
          <span>时长：{{ strategy.duration }}天</span>
          <span>主题：{{ strategy.theme }}</span>
        </div>
        <!-- 图片轮播 -->
        <el-carousel v-if="images.length > 0" height="500px" class="image-carousel">
          <el-carousel-item v-for="(img, index) in images" :key="index">
            <img :src="img.imageUrl" class="detail-image" />
          </el-carousel-item>
        </el-carousel>
        <!-- 单张图片 -->
        <img v-else-if="strategy.coverImage" :src="strategy.coverImage" class="main-image" />
        
        <!-- 攻略描述 -->
        <div v-if="strategy.description" class="description">
          <h2>攻略简介</h2>
          <div class="content-text" v-html="formatContent(strategy.description)"></div>
        </div>
        
        <!-- 详细攻略内容 -->
        <div v-if="strategy.content" class="content-section">
          <h2>详细攻略</h2>
          <div class="content-text" v-html="formatContent(strategy.content)"></div>
        </div>
        
        <!-- 注意事项 -->
        <div v-if="strategy.tips" class="tips-section">
          <h2>注意事项</h2>
          <div class="content-text" v-html="formatContent(strategy.tips)"></div>
        </div>
        
        <!-- 其他信息 -->
        <div v-if="strategy.budget || strategy.bestSeason" class="info-section">
          <h2>其他信息</h2>
          <div class="info-items">
            <div v-if="strategy.budget" class="info-item">
              <strong>预算：</strong>{{ strategy.budget }}元
            </div>
            <div v-if="strategy.bestSeason" class="info-item">
              <strong>最佳季节：</strong>{{ strategy.bestSeason }}
            </div>
          </div>
        </div>
        
        <!-- 路线规划 -->
        <div class="routes" v-if="routes.length > 0">
          <h2>路线规划</h2>
          <el-timeline>
            <el-timeline-item
              v-for="(route, index) in routes"
              :key="route.id"
              :timestamp="`第${index + 1}天`"
            >
              <h4>{{ route.title }}</h4>
              <p>{{ route.description }}</p>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getStrategyDetail, getStrategyRoutes, getStrategyImages } from '@/api/strategy'

export default {
  name: 'StrategyDetail',
  data() {
    return {
      strategy: null,
      routes: [],
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
        const res = await getStrategyDetail(id)
        if (res.code === 200) {
          this.strategy = res.data
          this.loadImages()
          this.loadRoutes()
        }
      } catch (error) {
        this.$message.error('加载详情失败')
      }
    },
    async loadImages() {
      try {
        const res = await getStrategyImages(this.strategy.id)
        if (res.code === 200) {
          this.images = res.data || []
          // 如果没有图片但有封面图，使用封面图
          if (this.images.length === 0 && this.strategy.coverImage) {
            this.images = [{ imageUrl: this.strategy.coverImage }]
          }
        }
      } catch (error) {
        console.error('加载图片失败:', error)
      }
    },
    async loadRoutes() {
      try {
        const res = await getStrategyRoutes(this.strategy.id)
        if (res.code === 200) {
          this.routes = res.data || []
        }
      } catch (error) {
        console.error('加载路线失败:', error)
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    formatContent(content) {
      if (!content) return ''
      // 将换行符转换为<br>，保留HTML格式
      return content.replace(/\n/g, '<br>')
    }
  }
}
</script>

<style lang="scss" scoped>
.strategy-detail {
  padding: 20px 0;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .detail-card {
    h1 {
      font-size: 28px;
      margin-bottom: 15px;
    }

    .meta {
      margin-bottom: 20px;
      color: #909399;

      span {
        margin-right: 20px;
      }
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

    .description,
    .content-section,
    .tips-section,
    .info-section {
      margin-bottom: 30px;

      h2 {
        font-size: 22px;
        color: #303133;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 2px solid #409EFF;
      }
    }

    .content-text {
      line-height: 1.8;
      color: #606266;
      font-size: 15px;
      white-space: pre-wrap;
      word-wrap: break-word;
    }

    .info-section {
      .info-items {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
      }

      .info-item {
        padding: 10px 15px;
        background: #f5f7fa;
        border-radius: 4px;
        color: #606266;

        strong {
          color: #303133;
          margin-right: 5px;
        }
      }
    }

    .routes {
      margin-top: 30px;

      h2 {
        font-size: 20px;
        margin-bottom: 20px;
      }
    }
  }
}
</style>

