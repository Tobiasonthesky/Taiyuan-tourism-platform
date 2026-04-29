<template>
  <div class="profile-page">
    <div class="container">
      <h1 class="page-title">{{ $t('user.profile') }}</h1>
      
      <!-- 个人信息编辑 -->
      <el-card class="profile-card">
        <h2 class="card-title">{{ $t('user.basicInfo') }}</h2>
        <el-form
          ref="profileForm"
          :model="profileForm"
          label-width="120px"
          @submit.native.prevent
        >
          <el-form-item :label="$t('user.username')">
            <el-input v-model="profileForm.username" disabled />
          </el-form-item>
          <el-form-item :label="$t('user.email')">
            <el-input v-model="profileForm.email" :placeholder="$t('user.pleaseEnterEmail')" />
          </el-form-item>
          <el-form-item :label="$t('user.phone')">
            <el-input v-model="profileForm.phone" :placeholder="$t('user.pleaseEnterPhone')" />
          </el-form-item>
          <el-form-item :label="$t('user.avatar')">
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
            <el-button type="primary" @click="handleUpdate">{{ $t('user.saveProfile') }}</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 修改密码 -->
      <el-card class="password-card">
        <h2 class="card-title">{{ $t('user.changePassword') }}</h2>
        <el-form
          ref="passwordForm"
          :model="passwordForm"
          label-width="120px"
          :rules="passwordRules"
          @submit.native.prevent
        >
          <el-form-item :label="$t('user.oldPassword')" prop="oldPassword">
            <el-input v-model="passwordForm.oldPassword" type="password" :placeholder="$t('user.pleaseEnterOldPassword')" />
          </el-form-item>
          <el-form-item :label="$t('user.newPassword')" prop="newPassword">
            <el-input v-model="passwordForm.newPassword" type="password" :placeholder="$t('user.pleaseEnterNewPassword')" />
          </el-form-item>
          <el-form-item :label="$t('user.confirmPassword')" prop="confirmPassword">
            <el-input v-model="passwordForm.confirmPassword" type="password" :placeholder="$t('user.pleaseConfirmPassword')" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleChangePassword">{{ $t('user.updatePassword') }}</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getUserInfo, updatePassword } from '@/api/user'
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
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: this.$t('user.oldPasswordRequired'), trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: this.$t('user.newPasswordRequired'), trigger: 'blur' },
          { min: 6, message: this.$t('user.passwordMinLength'), trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: this.$t('user.confirmPasswordRequired'), trigger: 'blur' },
          { validator: this.validateConfirmPassword, trigger: 'blur' }
        ]
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
        this.$message.success(this.$t('user.updateSuccess'))
        // 重新加载用户信息以确保显示最新数据
        await this.loadUserInfo()
      } catch (error) {
        console.error('更新失败:', error)
        this.$message.error(this.$t('user.updateFailed') + (error.message || ''))
      }
    },
    validateConfirmPassword(rule, value, callback) {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error(this.$t('user.passwordNotMatch')))
      } else {
        callback()
      }
    },
    async handleChangePassword() {
      try {
        const form = this.$refs.passwordForm
        await form.validate()
        
        const { oldPassword, newPassword } = this.passwordForm
        const res = await updatePassword({ oldPassword, newPassword })
        
        if (res.code === 200) {
          this.$message.success(this.$t('user.passwordUpdateSuccess'))
          // 清空密码表单
          this.passwordForm = {
            oldPassword: '',
            newPassword: '',
            confirmPassword: ''
          }
          form.resetFields()
        } else {
          this.$message.error(res.message || this.$t('user.passwordUpdateFailed'))
        }
      } catch (error) {
        console.error('修改密码失败:', error)
        this.$message.error(this.$t('user.passwordUpdateFailed') + (error.message || ''))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.profile-page {
  min-height: calc(100vh - 140px);
  padding: 40px 20px;
  background-color: #f5f5f7;

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .page-title {
    text-align: center;
    color: #1d1d1f;
    margin-bottom: 32px;
    font-size: 24px;
    font-weight: 600;
    letter-spacing: -0.02em;
  }

  .el-card {
    background: #ffffff;
    padding: 48px;
    border-radius: 12px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    transition: transform 0.3s ease, box-shadow 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    }
  }

  .el-form {
    width: 100%;
  }
  
  .el-form-item {
    margin-bottom: 20px;
  }
  
  .el-form-item__label {
    font-size: 14px;
    font-weight: 500;
    color: #1d1d1f;
  }

  .el-input {
    width: 100%;
  }
  
  .el-input__inner {
    height: 44px;
    font-size: 16px;
    border: 1px solid #d2d2d7;
    border-radius: 8px;
    transition: all 0.3s ease;

    &:focus {
      border-color: #0071e3;
      box-shadow: 0 0 0 3px rgba(0, 113, 227, 0.1);
    }
  }

  .el-button--primary {
    width: 100%;
    height: 44px;
    font-size: 16px;
    font-weight: 500;
    background-color: #0071e3 !important;
    border-color: #0071e3 !important;
    border-radius: 8px;
    transition: all 0.3s ease;
    
    &:hover {
      background-color: #0077ed !important;
      border-color: #0077ed !important;
    }
    
    &:active {
      background-color: #0066c0 !important;
      border-color: #0066c0 !important;
    }
  }

  .avatar-uploader {
    ::v-deep .el-upload {
      border: 1px dashed #d2d2d7;
      border-radius: 8px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: all 0.3s ease;

      &:hover {
        border-color: #0071e3;
      }
    }

    .avatar-uploader-icon {
      font-size: 28px;
      color: #86868b;
      width: 178px;
      height: 178px;
      line-height: 178px;
      text-align: center;
    }

    .avatar {
      width: 178px;
      height: 178px;
      display: block;
      border-radius: 8px;
    }
  }
}

@media (max-width: 768px) {
  .profile-page {
    padding: 20px;
  }
  
  .container {
    width: 100%;
  }
  
  .el-card {
    padding: 32px;
  }
  
  .page-title {
    font-size: 20px;
  }
}
</style>

