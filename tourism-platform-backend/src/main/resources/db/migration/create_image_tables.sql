-- 创建图片表SQL脚本
-- 用于支持文化、攻略、体验项目、酒店的多图片功能

-- ==================== 文化图片表 ====================
CREATE TABLE IF NOT EXISTS `culture_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `culture_id` BIGINT NOT NULL COMMENT '文化ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `image_type` VARCHAR(20) DEFAULT 'detail' COMMENT '图片类型：cover-封面，detail-详情',
  `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_culture_id` (`culture_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文化图片表';

-- ==================== 攻略图片表 ====================
CREATE TABLE IF NOT EXISTS `strategy_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `strategy_id` BIGINT NOT NULL COMMENT '攻略ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `image_type` VARCHAR(20) DEFAULT 'detail' COMMENT '图片类型：cover-封面，detail-详情',
  `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_id` (`strategy_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='攻略图片表';

-- ==================== 体验项目图片表 ====================
CREATE TABLE IF NOT EXISTS `experience_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `experience_id` BIGINT NOT NULL COMMENT '体验项目ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `image_type` VARCHAR(20) DEFAULT 'detail' COMMENT '图片类型：cover-封面，detail-详情',
  `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_experience_id` (`experience_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='体验项目图片表';

-- ==================== 酒店图片表 ====================
CREATE TABLE IF NOT EXISTS `hotel_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `hotel_id` BIGINT NOT NULL COMMENT '酒店ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `image_type` VARCHAR(20) DEFAULT 'detail' COMMENT '图片类型：cover-封面，room-房间，facility-设施，environment-环境',
  `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_hotel_id` (`hotel_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='酒店图片表';

