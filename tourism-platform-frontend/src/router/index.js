import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'
import { Message } from 'element-ui'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/Index.vue')
  },
  {
    path: '/attraction',
    name: 'Attraction',
    component: () => import('@/views/attraction/Index.vue')
  },
  {
    path: '/attraction/:id',
    name: 'AttractionDetail',
    component: () => import('@/views/attraction/Detail.vue')
  },
  {
    path: '/food',
    name: 'Food',
    component: () => import('@/views/food/Index.vue')
  },
  {
    path: '/food/:id',
    name: 'FoodDetail',
    component: () => import('@/views/food/Detail.vue')
  },
  {
    path: '/culture',
    name: 'Culture',
    component: () => import('@/views/culture/Index.vue')
  },
  {
    path: '/culture/:id',
    name: 'CultureDetail',
    component: () => import('@/views/culture/Detail.vue')
  },
  {
    path: '/strategy',
    name: 'Strategy',
    component: () => import('@/views/strategy/Index.vue')
  },
  {
    path: '/strategy/generate',
    name: 'StrategyGenerate',
    component: () => import('@/views/strategy/Generate.vue')
  },
  {
    path: '/strategy/:id',
    name: 'StrategyDetail',
    component: () => import('@/views/strategy/Detail.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/user/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/user/Register.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/user/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/submit',
    name: 'Submit',
    component: () => import('@/views/user/Submit.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/order',
    name: 'Order',
    component: () => import('@/views/order/Index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/order/:id',
    name: 'OrderDetail',
    component: () => import('@/views/order/Detail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/favorite',
    name: 'Favorite',
    component: () => import('@/views/favorite/Index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/map',
    name: 'Map',
    component: () => import('@/views/map/Index.vue')
  },
  {
    path: '/hotel',
    name: 'Hotel',
    component: () => import('@/views/hotel/Index.vue')
  },
  {
    path: '/hotel/:id',
    name: 'HotelDetail',
    component: () => import('@/views/hotel/Detail.vue')
  },
  {
    path: '/experience',
    name: 'Experience',
    component: () => import('@/views/experience/Index.vue')
  },
  {
    path: '/experience/:id',
    name: 'ExperienceDetail',
    component: () => import('@/views/experience/Detail.vue')
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/admin/Index.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/users',
    name: 'AdminUsers',
    component: () => import('@/views/admin/Users.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/attractions',
    name: 'AdminAttractions',
    component: () => import('@/views/admin/Attractions.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/foods',
    name: 'AdminFoods',
    component: () => import('@/views/admin/Foods.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/cultures',
    name: 'AdminCultures',
    component: () => import('@/views/admin/Cultures.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/strategies',
    name: 'AdminStrategies',
    component: () => import('@/views/admin/Strategies.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/hotels',
    name: 'AdminHotels',
    component: () => import('@/views/admin/Hotels.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/experiences',
    name: 'AdminExperiences',
    component: () => import('@/views/admin/Experiences.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/orders',
    name: 'AdminOrders',
    component: () => import('@/views/admin/Orders.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/announcements',
    name: 'AdminAnnouncements',
    component: () => import('@/views/admin/Announcements.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/audit',
    name: 'AdminAudit',
    component: () => import('@/views/admin/Audit.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 重写 push 方法，统一处理重复导航错误
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => {
    // 忽略重复导航错误和导航取消错误
    if (err.name !== 'NavigationDuplicated' && err.name !== 'NavigationCancelled') {
      return Promise.reject(err)
    }
  })
}

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const token = store.getters['user/token']
  let userInfo = store.getters['user/userInfo']
  
  // 如果token存在但userInfo不存在，尝试从服务器获取用户信息
  if (token && !userInfo) {
    try {
      await store.dispatch('user/getUserInfo')
      userInfo = store.getters['user/userInfo']
    } catch (error) {
      // 如果获取用户信息失败（token可能已过期），清除token
      console.error('获取用户信息失败:', error)
      await store.dispatch('user/logout')
      if (to.path !== '/login') {
        next('/login')
        return
      }
    }
  }
  
  const isAdmin = userInfo && userInfo.role === 'admin'
  
  // 需要登录的页面
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token) {
      next('/login')
      return
    }
    
    // 需要管理员权限的页面
    if (to.matched.some(record => record.meta.requiresAdmin)) {
      if (!isAdmin) {
        next('/')
        Message.error('您没有权限访问此页面！')
        return
      }
    }
  }
  
  next()
})

export default router
