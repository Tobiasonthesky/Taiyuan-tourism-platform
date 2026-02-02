<template>
  <div class="home">
    <!-- 轮播图 -->
    <el-carousel :interval="4000" type="card" height="400px" class="banner">
      <el-carousel-item v-for="(item, index) in banners" :key="index">
        <img :src="item.image" :alt="item.title" />
      </el-carousel-item>
    </el-carousel>

    <div class="container">
      <!-- 热门景点 / 为你推荐 -->
      <section class="section">
        <div class="section-header">
          <h2 class="section-title">{{ isLogin ? '为你推荐-景点' : '热门景点' }}</h2>
          <el-button 
            v-if="isLogin" 
            type="text" 
            icon="el-icon-refresh" 
            :loading="refreshing"
            @click="refreshRecommendations"
            class="refresh-btn"
          >
            刷新推荐
          </el-button>
        </div>
        <Skeleton v-if="loading" type="card" :count="4" />
        <el-row :gutter="20" v-else-if="hotAttractions.length > 0">
          <el-col :span="6" v-for="item in hotAttractions" :key="item.id">
            <el-card class="card-item" @click.native="goToDetail('attraction', item.id)">
              <LazyImage
                :src="item.coverImage || 'https://via.placeholder.com/300x200?text=暂无图片'"
                :alt="item.name"
                width="100%"
                height="200px"
                class="card-image"
              />
              <div class="card-content">
                <h3>{{ item.name }}</h3>
                <p class="text-ellipsis-2">{{ item.description }}</p>
                <div class="card-footer">
                  <span class="price">¥{{ item.ticketPrice }}</span>
                  <span class="views">
                    <i class="el-icon-view"></i> {{ item.viewCount }}
                  </span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <EmptyState v-else icon="el-icon-picture-outline" :title="isLogin ? '暂无推荐内容' : '暂无热门景点'" />
      </section>

      <!-- 特色美食 / 为你推荐 -->
      <section class="section">
        <div class="section-header">
          <h2 class="section-title">{{ isLogin ? '为你推荐-美食' : '特色美食' }}</h2>
        </div>
        <Skeleton v-if="loading" type="card" :count="4" />
        <el-row :gutter="20" v-else-if="hotFoods.length > 0">
          <el-col :span="6" v-for="item in hotFoods" :key="item.id">
            <el-card class="card-item" @click.native="goToDetail('food', item.id)">
              <LazyImage
                :src="item.coverImage || 'https://via.placeholder.com/300x200?text=暂无图片'"
                :alt="item.name"
                width="100%"
                height="200px"
                class="card-image"
              />
              <div class="card-content">
                <h3>{{ item.name }}</h3>
                <p class="text-ellipsis-2">{{ item.description }}</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <EmptyState v-else icon="el-icon-food" :title="isLogin ? '暂无推荐内容' : '暂无特色美食'" />
      </section>

      <!-- 活动公告 -->
      <section class="section">
        <h2 class="section-title">活动公告</h2>
        <el-timeline>
          <el-timeline-item
            v-for="item in announcements"
            :key="item.id"
            :timestamp="item.createTime"
            placement="top"
          >
            <el-card  > 
              <h4>{{ item.title }}</h4>
              <p>{{ item.content }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </section>
    </div>
  </div>
</template>

<script>
import { getHotRecommendations, getPersonalizedRecommendations } from '@/api/recommendation'
import { getAnnouncements, getBanners } from '@/api/announcement'
import { mapGetters } from 'vuex'
import LazyImage from '@/components/common/LazyImage'
import Skeleton from '@/components/common/Skeleton'
import EmptyState from '@/components/common/EmptyState'

export default {
  name: 'HomePage',
  components: {
    LazyImage,
    Skeleton,
    EmptyState
  },
  data() {
    return {
      banners: [
        { title: '家乡美景', image: 'https://via.placeholder.com/1200x400' },
        { title: '特色美食', image: 'https://via.placeholder.com/1200x400' },
        { title: '民俗文化', image: 'https://via.placeholder.com/1200x400' }
      ],
      hotAttractions: [],
      hotFoods: [],
      announcements: [],
      loading: true,
      refreshing: false
    }
  },
  computed: {
    ...mapGetters('user', ['isLogin'])
  },
  watch: {
    // 监听登录状态变化，重新加载推荐数据
    // 只在当前路由是首页且组件已挂载时才刷新，避免从其他页面返回时强制刷新
    isLogin(newVal, oldVal) {
      // 只有当当前路由是首页，组件已挂载，且登录状态确实发生变化时才刷新
      if (newVal !== undefined && 
          this.$route.path === '/' && 
          this._isMounted && 
          oldVal !== undefined && 
          newVal !== oldVal) {
        this.loadData()
      }
    }
  },
  mounted() {
    this._isMounted = true
    this.loadData()
  },
  beforeDestroy() {
    this._isMounted = false
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        // 加载轮播图
        await this.loadBanners()

        // 根据登录状态选择推荐方式
        if (this.isLogin) {
          // 已登录：使用个性化推荐
          await this.loadPersonalizedRecommendations()
        } else {
          // 未登录：使用热门推荐
          await this.loadHotRecommendations()
        }

        // 加载公告
        const annRes = await getAnnouncements({ page: 1, size: 5 })
        if (annRes.code === 200) {
          this.announcements = annRes.data?.records || []
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        // 如果个性化推荐失败，降级为热门推荐
        if (this.isLogin) {
          await this.loadHotRecommendations()
        }
      } finally {
        this.loading = false
      }
    },
    async loadBanners() {
      try {
        const res = await getBanners()
        if (res.code === 200 && res.data && res.data.length > 0) {
          // 将公告数据转换为轮播图格式
          this.banners = res.data.map(item => ({
            title: item.title,
            image: item.coverImage || 'https://via.placeholder.com/1200x400',
            id: item.id
          }))
        } else {
          // 如果没有轮播图，使用默认占位图
          this.banners = [
            { title: '家乡美景', image: 'https://via.placeholder.com/1200x400' },
            { title: '特色美食', image: 'https://via.placeholder.com/1200x400' },
            { title: '民俗文化', image: 'https://via.placeholder.com/1200x400' }
          ]
        }
      } catch (error) {
        console.error('加载轮播图失败:', error)
        // 加载失败时使用默认占位图
        this.banners = [
          { title: '家乡美景', image: 'https://via.placeholder.com/1200x400' },
          { title: '特色美食', image: 'https://via.placeholder.com/1200x400' },
          { title: '民俗文化', image: 'https://via.placeholder.com/1200x400' }
        ]
      }
    },
    async loadHotRecommendations() {
      // 加载热门景点
      const attRes = await getHotRecommendations('attraction', 4)
      if (attRes.code === 200 && attRes.data) {
        this.hotAttractions = attRes.data.attractions || []
      }

      // 加载热门美食
      const foodRes = await getHotRecommendations('food', 4)
      if (foodRes.code === 200 && foodRes.data) {
        this.hotFoods = foodRes.data.foods || []
      }
    },
    async loadPersonalizedRecommendations(forceRefresh = false) {
      // 加载个性化推荐（景点和美食）
      const recRes = await getPersonalizedRecommendations(4, forceRefresh)
      if (recRes.code === 200 && recRes.data) {
        // 保存旧数据用于对比
        const oldAttractions = [...this.hotAttractions]
        const oldFoods = [...this.hotFoods]
        
        // 先清空数据，确保UI更新
        this.hotAttractions = []
        this.hotFoods = []
        // 使用 $nextTick 确保DOM更新
        await this.$nextTick()
        // 设置新数据
        const newAttractions = recRes.data.attractions || []
        const newFoods = recRes.data.foods || []
        
        this.hotAttractions = newAttractions
        this.hotFoods = newFoods
        
        // 返回是否数据有变化
        const attractionsChanged = JSON.stringify(oldAttractions.map(a => a.id)) !== JSON.stringify(newAttractions.map(a => a.id))
        const foodsChanged = JSON.stringify(oldFoods.map(f => f.id)) !== JSON.stringify(newFoods.map(f => f.id))
        
        return attractionsChanged || foodsChanged
      }
      return false
    },
    async refreshRecommendations() {
      // 刷新个性化推荐
      if (!this.isLogin) {
        return
      }
      
      this.refreshing = true
      try {
        // 强制刷新，添加时间戳避免缓存
        const hasChanged = await this.loadPersonalizedRecommendations(true)
        if (hasChanged) {
          this.$message.success('推荐内容已刷新')
        } else {
          this.$message.info('推荐内容暂无变化，请稍后再试')
        }
      } catch (error) {
        console.error('刷新推荐失败:', error)
        this.$message.error('刷新失败，请稍后重试')
      } finally {
        this.refreshing = false
      }
    },
    goToDetail(type, id) {
      this.$router.push(`/${type}/${id}`)
    }
  }
}
</script>

<style scoped>
.home {
  min-height: calc(100vh - 140px);
  background: linear-gradient(180deg, #ffffff 0%, #f8f9fa 100%);
}

.home .banner {
  margin-bottom: 60px;
  border-radius: 0;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  position: relative;
}

.home .banner >>> .el-carousel__item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.home .banner >>> .el-carousel__item:hover img {
  transform: scale(1.05);
}

.home .banner >>> .el-carousel__indicators {
  bottom: 20px;
}

.home .banner >>> .el-carousel__button {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  transition: all 0.3s ease;
}

.home .banner >>> .el-carousel__button:hover {
  background: rgba(255, 255, 255, 0.8);
}

.home .container {
  width: 1200px;
  max-width: 100%;
  margin: 0 auto;
  padding: 0 24px;
}

.home .section {
  margin-bottom: 60px;
}

.home .section:last-child {
  margin-bottom: 40px;
}

.home .section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.home .section-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 0;
  color: #303133;
  position: relative;
  padding-left: 20px;
  display: flex;
  align-items: center;
  letter-spacing: 0.5px;
}

.home .refresh-btn {
  color: #606266;
  font-size: 14px;
  padding: 8px 16px;
  transition: all 0.3s ease;
}

.home .refresh-btn:hover {
  color: #409EFF;
  background-color: #ecf5ff;
}

.home .refresh-btn i {
  margin-right: 4px;
}

.home .section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 5px;
  height: 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 3px;
}

/* 确保 el-row 和 el-col 正确对齐 */
.home ::v-deep .el-row {
  display: flex;
  flex-wrap: wrap;
}

.home ::v-deep .el-col {
  display: flex;
  flex-direction: column;
}

.home .card-item {
  min-height: 420px; /* 统一最小高度 */
  cursor: pointer;
  margin-bottom: 24px;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.645, 0.045, 0.355, 1);
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #fff;
  border: 1px solid #EBEEF5;
}

.home .card-item:hover {
  transform: translateY(-10px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  border-color: transparent;
}

.home .card-item >>> .el-card__body {
  padding: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.home .card-item .card-image {
  width: 100%;
  height: 240px;
  object-fit: cover;
  transition: transform 0.6s ease;
  flex-shrink: 0;
}

.home .card-item:hover .card-image {
  transform: scale(1.08);
}

.home .card-item .card-content {
  padding: 24px;
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.home .card-item .card-content h3 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #303133;
  line-height: 1.4;
  min-height: 56px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.home .card-item .card-content p {
  color: #606266;
  margin-bottom: 16px;
  flex: 1;
  line-height: 1.7;
  font-size: 14px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin: 0 0 16px 0;
}

.home .card-item .card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #EBEEF5;
  margin-top: auto;
}

.home .card-item .card-footer .price {
  color: #F56C6C;
  font-size: 20px;
  font-weight: 700;
}

.home .card-item .card-footer .views {
  color: #909399;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.home .card-item .card-footer .views i {
  font-size: 16px;
}

.home >>> .el-timeline {
  padding-left: 0;
}

.home >>> .el-timeline-item {
  padding-bottom: 24px;
}

.home >>> .el-timeline-item__timestamp {
  color: #909399;
  font-size: 14px;
  font-weight: 500;
}

.home >>> .el-timeline .el-card {
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  border: 1px solid #EBEEF5;
}

.home >>> .el-timeline .el-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transform: translateX(8px);
  border-color: #DCDFE6;
}

.home >>> .el-timeline .el-card h4 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 10px;
  color: #303133;
}

.home >>> .el-timeline .el-card p {
  color: #606266;
  line-height: 1.7;
  font-size: 14px;
}

/* 响应式设计 - 平板 */
@media (max-width: 992px) {
  .home .container {
    width: 100%;
    padding: 0 16px;
  }

  .home ::v-deep .el-row .el-col {
    flex: 0 0 50%;
    max-width: 50%;
  }
}

@media (max-width: 768px) {
  .home {
    padding: 0;
  }

  .home .banner {
    margin-bottom: 30px;
    height: 250px !important;
  }
  
  .home .container {
    padding: 0 12px;
  }
  
  .home .section {
    margin-bottom: 30px;
  }
  
  .home .section-header {
    margin-bottom: 20px;
  }
  
  .home .section-title {
    font-size: 20px;
    padding-left: 10px;
    margin-bottom: 0;
  }
  
  .home .section-title::before {
    width: 3px;
    height: 20px;
  }
  
  .home .refresh-btn {
    font-size: 12px;
    padding: 6px 12px;
  }
  
  .home .refresh-btn span {
    display: none;
  }
  
  .home .refresh-btn i {
    margin-right: 0;
  }
  
  .home ::v-deep .el-row .el-col {
    flex: 0 0 100%;
    max-width: 100%;
  }

  .home .card-item {
    min-height: auto;
    margin-bottom: 15px;
  }
  
  .home .card-item .card-content {
    padding: 12px;
  }
  
  .home .card-item .card-content h3 {
    font-size: 16px;
    min-height: 44px;
  }
  
  .home .card-item .card-image {
    height: 180px;
  }

  .home .card-item .card-footer .price {
    font-size: 18px;
  }
}
</style>

