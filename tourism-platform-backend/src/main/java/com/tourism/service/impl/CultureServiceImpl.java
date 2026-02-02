package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.Culture;
import com.tourism.mapper.CultureMapper;
import com.tourism.service.CultureService;
import com.tourism.utils.PageUtil;
import com.tourism.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 民俗文化服务实现类
 */
@Service
public class CultureServiceImpl implements CultureService {
    
    @Autowired
    private CultureMapper cultureMapper;
    
    @Override
    public PageVO<Culture> getCultureList(Long categoryId, String keyword, Integer page, Integer size) {
        Page<Culture> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Culture> wrapper = new LambdaQueryWrapper<>();
        
        if (categoryId != null) {
            wrapper.eq(Culture::getCategoryId, categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Culture::getName, keyword)
                    .or().like(Culture::getDescription, keyword));
        }
        wrapper.eq(Culture::getStatus, 1);
        wrapper.orderByDesc(Culture::getSortOrder);
        wrapper.orderByDesc(Culture::getCreateTime);
        
        Page<Culture> result = cultureMapper.selectPage(pageParam, wrapper);
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
    }
    
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public Culture getCultureDetail(Long id) {
        Culture culture = cultureMapper.selectById(id);
        if (culture == null || culture.getStatus() != 1) {
            throw new com.tourism.exception.BusinessException("文化内容不存在");
        }
        
        // 增加浏览次数
        culture.setViewCount((culture.getViewCount() == null ? 0 : culture.getViewCount()) + 1);
        cultureMapper.updateById(culture);
        
        return culture;
    }
}

