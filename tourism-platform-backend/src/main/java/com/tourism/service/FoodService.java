package com.tourism.service;

import com.tourism.entity.Food;
import com.tourism.vo.PageVO;

/**
 * 美食服务接口
 */
public interface FoodService {
    
    /**
     * 获取美食列表
     */
    PageVO<Food> getFoodList(Long categoryId, String keyword, Integer page, Integer size);
    
    /**
     * 获取美食详情
     */
    Food getFoodDetail(Long id);
    
    /**
     * 搜索美食
     */
    PageVO<Food> searchFoods(String keyword, Integer page, Integer size);
}

