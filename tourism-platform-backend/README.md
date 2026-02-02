# 家乡文旅宣传平台 - 后端项目

## 项目简介

基于Spring Boot的家乡文旅宣传平台后端服务，提供景点展示、美食推荐、民俗文化、旅游攻略、在线预订等功能的API接口。

## 技术栈

- **框架**: Spring Boot 2.7.x
- **ORM**: MyBatis Plus 3.5.x
- **数据库**: MySQL 8.0+
- **缓存**: Redis（可选）
- **安全**: Spring Security + JWT
- **文档**: Swagger/Knife4j
- **工具**: Lombok, Hutool

## 项目结构

```
tourism-platform-backend/
├── src/main/java/com/tourism/
│   ├── TourismApplication.java      # 启动类
│   ├── config/                      # 配置类
│   ├── controller/                  # 控制器层
│   ├── service/                     # 服务层
│   │   └── impl/                   # 服务实现
│   ├── mapper/                      # Mapper接口
│   ├── entity/                      # 实体类
│   ├── dto/                         # 数据传输对象
│   ├── vo/                          # 视图对象
│   ├── utils/                       # 工具类
│   ├── exception/                   # 异常处理
│   └── security/                    # 安全配置
└── src/main/resources/
    └── application.yml              # 配置文件
```

## 功能模块

### 1. 用户模块
- 用户注册/登录
- 用户信息管理
- JWT认证

### 2. 景点模块
- 景点列表（分页、分类、搜索）
- 景点详情
- 景点图片管理
- 景点分类管理

### 3. 美食模块
- 美食列表
- 美食详情
- 美食图片管理
- 美食分类管理

### 4. 文化模块
- 文化列表
- 文化详情
- 文化分类管理

### 5. 攻略模块
- 攻略列表
- 攻略详情
- 攻略路线管理

### 6. 评论模块
- 添加评论
- 评论列表
- 评论回复
- 删除评论

### 7. 收藏模块
- 添加收藏
- 取消收藏
- 收藏列表
- 检查收藏状态

### 8. 订单模块
- 创建订单
- 订单列表
- 订单详情（含订单项）
- 取消订单
- 支付订单

### 9. 公告模块
- 公告列表
- 公告详情
- 轮播图

### 10. 浏览记录模块
- 添加浏览记录
- 浏览记录列表

### 11. 文件上传模块
- 图片上传
- 视频上传

### 12. 地图模块
- 景点地图数据

### 13. 推荐模块
- 热门推荐

## 快速开始

### 1. 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+（可选）

### 2. 数据库配置
1. 创建数据库：`tourism_platform`
2. 执行SQL脚本：`数据库SQL脚本.sql`
3. 修改 `application.yml` 中的数据库连接信息

### 3. 运行项目
```bash
# 方式1：使用Maven
mvn spring-boot:run

# 方式2：打包后运行
mvn clean package
java -jar target/tourism-platform-backend-0.0.1-SNAPSHOT.jar
```

### 4. 访问API文档
启动后访问：http://localhost:8080/api/doc.html

## API接口说明

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

## 配置说明

### application.yml 主要配置项

- **数据库连接**: 修改 `spring.datasource` 配置
- **JWT密钥**: 修改 `jwt.secret` 和 `jwt.expiration`
- **文件上传路径**: 修改 `file.upload.path` 和 `file.upload.max-size`
- **Redis配置**: 修改 `spring.redis` 配置（如使用Redis）

## 注意事项

1. **文件上传路径**: 确保配置的文件上传路径存在且有写权限
2. **数据库字符集**: 使用 utf8mb4 字符集以支持emoji
3. **Token过期**: 默认Token有效期为24小时
4. **跨域配置**: 已在 `WebMvcConfig` 中配置CORS

## 开发规范

1. **代码规范**: 遵循阿里巴巴Java开发手册
2. **命名规范**: 
   - Controller: 以Controller结尾
   - Service: 以Service结尾，实现类以ServiceImpl结尾
   - Mapper: 以Mapper结尾
   - Entity: 实体类名与数据库表名对应
3. **异常处理**: 使用统一的异常处理机制
4. **日志记录**: 关键操作记录日志

## 后续优化

1. 完善订单价格计算逻辑
2. 实现个性化推荐算法
3. 添加缓存机制
4. 性能优化
5. 添加单元测试
6. 完善API文档

## 许可证

MIT License

