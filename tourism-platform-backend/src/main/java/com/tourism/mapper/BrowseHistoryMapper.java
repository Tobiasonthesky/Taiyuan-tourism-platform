package com.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tourism.entity.BrowseHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户浏览记录Mapper接口
 */
@Mapper
public interface BrowseHistoryMapper extends BaseMapper<BrowseHistory> {
}

