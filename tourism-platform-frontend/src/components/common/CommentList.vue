<template>
  <div class="comment-list">
    <!-- 添加评论 -->
    <div class="add-comment" v-if="isLogin">
      <el-input
        v-model="commentContent"
        type="textarea"
        :rows="4"
        :placeholder="$t('comment.placeholder')"
      />
      <div class="comment-actions">
        <el-rate v-model="rating" />
        <el-button type="primary" @click="handleAddComment">{{ $t('comment.submit') }}</el-button>
      </div>
    </div>

    <el-empty v-else :description="$t('comment.loginToComment')" :image-size="80" />

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
          <span class="time">{{ formatTime(item.createTime) }}</span>
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
        <div class="comment-ops">
          <el-button type="text" size="mini" @click="openReplyInput(item.id, null)">{{ $t('comment.reply') }}</el-button>
          <el-button v-if="canDeleteComment(item)" type="text" size="mini" @click="handleDeleteComment(item.id)">{{ $t('common.delete') }}</el-button>
        </div>

        <div class="reply-editor" v-if="activeReplyEditor.commentId === item.id && activeReplyEditor.replyId === null">
          <el-input
            v-model="replyContent"
            type="textarea"
            :rows="2"
            :placeholder="$t('comment.replyPlaceholder')"
          />
          <div class="reply-editor-actions">
            <el-button size="mini" @click="cancelReply">{{ $t('common.cancel') }}</el-button>
            <el-button type="primary" size="mini" @click="submitReply(item.id, null)">{{ $t('comment.submitReply') }}</el-button>
          </div>
        </div>

        <div class="reply-list" v-if="item.replies && item.replies.length > 0">
          <div class="reply-item" v-for="reply in item.replies" :key="reply.id">
            <div class="reply-main">
              <span class="username">{{ reply.userName || $t('comment.anonymous') }}</span>
              <template v-if="reply.replyToUserName">
                <span class="reply-link">{{ $t('comment.reply') }}</span>
                <span class="username">{{ reply.replyToUserName }}</span>
              </template>
              <span class="reply-content">{{ reply.content }}</span>
            </div>
            <div class="reply-meta">
              <span class="time">{{ formatTime(reply.createTime) }}</span>
              <el-button type="text" size="mini" @click="openReplyInput(item.id, reply.id)">{{ $t('comment.reply') }}</el-button>
              <el-button v-if="canDeleteReply(reply)" type="text" size="mini" @click="handleDeleteReply(item.id, reply.id)">{{ $t('common.delete') }}</el-button>
            </div>

            <div class="reply-editor" v-if="activeReplyEditor.commentId === item.id && activeReplyEditor.replyId === reply.id">
              <el-input
                v-model="replyContent"
                type="textarea"
                :rows="2"
                :placeholder="`${$t('comment.reply')} ${reply.userName || $t('comment.anonymous')}`"
              />
              <div class="reply-editor-actions">
                <el-button size="mini" @click="cancelReply">{{ $t('common.cancel') }}</el-button>
                <el-button type="primary" size="mini" @click="submitReply(item.id, reply.id)">{{ $t('comment.submitReply') }}</el-button>
              </div>
            </div>
          </div>
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
import {
  getComments,
  addComment,
  deleteComment,
  getCommentReplies,
  addCommentReply,
  deleteCommentReply
} from '@/api/comment'
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
      replyContent: '',
      rating: 5,
      page: 1,
      size: 10,
      total: 0,
      activeReplyEditor: {
        commentId: null,
        replyId: null
      }
    }
  },
  computed: {
    ...mapGetters('user', ['isLogin', 'userInfo'])
  },
  watch: {
    targetId() {
      this.resetAndLoad()
    },
    targetType() {
      this.resetAndLoad()
    }
  },
  mounted() {
    this.loadComments()
  },
  methods: {
    resetAndLoad() {
      this.page = 1
      this.total = 0
      this.comments = []
      this.cancelReply()
      this.loadComments()
    },
    async loadComments() {
      try {
        const res = await getComments({
          targetType: this.targetType,
          targetId: this.targetId,
          page: this.page,
          size: this.size
        })
        if (res.code === 200) {
          const commentRecords = (res.data?.records || []).map((item) => ({
            ...item,
            images: this.normalizeImages(item.images),
            replies: []
          }))
          this.comments = commentRecords
          this.total = res.data?.total || 0
          await this.loadRepliesForComments(commentRecords)
        }
      } catch (error) {
        console.error('加载评论失败:', error)
      }
    },
    async loadRepliesForComments(commentList) {
      await Promise.all(
        commentList.map(async (comment) => {
          try {
            const res = await getCommentReplies(comment.id)
            if (res.code === 200) {
              comment.replies = res.data || []
            }
          } catch (error) {
            comment.replies = []
          }
        })
      )
    },
    async handleAddComment() {
      if (!this.commentContent.trim()) {
        this.$message.warning(this.$t('comment.enterContent'))
        return
      }

      try {
        await addComment({
          targetType: this.targetType,
          targetId: this.targetId,
          content: this.commentContent,
          rating: this.rating
        })
        this.$message.success(this.$t('comment.success'))
        this.commentContent = ''
        this.rating = 5
        this.page = 1
        await this.loadComments()
        this.$emit('refresh')
      } catch (error) {
        this.$message.error(error.response?.data?.message || '评论失败')
      }
    },
    handlePageChange(val) {
      this.page = val
      this.loadComments()
    },
    openReplyInput(commentId, replyId) {
      if (!this.isLogin) {
        this.$message.warning(this.$t('comment.loginFirst'))
        return
      }
      this.activeReplyEditor = { commentId, replyId }
      this.replyContent = ''
    },
    cancelReply() {
      this.activeReplyEditor = { commentId: null, replyId: null }
      this.replyContent = ''
    },
    async submitReply(commentId, replyToId) {
      if (!this.replyContent.trim()) {
        this.$message.warning(this.$t('comment.enterReply'))
        return
      }
      try {
        await addCommentReply(commentId, {
          content: this.replyContent,
          replyToId
        })
        this.$message.success(this.$t('comment.replySuccess'))
        this.cancelReply()
        await this.loadComments()
      } catch (error) {
        this.$message.error(error.response?.data?.message || '回复失败')
      }
    },
    canDeleteComment(comment) {
      return this.isLogin && this.userInfo && comment.userId === this.userInfo.id
    },
    canDeleteReply(reply) {
      return this.isLogin && this.userInfo && reply.userId === this.userInfo.id
    },
    async handleDeleteComment(commentId) {
      try {
        await this.$confirm(this.$t('comment.confirmDelete'), this.$t('admin.tip'), {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: 'warning'
        })
        await deleteComment(commentId)
        this.$message.success(this.$t('common.deleteSuccess'))
        await this.loadComments()
        this.$emit('refresh')
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error(error.response?.data?.message || '删除失败')
        }
      }
    },
    async handleDeleteReply(commentId, replyId) {
      try {
        await this.$confirm(this.$t('comment.confirmDeleteReply'), this.$t('admin.tip'), {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: 'warning'
        })
        await deleteCommentReply(commentId, replyId)
        this.$message.success(this.$t('common.deleteSuccess'))
        await this.loadComments()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error(error.response?.data?.message || '删除失败')
        }
      }
    },
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      if (Number.isNaN(date.getTime())) return time
      const now = Date.now()
      const diff = now - date.getTime()
      if (diff < 60 * 1000) return '刚刚'
      if (diff < 60 * 60 * 1000) return `${Math.floor(diff / (60 * 1000))}分钟前`
      if (diff < 24 * 60 * 60 * 1000) return `${Math.floor(diff / (60 * 60 * 1000))}小时前`
      return date.toLocaleString()
    },
    normalizeImages(images) {
      if (!images) {
        return []
      }
      if (Array.isArray(images)) {
        return images
      }
      if (typeof images === 'string') {
        try {
          const parsed = JSON.parse(images)
          return Array.isArray(parsed) ? parsed : []
        } catch (e) {
          return []
        }
      }
      return []
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

    .comment-ops {
      margin-bottom: 8px;
    }

    .reply-list {
      margin-top: 8px;
      padding: 10px 12px;
      background: #f5f7fa;
      border-radius: 4px;
    }

    .reply-item {
      padding: 6px 0;
      border-bottom: 1px solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      .reply-main {
        color: #606266;
        line-height: 1.6;

        .username {
          color: #409eff;
          margin-right: 4px;
        }

        .reply-link {
          color: #909399;
          margin-right: 4px;
        }

        .reply-content {
          margin-left: 4px;
        }
      }

      .reply-meta {
        display: flex;
        align-items: center;
        gap: 8px;

        .time {
          color: #909399;
          font-size: 12px;
        }
      }
    }

    .reply-editor {
      margin-top: 8px;

      .reply-editor-actions {
        margin-top: 8px;
        display: flex;
        justify-content: flex-end;
        gap: 8px;
      }
    }
  }
}
</style>

