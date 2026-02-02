# 家乡文旅宣传平台 - 功能实现文档

## 项目概述

这是一个基于 Spring Boot + Vue.js 的家乡文旅宣传平台，提供景点展示、美食推荐、民俗文化、旅游攻略、在线预订等功能。

---

## 一、用户模块

### 1.1 用户注册与登录

**后端实现：**
- `UserController.java` - 提供注册、登录接口
- `UserService.java` - 业务逻辑处理
- JWT Token 认证机制

**功能特点：**
- 用户注册：用户名、密码、邮箱验证
- 用户登录：JWT Token 生成和验证
- 密码加密：使用 BCrypt 加密存储
- Token 有效期：24小时

**前端实现：**
- `Login.vue` - 登录页面
- `Register.vue` - 注册页面
- `store/modules/user.js` - 用户状态管理
- Token 存储在 localStorage

### 1.2 用户信息管理

**功能：**
- 获取用户信息：`GET /user/info`
- 更新用户信息：`PUT /user/info`
- 个人中心页面：`Profile.vue`

---

## 二、内容模块

### 2.1 景点模块

**实体类：** `Attraction.java`
**服务类：** `AttractionServiceImpl.java`
**控制器：** `AttractionController.java`

**功能：**
1. **列表查询** (`GET /attractions`)
   - 支持分类筛选
   - 支持关键词搜索
   - 只返回已审核通过的内容（status=1）
   - 分页支持

2. **详情查询** (`GET /attractions/{id}`)
   - 自动增加浏览次数
   - 只返回已审核通过的内容

3. **用户提交** (`POST /attractions/submit`)
   - 需要登录（USER角色）
   - 状态设置为待审核（status=0）
   - 自动关联用户ID

4. **多图片支持**
   - 图片实体：`AttractionImage.java`
   - 图片列表：`GET /attractions/{id}/images`
   - 图片轮播展示

### 2.2 美食模块

**实现方式：** 与景点模块类似
**特点：** 支持制作方法、主要食材等字段

### 2.3 文化模块

**实现方式：** 与景点模块类似
**特点：** 支持历史背景、活动时间、活动地点等字段

### 2.4 攻略模块

**实体类：** `Strategy.java`
**特点：**
- 支持路线规划（`StrategyRoute.java`）
- 支持主题分类（1日游、2日游、主题游）
- 支持预算、最佳季节等字段

### 2.5 体验项目模块

**实体类：** `Experience.java`
**特点：**
- 支持体验时长、价格
- 支持使用日期和时间选择

### 2.6 酒店模块

**实体类：** `Hotel.java`
**特点：**
- 支持星级评定
- 支持房间管理（`HotelRoom.java`）
- 房间库存管理
- 支持入住/退房日期选择

---

## 三、订单模块

### 3.1 订单创建

**流程：**
1. 用户选择商品（景点门票/酒店房间/体验项目）
2. 填写联系信息和备注
3. 创建订单（状态：待支付）
4. **库存检查**：创建订单时检查酒店房间库存

**实现：**
- `OrderServiceImpl.createOrder()` - 创建订单
- 订单项保存到 `OrderItem` 表
- 酒店订单保存房间信息到 `extraInfo`（JSON格式）

### 3.2 订单支付

**流程：**
1. 用户点击支付
2. **扣减库存**：支付时扣减酒店房间库存
3. 更新订单状态为"已支付"

**实现：**
- `OrderServiceImpl.payOrder()` - 支付订单
- `decreaseStock()` - 扣减库存方法

### 3.3 订单取消

**流程：**
1. 待支付订单：直接取消，状态改为"已取消"
2. 已支付订单：
   - **恢复库存**：恢复酒店房间库存
   - 状态改为"已退款"

**实现：**
- `OrderServiceImpl.cancelOrder()` - 取消订单
- `restoreStock()` - 恢复库存方法

### 3.4 订单状态

- 0: 待支付
- 1: 已支付
- 2: 已使用
- 3: 已取消
- 4: 已退款

### 3.5 订单详情

**功能：**
- 显示订单基本信息
- 显示订单项列表（包含房间信息）
- 酒店订单显示：房间类型、房间名称、入住/退房日期

**实现：**
- `OrderDetailVO` - 订单详情视图对象
- `OrderItemVO` - 订单项视图对象（包含房间信息）

---

## 四、评论模块

### 4.1 添加评论

**功能：**
- 支持评分（0-5分）
- 支持评分详情
- 支持图片上传
- 自动更新目标实体的评论数和评分

**实现：**
- `CommentServiceImpl.addComment()` - 添加评论
- `updateTargetCommentCount()` - 更新评论数和评分

### 4.2 评论回复

**功能：**
- 支持回复评论
- 支持回复回复（二级回复）
- 自动更新评论的回复数

**实现：**
- `CommentReplyServiceImpl.addReply()` - 添加回复

### 4.3 删除评论

**功能：**
- 只能删除自己的评论
- 自动减少目标实体的评论数

---

## 五、收藏模块

### 5.1 添加收藏

**功能：**
- 支持收藏景点、美食、文化、攻略、体验项目、酒店
- 自动更新目标实体的收藏数

### 5.2 取消收藏

**功能：**
- 取消收藏
- 自动减少目标实体的收藏数

### 5.3 收藏列表

**功能：**
- 按类型筛选收藏
- 分页显示

---

## 六、图片管理模块

### 6.1 多图片支持

**支持的模块：**
- 景点（`AttractionImage`）
- 美食（`FoodImage`）
- 文化（`CultureImage`）
- 攻略（`StrategyImage`）
- 体验项目（`ExperienceImage`）
- 酒店（`HotelImage`）

### 6.2 图片上传

**用户提交内容时：**
1. 用户填写图片URL（每行一个）
2. 提交内容后，前端自动创建图片记录
3. 第一张图片标记为封面（imageType='cover'）
4. 其他图片标记为详情（imageType='detail'）

**实现：**
- `Submit.vue` - 用户提交页面
- `createImages()` - 前端方法，自动创建图片记录

### 6.3 图片展示

**详情页面：**
- 使用 Element UI 的 `el-carousel` 组件
- 支持图片轮播展示
- 如果没有多图片，自动使用封面图

### 6.4 管理员图片管理

**API接口：**
- `POST /admin/{module}-images` - 创建图片
- `PUT /admin/{module}-images/{id}` - 更新图片
- `DELETE /admin/{module}-images/{id}` - 删除图片

---

## 七、内容审核模块

### 7.1 用户提交

**流程：**
1. 用户登录后进入"提交内容"页面
2. 选择内容类型（景点/美食/文化/攻略）
3. 填写内容信息（包括多图片）
4. 提交后状态为"待审核"（status=0）

**实现：**
- `Submit.vue` - 用户提交页面
- `POST /{module}/submit` - 提交接口

### 7.2 管理员审核

**流程：**
1. 管理员进入"内容审核"页面
2. 查看待审核内容列表
3. 点击"通过"或"拒绝"
4. 状态更新为"已通过"（status=1）或"已拒绝"（status=2）

**实现：**
- `Audit.vue` - 管理员审核页面
- `PUT /admin/{module}/{id}/audit?status=1` - 审核接口

### 7.3 内容状态

- 0: 待审核
- 1: 已通过（用户可见）
- 2: 已拒绝

**查询逻辑：**
- 用户端查询只返回 status=1 的内容
- 管理员可以查看所有状态的内容

---

## 八、管理员模块

### 8.1 用户管理

**功能：**
- 用户列表查询（支持关键词、角色筛选）
- 修改用户角色
- 修改用户状态

### 8.2 内容管理

**功能：**
- 各模块的CRUD操作
- 内容审核
- 图片管理

### 8.3 订单管理

**功能：**
- 订单列表查询
- 订单详情查看

### 8.4 公告管理

**功能：**
- 创建公告
- 更新公告
- 删除公告
- 支持分类、状态筛选

---

## 九、浏览记录模块

### 9.1 添加浏览记录

**功能：**
- 用户查看详情时自动添加浏览记录
- 记录用户ID、目标类型、目标ID、浏览时间

### 9.2 浏览记录列表

**功能：**
- 查看用户的浏览历史
- 按时间倒序排列

---

## 十、推荐模块

### 10.1 热门推荐

**功能：**
- 根据浏览次数、评论数、收藏数推荐
- 支持推荐标记（isRecommend）

---

## 十一、安全与权限

### 11.1 JWT认证

**实现：**
- `JwtUtil` - JWT工具类
- `JwtAuthenticationFilter` - JWT认证过滤器
- Token存储在请求头：`Authorization: Bearer {token}`

### 11.2 角色权限

**角色：**
- USER: 普通用户
- ADMIN: 管理员

**权限控制：**
- Spring Security `@PreAuthorize` 注解
- 前端路由守卫（`router.beforeEach`）

### 11.3 路由守卫

**前端实现：**
- `requiresAuth: true` - 需要登录
- `requiresAdmin: true` - 需要管理员权限
- 自动跳转到登录页或首页

---

## 十二、技术实现细节

### 12.1 后端架构

**分层结构：**
- Controller层：处理HTTP请求
- Service层：业务逻辑处理
- Mapper层：数据库操作（MyBatis Plus）
- Entity层：实体类
- DTO层：数据传输对象
- VO层：视图对象

**技术栈：**
- Spring Boot 2.7.x
- MyBatis Plus 3.5.x
- Spring Security + JWT
- MySQL 8.0+
- Swagger/Knife4j

### 12.2 前端架构

**技术栈：**
- Vue 2.6.x
- Element UI 2.15.x
- Vue Router 3.x
- Vuex 3.x
- Axios 1.11.x

**组件结构：**
- `views/` - 页面组件
- `components/` - 公共组件
- `api/` - API接口封装
- `store/` - 状态管理
- `router/` - 路由配置

### 12.3 数据库设计

**主要表：**
- `user` - 用户表
- `attraction` - 景点表
- `food` - 美食表
- `culture` - 文化表
- `strategy` - 攻略表
- `experience` - 体验项目表
- `hotel` - 酒店表
- `hotel_room` - 酒店房间表
- `order` - 订单表
- `order_item` - 订单项表
- `comment` - 评论表
- `comment_reply` - 评论回复表
- `favorite` - 收藏表
- `{module}_image` - 各模块图片表

---

## 十三、逻辑闭环检查

### ✅ 已完善的逻辑

1. **订单流程闭环**
   - 创建订单 → 检查库存
   - 支付订单 → 扣减库存
   - 取消订单 → 恢复库存

2. **内容审核闭环**
   - 用户提交 → 待审核
   - 管理员审核 → 已通过/已拒绝
   - 用户端只显示已通过内容

3. **评论闭环**
   - 添加评论 → 更新评论数和评分
   - 删除评论 → 减少评论数和评分

4. **收藏闭环**
   - 添加收藏 → 更新收藏数
   - 取消收藏 → 减少收藏数

5. **图片管理闭环**
   - 用户提交 → 自动创建图片记录
   - 详情页面 → 图片轮播展示

### ⚠️ 可优化的地方

1. **级联删除**
   - 删除内容时，可以考虑级联删除关联的图片（可选）

2. **订单超时取消**
   - 可以添加定时任务，自动取消超时的待支付订单

3. **库存预占**
   - 创建订单时可以预占库存，支付时确认，超时释放

---

## 十四、API接口总结

### 用户相关
- `POST /user/register` - 注册
- `POST /user/login` - 登录
- `GET /user/info` - 获取用户信息
- `PUT /user/info` - 更新用户信息

### 内容相关
- `GET /{module}` - 获取列表
- `GET /{module}/{id}` - 获取详情
- `POST /{module}/submit` - 用户提交
- `GET /{module}/{id}/images` - 获取图片列表

### 订单相关
- `POST /orders` - 创建订单
- `GET /orders` - 获取订单列表
- `GET /orders/{id}` - 获取订单详情
- `PUT /orders/{id}/cancel` - 取消订单
- `POST /orders/{id}/pay` - 支付订单

### 评论相关
- `POST /comments` - 添加评论
- `GET /comments` - 获取评论列表
- `DELETE /comments/{id}` - 删除评论
- `POST /comment-replies` - 添加回复

### 收藏相关
- `POST /favorites` - 添加收藏
- `DELETE /favorites` - 取消收藏
- `GET /favorites` - 获取收藏列表
- `GET /favorites/check` - 检查收藏状态

### 管理员相关
- `GET /admin/{module}` - 获取列表
- `POST /admin/{module}` - 创建
- `PUT /admin/{module}/{id}` - 更新
- `DELETE /admin/{module}/{id}` - 删除
- `PUT /admin/{module}/{id}/audit` - 审核

---

## 总结

本项目实现了完整的文旅宣传平台功能，包括：
- ✅ 用户注册登录
- ✅ 内容管理（景点、美食、文化、攻略、体验项目、酒店）
- ✅ 订单管理（创建、支付、取消、库存管理）
- ✅ 评论系统
- ✅ 收藏功能
- ✅ 多图片支持
- ✅ 内容审核流程
- ✅ 管理员后台
- ✅ 权限控制

所有核心功能已实现逻辑闭环，可以正常运行。

