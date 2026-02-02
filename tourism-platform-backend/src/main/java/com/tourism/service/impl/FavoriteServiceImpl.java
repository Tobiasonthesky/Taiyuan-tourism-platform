package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.*;
import com.tourism.mapper.*;
import com.tourism.service.FavoriteService;
import com.tourism.utils.PageUtil;
import com.tourism.vo.FavoriteVO;
import com.tourism.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Autowired
    private AttractionMapper attractionMapper;
    
    @Autowired
    private FoodMapper foodMapper;
    
    @Autowired
    private CultureMapper cultureMapper;
    
    @Autowired
    private StrategyMapper strategyMapper;
    
    @Autowired
    private HotelMapper hotelMapper;
    
    @Autowired
    private ExperienceMapper experienceMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Long userId, String targetType, Long targetId) {
        // 检查是否已收藏
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId);
        Favorite exist = favoriteMapper.selectOne(wrapper);
        
        if (exist == null) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setTargetType(targetType);
            favorite.setTargetId(targetId);
            favoriteMapper.insert(favorite);
            
            // 更新对应实体的收藏数
            increaseFavoriteCount(targetType, targetId);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Long userId, String targetType, Long targetId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId);
        Favorite exist = favoriteMapper.selectOne(wrapper);
        
        if (exist != null) {
            favoriteMapper.delete(wrapper);
            
            // 更新对应实体的收藏数
            decreaseFavoriteCount(targetType, targetId);
        }
    }
    
    /**
     * 增加收藏数
     */
    private void increaseFavoriteCount(String targetType, Long targetId) {
        switch (targetType) {
            case "attraction":
                Attraction attraction = attractionMapper.selectById(targetId);
                if (attraction != null) {
                    attraction.setFavoriteCount((attraction.getFavoriteCount() == null ? 0 : attraction.getFavoriteCount()) + 1);
                    attractionMapper.updateById(attraction);
                }
                break;
            case "food":
                Food food = foodMapper.selectById(targetId);
                if (food != null) {
                    food.setFavoriteCount((food.getFavoriteCount() == null ? 0 : food.getFavoriteCount()) + 1);
                    foodMapper.updateById(food);
                }
                break;
            case "culture":
                Culture culture = cultureMapper.selectById(targetId);
                if (culture != null) {
                    culture.setFavoriteCount((culture.getFavoriteCount() == null ? 0 : culture.getFavoriteCount()) + 1);
                    cultureMapper.updateById(culture);
                }
                break;
            case "strategy":
                Strategy strategy = strategyMapper.selectById(targetId);
                if (strategy != null) {
                    strategy.setFavoriteCount((strategy.getFavoriteCount() == null ? 0 : strategy.getFavoriteCount()) + 1);
                    strategyMapper.updateById(strategy);
                }
                break;
            case "hotel":
                Hotel hotel = hotelMapper.selectById(targetId);
                if (hotel != null) {
                    // Hotel实体没有favoriteCount字段，跳过
                }
                break;
            case "experience":
                Experience experience = experienceMapper.selectById(targetId);
                if (experience != null) {
                    experience.setFavoriteCount((experience.getFavoriteCount() == null ? 0 : experience.getFavoriteCount()) + 1);
                    experienceMapper.updateById(experience);
                }
                break;
        }
    }
    
    /**
     * 减少收藏数
     */
    private void decreaseFavoriteCount(String targetType, Long targetId) {
        switch (targetType) {
            case "attraction":
                Attraction attraction = attractionMapper.selectById(targetId);
                if (attraction != null && attraction.getFavoriteCount() != null && attraction.getFavoriteCount() > 0) {
                    attraction.setFavoriteCount(attraction.getFavoriteCount() - 1);
                    attractionMapper.updateById(attraction);
                }
                break;
            case "food":
                Food food = foodMapper.selectById(targetId);
                if (food != null && food.getFavoriteCount() != null && food.getFavoriteCount() > 0) {
                    food.setFavoriteCount(food.getFavoriteCount() - 1);
                    foodMapper.updateById(food);
                }
                break;
            case "culture":
                Culture culture = cultureMapper.selectById(targetId);
                if (culture != null && culture.getFavoriteCount() != null && culture.getFavoriteCount() > 0) {
                    culture.setFavoriteCount(culture.getFavoriteCount() - 1);
                    cultureMapper.updateById(culture);
                }
                break;
            case "strategy":
                Strategy strategy = strategyMapper.selectById(targetId);
                if (strategy != null && strategy.getFavoriteCount() != null && strategy.getFavoriteCount() > 0) {
                    strategy.setFavoriteCount(strategy.getFavoriteCount() - 1);
                    strategyMapper.updateById(strategy);
                }
                break;
            case "hotel":
                // Hotel实体没有favoriteCount字段，跳过
                break;
            case "experience":
                Experience experience = experienceMapper.selectById(targetId);
                if (experience != null && experience.getFavoriteCount() != null && experience.getFavoriteCount() > 0) {
                    experience.setFavoriteCount(experience.getFavoriteCount() - 1);
                    experienceMapper.updateById(experience);
                }
                break;
        }
    }
    
    @Override
    public boolean isFavorite(Long userId, String targetType, Long targetId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId);
        return favoriteMapper.selectCount(wrapper) > 0;
    }
    
    @Override
    public PageVO<FavoriteVO> getFavoriteList(Long userId, String targetType, Integer page, Integer size) {
        Page<Favorite> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        if (targetType != null && !targetType.isEmpty()) {
            wrapper.eq(Favorite::getTargetType, targetType);
        }
        wrapper.orderByDesc(Favorite::getCreateTime);
        
        Page<Favorite> result = favoriteMapper.selectPage(pageParam, wrapper);
        
        // 转换为FavoriteVO，包含详细信息
        List<FavoriteVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .filter(vo -> vo != null)
                .collect(Collectors.toList());
        
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                voList
        );
    }
    
    /**
     * 将Favorite转换为FavoriteVO，包含详细信息
     */
    private FavoriteVO convertToVO(Favorite favorite) {
        FavoriteVO vo = new FavoriteVO();
        vo.setId(favorite.getId());
        vo.setTargetType(favorite.getTargetType());
        vo.setTargetId(favorite.getTargetId());
        vo.setCreateTime(favorite.getCreateTime());
        
        // 根据类型获取详细信息
        switch (favorite.getTargetType()) {
            case "attraction":
                Attraction attraction = attractionMapper.selectById(favorite.getTargetId());
                if (attraction != null) {
                    vo.setName(attraction.getName());
                    vo.setImage(attraction.getCoverImage());
                    vo.setCoverImage(attraction.getCoverImage());
                    vo.setDescription(attraction.getDescription());
                }
                break;
            case "food":
                Food food = foodMapper.selectById(favorite.getTargetId());
                if (food != null) {
                    vo.setName(food.getName());
                    vo.setImage(food.getCoverImage());
                    vo.setCoverImage(food.getCoverImage());
                    vo.setDescription(food.getDescription());
                }
                break;
            case "culture":
                Culture culture = cultureMapper.selectById(favorite.getTargetId());
                if (culture != null) {
                    vo.setName(culture.getName());
                    vo.setImage(culture.getCoverImage());
                    vo.setCoverImage(culture.getCoverImage());
                    vo.setDescription(culture.getDescription());
                }
                break;
            case "strategy":
                Strategy strategy = strategyMapper.selectById(favorite.getTargetId());
                if (strategy != null) {
                    vo.setTitle(strategy.getTitle());
                    vo.setName(strategy.getTitle());
                    vo.setImage(strategy.getCoverImage());
                    vo.setCoverImage(strategy.getCoverImage());
                    vo.setDescription(strategy.getDescription());
                }
                break;
            case "hotel":
                Hotel hotel = hotelMapper.selectById(favorite.getTargetId());
                if (hotel != null) {
                    vo.setName(hotel.getName());
                    vo.setImage(hotel.getCoverImage());
                    vo.setCoverImage(hotel.getCoverImage());
                    vo.setDescription(hotel.getDescription());
                }
                break;
            case "experience":
                Experience experience = experienceMapper.selectById(favorite.getTargetId());
                if (experience != null) {
                    vo.setName(experience.getName());
                    vo.setImage(experience.getCoverImage());
                    vo.setCoverImage(experience.getCoverImage());
                    vo.setDescription(experience.getDescription());
                }
                break;
        }
        
        return vo;
    }
}

