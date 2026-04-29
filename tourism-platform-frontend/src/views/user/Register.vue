<template>
  <div class="register-container">
    <div class="register-box">
      <h2>{{ $t('user.registerTitle') }}</h2>
      <el-form
        ref="registerForm"
        :model="registerForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item :label="$t('user.username')" prop="username">
          <el-input
            v-model="registerForm.username"
            :placeholder="$t('user.pleaseInputUsername')"
            prefix-icon="el-icon-user"
            autocomplete="off"
            :clearable="true"
          />
        </el-form-item>
        <el-form-item :label="$t('user.password')" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            :placeholder="$t('user.pleaseInputPassword')"
            prefix-icon="el-icon-lock"
            autocomplete="new-password"
            :clearable="true"
          />
        </el-form-item>
        <el-form-item :label="$t('user.confirmPassword')" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            :placeholder="$t('user.pleaseConfirmPassword')"
            prefix-icon="el-icon-lock"
            autocomplete="new-password"
            :clearable="true"
          />
        </el-form-item>
        <el-form-item :label="$t('user.email')" prop="email">
          <el-input
            v-model="registerForm.email"
            :placeholder="$t('user.pleaseInputEmail')"
            prefix-icon="el-icon-message"
            autocomplete="off"
            :clearable="true"
          />
        </el-form-item>
        <el-form-item :label="$t('user.phone')" prop="phone">
          <el-input
            v-model="registerForm.phone"
            :placeholder="$t('user.pleaseInputPhone')"
            prefix-icon="el-icon-phone"
            autocomplete="off"
            :clearable="true"
          />
        </el-form-item>
        <el-form-item label-width="0">
          <el-button
            type="primary"
            :loading="loading"
            @click="handleRegister"
            style="width: 100%"
          >
            {{ $t('user.register') }}
          </el-button>
        </el-form-item>
        <el-form-item label-width="0" style="text-align: center;">
          <div class="login-link">
            {{ $t('user.hasAccount') }}
            <router-link to="/login">{{ $t('user.login') }}</router-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RegisterPage',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }

    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        phone: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  mounted() {
    // 确保表单完全清空，防止任何预填值
    this.resetForm()
  },
  methods: {
    resetForm() {
      // 重置表单数据
      this.registerForm = {
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        phone: ''
      }
      // 重置表单验证状态
      this.$nextTick(() => {
        if (this.$refs.registerForm) {
          this.$refs.registerForm.clearValidate()
          this.$refs.registerForm.resetFields()
        }
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            await this.$store.dispatch('user/register', this.registerForm)
            this.$message.success('注册成功，请登录')
            this.$router.push('/login')
          } catch (error) {
            this.$message.error(error.message || '注册失败')
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.register-container {
  min-height: calc(100vh - 140px);
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f7;
  padding: 40px 20px;

  .register-box {
    width: 500px;
    background: #ffffff;
    padding: 48px;
    border-radius: 12px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    transition: transform 0.3s ease, box-shadow 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    }

    h2 {
      text-align: center;
      margin-bottom: 32px;
      font-size: 24px;
      font-weight: 600;
      color: #1d1d1f;
      letter-spacing: -0.02em;
    }

    .login-link {
      text-align: center;
      margin-top: 24px;
      font-size: 14px;
      color: #86868b;

      a {
        color: #0071e3;
        text-decoration: none;
        font-weight: 500;

        &:hover {
          text-decoration: underline;
        }
      }
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
  
  .el-button--primary.is-loading {
    background-color: #0071e3 !important;
    border-color: #0071e3 !important;
  }
}

@media (max-width: 768px) {
  .register-container {
    padding: 20px;
  }
  
  .register-box {
    width: 100%;
    max-width: 500px;
    padding: 32px;
  }
  
  h2 {
    font-size: 20px !important;
  }
}
</style>

