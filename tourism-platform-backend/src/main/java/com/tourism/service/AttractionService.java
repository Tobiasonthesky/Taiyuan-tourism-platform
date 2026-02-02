package com.tourism.service;

import com.tourism.entity.Attraction;
import com.tourism.vo.PageVO;

/**
 * 景点服务接口
 */
public interface AttractionService {
    
    /**
     * 获取景点列表
     */
    PageVO<Attraction> getAttractionList(Long categoryId, String keyword, Integer page, Integer size);
    
    /**
     * 获取景点详情
     */
    Attraction getAttractionDetail(Long id);
    
    /**
     * 搜索景点
     */
    PageVO<Attraction> searchAttractions(String keyword, Integer page, Integer size);
}

