-- 创建餐厅表SQL脚本
-- 用于独立管理餐厅信息，支持地图展示

-- ==================== 餐厅表 ====================
CREATE TABLE IF NOT EXISTS `restaurant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '餐厅ID',
  `name` VARCHAR(100) NOT NULL COMMENT '餐厅名称',
  `description` TEXT COMMENT '餐厅描述',
  `cover_image` VARCHAR(500) COMMENT '封面图片',
  `address` VARCHAR(200) COMMENT '地址',
  `longitude` DECIMAL(10, 7) COMMENT '经度',
  `latitude` DECIMAL(10, 7) COMMENT '纬度',
  `phone` VARCHAR(20) COMMENT '联系电话',
  `rating` DECIMAL(3, 2) DEFAULT 0.00 COMMENT '评分（0-5）',
  `price_range` VARCHAR(20) COMMENT '价格区间（如：50-100元）',
  `cuisine_type` VARCHAR(50) COMMENT '菜系类型',
  `opening_hours` VARCHAR(100) COMMENT '营业时间',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `comment_count` INT DEFAULT 0 COMMENT '评论数',
  `favorite_count` INT DEFAULT 0 COMMENT '收藏数',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_longitude_latitude` (`longitude`, `latitude`),
  KEY `idx_status` (`status`),
  KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='餐厅表';

-- ==================== 美食-餐厅关联表（可选）====================
-- 如果保留recommendedRestaurants字段，此表可选
-- 如果使用独立餐厅表，建议创建此关联表
CREATE TABLE IF NOT EXISTS `food_restaurant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `food_id` BIGINT NOT NULL COMMENT '美食ID',
  `restaurant_id` BIGINT NOT NULL COMMENT '餐厅ID',
  `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_food_id` (`food_id`),
  KEY `idx_restaurant_id` (`restaurant_id`),
  UNIQUE KEY `uk_food_restaurant` (`food_id`, `restaurant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美食-餐厅关联表';
