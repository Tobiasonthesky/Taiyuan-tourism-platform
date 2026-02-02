-- 更新地图坐标数据SQL脚本
-- 根据地址信息补充经纬度坐标（太原市坐标示例）

-- ==================== 更新景点坐标 ====================
-- 双塔博物馆（迎泽区）- 太原市迎泽区双塔寺
UPDATE `attraction` 
SET `longitude` = 112.549248, 
    `latitude` = 37.857014
WHERE `id` = 2 AND `name` = '双塔博物馆';

-- 注意：其他景点的坐标需要根据实际地址查询补充
-- 可以使用高德地图API或手动查询获取

-- ==================== 更新酒店坐标 ====================
-- 山西国贸大饭店（太原市府西街）
UPDATE `hotel` 
SET `longitude` = 112.548248, 
    `latitude` = 37.870014
WHERE `id` = 2 AND `name` = '山西国贸大饭店';

-- ==================== 更新体验项目坐标 ====================
-- 传统手工艺体验（文化街123号）
UPDATE `experience` 
SET `longitude` = 112.550248, 
    `latitude` = 37.860014
WHERE `id` = 1 AND `name` = '传统手工艺体验';

-- 特色美食制作（美食街88号）
UPDATE `experience` 
SET `longitude` = 112.551248, 
    `latitude` = 37.861014
WHERE `id` = 3 AND `name` = '特色美食制作';

-- ==================== 更新美食推荐餐厅 ====================
-- 示例：为美食添加推荐餐厅JSON数据
-- 格式：[{"name":"餐厅名称","longitude":112.549248,"latitude":37.857014,"address":"餐厅地址","image":"图片URL"}]
UPDATE `food` 
SET `recommended_restaurants` = '[{"name":"老太原餐厅","longitude":112.549248,"latitude":37.857014,"address":"太原市迎泽区","image":"https://images.pexels.com/photos/35175266/pexels-photo-35175266.jpeg"}]'
WHERE `id` = 1;

-- ==================== 说明 ====================
-- 1. 以上坐标为示例坐标，实际使用时需要根据真实地址查询
-- 2. 可以使用高德地图API的地理编码服务获取坐标
-- 3. 或者访问 https://lbs.amap.com/tools/picker 手动选择坐标
-- 4. 太原市中心坐标约为：经度112.549248，纬度37.857014
