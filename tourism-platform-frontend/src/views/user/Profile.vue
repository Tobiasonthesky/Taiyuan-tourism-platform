<template>
  <div class="profile-page">
    <div class="container">
      <h1 class="page-title">个人中心</h1>
      
      <el-card>
        <el-form
          ref="profileForm"
          :model="profileForm"
          label-width="100px"
          @submit.native.prevent
        >
          <el-form-item label="用户名">
            <el-input v-model="profileForm.username" disabled />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="profileForm.email" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="profileForm.phone" />
          </el-form-item>
          <el-form-item label="头像">
            <el-upload
              class="avatar-uploader"
              action="#"
              :show-file-list="false"
              :before-upload="beforeUpload"
            >
              <img v-if="profileForm.avatar" :src="profileForm.avatar" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleUpdate">保存</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getUserInfo } from '@/api/user'
import { uploadFile } from '@/api/upload'

export default {
  name: 'ProfilePage',
  data() {
    return {
      profileForm: {
        username: '',
        email: '',
        phone: '',
        avatar: ''
      }
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  methods: {
    // 处理图片 URL，将相对路径转换为完整 URL
    processImageUrl(url) {
      if (!url) return ''
      // 如果已经是完整 URL，直接返回
      if (url.startsWith('http') || url.startsWith('//')) {
        return url
      }
      // 获取 baseURL，优先使用环境变量，否则使用默认值
      const baseURL = process.env.VUE_APP_BASE_API || '/api'
      const safeBaseURL = baseURL || '/api'
      // 如果是相对路径，添加 base API
      if (url.startsWith('/')) {
        return safeBaseURL + url
      }
      return safeBaseURL + '/' + url
    },
    async loadUserInfo() {
      try {
        const res = await getUserInfo()
        if (res.code === 200) {
          const userData = { ...res.data }
          // 处理头像 URL
          if (userData.avatar) {
            userData.avatar = this.processImageUrl(userData.avatar)
          }
          this.profileForm = userData
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
      }
    },
    async beforeUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
        return false
      }

      try {
        const res = await uploadFile(file)
        if (res.code === 200 && res.data && res.data.url) {
          // 后端返回的是相对路径，如 /uploads/images/xxx.jpg
          const originalUrl = res.data.url
          // 显示时使用完整 URL
          const displayUrl = this.processImageUrl(originalUrl)
          this.profileForm.avatar = displayUrl
          this.$message.success('上传成功')
          // 保存到服务器时使用原始相对路径
          await this.saveAvatar(originalUrl)
        } else {
          this.$message.error(res.message || '上传失败')
        }
      } catch (error) {
        console.error('上传失败:', error)
        this.$message.error('上传失败：' + (error.message || '未知错误'))
      }

      return false
    },
    async saveAvatar(avatarUrl) {
      try {
        const updateData = {
          avatar: avatarUrl
        }
        await this.$store.dispatch('user/updateUserInfo', updateData)
        // 重新加载用户信息以确保显示最新头像
        await this.loadUserInfo()
      } catch (error) {
        console.error('保存头像失败:', error)
        // 不显示错误，因为上传已经成功，只是保存到用户信息失败
      }
    },
    async handleUpdate() {
      try {
        // 准备更新数据，头像使用相对路径（如果当前是完整URL，需要提取相对路径）
        const updateData = { ...this.profileForm }
        if (updateData.avatar) {
          // 如果头像 URL 包含 baseURL，提取相对路径
          const baseURL = process.env.VUE_APP_BASE_API || '/api'
          const safeBaseURL = baseURL || '/api'
          if (updateData.avatar.startsWith(safeBaseURL)) {
            updateData.avatar = updateData.avatar.substring(safeBaseURL.length)
          }
        }
        await this.$store.dispatch('user/updateUserInfo', updateData)
        this.$message.success('更新成功')
        // 重新加载用户信息以确保显示最新数据
        await this.loadUserInfo()
      } catch (error) {
        console.error('更新失败:', error)
        this.$message.error('更新失败：' + (error.message || '未知错误'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.profile-page {
  padding: 20px 0;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .avatar-uploader {
    ::v-deep .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;

      &:hover {
        border-color: #409eff;
      }
    }

    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      line-height: 178px;
      text-align: center;
    }

    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }
  }
}
</style>

