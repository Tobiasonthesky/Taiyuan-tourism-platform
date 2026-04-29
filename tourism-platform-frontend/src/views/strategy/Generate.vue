<template>
  <div class="strategy-generate-page">
    <div class="container">
      <el-card>
        <div slot="header" class="card-header">
          <span style="font-size: 20px; font-weight: bold;">
            <i class="el-icon-magic-stick"></i> {{ $t('strategy.aiGenerator') }}
          </span>
          <div>
            <el-button size="small" @click="goBack">{{ $t('common.back') }}</el-button>
          </div>
        </div>

        <!-- 生成表单 -->
        <el-form :model="form" label-width="140px" :rules="rules" ref="generateForm">
          <el-form-item :label="$t('strategy.duration')" prop="duration">
            <el-input-number 
              v-model="form.duration" 
              :min="1" 
              :max="7" 
              style="width: 200px;"
              :placeholder="$t('strategy.enterDuration')"
            />
            <span style="margin-left: 10px; color: #909399; font-size: 12px;">
              {{ $t('strategy.suggestDays') }}
            </span>
          </el-form-item>

          <el-form-item :label="$t('strategy.budget')">
            <el-input-number 
              v-model="form.budget" 
              :min="0" 
              :precision="0"
              style="width: 200px;"
              :placeholder="$t('strategy.budgetPlaceholder')"
            />
            <span style="margin-left: 10px; color: #909399; font-size: 12px;">
              {{ $t('strategy.budgetTip') }}
            </span>
          </el-form-item>

          <el-form-item :label="$t('strategy.theme')">
            <el-select v-model="form.theme" :placeholder="$t('strategy.selectTheme')" style="width: 200px;" clearable>
              <el-option :label="$t('strategy.themeFamily')" value="亲子" />
              <el-option :label="$t('strategy.themeCouple')" value="情侣" />
              <el-option :label="$t('strategy.themePhotography')" value="摄影" />
              <el-option :label="$t('strategy.themeCulture')" value="文化" />
              <el-option :label="$t('strategy.themeFood')" value="美食" />
              <el-option :label="$t('strategy.themeLeisure')" value="休闲" />
              <el-option :label="$t('strategy.themeAdventure')" value="探险" />
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('strategy.interests')">
            <el-input 
              v-model="form.interests" 
              :placeholder="$t('strategy.interestsPlaceholder')"
              style="width: 400px;"
            />
          </el-form-item>

          <el-form-item :label="$t('strategy.bestSeason')">
            <el-select v-model="form.bestSeason" :placeholder="$t('strategy.selectSeason')" style="width: 200px;" clearable>
              <el-option :label="$t('strategy.seasonSpring')" value="春季" />
              <el-option :label="$t('strategy.seasonSummer')" value="夏季" />
              <el-option :label="$t('strategy.seasonAutumn')" value="秋季" />
              <el-option :label="$t('strategy.seasonWinter')" value="冬季" />
              <el-option :label="$t('strategy.seasonAll')" value="全年" />
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('strategy.requirements')">
            <el-input 
              v-model="form.requirements" 
              type="textarea" 
              :rows="4"
              :placeholder="$t('strategy.requirementsPlaceholder')"
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
              <i class="el-icon-magic-stick"></i> {{ generating ? $t('strategy.generating') : $t('strategy.generate') }}
            </el-button>
            <el-button @click="resetForm">{{ $t('common.reset') }}</el-button>
          </el-form-item>
        </el-form>

        <!-- 生成结果 -->
        <el-card v-if="generatedStrategy" style="margin-top: 30px;" shadow="never">
          <div slot="header" class="card-header">
            <span style="font-size: 18px; font-weight: bold;">{{ $t('strategy.generatedResult') }}</span>
            <div>
              <el-button size="small" type="success" :loading="saving" @click="handleSave">
                {{ saving ? $t('strategy.saving') : $t('strategy.saveStrategy') }}
              </el-button>
              <el-button size="small" @click="handleRegenerate">{{ $t('strategy.regenerate') }}</el-button>
            </div>
          </div>

          <div class="generated-content">
            <h2 style="color: #303133; margin-bottom: 15px;">{{ generatedStrategy.title }}</h2>
            <div style="margin-bottom: 20px; color: #606266; line-height: 1.8;">
              {{ generatedStrategy.description }}
            </div>
            
            <el-divider></el-divider>
            
            <div style="margin-bottom: 20px;">
              <h3 style="color: #409EFF; margin-bottom: 10px;">{{ $t('strategy.detailContent') }}</h3>
              <div 
                v-html="formatContent(generatedStrategy.content)" 
                style="line-height: 1.8; color: #606266;"
              ></div>
            </div>

            <el-divider></el-divider>

            <div v-if="generatedStrategy.tips">
              <h3 style="color: #409EFF; margin-bottom: 10px;">{{ $t('strategy.tips') }}</h3>
              <div 
                v-html="formatContent(generatedStrategy.tips)" 
                style="line-height: 1.8; color: #606266;"
              ></div>
            </div>

            <div style="margin-top: 20px; padding: 15px; background: #f5f7fa; border-radius: 4px;">
              <div style="display: flex; justify-content: space-between; flex-wrap: wrap;">
                <span><strong>{{ $t('strategy.duration') }}：</strong>{{ generatedStrategy.duration }}{{ $t('strategy.day') }}</span>
                <span v-if="generatedStrategy.budget">
                  <strong>{{ $t('strategy.budget') }}：</strong>{{ generatedStrategy.budget }}{{ $t('strategy.yuan') }}
                </span>
                <span v-if="generatedStrategy.theme">
                  <strong>{{ $t('strategy.theme') }}：</strong>{{ getThemeLabel(generatedStrategy.theme) }}
                </span>
                <span v-if="generatedStrategy.bestSeason">
                  <strong>{{ $t('strategy.bestSeason') }}：</strong>{{ getSeasonLabel(generatedStrategy.bestSeason) }}
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
          { required: true, message: this.$t('strategy.durationRequired'), trigger: 'blur' }
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
            this.$message.success(this.$t('strategy.generateSuccess'))
            // 滚动到结果区域
            this.$nextTick(() => {
              const resultCard = document.querySelector('.generated-content')
              if (resultCard) {
                resultCard.scrollIntoView({ behavior: 'smooth', block: 'start' })
              }
            })
          } else {
            this.$message.error(res.message || this.$t('strategy.generateFailed'))
          }
        } catch (error) {
          console.error('生成攻略失败:', error)
          this.$message.error(this.$t('strategy.generateFailed') + (error.response?.data?.message || error.message || ''))
        } finally {
          this.generating = false
        }
      })
    },

    async handleSave() {
      if (!this.generatedStrategy) {
        this.$message.warning(this.$t('strategy.generateFirst'))
        return
      }

      // 检查是否登录
      if (!this.$store.getters['user/isLogin']) {
        this.$message.warning(this.$t('common.pleaseLogin'))
        this.$router.push('/login')
        return
      }

      // 验证必填字段
      if (!this.generatedStrategy.title || !this.generatedStrategy.content) {
        this.$message.error(this.$t('strategy.titleContentRequired'))
        return
      }

      this.saving = true
      try {
        const res = await submitStrategy(this.generatedStrategy)
        if (res.code === 200) {
          // 根据用户角色显示不同的提示信息
          const isAdmin = this.$store.getters['user/isAdmin']
          const defaultMessage = isAdmin ? this.$t('strategy.saveSuccessAdmin') : this.$t('strategy.saveSuccessPending')
          this.$message.success(res.message || defaultMessage)
          // 延迟跳转，让用户看到成功提示
          setTimeout(() => {
            this.$router.push('/strategy')
          }, 1500)
        } else {
          this.$message.error(res.message || this.$t('strategy.saveFailed'))
        }
      } catch (error) {
        console.error('保存攻略失败:', error)
        let errorMessage = this.$t('strategy.saveFailed')
        if (error.response) {
          if (error.response.data && error.response.data.message) {
            errorMessage = error.response.data.message
          } else if (error.response.status === 401) {
            errorMessage = this.$t('common.notLoggedIn')
            setTimeout(() => {
              this.$router.push('/login')
            }, 2000)
          } else if (error.response.status === 403) {
            errorMessage = this.$t('common.noPermission')
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

    getThemeLabel(theme) {
      const themeMap = {
        '亲子': this.$t('strategy.themeFamily'),
        '情侣': this.$t('strategy.themeCouple'),
        '摄影': this.$t('strategy.themePhotography'),
        '文化': this.$t('strategy.themeCulture'),
        '美食': this.$t('strategy.themeFood'),
        '休闲': this.$t('strategy.themeLeisure'),
        '探险': this.$t('strategy.themeAdventure')
      }
      return themeMap[theme] || theme
    },

    getSeasonLabel(season) {
      const seasonMap = {
        '春季': this.$t('strategy.seasonSpring'),
        '夏季': this.$t('strategy.seasonSummer'),
        '秋季': this.$t('strategy.seasonAutumn'),
        '冬季': this.$t('strategy.seasonWinter'),
        '全年': this.$t('strategy.seasonAll')
      }
      return seasonMap[season] || season
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

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
    width: 100%;
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
