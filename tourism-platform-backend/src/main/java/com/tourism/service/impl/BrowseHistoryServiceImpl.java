package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.BrowseHistory;
import com.tourism.mapper.BrowseHistoryMapper;
import com.tourism.service.BrowseHistoryService;
import com.tourism.utils.PageUtil;
import com.tourism.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 浏览记录服务实现类
 */
@Service
public class BrowseHistoryServiceImpl implements BrowseHistoryService {
    
    @Autowired
    private BrowseHistoryMapper browseHistoryMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBrowseHistory(Long userId, String targetType, Long targetId) {
        // 检查是否已有记录
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId)
                .eq(BrowseHistory::getTargetType, targetType)
                .eq(BrowseHistory::getTargetId, targetId)
                .orderByDesc(BrowseHistory::getBrowseTime)
                .last("LIMIT 1");
        
        BrowseHistory exist = browseHistoryMapper.selectOne(wrapper);
        
        if (exist == null) {
            // 创建新记录
            BrowseHistory history = new BrowseHistory();
            history.setUserId(userId);
            history.setTargetType(targetType);
            history.setTargetId(targetId);
            browseHistoryMapper.insert(history);
        } else {
            // 更新浏览时间
            exist.setBrowseTime(new java.util.Date());
            browseHistoryMapper.updateById(exist);
        }
        // 注意：viewCount的更新由各个Service的getDetail方法统一处理，避免重复计数
    }
    
    @Override
    public PageVO<BrowseHistory> getBrowseHistoryList(Long userId, String targetType, Integer page, Integer size) {
        Page<BrowseHistory> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId);
        if (targetType != null && !targetType.isEmpty()) {
            wrapper.eq(BrowseHistory::getTargetType, targetType);
        }
        wrapper.orderByDesc(BrowseHistory::getBrowseTime);
        
        Page<BrowseHistory> result = browseHistoryMapper.selectPage(pageParam, wrapper);
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
    }
}
