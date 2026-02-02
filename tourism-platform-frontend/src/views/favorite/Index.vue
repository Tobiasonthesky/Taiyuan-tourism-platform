<template>
  <div class="favorite-page">
    <div class="container">
      <h1 class="page-title">我的收藏</h1>
      
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="景点" name="attraction"></el-tab-pane>
        <el-tab-pane label="美食" name="food"></el-tab-pane>
        <el-tab-pane label="文化" name="culture"></el-tab-pane>
        <el-tab-pane label="攻略" name="strategy"></el-tab-pane>
        <el-tab-pane label="酒店" name="hotel"></el-tab-pane>
        <el-tab-pane label="体验项目" name="experience"></el-tab-pane>
      </el-tabs>

      <el-row :gutter="20" v-if="favorites.length > 0">
        <el-col :span="6" v-for="item in favorites" :key="item.id">
          <el-card class="favorite-card" @click.native="goToDetail(item)">
            <LazyImage :src="item.image || item.coverImage" class="card-image" />
            <div class="card-content">
              <h3>{{ item.name || item.title }}</h3>
              <p class="card-description" v-if="item.description">
                {{ item.description.length > 50 ? item.description.substring(0, 50) + '...' : item.description }}
              </p>
              <el-button
                type="danger"
                size="small"
                @click.stop="removeFavorite(item.targetType, item.targetId)"
              >
                取消收藏
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <EmptyState v-else message="暂无收藏内容" />

      <Pagination
        v-if="total > 0"
        :current-page="page"
        :page-size="size"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
import { getFavorites, removeFavorite } from '@/api/favorite'
import Pagination from '@/components/common/Pagination'
import LazyImage from '@/components/common/LazyImage'
import EmptyState from '@/components/common/EmptyState'

export default {
  name: 'FavoriteIndex',
  components: {
    Pagination,
    LazyImage,
    EmptyState
  },
  data() {
    return {
      favorites: [],
      activeTab: 'attraction',
      page: 1,
      size: 12,
      total: 0
    }
  },
  mounted() {
    this.loadFavorites()
  },
  methods: {
    async loadFavorites() {
      try {
        const res = await getFavorites({
          targetType: this.activeTab,
          page: this.page,
          size: this.size
        })
        if (res.code === 200) {
          this.favorites = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        console.error('加载收藏失败:', error)
      }
    },
    handleTabClick() {
      this.page = 1
      this.loadFavorites()
    },
    handlePageChange(val) {
      this.page = val
      this.loadFavorites()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadFavorites()
    },
    async removeFavorite(targetType, targetId) {
      this.$confirm('确定要取消收藏吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await removeFavorite(targetType, targetId)
          this.$message.success('取消收藏成功')
          this.loadFavorites()
        } catch (error) {
          this.$message.error('操作失败')
        }
      }).catch(() => {})
    },
    goToDetail(item) {
      const routes = {
        attraction: `/attraction/${item.targetId}`,
        food: `/food/${item.targetId}`,
        culture: `/culture/${item.targetId}`,
        strategy: `/strategy/${item.targetId}`,
        hotel: `/hotel/${item.targetId}`,
        experience: `/experience/${item.targetId}`
      }
      const route = routes[item.targetType]
      if (route) {
        this.$router.push(route)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.favorite-page {
  padding: 20px 0;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .favorite-card {
    cursor: pointer;
    transition: transform 0.3s, box-shadow 0.3s;
    margin-bottom: 20px;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
  }

  .card-image {
    width: 100%;
    height: 200px;
    object-fit: cover;
  }

  .card-content {
    padding: 15px 0 0;

    h3 {
      margin-bottom: 10px;
      font-size: 16px;
      color: #303133;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .card-description {
      color: #909399;
      font-size: 14px;
      margin-bottom: 10px;
      line-height: 1.5;
      min-height: 42px;
    }
  }
}
</style>

