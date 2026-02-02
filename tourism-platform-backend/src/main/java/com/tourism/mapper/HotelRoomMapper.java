package com.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tourism.entity.HotelRoom;
import org.apache.ibatis.annotations.Mapper;

/**
 * 酒店房间Mapper接口
 */
@Mapper
public interface HotelRoomMapper extends BaseMapper<HotelRoom> {
}

