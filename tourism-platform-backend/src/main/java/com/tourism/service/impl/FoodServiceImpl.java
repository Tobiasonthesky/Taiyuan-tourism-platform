package com.tourism.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.Food;
import com.tourism.entity.FoodCategory;
import com.tourism.mapper.FoodMapper;
import com.tourism.mapper.FoodCategoryMapper;
import com.tourism.service.FoodService;
import com.tourism.utils.PageUtil;
import com.tourism.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 美食服务实现类
 */
@Service
public class FoodServiceImpl implements FoodService {
    
    @Autowired
    private FoodMapper foodMapper;
    
    @Autowired
    private FoodCategoryMapper categoryMapper;
    
    @Override
    public PageVO<Food> getFoodList(Long categoryId, String keyword, Integer page, Integer size) {
        Page<Food> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        
        if (categoryId != null) {
            wrapper.eq(Food::getCategoryId, categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Food::getName, keyword)
                    .or().like(Food::getDescription, keyword));
        }
        wrapper.eq(Food::getStatus, 1);
        wrapper.orderByDesc(Food::getSortOrder);
        wrapper.orderByDesc(Food::getCreateTime);
        
        Page<Food> result = foodMapper.selectPage(pageParam, wrapper);
        
        // 解析recommendedRestaurants字段，填充restaurant和address字段
        List<Food> records = result.getRecords();
        for (Food food : records) {
            parseRecommendedRestaurants(food);
            fillCategoryName(food);
        }
        
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                records
        );
    }
    
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public Food getFoodDetail(Long id) {
        Food food = foodMapper.selectById(id);
        if (food == null || food.getStatus() != 1) {
            throw new com.tourism.exception.BusinessException("美食不存在");
        }
        
        // 增加浏览次数
        food.setViewCount((food.getViewCount() == null ? 0 : food.getViewCount()) + 1);
        foodMapper.updateById(food);
        
        // 解析recommendedRestaurants字段
        parseRecommendedRestaurants(food);
        
        // 填充分类名称
        fillCategoryName(food);
        
        return food;
    }
    
    @Override
    public PageVO<Food> searchFoods(String keyword, Integer page, Integer size) {
        Page<Food> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.like(Food::getName, keyword)
                .or().like(Food::getDescription, keyword));
        wrapper.eq(Food::getStatus, 1);
        wrapper.orderByDesc(Food::getCreateTime);
        
        Page<Food> result = foodMapper.selectPage(pageParam, wrapper);
        
        // 解析recommendedRestaurants字段，填充restaurant和address字段
        List<Food> records = result.getRecords();
        for (Food food : records) {
            parseRecommendedRestaurants(food);
            fillCategoryName(food);
        }
        
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                records
        );
    }
    
    /**
     * 填充分类名称
     */
    private void fillCategoryName(Food food) {
        if (food != null && food.getCategoryId() != null) {
            FoodCategory category = categoryMapper.selectById(food.getCategoryId());
            if (category != null) {
                food.setCategoryName(category.getName());
            }
        }
    }
    
    /**
     * 从recommendedRestaurants JSON中解析出restaurant和address字段
     */
    private void parseRecommendedRestaurants(Food food) {
        if (food.getRecommendedRestaurants() != null && !food.getRecommendedRestaurants().isEmpty()) {
            try {
                System.out.println("解析recommendedRestaurants - 原始JSON: " + food.getRecommendedRestaurants());
                JSONArray restaurants = JSON.parseArray(food.getRecommendedRestaurants());
                System.out.println("解析recommendedRestaurants - 解析后的数组大小: " + (restaurants != null ? restaurants.size() : 0));
                if (restaurants != null && restaurants.size() > 0) {
                    JSONObject firstRestaurant = restaurants.getJSONObject(0);
                    String restaurantName = firstRestaurant.getString("name");
                    String address = firstRestaurant.getString("address");
                    System.out.println("解析recommendedRestaurants - restaurant: " + restaurantName + ", address: " + address);
                    food.setRestaurant(restaurantName);
                    food.setAddress(address);
                    if (firstRestaurant.containsKey("longitude")) {
                        food.setLongitude(firstRestaurant.getBigDecimal("longitude"));
                    }
                    if (firstRestaurant.containsKey("latitude")) {
                        food.setLatitude(firstRestaurant.getBigDecimal("latitude"));
                    }
                } else {
                    System.out.println("解析recommendedRestaurants - 数组为空或null");
                }
            } catch (Exception e) {
                System.err.println("解析recommendedRestaurants失败: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("解析recommendedRestaurants - recommendedRestaurants字段为空或null");
        }
    }
}

