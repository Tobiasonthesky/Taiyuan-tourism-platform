package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.Attraction;
import com.tourism.entity.BrowseHistory;
import com.tourism.entity.Food;
import com.tourism.entity.Favorite;
import com.tourism.entity.Strategy;
import com.tourism.entity.Hotel;
import com.tourism.entity.Experience;
import com.tourism.entity.Culture;
import com.tourism.mapper.AttractionMapper;
import com.tourism.mapper.BrowseHistoryMapper;
import com.tourism.mapper.FavoriteMapper;
import com.tourism.mapper.FoodMapper;
import com.tourism.mapper.StrategyMapper;
import com.tourism.mapper.ExperienceMapper;
import com.tourism.mapper.HotelMapper;
import com.tourism.mapper.CultureMapper;
import com.tourism.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 推荐服务实现类
 */
@Service
public class RecommendationServiceImpl implements RecommendationService {
    
    @Autowired
    private AttractionMapper attractionMapper;
    
    @Autowired
    private FoodMapper foodMapper;
    
    @Autowired
    private StrategyMapper strategyMapper;
    
    @Autowired
    private HotelMapper hotelMapper;
    
    @Autowired
    private ExperienceMapper experienceMapper;
    
    @Autowired
    private CultureMapper cultureMapper;
    
    @Autowired
    private BrowseHistoryMapper browseHistoryMapper;
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Override
    public Map<String, ?> getHotRecommendations(String type, Integer limit) {
        Map<String, List<?>> result = new HashMap<>();
        
        if (type == null || "attraction".equals(type)) {
            LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Attraction::getStatus, 1);
            wrapper.orderByDesc(Attraction::getViewCount);
            wrapper.orderByDesc(Attraction::getRating);
            wrapper.last("LIMIT " + limit);
            List<Attraction> attractions = attractionMapper.selectList(wrapper);
            result.put("attractions", attractions);
        }
        
        if (type == null || "food".equals(type)) {
            LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Food::getStatus, 1);
            wrapper.orderByDesc(Food::getViewCount);
            wrapper.orderByDesc(Food::getRating);
            wrapper.last("LIMIT " + limit);
            List<Food> foods = foodMapper.selectList(wrapper);
            result.put("foods", foods);
        }
        
        if (type == null || "strategy".equals(type)) {
            LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Strategy::getStatus, 1);
            wrapper.eq(Strategy::getIsRecommend, 1);
            wrapper.orderByDesc(Strategy::getViewCount);
            wrapper.last("LIMIT " + limit);
            List<Strategy> strategies = strategyMapper.selectList(wrapper);
            result.put("strategies", strategies);
        }
        
        if (type == null || "hotel".equals(type)) {
            LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Hotel::getStatus, 1);
            wrapper.orderByDesc(Hotel::getRating);
            wrapper.orderByDesc(Hotel::getViewCount);
            wrapper.last("LIMIT " + limit);
            List<Hotel> hotels = hotelMapper.selectList(wrapper);
            result.put("hotels", hotels);
        }
        
        if (type == null || "experience".equals(type)) {
            LambdaQueryWrapper<Experience> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Experience::getStatus, 1);
            wrapper.orderByDesc(Experience::getRating);
            wrapper.orderByDesc(Experience::getViewCount);
            wrapper.last("LIMIT " + limit);
            List<Experience> experiences = experienceMapper.selectList(wrapper);
            result.put("experiences", experiences);
        }
        
        if (type == null || "culture".equals(type)) {
            LambdaQueryWrapper<Culture> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Culture::getStatus, 1);
            wrapper.orderByDesc(Culture::getRating);
            wrapper.orderByDesc(Culture::getViewCount);
            wrapper.last("LIMIT " + limit);
            List<Culture> cultures = cultureMapper.selectList(wrapper);
            result.put("cultures", cultures);
        }
        
        return result;
    }
    
    @Override
    public Map<String, ?> getPersonalizedRecommendations(Long userId, Integer limit) {
        Map<String, List<?>> result = new HashMap<>();
        
        // 基于浏览历史的推荐
        List<Attraction> attractions = recommendByBrowseHistory(userId, limit);
        result.put("attractions", attractions);
        
        // 基于收藏的推荐
        List<Food> foods = recommendByFavorites(userId, limit);
        result.put("foods", foods);
        
        // 推荐攻略
        List<Strategy> strategies = recommendStrategies(userId, limit);
        result.put("strategies", strategies);
        
        // 推荐酒店
        List<Hotel> hotels = recommendHotels(userId, limit);
        result.put("hotels", hotels);
        
        // 推荐体验项目
        List<Experience> experiences = recommendExperiences(userId, limit);
        result.put("experiences", experiences);
        
        // 推荐文化
        List<Culture> cultures = recommendCultures(userId, limit);
        result.put("cultures", cultures);
        
        return result;
    }
    
    @Override
    public List<Attraction> recommendByBrowseHistory(Long userId, Integer limit) {
        // 获取用户浏览历史
        LambdaQueryWrapper<BrowseHistory> historyWrapper = new LambdaQueryWrapper<>();
        historyWrapper.eq(BrowseHistory::getUserId, userId)
                .eq(BrowseHistory::getTargetType, "attraction")
                .orderByDesc(BrowseHistory::getBrowseTime)
                .last("LIMIT 10");
        List<BrowseHistory> histories = browseHistoryMapper.selectList(historyWrapper);
        
        if (histories.isEmpty()) {
            // 如果没有浏览历史，返回热门景点
            return getHotAttractions(limit);
        }
        
        // 获取浏览过的景点分类
        Set<Long> categoryIds = new HashSet<>();
        for (BrowseHistory history : histories) {
            Attraction attraction = attractionMapper.selectById(history.getTargetId());
            if (attraction != null && attraction.getCategoryId() != null) {
                categoryIds.add(attraction.getCategoryId());
            }
        }
        
        // 推荐同分类的其他景点
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attraction::getStatus, 1);
        if (!categoryIds.isEmpty()) {
            wrapper.in(Attraction::getCategoryId, categoryIds);
        }
        // 排除已浏览的景点
        Set<Long> viewedIds = histories.stream()
                .map(BrowseHistory::getTargetId)
                .collect(Collectors.toSet());
        if (!viewedIds.isEmpty()) {
            wrapper.notIn(Attraction::getId, viewedIds);
        }
        wrapper.orderByDesc(Attraction::getRating);
        wrapper.orderByDesc(Attraction::getViewCount);
        wrapper.last("LIMIT " + limit);
        
        List<Attraction> recommendations = attractionMapper.selectList(wrapper);
        
        // 如果推荐数量不足，补充热门景点
        if (recommendations.size() < limit) {
            List<Attraction> hotAttractions = getHotAttractions(limit - recommendations.size());
            for (Attraction hot : hotAttractions) {
                if (!viewedIds.contains(hot.getId()) && 
                    !recommendations.stream().anyMatch(r -> r.getId().equals(hot.getId()))) {
                    recommendations.add(hot);
                }
            }
        }
        
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public List<Food> recommendByFavorites(Long userId, Integer limit) {
        // 获取用户收藏
        LambdaQueryWrapper<Favorite> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, "food")
                .orderByDesc(Favorite::getCreateTime)
                .last("LIMIT 10");
        List<Favorite> favorites = favoriteMapper.selectList(favoriteWrapper);
        
        if (favorites.isEmpty()) {
            // 如果没有收藏，返回热门美食
            return getHotFoods(limit);
        }
        
        // 获取收藏的美食分类
        Set<Long> categoryIds = new HashSet<>();
        for (Favorite favorite : favorites) {
            Food food = foodMapper.selectById(favorite.getTargetId());
            if (food != null && food.getCategoryId() != null) {
                categoryIds.add(food.getCategoryId());
            }
        }
        
        // 推荐同分类的其他美食
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getStatus, 1);
        if (!categoryIds.isEmpty()) {
            wrapper.in(Food::getCategoryId, categoryIds);
        }
        // 排除已收藏的美食
        Set<Long> favoriteIds = favorites.stream()
                .map(Favorite::getTargetId)
                .collect(Collectors.toSet());
        if (!favoriteIds.isEmpty()) {
            wrapper.notIn(Food::getId, favoriteIds);
        }
        wrapper.orderByDesc(Food::getRating);
        wrapper.orderByDesc(Food::getViewCount);
        wrapper.last("LIMIT " + limit);
        
        List<Food> recommendations = foodMapper.selectList(wrapper);
        
        // 如果推荐数量不足，补充热门美食
        if (recommendations.size() < limit) {
            List<Food> hotFoods = getHotFoods(limit - recommendations.size());
            for (Food hot : hotFoods) {
                if (!favoriteIds.contains(hot.getId()) && 
                    !recommendations.stream().anyMatch(r -> r.getId().equals(hot.getId()))) {
                    recommendations.add(hot);
                }
            }
        }
        
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }
    
    /**
     * 推荐攻略
     */
    private List<Strategy> recommendStrategies(Long userId, Integer limit) {
        // 获取用户收藏的攻略
        LambdaQueryWrapper<Favorite> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, "strategy")
                .orderByDesc(Favorite::getCreateTime)
                .last("LIMIT 5");
        List<Favorite> favorites = favoriteMapper.selectList(favoriteWrapper);
        
        LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strategy::getStatus, 1);
        wrapper.eq(Strategy::getIsRecommend, 1);
        
        // 如果有收藏，推荐相似主题的攻略
        if (!favorites.isEmpty()) {
            Set<String> themes = new HashSet<>();
            for (Favorite favorite : favorites) {
                Strategy strategy = strategyMapper.selectById(favorite.getTargetId());
                if (strategy != null && strategy.getTheme() != null) {
                    themes.add(strategy.getTheme());
                }
            }
            if (!themes.isEmpty()) {
                wrapper.in(Strategy::getTheme, themes);
            }
        }
        
        wrapper.orderByDesc(Strategy::getViewCount);
        wrapper.orderByDesc(Strategy::getFavoriteCount);
        wrapper.last("LIMIT " + limit);
        
        return strategyMapper.selectList(wrapper);
    }
    
    /**
     * 获取热门景点
     */
    private List<Attraction> getHotAttractions(Integer limit) {
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attraction::getStatus, 1);
        wrapper.orderByDesc(Attraction::getViewCount);
        wrapper.orderByDesc(Attraction::getRating);
        wrapper.last("LIMIT " + limit);
        return attractionMapper.selectList(wrapper);
    }
    
    /**
     * 获取热门美食
     */
    private List<Food> getHotFoods(Integer limit) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getStatus, 1);
        wrapper.orderByDesc(Food::getViewCount);
        wrapper.orderByDesc(Food::getRating);
        wrapper.last("LIMIT " + limit);
        return foodMapper.selectList(wrapper);
    }
    
    /**
     * 推荐酒店
     */
    private List<Hotel> recommendHotels(Long userId, Integer limit) {
        // 获取用户收藏的酒店
        LambdaQueryWrapper<Favorite> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, "hotel")
                .orderByDesc(Favorite::getCreateTime)
                .last("LIMIT 5");
        List<Favorite> favorites = favoriteMapper.selectList(favoriteWrapper);
        
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Hotel::getStatus, 1);
        
        // 如果有收藏，推荐相似星级的酒店
        if (!favorites.isEmpty()) {
            Set<Integer> starLevels = new HashSet<>();
            for (Favorite favorite : favorites) {
                Hotel hotel = hotelMapper.selectById(favorite.getTargetId());
                if (hotel != null && hotel.getStarLevel() != null) {
                    starLevels.add(hotel.getStarLevel());
                }
            }
            if (!starLevels.isEmpty()) {
                wrapper.in(Hotel::getStarLevel, starLevels);
            }
            
            // 排除已收藏的酒店
            Set<Long> favoriteIds = favorites.stream()
                    .map(Favorite::getTargetId)
                    .collect(Collectors.toSet());
            if (!favoriteIds.isEmpty()) {
                wrapper.notIn(Hotel::getId, favoriteIds);
            }
        }
        
        wrapper.orderByDesc(Hotel::getRating);
        wrapper.orderByDesc(Hotel::getViewCount);
        wrapper.last("LIMIT " + limit);
        
        List<Hotel> recommendations = hotelMapper.selectList(wrapper);
        
        // 如果推荐数量不足，补充热门酒店
        if (recommendations.size() < limit) {
            List<Hotel> hotHotels = getHotHotels(limit - recommendations.size());
            Set<Long> favoriteIds = favorites.stream()
                    .map(Favorite::getTargetId)
                    .collect(Collectors.toSet());
            for (Hotel hot : hotHotels) {
                if (!favoriteIds.contains(hot.getId()) && 
                    !recommendations.stream().anyMatch(r -> r.getId().equals(hot.getId()))) {
                    recommendations.add(hot);
                }
            }
        }
        
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }
    
    /**
     * 获取热门酒店
     */
    private List<Hotel> getHotHotels(Integer limit) {
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Hotel::getStatus, 1);
        wrapper.orderByDesc(Hotel::getRating);
        wrapper.orderByDesc(Hotel::getViewCount);
        wrapper.last("LIMIT " + limit);
        return hotelMapper.selectList(wrapper);
    }
    
    /**
     * 推荐体验项目
     */
    private List<Experience> recommendExperiences(Long userId, Integer limit) {
        // 获取用户收藏的体验项目
        LambdaQueryWrapper<Favorite> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, "experience")
                .orderByDesc(Favorite::getCreateTime)
                .last("LIMIT 5");
        List<Favorite> favorites = favoriteMapper.selectList(favoriteWrapper);
        
        LambdaQueryWrapper<Experience> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Experience::getStatus, 1);
        
        // 排除已收藏的体验项目
        if (!favorites.isEmpty()) {
            Set<Long> favoriteIds = favorites.stream()
                    .map(Favorite::getTargetId)
                    .collect(Collectors.toSet());
            if (!favoriteIds.isEmpty()) {
                wrapper.notIn(Experience::getId, favoriteIds);
            }
        }
        
        wrapper.orderByDesc(Experience::getRating);
        wrapper.orderByDesc(Experience::getViewCount);
        wrapper.last("LIMIT " + limit);
        
        List<Experience> recommendations = experienceMapper.selectList(wrapper);
        
        // 如果推荐数量不足，补充热门体验项目
        if (recommendations.size() < limit) {
            List<Experience> hotExperiences = getHotExperiences(limit - recommendations.size());
            Set<Long> favoriteIds = favorites.stream()
                    .map(Favorite::getTargetId)
                    .collect(Collectors.toSet());
            for (Experience hot : hotExperiences) {
                if (!favoriteIds.contains(hot.getId()) && 
                    !recommendations.stream().anyMatch(r -> r.getId().equals(hot.getId()))) {
                    recommendations.add(hot);
                }
            }
        }
        
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }
    
    /**
     * 推荐文化
     */
    private List<Culture> recommendCultures(Long userId, Integer limit) {
        // 获取用户收藏的文化
        LambdaQueryWrapper<Favorite> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, "culture")
                .orderByDesc(Favorite::getCreateTime)
                .last("LIMIT 5");
        List<Favorite> favorites = favoriteMapper.selectList(favoriteWrapper);
        
        LambdaQueryWrapper<Culture> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Culture::getStatus, 1);
        
        // 如果有收藏，推荐相似分类的文化
        if (!favorites.isEmpty()) {
            Set<Long> categoryIds = new HashSet<>();
            for (Favorite favorite : favorites) {
                Culture culture = cultureMapper.selectById(favorite.getTargetId());
                if (culture != null && culture.getCategoryId() != null) {
                    categoryIds.add(culture.getCategoryId());
                }
            }
            if (!categoryIds.isEmpty()) {
                wrapper.in(Culture::getCategoryId, categoryIds);
            }
            
            // 排除已收藏的文化
            Set<Long> favoriteIds = favorites.stream()
                    .map(Favorite::getTargetId)
                    .collect(Collectors.toSet());
            if (!favoriteIds.isEmpty()) {
                wrapper.notIn(Culture::getId, favoriteIds);
            }
        }
        
        wrapper.orderByDesc(Culture::getRating);
        wrapper.orderByDesc(Culture::getViewCount);
        wrapper.last("LIMIT " + limit);
        
        List<Culture> recommendations = cultureMapper.selectList(wrapper);
        
        // 如果推荐数量不足，补充热门文化
        if (recommendations.size() < limit) {
            List<Culture> hotCultures = getHotCultures(limit - recommendations.size());
            Set<Long> favoriteIds = favorites.stream()
                    .map(Favorite::getTargetId)
                    .collect(Collectors.toSet());
            for (Culture hot : hotCultures) {
                if (!favoriteIds.contains(hot.getId()) && 
                    !recommendations.stream().anyMatch(r -> r.getId().equals(hot.getId()))) {
                    recommendations.add(hot);
                }
            }
        }
        
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }
    
    /**
     * 获取热门体验项目
     */
    private List<Experience> getHotExperiences(Integer limit) {
        LambdaQueryWrapper<Experience> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Experience::getStatus, 1);
        wrapper.orderByDesc(Experience::getRating);
        wrapper.orderByDesc(Experience::getViewCount);
        wrapper.last("LIMIT " + limit);
        return experienceMapper.selectList(wrapper);
    }
    
    /**
     * 获取热门文化
     */
    private List<Culture> getHotCultures(Integer limit) {
        LambdaQueryWrapper<Culture> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Culture::getStatus, 1);
        wrapper.orderByDesc(Culture::getRating);
        wrapper.orderByDesc(Culture::getViewCount);
        wrapper.last("LIMIT " + limit);
        return cultureMapper.selectList(wrapper);
    }
}

