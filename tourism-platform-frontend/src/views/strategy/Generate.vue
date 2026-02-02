<template>
  <div class="strategy-generate-page">
    <div class="container">
      <el-card>
        <div slot="header">
          <span style="font-size: 20px; font-weight: bold;">
            <i class="el-icon-magic-stick"></i> AI智能攻略生成助手
          </span>
          <div style="float: right;">
            <el-button size="small" @click="goBack">返回</el-button>
          </div>
        </div>

        <!-- 生成表单 -->
        <el-form :model="form" label-width="120px" :rules="rules" ref="generateForm">
          <el-form-item label="游玩天数" prop="duration">
            <el-input-number 
              v-model="form.duration" 
              :min="1" 
              :max="7" 
              style="width: 200px;"
              placeholder="请输入游玩天数"
            />
            <span style="margin-left: 10px; color: #909399; font-size: 12px;">
              建议1-7天
            </span>
          </el-form-item>

          <el-form-item label="预算（元）">
            <el-input-number 
              v-model="form.budget" 
              :min="0" 
              :precision="0"
              style="width: 200px;"
              placeholder="可选，不填则不限预算"
            />
            <span style="margin-left: 10px; color: #909399; font-size: 12px;">
              可选，AI会根据预算合理规划
            </span>
          </el-form-item>

          <el-form-item label="主题">
            <el-select v-model="form.theme" placeholder="请选择主题" style="width: 200px;" clearable>
              <el-option label="亲子游" value="亲子" />
              <el-option label="情侣游" value="情侣" />
              <el-option label="摄影游" value="摄影" />
              <el-option label="文化游" value="文化" />
              <el-option label="美食游" value="美食" />
              <el-option label="休闲游" value="休闲" />
              <el-option label="探险游" value="探险" />
            </el-select>
          </el-form-item>

          <el-form-item label="兴趣偏好">
            <el-input 
              v-model="form.interests" 
              placeholder="如：历史、自然、美食、购物等，多个用逗号分隔"
              style="width: 400px;"
            />
          </el-form-item>

          <el-form-item label="最佳季节">
            <el-select v-model="form.bestSeason" placeholder="请选择最佳季节" style="width: 200px;" clearable>
              <el-option label="春季" value="春季" />
              <el-option label="夏季" value="夏季" />
              <el-option label="秋季" value="秋季" />
              <el-option label="冬季" value="冬季" />
              <el-option label="全年" value="全年" />
            </el-select>
          </el-form-item>

          <el-form-item label="特殊要求">
            <el-input 
              v-model="form.requirements" 
              type="textarea" 
              :rows="4"
              placeholder="请输入特殊要求，如：需要无障碍设施、适合老人、需要停车场等"
              style="width: 600px;"
            />
          </el-form-item>

          <el-form-item>
            <el-button 
              type="primary" 
              :loading="generating" 
              @click="handleGenerate"
              size="medium"
            >
              <i class="el-icon-magic-stick"></i> {{ generating ? '正在生成中...' : '生成攻略' }}
            </el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- 生成结果 -->
        <el-card v-if="generatedStrategy" style="margin-top: 30px;" shadow="never">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">生成结果</span>
            <div style="float: right;">
              <el-button size="small" type="success" :loading="saving" @click="handleSave">
                {{ saving ? '保存中...' : '保存攻略' }}
              </el-button>
              <el-button size="small" @click="handleRegenerate">重新生成</el-button>
            </div>
          </div>

          <div class="generated-content">
            <h2 style="color: #303133; margin-bottom: 15px;">{{ generatedStrategy.title }}</h2>
            <div style="margin-bottom: 20px; color: #606266; line-height: 1.8;">
              {{ generatedStrategy.description }}
            </div>
            
            <el-divider></el-divider>
            
            <div style="margin-bottom: 20px;">
              <h3 style="color: #409EFF; margin-bottom: 10px;">详细攻略</h3>
              <div 
                v-html="formatContent(generatedStrategy.content)" 
                style="line-height: 1.8; color: #606266;"
              ></div>
            </div>

            <el-divider></el-divider>

            <div v-if="generatedStrategy.tips">
              <h3 style="color: #409EFF; margin-bottom: 10px;">注意事项</h3>
              <div 
                v-html="formatContent(generatedStrategy.tips)" 
                style="line-height: 1.8; color: #606266;"
              ></div>
            </div>

            <div style="margin-top: 20px; padding: 15px; background: #f5f7fa; border-radius: 4px;">
              <div style="display: flex; justify-content: space-between; flex-wrap: wrap;">
                <span><strong>游玩天数：</strong>{{ generatedStrategy.duration }}天</span>
                <span v-if="generatedStrategy.budget">
                  <strong>预算：</strong>{{ generatedStrategy.budget }}元
                </span>
                <span v-if="generatedStrategy.theme">
                  <strong>主题：</strong>{{ generatedStrategy.theme }}
                </span>
                <span v-if="generatedStrategy.bestSeason">
                  <strong>最佳季节：</strong>{{ generatedStrategy.bestSeason }}
                </span>
              </div>
            </div>
          </div>
        </el-card>
      </el-card>
    </div>
  </div>
</template>

<script>
import { generateStrategy, submitStrategy } from '@/api/strategy'

export default {
  name: 'StrategyGenerate',
  data() {
    return {
      form: {
        duration: 2,
        budget: null,
        theme: '',
        interests: '',
        bestSeason: '',
        requirements: ''
      },
      rules: {
        duration: [
          { required: true, message: '请输入游玩天数', trigger: 'blur' }
        ]
      },
      generating: false,
      saving: false,
      generatedStrategy: null
    }
  },
  methods: {
    async handleGenerate() {
      this.$refs.generateForm.validate(async (valid) => {
        if (!valid) {
          return false
        }

        this.generating = true
        this.generatedStrategy = null

        try {
          const res = await generateStrategy({
            duration: this.form.duration,
            budget: this.form.budget,
            theme: this.form.theme || null,
            interests: this.form.interests || null,
            bestSeason: this.form.bestSeason || null,
            requirements: this.form.requirements || null
          })

          if (res.code === 200) {
            this.generatedStrategy = res.data
            this.$message.success('攻略生成成功！')
            // 滚动到结果区域
            this.$nextTick(() => {
              const resultCard = document.querySelector('.generated-content')
              if (resultCard) {
                resultCard.scrollIntoView({ behavior: 'smooth', block: 'start' })
              }
            })
          } else {
            this.$message.error(res.message || '生成失败，请稍后重试')
          }
        } catch (error) {
          console.error('生成攻略失败:', error)
          this.$message.error('生成失败：' + (error.response?.data?.message || error.message || '请检查网络连接'))
        } finally {
          this.generating = false
        }
      })
    },

    async handleSave() {
      if (!this.generatedStrategy) {
        this.$message.warning('请先生成攻略')
        return
      }

      // 检查是否登录
      if (!this.$store.getters['user/isLogin']) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }

      // 验证必填字段
      if (!this.generatedStrategy.title || !this.generatedStrategy.content) {
        this.$message.error('攻略标题和内容不能为空')
        return
      }

      this.saving = true
      try {
        const res = await submitStrategy(this.generatedStrategy)
        if (res.code === 200) {
          // 根据用户角色显示不同的提示信息
          const isAdmin = this.$store.getters['user/isAdmin']
          const defaultMessage = isAdmin ? '攻略已保存，已直接发布' : '攻略已保存，等待审核'
          this.$message.success(res.message || defaultMessage)
          // 延迟跳转，让用户看到成功提示
          setTimeout(() => {
            this.$router.push('/strategy')
          }, 1500)
        } else {
          this.$message.error(res.message || '保存失败')
        }
      } catch (error) {
        console.error('保存攻略失败:', error)
        let errorMessage = '保存失败'
        if (error.response) {
          if (error.response.data && error.response.data.message) {
            errorMessage = error.response.data.message
          } else if (error.response.status === 401) {
            errorMessage = '未登录或登录已过期，请重新登录'
            setTimeout(() => {
              this.$router.push('/login')
            }, 2000)
          } else if (error.response.status === 403) {
            errorMessage = '没有权限执行此操作'
          }
        } else if (error.message) {
          errorMessage = error.message
        }
        this.$message.error(errorMessage)
      } finally {
        this.saving = false
      }
    },

    handleRegenerate() {
      this.generatedStrategy = null
      this.handleGenerate()
    },

    resetForm() {
      this.$refs.generateForm.resetFields()
      this.generatedStrategy = null
      this.form = {
        duration: 2,
        budget: null,
        theme: '',
        interests: '',
        bestSeason: '',
        requirements: ''
      }
    },

    formatContent(content) {
      if (!content) return ''
      // 将换行符转换为<br>
      return content.replace(/\n/g, '<br>')
    },

    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.strategy-generate-page {
  padding: 20px 0;
  min-height: calc(100vh - 200px);

  .container {
    width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .generated-content {
    h2 {
      font-size: 24px;
      margin-bottom: 15px;
    }

    h3 {
      font-size: 18px;
      margin-bottom: 10px;
    }
  }
}
</style>
