package com.tourism.service;

import com.tourism.entity.Hotel;
import com.tourism.entity.HotelRoom;
import com.tourism.vo.PageVO;

import java.util.List;

/**
 * 酒店服务接口
 */
public interface HotelService {
    
    /**
     * 获取酒店列表
     */
    PageVO<Hotel> getHotelList(String keyword, Integer starLevel, Integer page, Integer size);
    
    /**
     * 获取酒店详情
     */
    Hotel getHotelDetail(Long id);
    
    /**
     * 获取酒店房间列表
     */
    List<HotelRoom> getHotelRooms(Long hotelId);
    
    /**
     * 搜索酒店
     */
    PageVO<Hotel> searchHotels(String keyword, Integer page, Integer size);
    
    /**
     * 检查房间可用性
     */
    boolean checkRoomAvailability(Long roomId, String checkInDate, String checkOutDate);
    
    /**
     * 根据房型价格更新酒店最低价格
     */
    void updateHotelMinPrice(Long hotelId);
}

