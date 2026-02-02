package com.tourism.service;

import com.tourism.entity.Culture;
import com.tourism.vo.PageVO;

/**
 * 民俗文化服务接口
 */
public interface CultureService {
    
    /**
     * 获取文化列表
     */
    PageVO<Culture> getCultureList(Long categoryId, String keyword, Integer page, Integer size);
    
    /**
     * 获取文化详情
     */
    Culture getCultureDetail(Long id);
}

