import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import i18n from './i18n'

// 引入 Element UI
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale'
import zhLocale from 'element-ui/lib/locale/lang/zh-CN'
import enLocale from 'element-ui/lib/locale/lang/en'

// 引入全局样式
import './assets/styles/index.scss'

Vue.use(ElementUI)

// 根据i18n语言设置Element UI语言
const updateElementLocale = () => {
  const lang = i18n.locale === 'zh' ? zhLocale : enLocale
  locale.use(lang)
}
updateElementLocale()
i18n.vm.$watch('locale', updateElementLocale)

Vue.config.productionTip = false

new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app')
