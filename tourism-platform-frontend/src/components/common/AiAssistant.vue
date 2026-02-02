<template>
  <div class="ai-assistant">
    <!-- 浮窗按钮 -->
    <div 
      class="ai-assistant-btn" 
      :class="{ 'expanded': isExpanded }"
      @click="toggleExpanded"
    >
      <i class="el-icon-chat-line-round"></i>
      <span v-if="isExpanded" class="btn-text">收起</span>
    </div>

    <!-- 对话窗口 -->
    <transition name="slide-up">
      <div v-if="isExpanded" class="ai-assistant-panel">
        <div class="panel-header">
          <h3>AI智能助手</h3>
          <div class="header-actions">
            <el-button 
              type="text" 
              icon="el-icon-refresh" 
              @click="clearConversation"
              size="small"
            >
              清空对话
            </el-button>
            <el-button 
              type="text" 
              icon="el-icon-close" 
              @click="toggleExpanded"
              size="small"
            >
            </el-button>
          </div>
        </div>

        <div class="panel-content" ref="messageContainer">
          <div class="message-list">
            <div 
              v-for="(msg, index) in messages" 
              :key="index"
              class="message-item"
              :class="msg.role"
            >
              <div class="message-avatar">
                <i v-if="msg.role === 'user'" class="el-icon-user"></i>
                <i v-else class="el-icon-chat-line-round"></i>
              </div>
              <div class="message-content">
                <div class="message-text" v-html="formatMessage(msg.content)"></div>
                <div class="message-time">{{ formatTime(msg.timestamp) }}</div>
              </div>
            </div>
            
            <!-- 加载中提示 -->
            <div v-if="isLoading" class="message-item assistant">
              <div class="message-avatar">
                <i class="el-icon-chat-line-round"></i>
              </div>
              <div class="message-content">
                <div class="message-text loading">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>
            </div>
          </div>

          <!-- 快捷操作 -->
          <div v-if="messages.length === 0" class="quick-actions">
            <div class="quick-action-title">你可以问我：</div>
            <div class="quick-action-buttons">
              <el-button 
                size="small" 
                @click="sendQuickMessage('推荐一些热门景点')"
              >
                推荐热门景点
              </el-button>
              <el-button 
                size="small" 
                @click="sendQuickMessage('有什么好吃的推荐吗？')"
              >
                美食推荐
              </el-button>
              <el-button 
                size="small" 
                @click="sendQuickMessage('帮我规划一个2日游路线')"
              >
                规划路线
              </el-button>
              <el-button 
                size="small" 
                @click="sendQuickMessage('生成一份旅游攻略')"
              >
                生成攻略
              </el-button>
            </div>
          </div>
        </div>

        <div class="panel-footer">
          <div class="input-area">
            <el-input
              v-model="inputMessage"
              type="textarea"
              :rows="2"
              placeholder="输入您的问题..."
              @keydown.ctrl.enter="sendMessage"
              @keydown.meta.enter="sendMessage"
              :disabled="isLoading"
            ></el-input>
            <div class="input-actions">
              <el-button 
                type="text" 
                size="small"
                @click="showStrategyDialog = true"
              >
                <i class="el-icon-magic-stick"></i> 生成攻略
              </el-button>
              <el-button 
                type="primary" 
                size="small"
                @click="sendMessage"
                :loading="isLoading"
                :disabled="!inputMessage.trim()"
              >
                发送
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <!-- AI攻略生成对话框 -->
    <el-dialog
      title="AI生成攻略"
      :visible.sync="showStrategyDialog"
      width="600px"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      :modal="true"
      :modal-append-to-body="true"
      :append-to-body="true"
      :z-index="3000"
      @close="handleDialogClose"
    >
      <el-form :model="strategyForm" label-width="100px">
        <el-form-item label="游玩天数" required>
          <el-select v-model="strategyForm.duration" placeholder="请选择游玩天数">
            <el-option label="1天" :value="1"></el-option>
            <el-option label="2天" :value="2"></el-option>
            <el-option label="3天" :value="3"></el-option>
            <el-option label="4天" :value="4"></el-option>
            <el-option label="5天" :value="5"></el-option>
            <el-option label="6天" :value="6"></el-option>
            <el-option label="7天" :value="7"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预算（元）">
          <el-input-number v-model="strategyForm.budget" :min="0" :step="100"></el-input-number>
        </el-form-item>
        <el-form-item label="主题">
          <el-input v-model="strategyForm.theme" placeholder="如：亲子、情侣、摄影等"></el-input>
        </el-form-item>
        <el-form-item label="兴趣偏好">
          <el-input v-model="strategyForm.interests" placeholder="如：历史、美食、自然等"></el-input>
        </el-form-item>
        <el-form-item label="最佳季节">
          <el-input v-model="strategyForm.bestSeason" placeholder="如：春季、夏季等"></el-input>
        </el-form-item>
        <el-form-item label="特殊要求">
          <el-input 
            v-model="strategyForm.requirements" 
            type="textarea" 
            :rows="3"
            placeholder="如：需要无障碍设施、适合老人等"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showStrategyDialog = false">取消</el-button>
        <el-button type="primary" @click="generateStrategy" :loading="isGenerating">
          生成攻略
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { chatWithAI } from '@/api/aiAssistant'
import { generateStrategy, submitStrategy } from '@/api/strategy'
import { Message } from 'element-ui'
import store from '@/store'

export default {
  name: 'AiAssistant',
  data() {
    return {
      isExpanded: false,
      messages: [],
      inputMessage: '',
      isLoading: false,
      showStrategyDialog: false,
      isGenerating: false,
      strategyForm: {
        duration: 2,
        budget: null,
        theme: '',
        interests: '',
        bestSeason: '',
        requirements: ''
      }
    }
  },
  mounted() {
    // 添加欢迎消息
    this.addMessage('assistant', '您好！我是AI智能助手，可以帮您查询景点、美食、酒店信息，规划旅游路线，或者生成旅游攻略。有什么可以帮您的吗？')
  },
  methods: {
    toggleExpanded() {
      this.isExpanded = !this.isExpanded
      if (this.isExpanded) {
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      }
    },
    sendMessage() {
      if (!this.inputMessage.trim() || this.isLoading) {
        return
      }

      const userMessage = this.inputMessage.trim()
      this.inputMessage = ''
      
      // 添加用户消息
      this.addMessage('user', userMessage)
      
      // 发送到AI
      this.isLoading = true
      const conversationHistory = this.messages
        .filter(msg => msg.role !== 'system')
        .map(msg => ({
          role: msg.role,
          content: msg.content
        }))
      
      chatWithAI(userMessage, conversationHistory)
        .then(res => {
          if (res.code === 200) {
            this.addMessage('assistant', res.data)
          } else {
            const errorMsg = res.message || '抱歉，我现在无法回答您的问题，请稍后再试。'
            this.addMessage('assistant', errorMsg)
          }
        })
        .catch(err => {
          console.error('AI对话失败:', err)
          // 提取错误消息，如果是Error对象则获取message
          const errorMsg = err.message || err.response?.data?.message || '抱歉，服务暂时不可用，请稍后再试。'
          this.addMessage('assistant', `抱歉，${errorMsg}`)
        })
        .finally(() => {
          this.isLoading = false
          this.$nextTick(() => {
            this.scrollToBottom()
          })
        })
    },
    sendQuickMessage(message) {
      this.inputMessage = message
      this.sendMessage()
    },
    addMessage(role, content) {
      this.messages.push({
        role,
        content,
        timestamp: new Date()
      })
      this.$nextTick(() => {
        this.scrollToBottom()
      })
    },
    clearConversation() {
      this.$confirm('确定要清空对话记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.messages = []
        this.addMessage('assistant', '对话已清空，有什么可以帮您的吗？')
      }).catch(() => {})
    },
    scrollToBottom() {
      const container = this.$refs.messageContainer
      if (container) {
        container.scrollTop = container.scrollHeight
      }
    },
    formatMessage(content) {
      // 简单的Markdown格式化
      if (!content) return ''
      
      // 换行处理
      let formatted = content.replace(/\n/g, '<br>')
      
      // 加粗处理
      formatted = formatted.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
      
      // 链接处理
      formatted = formatted.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank">$1</a>')
      
      return formatted
    },
    formatTime(timestamp) {
      if (!timestamp) return ''
      const date = new Date(timestamp)
      const now = new Date()
      const diff = now - date
      
      if (diff < 60000) {
        return '刚刚'
      } else if (diff < 3600000) {
        return Math.floor(diff / 60000) + '分钟前'
      } else if (diff < 86400000) {
        return Math.floor(diff / 3600000) + '小时前'
      } else {
        return date.toLocaleString('zh-CN', { 
          month: '2-digit', 
          day: '2-digit', 
          hour: '2-digit', 
          minute: '2-digit' 
        })
      }
    },
    handleDialogClose() {
      // 对话框关闭时重置表单
      this.strategyForm = {
        duration: 2,
        budget: null,
        theme: '',
        interests: '',
        bestSeason: '',
        requirements: ''
      }
      this.isGenerating = false
    },
    generateStrategy() {
      if (!this.strategyForm.duration) {
        Message.warning('请选择游玩天数')
        return
      }

      this.isGenerating = true
      generateStrategy(this.strategyForm)
        .then(res => {
          if (res.code === 200) {
            const strategy = res.data
            
            // 生成攻略后，自动保存到数据库
            this.submitStrategy(strategy)
          } else {
            Message.error(res.message || '攻略生成失败')
            this.isGenerating = false
          }
        })
        .catch(err => {
          console.error('生成攻略失败:', err)
          Message.error('攻略生成失败，请稍后再试')
          this.isGenerating = false
        })
    },
    submitStrategy(strategy) {
      // 检查用户是否登录
      const isLogin = store.getters['user/isLogin']
      
      if (!isLogin) {
        Message.warning('请先登录后再保存攻略')
        this.addMessage('assistant', `攻略已生成！标题：${strategy.title}。但保存需要登录，请先登录后再保存。`)
        this.isGenerating = false
        // 提示用户登录
        this.$confirm('攻略已生成，但保存需要登录。是否前往登录页面？', '提示', {
          confirmButtonText: '去登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$router.push('/login')
        }).catch(() => {})
        return
      }
      
      // 调用submit接口保存攻略
      submitStrategy(strategy)
        .then(res => {
          if (res.code === 200) {
            Message.success(res.message || '攻略保存成功！')
            this.showStrategyDialog = false
            
            // 添加消息提示用户
            const statusMsg = res.message && res.message.includes('审核') 
              ? '攻略已提交，等待审核通过后即可在攻略页面查看。' 
              : '攻略已保存并发布，您可以在攻略页面查看详情。'
            
            this.addMessage('assistant', `攻略已生成并保存！标题：${strategy.title}。${statusMsg}`)
            
            // 如果攻略已发布（管理员），可以跳转到攻略详情页
            if (res.data && res.data.id && res.data.status === 1) {
              // 延迟跳转，让用户看到消息
              setTimeout(() => {
                this.$router.push(`/strategy/${res.data.id}`)
              }, 1500)
            }
          } else {
            Message.warning(res.message || '攻略保存失败')
            // 即使保存失败，也提示用户攻略已生成
            this.addMessage('assistant', `攻略已生成！标题：${strategy.title}。但保存失败：${res.message || '未知错误'}，您可以手动保存。`)
          }
        })
        .catch(err => {
          console.error('保存攻略失败:', err)
          const errorMsg = err.response?.data?.message || err.message || '未知错误'
          Message.warning('攻略生成成功，但保存失败：' + errorMsg)
          // 即使保存失败，也提示用户攻略已生成
          this.addMessage('assistant', `攻略已生成！标题：${strategy.title}。但保存失败：${errorMsg}，您可以稍后重试保存。`)
        })
        .finally(() => {
          this.isGenerating = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.ai-assistant {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
}

.ai-assistant-btn {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
  transition: all 0.3s ease;
  font-size: 24px;

  &:hover {
    transform: scale(1.1);
    box-shadow: 0 6px 16px rgba(102, 126, 234, 0.5);
  }

  &.expanded {
    border-radius: 8px 8px 0 0;
    width: auto;
    height: auto;
    padding: 8px 16px;
    font-size: 14px;

    .btn-text {
      margin-left: 8px;
    }
  }
}

.ai-assistant-panel {
  position: absolute;
  bottom: 60px;
  right: 0;
  width: 400px;
  height: 600px;
  background: white;
  border-radius: 12px 12px 0 0;
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  padding: 16px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;

  h3 {
    margin: 0;
    font-size: 16px;
    font-weight: 500;
  }

  .header-actions {
    display: flex;
    gap: 8px;

    .el-button {
      color: white;
      padding: 4px 8px;

      &:hover {
        background: rgba(255, 255, 255, 0.1);
      }
    }
  }
}

.panel-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: #f5f7fa;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  display: flex;
  gap: 12px;

  &.user {
    flex-direction: row-reverse;

    .message-content {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
    }
  }

  &.assistant {
    .message-content {
      background: white;
      color: #303133;
    }
  }
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #e4e7ed;
  color: #909399;
  flex-shrink: 0;
  font-size: 18px;
}

.message-content {
  max-width: 70%;
  padding: 10px 14px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.message-text {
  line-height: 1.6;
  word-wrap: break-word;

  &.loading {
    display: flex;
    gap: 4px;
    padding: 4px 0;

    span {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: #909399;
      animation: loading 1.4s infinite ease-in-out;

      &:nth-child(1) {
        animation-delay: -0.32s;
      }

      &:nth-child(2) {
        animation-delay: -0.16s;
      }
    }
  }
}

@keyframes loading {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

.message-time {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  opacity: 0.7;
}

.quick-actions {
  padding: 20px;
  text-align: center;
}

.quick-action-title {
  color: #909399;
  font-size: 14px;
  margin-bottom: 12px;
}

.quick-action-buttons {
  display: flex;
  flex-direction: column;
  gap: 8px;

  .el-button {
    width: 100%;
    justify-content: flex-start;
  }
}

.panel-footer {
  padding: 16px;
  border-top: 1px solid #e4e7ed;
  background: white;
}

.input-area {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s ease;
}

.slide-up-enter,
.slide-up-leave-to {
  transform: translateY(100%);
  opacity: 0;
}

/* 滚动条样式 */
.panel-content::-webkit-scrollbar {
  width: 6px;
}

.panel-content::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.panel-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.panel-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
