package com.tourism.service;

import com.tourism.entity.BrowseHistory;
import com.tourism.vo.PageVO;

/**
 * 浏览记录服务接口
 */
public interface BrowseHistoryService {
    
    /**
     * 添加浏览记录
     */
    void addBrowseHistory(Long userId, String targetType, Long targetId);
    
    /**
     * 获取浏览记录列表
     */
    PageVO<BrowseHistory> getBrowseHistoryList(Long userId, String targetType, Integer page, Integer size);
}

