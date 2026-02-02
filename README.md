# 家乡文旅宣传平台

## 项目简介

基于 Spring Boot + Vue.js 的前后端分离架构的家乡文旅宣传平台，提供景点展示、美食推荐、民俗文化、旅游攻略、在线预订、地图导航、AI智能助手等功能。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.x
- **ORM**: MyBatis Plus 3.5.x
- **数据库**: MySQL 8.0+
- **缓存**: Redis（可选）
- **安全**: Spring Security + JWT
- **文档**: Swagger/Knife4j
- **工具**: Lombok, Hutool

### 前端
- **框架**: Vue 2.6.x
- **UI组件**: Element UI 2.15.x
- **路由**: Vue Router 3.x
- **状态管理**: Vuex 3.x
- **HTTP客户端**: Axios 1.11.x
- **地图**: 高德地图 API

## 项目结构

```
tourism/
├── tourism-platform-backend/     # 后端项目
│   ├── src/main/java/com/tourism/
│   │   ├── TourismApplication.java
│   │   ├── config/               # 配置类
│   │   ├── controller/           # 控制器层
│   │   ├── service/              # 服务层
│   │   ├── mapper/               # Mapper接口
│   │   ├── entity/               # 实体类
│   │   └── utils/                # 工具类
│   └── src/main/resources/
│       └── application.yml        # 配置文件
├── tourism-platform-frontend/     # 前端项目
│   ├── src/
│   │   ├── api/                  # API接口
│   │   ├── components/           # 公共组件
│   │   ├── router/               # 路由配置
│   │   ├── store/                # Vuex状态管理
│   │   └── views/                # 页面组件
│   └── .env.development          # 开发环境配置
├── insert_sample_data.py         # 示例数据插入脚本
├── requirements.txt              # Python依赖
└── README.md                     # 项目文档（本文件）
```

## 功能模块

### 用户端功能
1. **用户模块**
   - 用户注册、登录
   - 个人中心、信息修改
   - JWT Token认证

2. **景点模块**
   - 景点列表（分页、分类筛选）
   - 景点详情、图片展示
   - 景点搜索
   - 用户提交景点（待审核）
   - 景点收藏

3. **美食模块**
   - 美食列表（分页、分类筛选）
   - 美食详情、制作方法展示
   - 美食搜索
   - 推荐餐厅信息（含地址、经纬度）
   - 用户提交美食（待审核）
   - 美食收藏

4. **文化模块**
   - 文化列表（分页、分类筛选）
   - 文化详情、历史介绍
   - 活动地点、活动时间
   - 用户提交文化（待审核）
   - 文化收藏

5. **攻略模块**
   - 攻略列表（分类、主题筛选）
   - 攻略详情、路线展示
   - AI智能生成攻略
   - 用户提交攻略（待审核）
   - 攻略收藏

6. **酒店模块**
   - 酒店列表（分页、搜索）
   - 酒店详情、图片展示
   - 房型列表、价格展示
   - 房间可用性查询
   - 酒店收藏
   - 最低价格自动计算

7. **体验项目模块**
   - 体验项目列表（分页、搜索）
   - 体验项目详情、图片展示
   - 体验项目收藏

8. **订单模块**
   - 创建订单（酒店、体验项目）
   - 订单列表（按状态筛选）
   - 订单详情（含订单项）
   - 订单取消
   - 订单支付

9. **评论模块**
   - 添加评论、评分
   - 评论列表
   - 评论回复
   - 删除自己的评论

10. **收藏模块**
    - 添加收藏
    - 取消收藏
    - 收藏列表（按类型筛选）
    - 检查收藏状态

11. **浏览记录模块**
    - 自动记录浏览历史
    - 浏览记录列表

12. **地图模块**
    - 地图展示（景点、餐厅、酒店、文化）
    - 分类筛选
    - 地点搜索
    - 路线规划（驾车、步行、公交）
    - 当前位置定位
    - 信息窗口展示
    - 详情页跳转

13. **公告模块**
    - 公告列表
    - 公告详情
    - 轮播图展示

14. **推荐模块**
    - 热门推荐
    - 个性化推荐

15. **AI助手模块**
    - 浮窗式对话助手
    - 智能问答（景点、美食、酒店、攻略查询）
    - 网站数据摘要
    - 攻略生成入口

16. **文件上传模块**
    - 图片上传
    - 视频上传

### 管理员功能
1. **用户管理**
   - 用户列表（搜索、角色筛选）
   - 修改用户角色
   - 修改用户状态
   - 删除用户（级联删除相关数据）

2. **内容管理**
   - 景点管理（创建、更新、删除、审核）
   - 美食管理（创建、更新、删除、审核）
   - 文化管理（创建、更新、删除、审核）
   - 攻略管理（创建、更新、删除、审核）
   - 酒店管理（创建、更新、删除）
   - 酒店房型管理（创建、更新、删除）
   - 体验项目管理（创建、更新、删除）
   - 公告管理（创建、更新、删除、列表）

3. **审核管理**
   - 待审核景点列表
   - 待审核美食列表
   - 待审核文化列表
   - 待审核攻略列表
   - 审核通过/拒绝
   - 审核时自动获取经纬度（景点、美食、文化）

4. **图片管理**
   - 景点图片管理（创建、更新、删除）
   - 美食图片管理（创建、更新、删除）
   - 文化图片管理（创建、更新、删除）
   - 攻略图片管理（创建、更新、删除）
   - 体验项目图片管理（创建、更新、删除）
   - 酒店图片管理（创建、更新、删除）

5. **订单管理**
   - 订单列表（搜索、状态筛选）
   - 订单详情

6. **数据统计**
   - 用户统计（总数、管理员数）
   - 内容统计（景点数、美食数）
   - 订单统计
   - 待审核数量统计

7. **分类管理**
   - 景点分类列表
   - 美食分类列表
   - 文化分类列表

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 14+
- MySQL 8.0+
- Redis 6.0+（可选）

### 后端启动

1. **数据库配置**
   ```bash
   # 创建数据库
   CREATE DATABASE tourism_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # 执行SQL脚本初始化表结构
   # （SQL脚本位置：tourism-platform-backend/src/main/resources/db/migration/）
   ```

2. **修改配置**
   编辑 `tourism-platform-backend/src/main/resources/application.yml`：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/tourism_platform
       username: root
       password: your_password
   ```

3. **运行项目**
   ```bash
   cd tourism-platform-backend
   mvn spring-boot:run
   ```
   
   访问：http://localhost:8080/api/doc.html（API文档）

### 前端启动

1. **安装依赖**
   ```bash
   cd tourism-platform-frontend
   npm install
   ```

2. **配置环境变量**
   创建 `.env.development` 文件：
   ```env
   VUE_APP_BASE_API=http://localhost:8080/api
   VUE_APP_TITLE=家乡文旅宣传平台
   VUE_APP_MAP_KEY=你的高德地图API密钥
   ```

3. **运行项目**
   ```bash
   npm run serve
   ```
   
   访问：http://localhost:8081

### 初始化数据

使用Python脚本插入示例数据：
```bash
# 安装Python依赖
pip install -r requirements.txt

# 修改 insert_sample_data.py 中的数据库配置
# 运行脚本
python insert_sample_data.py
```

## 配置说明

详细配置说明请参考：
- [配置指南.md](./配置指南.md) - 包含AI、地图、API密钥等所有配置说明

## 开发指南

开发相关文档：
- [开发指南.md](./开发指南.md) - 包含地图集成、数据库、路径迁移等开发说明
- [数据脚本使用说明.md](./数据脚本使用说明.md) - 数据插入脚本使用说明

## API接口

### 基础URL
```
http://localhost:8080/api
```

### 认证方式
使用JWT Token认证，请求头格式：
```
Authorization: Bearer {token}
```

### 统一响应格式
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1640995200000
}
```

## 注意事项

1. **文件上传路径**: 默认使用相对路径 `./uploads/`，系统会自动解析为绝对路径
2. **数据库字符集**: 使用 utf8mb4 字符集以支持emoji
3. **Token过期**: 默认Token有效期为24小时
4. **跨域配置**: 已在后端配置CORS
5. **地图API密钥**: 前端和后端需要分别配置不同类型的Key（详见配置指南）

## 开发规范

### 后端
- 遵循阿里巴巴Java开发手册
- Controller、Service、Mapper命名规范
- 统一异常处理机制
- 关键操作记录日志

### 前端
- 组件命名使用 PascalCase
- 文件命名使用 kebab-case
- API接口统一返回 Result 格式
- 使用 Element UI 组件库

## 常见问题

### 1. 跨域问题
开发环境已配置代理，如果仍有问题，检查 `vue.config.js` 中的代理配置。

### 2. Token过期
Token过期会自动跳转到登录页面，需要重新登录。

### 3. 地图加载失败
检查 `.env.development` 文件中的 `VUE_APP_MAP_KEY` 是否正确配置，参考[配置指南.md](./配置指南.md)。

### 4. 数据库连接失败
检查 `application.yml` 中的数据库配置是否正确，确保数据库服务已启动。

## 许可证

MIT License
