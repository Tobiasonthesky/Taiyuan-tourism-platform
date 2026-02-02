package com.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tourism.entity.Food;
import org.apache.ibatis.annotations.Mapper;

/**
 * 美食Mapper接口
 */
@Mapper
public interface FoodMapper extends BaseMapper<Food> {
}

