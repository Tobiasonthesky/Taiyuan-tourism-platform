<template>
  <div class="culture-page">
    <div class="container">
      <h1 class="page-title">民俗文化</h1>
      
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in cultures" :key="item.id">
          <el-card class="culture-card" @click.native="goToDetail(item.id)">
            <img v-if="item.coverImage" :src="item.coverImage" class="card-image" />
            <div v-else class="card-image-placeholder">
              <i class="el-icon-picture-outline"></i>
              <span>暂无图片</span>
            </div>
            <div class="card-content">
              <h3>{{ item.name }}</h3>
              <p class="text-ellipsis-2">{{ item.description }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <Pagination
        :current-page="page"
        :page-size="size"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { getCultures } from '@/api/culture'
import Pagination from '@/components/common/Pagination'

export default {
  name: 'CultureIndex',
  components: {
    Pagination
  },
  data() {
    return {
      cultures: [],
      page: 1,
      size: 12,
      total: 0
    }
  },
  mounted() {
    this.loadCultures()
  },
  methods: {
    async loadCultures() {
      try {
        const res = await getCultures({
          page: this.page,
          size: this.size
        })
        if (res.code === 200) {
          this.cultures = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        console.error('加载文化失败:', error)
      }
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadCultures()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadCultures()
    },
    goToDetail(id) {
      this.$router.push(`/culture/${id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.culture-page {
  padding: 20px 0;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .page-title {
    font-size: 32px;
    font-weight: 700;
    margin-bottom: 32px;
    color: #303133;
    position: relative;
    padding-bottom: 16px;
    letter-spacing: 0.5px;
  }

  .page-title::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 80px;
    height: 4px;
    background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
    border-radius: 2px;
  }

  // 确保 el-row 和 el-col 正确对齐
  ::v-deep .el-row {
    display: flex;
    flex-wrap: wrap;
  }

  ::v-deep .el-col {
    display: flex;
    flex-direction: column;
  }

  .culture-card {
    cursor: pointer;
    margin-bottom: 20px;
    transition: transform 0.3s;
    display: flex;
    flex-direction: column;
    height: 100%;
    min-height: 380px; // 统一最小高度

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
    }

    // 确保卡片主体使用 flex 布局
    ::v-deep .el-card__body {
      display: flex;
      flex-direction: column;
      flex: 1;
      padding: 0;
    }

    .card-image {
      width: 100%;
      height: 200px;
      object-fit: cover;
      flex-shrink: 0;
    }

    .card-image-placeholder {
      width: 100%;
      height: 200px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background-color: #f5f7fa;
      color: #909399;
      font-size: 14px;
      flex-shrink: 0;

      i {
        font-size: 48px;
        margin-bottom: 8px;
      }
    }

    .card-content {
      padding: 15px;
      flex: 1;
      display: flex;
      flex-direction: column;

      h3 {
        font-size: 18px;
        margin-bottom: 10px;
        color: #303133;
        font-weight: 600;
        line-height: 1.4;
        min-height: 50px; // 确保标题区域高度一致
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }

      p {
        color: #606266;
        line-height: 1.6;
        flex: 1;
        margin: 0;
      }
    }
  }
}

/* 响应式设计 - 平板 */
@media (max-width: 992px) {
  .culture-page .container {
    width: 100%;
    padding: 0 16px;
  }

  ::v-deep .el-col {
    flex: 0 0 50%;
    max-width: 50%;
  }
}

/* 响应式设计 - 手机 */
@media (max-width: 768px) {
  .culture-page {
    padding: 15px 0;
  }

  .culture-page .container {
    padding: 0 12px;
  }

  .culture-page .page-title {
    font-size: 22px;
    margin-bottom: 20px;
  }

  ::v-deep .el-col {
    flex: 0 0 100%;
    max-width: 100%;
  }

  .culture-card {
    min-height: auto;
    margin-bottom: 15px;
  }

  .culture-card .card-image,
  .culture-card .card-image-placeholder {
    height: 180px;
  }

  .culture-card .card-content {
    padding: 12px;
  }

  .culture-card .card-content h3 {
    font-size: 16px;
    min-height: 44px;
  }
}
</style>

