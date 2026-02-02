<template>
  <div class="attraction-page">
    <div class="container">
      <h1 class="page-title">景点列表</h1>
      
      <!-- 搜索和筛选 -->
      <el-card class="filter-card">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索景点"
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

      <!-- 景点列表 -->
      <el-row :gutter="20" v-else-if="attractions.length > 0">
        <el-col :span="6" v-for="item in attractions" :key="item.id">
          <el-card class="attraction-card" @click.native="goToDetail(item.id)">
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
              <h3>{{ item.name }}</h3>
              <div v-if="item.categoryName" class="card-category">
                <el-tag size="small" type="info">{{ item.categoryName }}</el-tag>
              </div>
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

      <!-- 空状态 -->
      <EmptyState
        v-else
        icon="el-icon-picture-outline"
        title="暂无景点"
        description="暂时没有找到相关景点"
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
import { getAttractions, getAttractionCategories } from '@/api/attraction'
import Pagination from '@/components/common/Pagination'
import LazyImage from '@/components/common/LazyImage'
import Skeleton from '@/components/common/Skeleton'
import EmptyState from '@/components/common/EmptyState'
import { debounce } from '@/utils/debounce'

export default {
  name: 'AttractionIndex',
  components: {
    Pagination,
    LazyImage,
    Skeleton,
    EmptyState
  },
  data() {
    return {
      attractions: [],
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
    this.loadAttractions()
  },
  methods: {
    async loadCategories() {
      try {
        const res = await getAttractionCategories()
        if (res.code === 200) {
          this.categories = res.data || []
        }
      } catch (error) {
        console.error('加载分类失败:', error)
      }
    },
    async loadAttractions() {
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

        const res = await getAttractions(params)
        if (res.code === 200) {
          this.attractions = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        console.error('加载景点失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadAttractions()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadAttractions()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadAttractions()
    },
    goToDetail(id) {
      this.$router.push(`/attraction/${id}`)
    }
  }
}
</script>

<style scoped>
.attraction-page {
  padding: 40px 0;
  min-height: calc(100vh - 140px);
  background: linear-gradient(180deg, #ffffff 0%, #f8f9fa 100%);
}

.attraction-page .container {
  width: 1200px;
  max-width: 100%;
  margin: 0 auto;
  padding: 0 24px;
}

.attraction-page .page-title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 32px;
  color: #303133;
  position: relative;
  padding-bottom: 16px;
  letter-spacing: 0.5px;
}

.attraction-page .page-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 80px;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

.attraction-page .filter-card {
  margin-bottom: 40px;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  border: 1px solid #EBEEF5;
  background: #fff;
}

.attraction-page .filter-card >>> .el-card__body {
  padding: 24px;
}

.attraction-page .filter-card >>> .el-input__inner {
  border-radius: 8px;
  border-color: #DCDFE6;
  transition: all 0.3s ease;
}

.attraction-page .filter-card >>> .el-input__inner:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.attraction-page .filter-card >>> .el-select {
  width: 100%;
}

.attraction-page .filter-card >>> .el-button {
  width: 100%;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  font-weight: 500;
  transition: all 0.3s ease;
}

.attraction-page .filter-card >>> .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

/* 确保 el-row 和 el-col 正确对齐 */
.attraction-page ::v-deep .el-row {
  display: flex;
  flex-wrap: wrap;
}

.attraction-page ::v-deep .el-col {
  display: flex;
  flex-direction: column;
}

.attraction-page .attraction-card {
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

.attraction-page .attraction-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  border-color: transparent;
}

.attraction-page .attraction-card >>> .el-card__body {
  padding: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.attraction-page .attraction-card .card-image {
  width: 100%;
  height: 240px;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.attraction-page .attraction-card:hover .card-image {
  transform: scale(1.08);
}

.attraction-page .attraction-card .card-image-placeholder {
  width: 100%;
  height: 240px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 14px;
}

.attraction-page .attraction-card .card-image-placeholder i {
  font-size: 48px;
  margin-bottom: 8px;
}

.attraction-page .attraction-card .card-content {
  padding: 24px;
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.attraction-page .attraction-card .card-content h3 {
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

.attraction-page .attraction-card .card-content .card-category {
  margin-bottom: 8px;
}

.attraction-page .attraction-card .card-content p {
  color: #606266;
  margin-bottom: 16px;
  flex: 1;
  line-height: 1.7;
  font-size: 14px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.attraction-page .attraction-card .card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #EBEEF5;
  margin-top: auto;
}

.attraction-page .attraction-card .card-footer .price {
  color: #F56C6C;
  font-size: 20px;
  font-weight: 700;
}

.attraction-page .attraction-card .card-footer .views {
  color: #909399;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.attraction-page .attraction-card .card-footer .views i {
  font-size: 16px;
}

/* 响应式设计 - 平板 */
@media (max-width: 992px) {
  .attraction-page .container {
    width: 100%;
    padding: 0 16px;
  }

  .attraction-page .filter-card ::v-deep .el-col {
    flex: 0 0 100%;
    max-width: 100%;
    margin-bottom: 10px;
  }

  .attraction-page ::v-deep .el-row .el-col {
    flex: 0 0 50%;
    max-width: 50%;
  }
}

@media (max-width: 768px) {
  .attraction-page {
    padding: 15px 0;
  }
  
  .attraction-page .container {
    padding: 0 12px;
  }
  
  .attraction-page .page-title {
    font-size: 22px;
    margin-bottom: 20px;
  }
  
  .attraction-page .filter-card {
    margin-bottom: 15px;
  }
  
  .attraction-page .filter-card >>> .el-card__body {
    padding: 15px;
  }

  .attraction-page .filter-card ::v-deep .el-col {
    flex: 0 0 100%;
    max-width: 100%;
    margin-bottom: 10px;
  }
  
  .attraction-page ::v-deep .el-row .el-col {
    flex: 0 0 100%;
    max-width: 100%;
  }
  
  .attraction-page .attraction-card {
    min-height: auto;
    margin-bottom: 15px;
  }
  
  .attraction-page .attraction-card .card-content {
    padding: 12px;
  }
  
  .attraction-page .attraction-card .card-content h3 {
    font-size: 16px;
    min-height: 44px;
  }
  
  .attraction-page .attraction-card .card-image,
  .attraction-page .attraction-card .card-image-placeholder {
    height: 180px;
  }
}
</style>

