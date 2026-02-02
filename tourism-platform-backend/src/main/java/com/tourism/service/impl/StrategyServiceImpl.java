package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.Strategy;
import com.tourism.mapper.StrategyMapper;
import com.tourism.service.StrategyService;
import com.tourism.utils.PageUtil;
import com.tourism.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 旅游攻略服务实现类
 */
@Service
public class StrategyServiceImpl implements StrategyService {
    
    @Autowired
    private StrategyMapper strategyMapper;
    
    @Override
    public PageVO<Strategy> getStrategyList(String category, String theme, Boolean isRecommend, Integer page, Integer size) {
        Page<Strategy> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<>();
        
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Strategy::getCategory, category);
        }
        if (theme != null && !theme.isEmpty()) {
            wrapper.eq(Strategy::getTheme, theme);
        }
        if (isRecommend != null && isRecommend) {
            wrapper.eq(Strategy::getIsRecommend, 1);
        }
        wrapper.eq(Strategy::getStatus, 1);
        wrapper.orderByDesc(Strategy::getIsRecommend);
        wrapper.orderByDesc(Strategy::getViewCount);
        wrapper.orderByDesc(Strategy::getCreateTime);
        
        Page<Strategy> result = strategyMapper.selectPage(pageParam, wrapper);
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
    public Strategy getStrategyDetail(Long id) {
        Strategy strategy = strategyMapper.selectById(id);
        if (strategy == null || strategy.getStatus() != 1) {
            throw new com.tourism.exception.BusinessException("攻略不存在");
        }
        
        // 增加浏览次数
        strategy.setViewCount((strategy.getViewCount() == null ? 0 : strategy.getViewCount()) + 1);
        strategyMapper.updateById(strategy);
        
        return strategy;
    }
}

