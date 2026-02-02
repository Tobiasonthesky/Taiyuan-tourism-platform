package com.tourism.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.dto.StrategyGenerateDTO;
import com.tourism.entity.Attraction;
import com.tourism.entity.Food;
import com.tourism.entity.Hotel;
import com.tourism.entity.Strategy;
import com.tourism.mapper.AttractionMapper;
import com.tourism.mapper.FoodMapper;
import com.tourism.mapper.HotelMapper;
import com.tourism.service.AiService;
import com.tourism.service.PromptTemplateBuilder;
import com.tourism.service.StrategyGenerateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 攻略生成服务实现类
 */
@Service
public class StrategyGenerateServiceImpl implements StrategyGenerateService {
    
    private static final Logger logger = LoggerFactory.getLogger(StrategyGenerateServiceImpl.class);
    
    @Autowired
    private AiService aiService;
    
    @Autowired
    private PromptTemplateBuilder promptTemplateBuilder;
    
    @Autowired
    private AttractionMapper attractionMapper;
    
    @Autowired
    private FoodMapper foodMapper;
    
    @Autowired
    private HotelMapper hotelMapper;
    
    @Override
    public Strategy generateStrategy(StrategyGenerateDTO dto, Long userId) {
        // 1. 获取平台数据作为上下文
        List<Attraction> attractions = getAttractions();
        List<Food> foods = getFoods();
        List<Hotel> hotels = getHotels();
        
        // 2. 构建提示词
        String prompt = promptTemplateBuilder.buildStrategyPrompt(dto, attractions, foods, hotels);
        
        // 3. 调用AI生成攻略
        String aiResponse = aiService.generateStrategy(prompt);
        
        if (aiResponse == null || aiResponse.isEmpty()) {
            throw new RuntimeException("AI生成攻略失败，请稍后重试");
        }
        
        // 4. 解析AI返回的内容并构建攻略对象
        Strategy strategy = parseAiResponse(aiResponse, dto, userId);
        
        return strategy;
    }
    
    /**
     * 获取景点列表（用于生成攻略）
     */
    private List<Attraction> getAttractions() {
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attraction::getStatus, 1);
        wrapper.orderByDesc(Attraction::getRating);
        wrapper.orderByDesc(Attraction::getViewCount);
        wrapper.last("LIMIT 20");
        return attractionMapper.selectList(wrapper);
    }
    
    /**
     * 获取美食列表
     */
    private List<Food> getFoods() {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getStatus, 1);
        wrapper.orderByDesc(Food::getRating);
        wrapper.orderByDesc(Food::getViewCount);
        wrapper.last("LIMIT 15");
        return foodMapper.selectList(wrapper);
    }
    
    /**
     * 获取酒店列表
     */
    private List<Hotel> getHotels() {
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Hotel::getStatus, 1);
        wrapper.orderByDesc(Hotel::getRating);
        wrapper.last("LIMIT 10");
        return hotelMapper.selectList(wrapper);
    }
    
    /**
     * 解析AI返回的内容
     */
    private Strategy parseAiResponse(String aiResponse, StrategyGenerateDTO dto, Long userId) {
        Strategy strategy = new Strategy();
        
        try {
            // 尝试提取JSON（AI可能返回markdown格式的代码块）
            String jsonStr = aiResponse.trim();
            if (jsonStr.startsWith("```json")) {
                jsonStr = jsonStr.substring(7);
            }
            if (jsonStr.startsWith("```")) {
                jsonStr = jsonStr.substring(3);
            }
            if (jsonStr.endsWith("```")) {
                jsonStr = jsonStr.substring(0, jsonStr.length() - 3);
            }
            jsonStr = jsonStr.trim();
            
            JSONObject json = JSON.parseObject(jsonStr);
            
            strategy.setTitle(json.getString("title"));
            strategy.setDescription(json.getString("description"));
            strategy.setContent(json.getString("content"));
            strategy.setTips(json.getString("tips"));
            
            // 处理 bestSeason 字段，限制长度为50字符
            String bestSeason = json.getString("bestSeason") != null ? 
                json.getString("bestSeason") : dto.getBestSeason();
            if (bestSeason != null && bestSeason.length() > 50) {
                bestSeason = bestSeason.substring(0, 50);
            }
            strategy.setBestSeason(bestSeason);
            strategy.setDuration(dto.getDuration());
            strategy.setBudget(dto.getBudget());
            strategy.setTheme(dto.getTheme());
            strategy.setUserId(userId);
            strategy.setStatus(0); // 待审核
            strategy.setIsRecommend(0);
            strategy.setViewCount(0);
            strategy.setCommentCount(0);
            strategy.setFavoriteCount(0);
            strategy.setLikeCount(0);
            
            // 根据天数设置分类
            if (dto.getDuration() == 1) {
                strategy.setCategory("1day");
            } else if (dto.getDuration() == 2) {
                strategy.setCategory("2day");
            } else {
                strategy.setCategory("theme");
            }
            
        } catch (Exception e) {
            logger.error("解析AI返回内容失败", e);
            // 如果解析失败，使用原始内容
            strategy.setTitle("AI生成的" + dto.getDuration() + "日游攻略");
            strategy.setDescription("AI智能生成的旅游攻略，请查看详细内容");
            strategy.setContent(aiResponse);
            strategy.setDuration(dto.getDuration());
            strategy.setBudget(dto.getBudget());
            strategy.setTheme(dto.getTheme());
            strategy.setUserId(userId);
            strategy.setStatus(0);
            strategy.setIsRecommend(0);
            strategy.setViewCount(0);
            strategy.setCommentCount(0);
            strategy.setFavoriteCount(0);
            strategy.setLikeCount(0);
            
            if (dto.getDuration() == 1) {
                strategy.setCategory("1day");
            } else if (dto.getDuration() == 2) {
                strategy.setCategory("2day");
            } else {
                strategy.setCategory("theme");
            }
        }
        
        return strategy;
    }
}
