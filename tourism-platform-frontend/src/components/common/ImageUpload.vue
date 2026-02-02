<template>
  <div class="image-upload">
    <!-- 图片预览 -->
    <div v-if="imageUrl" class="image-preview">
      <img :src="imageUrl" alt="预览图片" />
      <div class="image-actions">
        <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleRemove">删除</el-button>
      </div>
    </div>
    
    <!-- 上传区域 -->
    <div v-else class="upload-area">
      <el-upload
        class="upload-demo"
        :action="uploadAction"
        :headers="uploadHeaders"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        :show-file-list="false"
        :disabled="uploading"
      >
        <el-button size="small" type="primary" :loading="uploading">
          <i class="el-icon-upload"></i> 上传图片
        </el-button>
      </el-upload>
      
      <div class="upload-divider">或</div>
      
      <el-input
        v-model="urlInput"
        placeholder="输入图片URL"
        size="small"
        style="width: 300px;"
        @blur="handleUrlInput"
      >
        <template slot="append">
          <el-button @click="handleUrlConfirm">确定</el-button>
        </template>
      </el-input>
    </div>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'

export default {
  name: 'ImageUpload',
  props: {
    value: {
      type: String,
      default: ''
    },
    action: {
      type: String,
      default: '/upload/image'
    }
  },
  data() {
    return {
      imageUrl: this.value || '',
      urlInput: '',
      uploading: false,
      uploadAction: (process.env.VUE_APP_BASE_API || '/api') + '/upload/image'
    }
  },
  computed: {
    uploadHeaders() {
      return {
        Authorization: 'Bearer ' + getToken()
      }
    }
  },
  watch: {
    value(newVal) {
      this.imageUrl = newVal || ''
    },
    imageUrl(newVal) {
      this.$emit('input', newVal)
    }
  },
  methods: {
    beforeUpload(file) {
      const isValidType = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/bmp', 'image/webp'].includes(file.type)
      const isLt5M = file.size / 1024 / 1024 < 5

      if (!isValidType) {
        this.$message.error('上传图片只能是 JPG/PNG/GIF/BMP/WEBP 格式!')
        return false
      }
      if (!isLt5M) {
        this.$message.error('上传图片大小不能超过 5MB!')
        return false
      }
      
      this.uploading = true
      return true
    },
    async handleUploadSuccess(response) {
      this.uploading = false
      if (response.code === 200 && response.data && response.data.url) {
        // 处理图片 URL，将相对路径转换为完整 URL（用于显示）
        const url = response.data.url
        let displayUrl = url
        if (url && !url.startsWith('http') && !url.startsWith('//')) {
          const baseURL = process.env.VUE_APP_BASE_API || '/api'
          const safeBaseURL = baseURL || '/api'
          if (url.startsWith('/')) {
            displayUrl = safeBaseURL + url
          } else {
            displayUrl = safeBaseURL + '/' + url
          }
        }
        this.imageUrl = displayUrl
        this.$message.success('上传成功')
        // 传递给父组件时使用原始相对路径
        this.$emit('change', url)
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },
    handleUploadError(err) {
      this.uploading = false
      this.$message.error('上传失败：' + (err.message || '未知错误'))
    },
    handleUrlInput() {
      // URL输入失焦时自动确认（如果URL有效）
      if (this.urlInput && this.isValidUrl(this.urlInput)) {
        this.handleUrlConfirm()
      }
    },
    handleUrlConfirm() {
      if (!this.urlInput) {
        this.$message.warning('请输入图片URL')
        return
      }
      
      if (!this.isValidUrl(this.urlInput)) {
        this.$message.warning('请输入有效的图片URL')
        return
      }
      
      this.imageUrl = this.urlInput
      this.urlInput = ''
      this.$emit('change', this.imageUrl)
    },
    isValidUrl(string) {
      try {
        const url = new URL(string)
        return url.protocol === 'http:' || url.protocol === 'https:'
      } catch (_) {
        // 相对路径也认为是有效的
        return string.startsWith('/') || string.startsWith('./')
      }
    },
    handleRemove() {
      this.imageUrl = ''
      this.urlInput = ''
      this.$emit('change', '')
    }
  }
}
</script>

<style scoped>
.image-upload {
  width: 100%;
}

.image-preview {
  position: relative;
  display: inline-block;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 8px;
  background: #f5f7fa;
}

.image-preview img {
  max-width: 300px;
  max-height: 200px;
  display: block;
  border-radius: 4px;
}

.image-actions {
  margin-top: 8px;
  text-align: center;
}

.upload-area {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.upload-divider {
  color: #909399;
  font-size: 14px;
}
</style>

