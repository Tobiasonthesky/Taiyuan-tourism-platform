package com.tourism.service;

import com.tourism.entity.Strategy;
import com.tourism.vo.PageVO;

/**
 * 旅游攻略服务接口
 */
public interface StrategyService {
    
    /**
     * 获取攻略列表
     */
    PageVO<Strategy> getStrategyList(String category, String theme, Boolean isRecommend, Integer page, Integer size);
    
    /**
     * 获取攻略详情
     */
    Strategy getStrategyDetail(Long id);
}

