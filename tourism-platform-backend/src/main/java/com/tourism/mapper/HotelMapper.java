package com.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tourism.entity.Hotel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 酒店Mapper接口
 */
@Mapper
public interface HotelMapper extends BaseMapper<Hotel> {
}

