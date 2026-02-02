package com.tourism.service;

import com.tourism.entity.Experience;
import com.tourism.vo.PageVO;

/**
 * 体验项目服务接口
 */
public interface ExperienceService {
    
    /**
     * 获取体验项目列表
     */
    PageVO<Experience> getExperienceList(String keyword, Integer page, Integer size);
    
    /**
     * 获取体验项目详情
     */
    Experience getExperienceDetail(Long id);
    
    /**
     * 搜索体验项目
     */
    PageVO<Experience> searchExperiences(String keyword, Integer page, Integer size);
}

