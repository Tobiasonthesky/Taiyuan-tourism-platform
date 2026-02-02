<template>
  <div class="experience-page">
    <div class="container">
      <h1 class="page-title">特色体验项目</h1>
      
      <!-- 搜索 -->
      <el-card class="filter-card">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索体验项目"
              prefix-icon="el-icon-search"
              @input="debouncedSearch"
              @keyup.enter.native="handleSearch"
            />
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </el-col>
        </el-row>
      </el-card>

      <!-- 加载状态 -->
      <Skeleton v-if="loading" type="card" :count="4" />

      <!-- 体验项目列表 -->
      <el-row :gutter="20" v-else-if="experiences.length > 0">
        <el-col :span="6" v-for="item in experiences" :key="item.id">
          <el-card class="experience-card" @click.native="goToDetail(item.id)">
            <LazyImage
              :src="item.coverImage"
              :alt="item.name"
              width="100%"
              height="200px"
            />
            <div class="card-content">
              <h3>{{ item.name }}</h3>
              <p class="text-ellipsis-2">{{ item.description }}</p>
              <div class="card-footer">
                <span class="price">¥{{ item.price }}</span>
                <span class="duration">
                  <i class="el-icon-time"></i> {{ item.duration }}分钟
                </span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 空状态 -->
      <EmptyState
        v-else
        icon="el-icon-star-on"
        title="暂无体验项目"
        description="暂时没有找到相关体验项目"
      />

      <!-- 分页 -->
      <Pagination
        v-if="total > 0"
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
import { getExperiences } from '@/api/experience'
import Pagination from '@/components/common/Pagination'
import LazyImage from '@/components/common/LazyImage'
import Skeleton from '@/components/common/Skeleton'
import EmptyState from '@/components/common/EmptyState'
import { debounce } from '@/utils/debounce'

export default {
  name: 'ExperienceIndex',
  components: {
    Pagination,
    LazyImage,
    Skeleton,
    EmptyState
  },
  data() {
    return {
      experiences: [],
      searchKeyword: '',
      page: 1,
      size: 12,
      total: 0,
      loading: false
    }
  },
  created() {
    // 防抖搜索 - 必须在created中定义，因为模板渲染时需要用到
    this.debouncedSearch = debounce(this.handleSearch, 500)
  },
  mounted() {
    this.loadExperiences()
  },
  methods: {
    async loadExperiences() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword
        }

        const res = await getExperiences(params)
        if (res.code === 200) {
          this.experiences = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        console.error('加载体验项目失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadExperiences()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadExperiences()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadExperiences()
    },
    goToDetail(id) {
      this.$router.push(`/experience/${id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.experience-page {
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

  .filter-card {
    margin-bottom: 20px;
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

  .experience-card {
    cursor: pointer;
    margin-bottom: 20px;
    transition: transform 0.3s;
    display: flex;
    flex-direction: column;
    height: 100%;
    min-height: 420px; // 统一最小高度（包含价格和时长）

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

    // LazyImage 组件样式
    ::v-deep img {
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
        align-items: center;
        margin-top: auto;
        padding-top: 10px;
        border-top: 1px solid #EBEEF5;

        .price {
          color: #f56c6c;
          font-size: 18px;
          font-weight: bold;
        }

        .duration {
          color: #909399;
          font-size: 14px;
        }
      }
    }
  }
}

/* 响应式设计 - 平板 */
@media (max-width: 992px) {
  .experience-page .container {
    width: 100%;
    padding: 0 16px;
  }

  .experience-page .filter-card ::v-deep .el-col {
    flex: 0 0 100%;
    max-width: 100%;
    margin-bottom: 10px;
  }

  ::v-deep .el-row .el-col {
    flex: 0 0 50%;
    max-width: 50%;
  }
}

/* 响应式设计 - 手机 */
@media (max-width: 768px) {
  .experience-page {
    padding: 15px 0;
  }

  .experience-page .container {
    padding: 0 12px;
  }

  .experience-page .page-title {
    font-size: 22px;
    margin-bottom: 20px;
  }

  .experience-page .filter-card {
    margin-bottom: 15px;
  }

  .experience-page .filter-card ::v-deep .el-card__body {
    padding: 15px;
  }

  .experience-page .filter-card ::v-deep .el-col {
    flex: 0 0 100%;
    max-width: 100%;
    margin-bottom: 10px;
  }

  ::v-deep .el-row .el-col {
    flex: 0 0 100%;
    max-width: 100%;
  }

  .experience-card {
    min-height: auto;
    margin-bottom: 15px;
  }

  .experience-card ::v-deep img {
    height: 180px;
  }

  .experience-card .card-content {
    padding: 12px;
  }

  .experience-card .card-content h3 {
    font-size: 16px;
    min-height: 44px;
  }

  .experience-card .card-content .card-footer .price {
    font-size: 16px;
  }
}
</style>

