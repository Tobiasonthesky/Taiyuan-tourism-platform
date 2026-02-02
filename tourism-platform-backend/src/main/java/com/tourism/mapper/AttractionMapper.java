package com.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tourism.entity.Attraction;
import org.apache.ibatis.annotations.Mapper;

/**
 * 景点Mapper接口
 */
@Mapper
public interface AttractionMapper extends BaseMapper<Attraction> {
}

