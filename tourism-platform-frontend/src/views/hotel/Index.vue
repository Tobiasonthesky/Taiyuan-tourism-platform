<template>
  <div class="hotel-page">
    <div class="container">
      <h1 class="page-title">酒店预订</h1>
      
      <!-- 搜索和筛选 -->
      <el-card class="filter-card">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索酒店"
              prefix-icon="el-icon-search"
              @input="debouncedSearch"
              @keyup.enter.native="handleSearch"
            />
          </el-col>
          <el-col :span="6">
            <el-select v-model="starLevel" placeholder="选择星级" clearable @change="handleSearch">
              <el-option label="五星级" :value="5" />
              <el-option label="四星级" :value="4" />
              <el-option label="三星级" :value="3" />
              <el-option label="二星级" :value="2" />
              <el-option label="一星级" :value="1" />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </el-col>
        </el-row>
      </el-card>

      <!-- 加载状态 -->
      <Skeleton v-if="loading" type="card" :count="4" />

      <!-- 酒店列表 -->
      <el-row :gutter="20" v-else-if="hotels.length > 0">
        <el-col :span="12" v-for="item in hotels" :key="item.id">
          <el-card class="hotel-card" @click.native="goToDetail(item.id)">
            <el-row :gutter="20">
              <el-col :span="8">
                <LazyImage
                  :src="item.coverImage || ''"
                  :alt="item.name"
                  width="100%"
                  height="150px"
                />
              </el-col>
              <el-col :span="16">
                <div class="hotel-info">
                  <h3>{{ item.name }}</h3>
                  <div class="hotel-meta">
                    <el-rate
                      :value="item.starLevel"
                      disabled
                      show-score
                      text-color="#ff9900"
                    />
                    <span class="rating">评分：{{ item.rating }}</span>
                  </div>
                  <p class="address">
                    <i class="el-icon-location"></i> {{ item.address }}
                  </p>
                  <div class="hotel-footer">
                    <span class="price">¥{{ item.minPrice }}/晚起</span>
                    <span class="views">
                      <i class="el-icon-view"></i> {{ item.viewCount }}
                    </span>
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>

      <!-- 空状态 -->
      <EmptyState
        v-else
        icon="el-icon-office-building"
        title="暂无酒店"
        description="暂时没有找到相关酒店"
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
import { getHotels } from '@/api/hotel'
import Pagination from '@/components/common/Pagination'
import LazyImage from '@/components/common/LazyImage'
import Skeleton from '@/components/common/Skeleton'
import EmptyState from '@/components/common/EmptyState'
import { debounce } from '@/utils/debounce'

export default {
  name: 'HotelIndex',
  components: {
    Pagination,
    LazyImage,
    Skeleton,
    EmptyState
  },
  data() {
    return {
      hotels: [],
      searchKeyword: '',
      starLevel: null,
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
    this.loadHotels()
  },
  methods: {
    async loadHotels() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.starLevel) {
          params.starLevel = this.starLevel
        }
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword
        }

        const res = await getHotels(params)
        if (res.code === 200) {
          this.hotels = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        console.error('加载酒店失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadHotels()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadHotels()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadHotels()
    },
    goToDetail(id) {
      if (id) {
        this.$router.push(`/hotel/${id}`).catch(err => {
          // 忽略导航重复错误
          if (err.name !== 'NavigationDuplicated') {
            console.error('导航失败:', err)
          }
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.hotel-page {
  padding: 20px 0;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .filter-card {
    margin-bottom: 20px;
  }

  .hotel-card {
    cursor: pointer;
    margin-bottom: 20px;
    transition: transform 0.3s;

    &:hover {
      transform: translateY(-5px);
    }

    .hotel-info {
      h3 {
        font-size: 20px;
        margin-bottom: 10px;
        color: #303133;
      }

      .hotel-meta {
        display: flex;
        align-items: center;
        margin-bottom: 10px;
        gap: 10px;

        .rating {
          color: #909399;
          font-size: 14px;
        }
      }

      .address {
        color: #606266;
        margin-bottom: 15px;
        font-size: 14px;
      }

      .hotel-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .price {
          color: #f56c6c;
          font-size: 20px;
          font-weight: bold;
        }

        .views {
          color: #909399;
          font-size: 14px;
        }
      }
    }
  }
}
</style>

