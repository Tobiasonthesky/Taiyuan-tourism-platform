package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.Experience;
import com.tourism.exception.BusinessException;
import com.tourism.mapper.ExperienceMapper;
import com.tourism.service.ExperienceService;
import com.tourism.utils.PageUtil;
import com.tourism.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 体验项目服务实现类
 */
@Service
public class ExperienceServiceImpl implements ExperienceService {
    
    @Autowired
    private ExperienceMapper experienceMapper;
    
    @Override
    public PageVO<Experience> getExperienceList(String keyword, Integer page, Integer size) {
        Page<Experience> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Experience> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Experience::getName, keyword)
                    .or().like(Experience::getDescription, keyword));
        }
        wrapper.eq(Experience::getStatus, 1);
        wrapper.orderByDesc(Experience::getSortOrder);
        wrapper.orderByDesc(Experience::getRating);
        wrapper.orderByDesc(Experience::getCreateTime);
        
        Page<Experience> result = experienceMapper.selectPage(pageParam, wrapper);
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Experience getExperienceDetail(Long id) {
        Experience experience = experienceMapper.selectById(id);
        if (experience == null || experience.getStatus() != 1) {
            throw new BusinessException("体验项目不存在");
        }
        
        // 增加浏览次数
        experience.setViewCount(experience.getViewCount() + 1);
        experienceMapper.updateById(experience);
        
        return experience;
    }
    
    @Override
    public PageVO<Experience> searchExperiences(String keyword, Integer page, Integer size) {
        Page<Experience> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Experience> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.like(Experience::getName, keyword)
                .or().like(Experience::getDescription, keyword));
        wrapper.eq(Experience::getStatus, 1);
        wrapper.orderByDesc(Experience::getCreateTime);
        
        Page<Experience> result = experienceMapper.selectPage(pageParam, wrapper);
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
    }
}

