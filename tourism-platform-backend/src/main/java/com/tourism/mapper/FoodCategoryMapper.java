package com.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tourism.entity.FoodCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 美食分类Mapper接口
 */
@Mapper
public interface FoodCategoryMapper extends BaseMapper<FoodCategory> {
}

