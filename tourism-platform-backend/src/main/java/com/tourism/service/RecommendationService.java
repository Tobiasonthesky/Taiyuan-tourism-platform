package com.tourism.service;

import com.tourism.entity.Attraction;
import com.tourism.entity.Food;
import com.tourism.entity.Strategy;

import java.util.List;
import java.util.Map;

/**
 * 推荐服务接口
 */
public interface RecommendationService {
    
    /**
     * 获取热门推荐
     */
    Map<String, ?> getHotRecommendations(String type, Integer limit);
    
    /**
     * 获取个性化推荐（基于用户浏览历史和收藏）
     */
    Map<String, ?> getPersonalizedRecommendations(Long userId, Integer limit);
    
    /**
     * 基于浏览历史的推荐
     */
    List<Attraction> recommendByBrowseHistory(Long userId, Integer limit);
    
    /**
     * 基于收藏的推荐
     */
    List<Food> recommendByFavorites(Long userId, Integer limit);
}

