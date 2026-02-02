<template>
  <div class="food-page">
    <div class="container">
      <h1 class="page-title">美食列表</h1>
      
      <!-- 搜索和筛选 -->
      <el-card class="filter-card">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索美食"
              prefix-icon="el-icon-search"
              @input="debouncedSearch"
              @keyup.enter.native="handleSearch"
            />
          </el-col>
          <el-col :span="6">
            <el-select v-model="categoryId" placeholder="选择分类" clearable @change="handleSearch">
              <el-option
                v-for="item in categories"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </el-col>
        </el-row>
      </el-card>

      <!-- 加载状态 -->
      <Skeleton v-if="loading" type="card" :count="4" />

      <!-- 美食列表 -->
      <el-row :gutter="20" v-else-if="foods.length > 0">
        <el-col :span="6" v-for="item in foods" :key="item.id">
          <el-card class="food-card" @click.native="goToDetail(item.id)">
            <LazyImage
              v-if="item.coverImage"
              :src="item.coverImage"
              :alt="item.name"
              width="100%"
              height="200px"
              class="card-image"
            />
            <div v-else class="card-image-placeholder">
              <i class="el-icon-picture-outline"></i>
              <span>暂无图片</span>
            </div>
            <div class="card-content">
              <h3 class="card-title">{{ item.name }}</h3>
              <div v-if="item.restaurant" class="card-restaurant">
                <i class="el-icon-location-outline"></i>
                <span>{{ item.restaurant }}</span>
              </div>
              <div v-if="item.categoryName" class="card-category">
                <el-tag size="small">{{ item.categoryName }}</el-tag>
              </div>
              <p v-if="item.description && item.description.trim() !== item.name.trim()" class="card-description">{{ item.description }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 空状态 -->
      <EmptyState
        v-else
        icon="el-icon-food"
        title="暂无美食"
        description="暂时没有找到相关美食"
      />

      <!-- 分页 -->
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
import { getFoods, getFoodCategories } from '@/api/food'
import Pagination from '@/components/common/Pagination'
import LazyImage from '@/components/common/LazyImage'
import Skeleton from '@/components/common/Skeleton'
import EmptyState from '@/components/common/EmptyState'
import { debounce } from '@/utils/debounce'

export default {
  name: 'FoodIndex',
  components: {
    Pagination,
    LazyImage,
    Skeleton,
    EmptyState
  },
  data() {
    return {
      foods: [],
      categories: [],
      searchKeyword: '',
      categoryId: null,
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
    this.loadCategories()
    this.loadFoods()
  },
  methods: {
    async loadCategories() {
      try {
        const res = await getFoodCategories()
        if (res.code === 200) {
          this.categories = res.data || []
        }
      } catch (error) {
        console.error('加载分类失败:', error)
      }
    },
    async loadFoods() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.categoryId) {
          params.categoryId = this.categoryId
        }
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword
        }

        const res = await getFoods(params)
        if (res.code === 200) {
          this.foods = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        console.error('加载美食失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadFoods()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadFoods()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadFoods()
    },
    goToDetail(id) {
      this.$router.push(`/food/${id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.food-page {
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

  .food-card {
    cursor: pointer;
    margin-bottom: 20px;
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;
    height: 100%;
    min-height: 380px;
    border-radius: 8px;
    overflow: hidden;
    border: 1px solid #ebeef5;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
      border-color: #c0c4cc;
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
      padding: 16px;
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      text-align: left;
      box-sizing: border-box;
      align-items: flex-start; /* 确保所有子元素左对齐 */

      .card-title {
        font-size: 18px;
        margin: 0 0 12px 0 !important;
        padding: 0 !important;
        color: #303133;
        font-weight: 600;
        line-height: 1.4;
        min-height: 50px;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        word-break: break-word;
        text-align: left !important;
        width: 100%;
        box-sizing: border-box;
        align-self: flex-start;
        letter-spacing: 0.2px;
      }

      .card-category {
        margin: 0 0 10px 0 !important;
        padding: 0 !important;
        line-height: 1;
        text-align: left;
        align-self: flex-start;
        width: auto;
        box-sizing: border-box;
        
        ::v-deep .el-tag {
          font-size: 12px;
          padding: 5px 12px !important;
          border-radius: 12px;
          line-height: 1.2;
          height: auto;
          display: inline-block;
          text-align: left;
          margin: 0 !important;
          vertical-align: baseline;
          box-sizing: border-box;
          font-weight: 500;
        }
      }

      .card-restaurant {
        margin: 0 0 10px 0 !important;
        padding: 0 !important;
        line-height: 1.5;
        text-align: left;
        align-self: flex-start;
        width: 100%;
        box-sizing: border-box;
        display: flex;
        align-items: center;
        gap: 6px;
        color: #606266;
        font-size: 13px;

        i {
          font-size: 15px;
          color: #909399;
          flex-shrink: 0;
        }

        span {
          flex: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          text-align: left;
          font-weight: 400;
        }
      }

      .card-description {
        color: #606266;
        font-size: 14px;
        margin: 0 !important;
        padding: 0 !important;
        flex: 1;
        line-height: 1.7;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
        word-break: break-word;
        min-height: 48px;
        text-align: left !important;
        width: 100%;
        box-sizing: border-box;
        align-self: flex-start;
        margin-top: auto !important;
      }
    }
  }
}

/* 响应式设计 - 平板 */
@media (max-width: 992px) {
  .food-page .container {
    width: 100%;
    padding: 0 16px;
  }

  .food-page .filter-card ::v-deep .el-col {
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
  .food-page {
    padding: 15px 0;
  }

  .food-page .container {
    padding: 0 12px;
  }

  .food-page .page-title {
    font-size: 22px;
    margin-bottom: 20px;
  }

  .food-page .filter-card {
    margin-bottom: 15px;
  }

  .food-page .filter-card ::v-deep .el-card__body {
    padding: 15px;
  }

  .food-page .filter-card ::v-deep .el-col {
    flex: 0 0 100%;
    max-width: 100%;
    margin-bottom: 10px;
  }

  ::v-deep .el-row .el-col {
    flex: 0 0 100%;
    max-width: 100%;
  }

  .food-card {
    min-height: auto;
    margin-bottom: 15px;
  }

  .food-card .card-image,
  .food-card .card-image-placeholder {
    height: 180px;
  }

  .food-card .card-content {
    padding: 12px;
  }

  .food-card .card-content .card-title {
    font-size: 16px;
    min-height: 44px;
    line-height: 1.4;
    margin-bottom: 10px !important;
    letter-spacing: 0.1px;
  }

  .food-card .card-content .card-category {
    margin-bottom: 8px !important;
  }

  .food-card .card-content .card-category ::v-deep .el-tag {
    font-size: 11px;
    padding: 4px 10px !important;
    border-radius: 10px;
  }

  .food-card .card-content .card-restaurant {
    margin-bottom: 8px !important;
    font-size: 12px;
    gap: 5px;

    i {
      font-size: 14px;
    }
  }

  .food-card .card-content .card-description {
    font-size: 13px;
    -webkit-line-clamp: 2;
    min-height: 44px;
    line-height: 1.65;
    margin-top: auto !important;
  }
}
</style>

