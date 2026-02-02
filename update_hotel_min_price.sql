-- 批量更新酒店最低价格脚本
-- 根据房型的最低价格更新酒店的最低价格

-- 方法1：使用子查询更新（推荐）
UPDATE hotel h
SET min_price = (
    SELECT MIN(price)
    FROM hotel_room hr
    WHERE hr.hotel_id = h.id
      AND hr.status = 1
      AND hr.price IS NOT NULL
)
WHERE EXISTS (
    SELECT 1
    FROM hotel_room hr
    WHERE hr.hotel_id = h.id
      AND hr.status = 1
      AND hr.price IS NOT NULL
);

-- 方法2：如果没有上架房型的酒店，将最低价格设为NULL
UPDATE hotel h
SET min_price = NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM hotel_room hr
    WHERE hr.hotel_id = h.id
      AND hr.status = 1
      AND hr.price IS NOT NULL
);

-- 验证更新结果
SELECT 
    h.id,
    h.name,
    h.min_price AS hotel_min_price,
    MIN(hr.price) AS calculated_min_price,
    CASE 
        WHEN h.min_price = MIN(hr.price) THEN '✓ 一致'
        ELSE '✗ 不一致'
    END AS status
FROM hotel h
LEFT JOIN hotel_room hr ON hr.hotel_id = h.id AND hr.status = 1 AND hr.price IS NOT NULL
WHERE h.status = 1
GROUP BY h.id, h.name, h.min_price
ORDER BY h.id;
