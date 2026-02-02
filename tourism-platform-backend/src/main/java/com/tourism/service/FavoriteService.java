package com.tourism.service;

import com.tourism.entity.Favorite;
import com.tourism.vo.FavoriteVO;
import com.tourism.vo.PageVO;

/**
 * 收藏服务接口
 */
public interface FavoriteService {
    
    /**
     * 添加收藏
     */
    void addFavorite(Long userId, String targetType, Long targetId);
    
    /**
     * 取消收藏
     */
    void removeFavorite(Long userId, String targetType, Long targetId);
    
    /**
     * 检查是否已收藏
     */
    boolean isFavorite(Long userId, String targetType, Long targetId);
    
    /**
     * 获取收藏列表（包含详细信息）
     */
    PageVO<FavoriteVO> getFavoriteList(Long userId, String targetType, Integer page, Integer size);
}

