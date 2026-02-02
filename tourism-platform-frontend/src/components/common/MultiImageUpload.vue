<template>
  <div class="multi-image-upload">
    <!-- 已上传的图片列表 -->
    <div v-if="imageList.length > 0" class="image-list">
      <div v-for="(img, index) in imageList" :key="index" class="image-item">
        <img :src="img.imageUrl" alt="图片" />
        <div class="image-overlay">
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleRemove(index)"
          ></el-button>
        </div>
        <div class="image-sort">
          <el-button
            size="mini"
            icon="el-icon-arrow-up"
            :disabled="index === 0"
            @click="handleMoveUp(index)"
          ></el-button>
          <el-button
            size="mini"
            icon="el-icon-arrow-down"
            :disabled="index === imageList.length - 1"
            @click="handleMoveDown(index)"
          ></el-button>
        </div>
      </div>
    </div>

    <!-- 上传区域 -->
    <div class="upload-area">
      <el-upload
        class="upload-demo"
        :action="uploadAction"
        :headers="uploadHeaders"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        :show-file-list="false"
        :disabled="uploading"
        :multiple="true"
      >
        <el-button size="small" type="primary" :loading="uploading">
          <i class="el-icon-upload"></i> 上传图片
        </el-button>
      </el-upload>

      <div class="upload-divider">或</div>

      <div style="width: 100%;">
        <el-input
          v-model="urlInput"
          placeholder="输入图片URL（每行一个）"
          type="textarea"
          :rows="3"
          style="width: 100%; margin-bottom: 8px;"
          @blur="handleUrlInput"
        />
        <el-button size="small" @click="handleUrlConfirm">确定添加</el-button>
      </div>
    </div>

    <div class="upload-tip">
      <i class="el-icon-info"></i>
      支持上传多张图片，可通过拖拽调整顺序
    </div>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'

export default {
  name: 'MultiImageUpload',
  props: {
    value: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      imageList: this.value || [],
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
      this.imageList = newVal || []
    },
    imageList(newVal) {
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
        this.imageList.push({
          imageUrl: displayUrl, // 显示时使用完整 URL
          imageType: this.imageList.length === 0 ? 'cover' : 'detail',
          sortOrder: this.imageList.length + 1
        })
        this.$message.success('上传成功')
        // 传递给父组件时使用原始相对路径
        const updatedList = this.imageList.map((img, index) => {
          if (index === this.imageList.length - 1) {
            // 最新添加的图片使用原始相对路径
            return { ...img, imageUrl: url }
          }
          return img
        })
        this.$emit('change', updatedList)
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
      if (this.urlInput && this.urlInput.trim()) {
        this.handleUrlConfirm()
      }
    },
    handleUrlConfirm() {
      if (!this.urlInput || !this.urlInput.trim()) {
        this.$message.warning('请输入图片URL')
        return
      }
      
      const urls = this.urlInput.split('\n').filter(url => url.trim())
      if (urls.length === 0) {
        this.$message.warning('请输入有效的图片URL')
        return
      }

      urls.forEach((url) => {
        if (this.isValidUrl(url.trim())) {
          this.imageList.push({
            imageUrl: url.trim(),
            imageType: this.imageList.length === 0 ? 'cover' : 'detail',
            sortOrder: this.imageList.length + 1
          })
        }
      })

      this.urlInput = ''
      this.$message.success(`成功添加 ${urls.length} 张图片`)
      this.$emit('change', this.imageList)
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
    handleRemove(index) {
      this.imageList.splice(index, 1)
      // 更新排序
      this.imageList.forEach((img, idx) => {
        img.sortOrder = idx + 1
      })
      this.$emit('change', this.imageList)
    },
    handleMoveUp(index) {
      if (index > 0) {
        const temp = this.imageList[index]
        this.imageList[index] = this.imageList[index - 1]
        this.imageList[index - 1] = temp
        // 更新排序
        this.imageList.forEach((img, idx) => {
          img.sortOrder = idx + 1
        })
        this.$emit('change', this.imageList)
      }
    },
    handleMoveDown(index) {
      if (index < this.imageList.length - 1) {
        const temp = this.imageList[index]
        this.imageList[index] = this.imageList[index + 1]
        this.imageList[index + 1] = temp
        // 更新排序
        this.imageList.forEach((img, idx) => {
          img.sortOrder = idx + 1
        })
        this.$emit('change', this.imageList)
      }
    }
  }
}
</script>

<style scoped>
.multi-image-upload {
  width: 100%;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
}

.image-item {
  position: relative;
  width: 150px;
  height: 150px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  background: #f5f7fa;
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 0;
  right: 0;
  padding: 4px;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 0 0 0 4px;
}

.image-sort {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  gap: 4px;
  padding: 4px;
  background: rgba(0, 0, 0, 0.5);
}

.upload-area {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 8px;
}

.upload-divider {
  color: #909399;
  font-size: 14px;
  text-align: center;
}

.upload-tip {
  color: #909399;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>

