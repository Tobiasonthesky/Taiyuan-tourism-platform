package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.Attraction;
import com.tourism.entity.AttractionCategory;
import com.tourism.exception.BusinessException;
import com.tourism.mapper.AttractionMapper;
import com.tourism.mapper.AttractionCategoryMapper;
import com.tourism.service.AttractionService;
import com.tourism.utils.PageUtil;
import com.tourism.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 景点服务实现类
 */
@Service
public class AttractionServiceImpl implements AttractionService {
    
    @Autowired
    private AttractionMapper attractionMapper;
    
    @Autowired
    private AttractionCategoryMapper categoryMapper;
    
    @Override
    public PageVO<Attraction> getAttractionList(Long categoryId, String keyword, Integer page, Integer size) {
        Page<Attraction> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        
        if (categoryId != null) {
            wrapper.eq(Attraction::getCategoryId, categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Attraction::getName, keyword)
                    .or().like(Attraction::getDescription, keyword));
        }
        wrapper.eq(Attraction::getStatus, 1);
        wrapper.orderByDesc(Attraction::getSortOrder);
        wrapper.orderByDesc(Attraction::getCreateTime);
        
        Page<Attraction> result = attractionMapper.selectPage(pageParam, wrapper);
        
        // 填充分类名称
        List<Attraction> records = result.getRecords();
        fillCategoryNames(records);
        
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                records
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Attraction getAttractionDetail(Long id) {
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction == null || attraction.getStatus() != 1) {
            throw new BusinessException("景点不存在");
        }
        
        // 增加浏览次数
        attraction.setViewCount((attraction.getViewCount() == null ? 0 : attraction.getViewCount()) + 1);
        attractionMapper.updateById(attraction);
        
        // 填充分类名称
        fillCategoryName(attraction);
        
        return attraction;
    }
    
    @Override
    public PageVO<Attraction> searchAttractions(String keyword, Integer page, Integer size) {
        Page<Attraction> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.like(Attraction::getName, keyword)
                .or().like(Attraction::getDescription, keyword));
        wrapper.eq(Attraction::getStatus, 1);
        wrapper.orderByDesc(Attraction::getCreateTime);
        
        Page<Attraction> result = attractionMapper.selectPage(pageParam, wrapper);
        
        // 填充分类名称
        List<Attraction> records = result.getRecords();
        fillCategoryNames(records);
        
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                records
        );
    }
    
    /**
     * 填充分类名称（单个）
     */
    private void fillCategoryName(Attraction attraction) {
        if (attraction != null && attraction.getCategoryId() != null) {
            AttractionCategory category = categoryMapper.selectById(attraction.getCategoryId());
            if (category != null) {
                attraction.setCategoryName(category.getName());
            }
        }
    }
    
    /**
     * 填充分类名称（列表）
     */
    private void fillCategoryNames(List<Attraction> attractions) {
        if (attractions != null && !attractions.isEmpty()) {
            for (Attraction attraction : attractions) {
                fillCategoryName(attraction);
            }
        }
    }
}

