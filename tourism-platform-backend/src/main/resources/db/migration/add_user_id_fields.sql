-- 为景点表添加user_id字段
ALTER TABLE `attraction` 
ADD COLUMN `user_id` BIGINT NULL COMMENT '创建用户ID（可为空，系统创建）' AFTER `category_id`;

-- 为美食表添加user_id字段
ALTER TABLE `food` 
ADD COLUMN `user_id` BIGINT NULL COMMENT '创建用户ID（可为空，系统创建）' AFTER `category_id`;

-- 为文化表添加user_id字段
ALTER TABLE `culture` 
ADD COLUMN `user_id` BIGINT NULL COMMENT '创建用户ID（可为空，系统创建）' AFTER `category_id`;

-- 注意：strategy表已经有user_id字段，不需要添加

-- 更新现有数据的status字段说明（如果需要的话，可以添加注释）
-- ALTER TABLE `attraction` MODIFY COLUMN `status` INT NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已通过，2-已拒绝';
-- ALTER TABLE `food` MODIFY COLUMN `status` INT NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已通过，2-已拒绝';
-- ALTER TABLE `culture` MODIFY COLUMN `status` INT NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已通过，2-已拒绝';
-- ALTER TABLE `strategy` MODIFY COLUMN `status` INT NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已通过，2-已拒绝';

