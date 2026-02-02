# 家乡文旅宣传平台 - 前端项目

## 项目简介

基于 Vue 2.x + Element UI 的家乡文旅宣传平台前端项目，提供景点展示、美食推荐、民俗文化、旅游攻略、在线预订等功能。

## 技术栈

- Vue 2.6.x
- Element UI 2.15.x
- Vue Router 3.x
- Vuex 3.x
- Axios 1.11.x
- 高德地图 API

## 项目结构

```
src/
├── api/              # API 接口
│   ├── user.js
│   ├── attraction.js
│   ├── food.js
│   ├── culture.js
│   ├── strategy.js
│   ├── order.js
│   ├── comment.js
│   ├── favorite.js
│   ├── recommendation.js
│   ├── announcement.js
│   ├── map.js
│   └── upload.js
├── assets/           # 静态资源
│   └── styles/       # 全局样式
├── components/        # 公共组件
│   ├── layout/       # 布局组件
│   │   ├── Header.vue
│   │   └── Footer.vue
│   └── common/       # 通用组件
│       ├── Pagination.vue
│       └── CommentList.vue
├── router/           # 路由配置
├── store/            # Vuex 状态管理
│   └── modules/
│       └── user.js
├── utils/            # 工具函数
│   ├── request.js   # Axios 封装
│   └── auth.js      # 认证工具
└── views/           # 页面组件
    ├── home/        # 首页
    ├── attraction/  # 景点模块
    ├── food/        # 美食模块
    ├── culture/     # 文化模块
    ├── strategy/    # 攻略模块
    ├── order/       # 订单模块
    ├── user/        # 用户模块
    ├── favorite/    # 收藏模块
    └── map/         # 地图模块
```

## 环境配置

### 开发环境

创建 `.env.development` 文件：

```env
VUE_APP_BASE_API=http://localhost:8080/api
VUE_APP_TITLE=家乡文旅宣传平台
VUE_APP_MAP_KEY=你的地图API密钥
```

### 生产环境

创建 `.env.production` 文件：

```env
VUE_APP_BASE_API=http://your-domain.com/api
VUE_APP_TITLE=家乡文旅宣传平台
VUE_APP_MAP_KEY=你的地图API密钥
```

## 安装依赖

```bash
npm install
```

## 运行项目

```bash
npm run serve
```

访问地址：http://localhost:8081

## 构建生产版本

```bash
npm run build
```

构建后的文件在 `dist` 目录中。

## 功能模块

### 1. 用户模块
- 用户注册/登录
- 个人中心
- 用户信息修改

### 2. 景点模块
- 景点列表（分页、搜索、分类筛选）
- 景点详情
- 景点图片展示
- 景点收藏

### 3. 美食模块
- 美食列表（分页、搜索、分类筛选）
- 美食详情
- 制作方法展示
- 美食收藏

### 4. 文化模块
- 文化列表
- 文化详情

### 5. 攻略模块
- 攻略列表
- 攻略详情
- 路线规划

### 6. 订单模块
- 订单列表（按状态筛选）
- 订单详情
- 订单支付
- 订单取消

### 7. 评论模块
- 评论列表
- 添加评论
- 评分功能

### 8. 收藏模块
- 收藏列表（按类型筛选）
- 取消收藏

### 9. 地图模块
- 景点地图展示
- 地图标记
- 信息窗口

### 10. 推荐模块
- 热门推荐
- 个性化推荐

## API 接口说明

所有 API 接口都在 `src/api/` 目录下，统一使用 `request.js` 进行封装，自动处理：
- Token 认证
- 请求/响应拦截
- 错误处理
- 统一响应格式

## 路由守卫

需要登录的页面在路由配置中添加 `meta: { requiresAuth: true }`，路由守卫会自动检查登录状态。

## 状态管理

使用 Vuex 管理全局状态，当前包含：
- `user` 模块：用户信息、登录状态

## 注意事项

1. **地图 API 密钥**：需要在 `.env` 文件中配置高德地图 API 密钥
2. **后端 API 地址**：确保后端服务运行在 `http://localhost:8080`
3. **CORS 跨域**：开发环境使用代理，生产环境需要后端配置 CORS
4. **Token 存储**：使用 localStorage 存储 JWT Token

## 开发规范

1. 组件命名使用 PascalCase
2. 文件命名使用 kebab-case
3. API 接口统一返回 `Result` 格式
4. 使用 Element UI 组件库
5. 样式使用 SCSS 预处理器

## 常见问题

### 1. 跨域问题
开发环境已配置代理，如果仍有问题，检查 `vue.config.js` 中的代理配置。

### 2. Token 过期
Token 过期会自动跳转到登录页面，需要重新登录。

### 3. 地图加载失败
检查 `.env` 文件中的 `VUE_APP_MAP_KEY` 是否正确配置。

## 后续优化

- [ ] 添加国际化支持
- [ ] 优化图片懒加载
- [ ] 添加骨架屏
- [ ] 优化 SEO
- [ ] 添加 PWA 支持
- [ ] 性能优化
