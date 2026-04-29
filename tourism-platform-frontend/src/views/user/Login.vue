<template>
  <div class="login-container">
    <div class="login-box">
      <h2>{{ $t('user.loginTitle') }}</h2>
      <!-- 隐藏的假输入框，用于欺骗浏览器自动填充 -->
      <input type="text" style="position: absolute; left: -9999px; opacity: 0;" autocomplete="off" tabindex="-1" />
      <input type="password" style="position: absolute; left: -9999px; opacity: 0;" autocomplete="off" tabindex="-1" />
      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="rules"
        label-width="80px"
        autocomplete="off"
      >
        <el-form-item :label="$t('user.username')" prop="username">
          <el-input
            ref="usernameInput"
            v-model="loginForm.username"
            :placeholder="$t('user.pleaseInputUsername')"
            prefix-icon="el-icon-user"
            autocomplete="off"
            :name="'username-' + Date.now()"
            :readonly="usernameReadonly"
            @focus="handleUsernameFocus"
            @input="handleUsernameInput"
          />
        </el-form-item>
        <el-form-item :label="$t('user.password')" prop="password">
          <el-input
            ref="passwordInput"
            v-model="loginForm.password"
            type="password"
            :placeholder="$t('user.pleaseInputPassword')"
            prefix-icon="el-icon-lock"
            autocomplete="new-password"
            :name="'password-' + Date.now()"
            :readonly="passwordReadonly"
            @focus="handlePasswordFocus"
            @input="handlePasswordInput"
            @keyup.enter.native="handleLogin"
          />
        </el-form-item>
        <el-form-item label-width="0">
          <el-button
            type="primary"
            :loading="loading"
            @click="handleLogin"
            style="width: 100%"
          >
            {{ $t('user.login') }}
          </el-button>
        </el-form-item>
        <el-form-item label-width="0" style="text-align: center;">
          <div class="register-link">
            {{ $t('user.noAccount') }}
            <router-link to="/register">{{ $t('user.register') }}</router-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginPage',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ]
      },
      loading: false,
      usernameReadonly: true,
      passwordReadonly: true
    }
  },
  mounted() {
    // 清除表单数据，防止保留之前的登录信息
    this.loginForm.username = ''
    this.loginForm.password = ''
    this.usernameReadonly = true
    this.passwordReadonly = true
    
    // 使用多个 setTimeout 确保在浏览器自动填充之后清除
    // 浏览器自动填充可能在页面加载后延迟发生
    const clearForm = () => {
      this.loginForm.username = ''
      this.loginForm.password = ''
      
      // 清除表单验证状态
      if (this.$refs.loginForm) {
        this.$refs.loginForm.clearValidate()
      }
      
      // 强制清除输入框的值（防止浏览器自动填充）
      this.$nextTick(() => {
        const usernameInput = this.$refs.usernameInput?.$el?.querySelector('input')
        const passwordInput = this.$refs.passwordInput?.$el?.querySelector('input')
        if (usernameInput) {
          usernameInput.value = ''
          usernameInput.removeAttribute('value')
          usernameInput.setAttribute('autocomplete', 'off')
        }
        if (passwordInput) {
          passwordInput.value = ''
          passwordInput.removeAttribute('value')
          passwordInput.setAttribute('autocomplete', 'new-password')
        }
      })
    }
    
    // 立即清除
    clearForm()
    // 延迟清除（防止浏览器自动填充）
    setTimeout(clearForm, 100)
    setTimeout(clearForm, 300)
    setTimeout(clearForm, 500)
  },
  beforeDestroy() {
    // 离开页面时清除表单
    this.loginForm.username = ''
    this.loginForm.password = ''
    this.usernameReadonly = true
    this.passwordReadonly = true
  },
  activated() {
    // 如果使用了keep-alive，激活时也清除表单
    this.loginForm.username = ''
    this.loginForm.password = ''
    this.usernameReadonly = true
    this.passwordReadonly = true
    if (this.$refs.loginForm) {
      this.$refs.loginForm.clearValidate()
    }
  },
  methods: {
    handleUsernameInput() {
      // 监听输入，如果检测到可能是自动填充的值，清除它
      // 这里可以根据需要添加逻辑
    },
    handlePasswordInput() {
      // 监听输入，如果检测到可能是自动填充的值，清除它
      // 这里可以根据需要添加逻辑
    },
    handleUsernameFocus() {
      // 移除readonly属性，允许用户输入
      this.usernameReadonly = false
      // 聚焦时清除用户名（防止自动填充）
      this.$nextTick(() => {
        const input = this.$refs.usernameInput?.$el?.querySelector('input')
        if (input) {
          // 立即清除
          this.loginForm.username = ''
          input.value = ''
          input.removeAttribute('value')
          // 延迟检查，因为浏览器可能在focus之后才填充
          setTimeout(() => {
            if (input.value && !this.loginForm.username) {
              // 如果DOM有值但Vue模型没有，说明是浏览器自动填充
              this.loginForm.username = ''
              input.value = ''
              input.removeAttribute('value')
            }
          }, 100)
        }
      })
    },
    handlePasswordFocus() {
      // 移除readonly属性，允许用户输入
      this.passwordReadonly = false
      // 聚焦时清除密码（防止自动填充）
      this.$nextTick(() => {
        const input = this.$refs.passwordInput?.$el?.querySelector('input')
        if (input) {
          // 立即清除
          this.loginForm.password = ''
          input.value = ''
          input.removeAttribute('value')
          // 延迟检查，因为浏览器可能在focus之后才填充
          setTimeout(() => {
            if (input.value && !this.loginForm.password) {
              // 如果DOM有值但Vue模型没有，说明是浏览器自动填充
              this.loginForm.password = ''
              input.value = ''
              input.removeAttribute('value')
            }
          }, 100)
        }
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            await this.$store.dispatch('user/login', this.loginForm)
            this.$message.success('登录成功')
            const redirect = this.$route.query.redirect || '/'
            this.$router.push(redirect)
          } catch (error) {
            this.$message.error(error.message || '登录失败')
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
.login-container {
  min-height: calc(100vh - 140px);
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f7;
  padding: 40px 20px;

  .login-box {
    width: 400px;
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

    .register-link {
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
  
  .el-form-item:last-child {
    margin-bottom: 0;
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
  .login-container {
    padding: 20px;
  }
  
  .login-box {
    width: 120%;
    max-width: 450px;
    padding: 32px;
  }
  
  h2 {
    font-size: 20px !important;
  }
}
</style>

