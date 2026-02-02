import Vue from 'vue'
import VueI18n from 'vue-i18n'
import zh from './locales/zh'
import en from './locales/en'

Vue.use(VueI18n)

const messages = {
  zh,
  en
}

// 从localStorage获取语言设置，默认为中文
const locale = localStorage.getItem('language') || 'zh'

const i18n = new VueI18n({
  locale,
  fallbackLocale: 'zh',
  messages
})

export default i18n

