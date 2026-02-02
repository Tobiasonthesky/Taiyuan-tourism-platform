package com.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tourism.entity.AttractionCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 景点分类Mapper接口
 */
@Mapper
public interface AttractionCategoryMapper extends BaseMapper<AttractionCategory> {
}

