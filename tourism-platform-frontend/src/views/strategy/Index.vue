<template>
  <div class="strategy-page">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">旅游攻略</h1>
        <el-button 
          type="primary" 
          icon="el-icon-magic-stick" 
          @click="goToGenerate"
          style="margin-left: 20px;"
        >
          AI生成攻略
        </el-button>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in strategies" :key="item.id">
          <el-card class="strategy-card" @click.native="goToDetail(item.id)">
            <img :src="item.coverImage" class="card-image" />
            <div class="card-content">
              <h3>{{ item.title }}</h3>
              <p class="text-ellipsis-2">{{ item.description }}</p>
              <div class="card-footer">
                <span>时长：{{ item.duration }}天</span>
                <span>主题：{{ item.theme }}</span>
              </div>
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
import { getStrategies } from '@/api/strategy'
import Pagination from '@/components/common/Pagination'

export default {
  name: 'StrategyIndex',
  components: {
    Pagination
  },
  data() {
    return {
      strategies: [],
      page: 1,
      size: 12,
      total: 0
    }
  },
  mounted() {
    this.loadStrategies()
  },
  methods: {
    async loadStrategies() {
      try {
        const res = await getStrategies({
          page: this.page,
          size: this.size
        })
        if (res.code === 200) {
          this.strategies = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        console.error('加载攻略失败:', error)
      }
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadStrategies()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadStrategies()
    },
    goToDetail(id) {
      this.$router.push(`/strategy/${id}`)
    },
    goToGenerate() {
      this.$router.push('/strategy/generate')
    }
  }
}
</script>

<style lang="scss" scoped>
.strategy-page {
  padding: 20px 0;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .page-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
  }

  .page-title {
    font-size: 28px;
    font-weight: 700;
    color: #303133;
    margin: 0;
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

  .strategy-card {
    cursor: pointer;
    margin-bottom: 20px;
    transition: transform 0.3s;
    display: flex;
    flex-direction: column;
    height: 100%;
    min-height: 400px; // 统一最小高度

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
        margin-bottom: 10px;
        flex: 1;
        line-height: 1.6;
      }

      .card-footer {
        display: flex;
        justify-content: space-between;
        color: #909399;
        font-size: 14px;
        margin-top: auto;
        padding-top: 10px;
        border-top: 1px solid #EBEEF5;
      }
    }
  }
}

/* 响应式设计 - 平板 */
@media (max-width: 992px) {
  .strategy-page .container {
    width: 100%;
    padding: 0 16px;
  }

  ::v-deep .el-row .el-col {
    flex: 0 0 50%;
    max-width: 50%;
  }
}

/* 响应式设计 - 手机 */
@media (max-width: 768px) {
  .strategy-page {
    padding: 15px 0;
  }

  .strategy-page .container {
    padding: 0 12px;
  }

  .strategy-page .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .strategy-page .page-title {
    font-size: 22px;
    margin-bottom: 0;
  }

  .strategy-page .page-header .el-button {
    margin-left: 0;
    width: 100%;
  }

  ::v-deep .el-row .el-col {
    flex: 0 0 100%;
    max-width: 100%;
  }

  .strategy-card {
    min-height: auto;
    margin-bottom: 15px;
  }

  .strategy-card .card-image {
    height: 180px;
  }

  .strategy-card .card-content {
    padding: 12px;
  }

  .strategy-card .card-content h3 {
    font-size: 16px;
    min-height: 44px;
  }

  .strategy-card .card-content .card-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
    font-size: 12px;
  }
}
</style>

