-- 为文化表添加地理位置字段
-- 用于存储活动地点的经纬度坐标，支持地图功能

-- 添加经度字段
ALTER TABLE `culture` 
ADD COLUMN `longitude` DECIMAL(10, 7) NULL COMMENT '活动地点经度' AFTER `activity_location`;

-- 添加纬度字段
ALTER TABLE `culture` 
ADD COLUMN `latitude` DECIMAL(10, 7) NULL COMMENT '活动地点纬度' AFTER `longitude`;

-- 说明：
-- 1. longitude 和 latitude 字段为可选字段，允许为 NULL
-- 2. DECIMAL(10, 7) 格式可以精确存储经纬度坐标
-- 3. 字段位置在 activity_location 之后，便于理解字段关系
