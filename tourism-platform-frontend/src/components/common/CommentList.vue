<template>
  <div class="comment-list">
    <!-- 添加评论 -->
    <div class="add-comment" v-if="isLogin">
      <el-input
        v-model="commentContent"
        type="textarea"
        :rows="4"
        placeholder="请输入评论内容"
      />
      <div class="comment-actions">
        <el-rate v-model="rating" />
        <el-button type="primary" @click="handleAddComment">发表评论</el-button>
      </div>
    </div>

    <!-- 评论列表 -->
    <div class="comments">
      <div class="comment-item" v-for="item in comments" :key="item.id">
        <div class="comment-header">
          <span class="username">{{ item.userName || '匿名用户' }}</span>
          <el-rate
            :value="item.rating"
            disabled
            show-score
            text-color="#ff9900"
          />
          <span class="time">{{ item.createTime }}</span>
        </div>
        <div class="comment-content">{{ item.content }}</div>
        <div class="comment-images" v-if="item.images && item.images.length > 0">
          <img
            v-for="(img, index) in item.images"
            :key="index"
            :src="img"
            @click="previewImage(img)"
          />
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <Pagination
      v-if="total > 0"
      :current-page="page"
      :page-size="size"
      :total="total"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script>
import { getComments, addComment } from '@/api/comment'
import { mapGetters } from 'vuex'
import Pagination from './Pagination'

export default {
  name: 'CommentList',
  components: {
    Pagination
  },
  props: {
    targetType: {
      type: String,
      required: true
    },
    targetId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      comments: [],
      commentContent: '',
      rating: 5,
      page: 1,
      size: 10,
      total: 0
    }
  },
  computed: {
    ...mapGetters('user', ['isLogin'])
  },
  mounted() {
    this.loadComments()
  },
  methods: {
    async loadComments() {
      try {
        const res = await getComments({
          targetType: this.targetType,
          targetId: this.targetId,
          page: this.page,
          size: this.size
        })
        if (res.code === 200) {
          this.comments = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        console.error('加载评论失败:', error)
      }
    },
    async handleAddComment() {
      if (!this.commentContent.trim()) {
        this.$message.warning('请输入评论内容')
        return
      }

      try {
        await addComment({
          targetType: this.targetType,
          targetId: this.targetId,
          content: this.commentContent,
          rating: this.rating
        })
        this.$message.success('评论成功')
        this.commentContent = ''
        this.rating = 5
        this.page = 1
        this.loadComments()
        this.$emit('refresh')
      } catch (error) {
        this.$message.error('评论失败')
      }
    },
    handlePageChange(val) {
      this.page = val
      this.loadComments()
    },
    previewImage(img) {
      // 图片预览功能
      this.$imagePreview([img])
    }
  }
}
</script>

<style lang="scss" scoped>
.comment-list {
  .add-comment {
    margin-bottom: 30px;

    .comment-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 10px;
    }
  }

  .comment-item {
    padding: 15px 0;
    border-bottom: 1px solid #ebeef5;

    .comment-header {
      display: flex;
      align-items: center;
      margin-bottom: 10px;

      .username {
        font-weight: bold;
        margin-right: 10px;
        color: #303133;
      }

      .time {
        margin-left: auto;
        color: #909399;
        font-size: 12px;
      }
    }

    .comment-content {
      color: #606266;
      line-height: 1.6;
      margin-bottom: 10px;
    }

    .comment-images {
      display: flex;
      gap: 10px;
      flex-wrap: wrap;

      img {
        width: 100px;
        height: 100px;
        object-fit: cover;
        cursor: pointer;
        border-radius: 4px;
      }
    }
  }
}
</style>

