package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.Hotel;
import com.tourism.entity.HotelRoom;
import com.tourism.entity.OrderEntity;
import com.tourism.entity.OrderItem;
import com.tourism.exception.BusinessException;
import com.tourism.mapper.HotelMapper;
import com.tourism.mapper.HotelRoomMapper;
import com.tourism.mapper.OrderItemMapper;
import com.tourism.mapper.OrderMapper;
import com.tourism.service.HotelService;
import com.tourism.utils.PageUtil;
import com.tourism.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 酒店服务实现类
 */
@Service
public class HotelServiceImpl implements HotelService {
    
    @Autowired
    private HotelMapper hotelMapper;
    
    @Autowired
    private HotelRoomMapper hotelRoomMapper;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Override
    public PageVO<Hotel> getHotelList(String keyword, Integer starLevel, Integer page, Integer size) {
        Page<Hotel> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Hotel::getName, keyword)
                    .or().like(Hotel::getDescription, keyword)
                    .or().like(Hotel::getAddress, keyword));
        }
        if (starLevel != null) {
            wrapper.eq(Hotel::getStarLevel, starLevel);
        }
        wrapper.eq(Hotel::getStatus, 1);
        wrapper.orderByDesc(Hotel::getRating);
        wrapper.orderByDesc(Hotel::getViewCount);
        
        Page<Hotel> result = hotelMapper.selectPage(pageParam, wrapper);
        
        // 确保每个酒店的最低价格是从房型价格中计算得出的
        for (Hotel hotel : result.getRecords()) {
            updateHotelMinPriceIfNeeded(hotel.getId(), hotel);
        }
        
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Hotel getHotelDetail(Long id) {
        Hotel hotel = hotelMapper.selectById(id);
        if (hotel == null || hotel.getStatus() != 1) {
            throw new BusinessException("酒店不存在");
        }
        
        // 确保最低价格是从房型价格中计算得出的
        updateHotelMinPriceIfNeeded(id, hotel);
        
        // 增加浏览次数
        hotel.setViewCount((hotel.getViewCount() == null ? 0 : hotel.getViewCount()) + 1);
        hotelMapper.updateById(hotel);
        
        return hotel;
    }
    
    @Override
    public List<HotelRoom> getHotelRooms(Long hotelId) {
        LambdaQueryWrapper<HotelRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HotelRoom::getHotelId, hotelId);
        wrapper.eq(HotelRoom::getStatus, 1);
        wrapper.orderByAsc(HotelRoom::getPrice);
        
        return hotelRoomMapper.selectList(wrapper);
    }
    
    @Override
    public PageVO<Hotel> searchHotels(String keyword, Integer page, Integer size) {
        Page<Hotel> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.like(Hotel::getName, keyword)
                .or().like(Hotel::getDescription, keyword)
                .or().like(Hotel::getAddress, keyword));
        wrapper.eq(Hotel::getStatus, 1);
        wrapper.orderByDesc(Hotel::getRating);
        
        Page<Hotel> result = hotelMapper.selectPage(pageParam, wrapper);
        
        // 确保每个酒店的最低价格是从房型价格中计算得出的
        for (Hotel hotel : result.getRecords()) {
            updateHotelMinPriceIfNeeded(hotel.getId(), hotel);
        }
        
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
    }
    
    @Override
    public boolean checkRoomAvailability(Long roomId, String checkInDate, String checkOutDate) {
        HotelRoom room = hotelRoomMapper.selectById(roomId);
        if (room == null || room.getStatus() == 0) {
            return false;
        }
        
        // 检查是否有已支付的订单占用该房间
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date checkIn = sdf.parse(checkInDate);
            Date checkOut = sdf.parse(checkOutDate);
            
            // 查询该房间在指定日期范围内的已支付订单
            LambdaQueryWrapper<OrderEntity> orderWrapper = new LambdaQueryWrapper<>();
            orderWrapper.eq(OrderEntity::getOrderType, "hotel")
                    .eq(OrderEntity::getPayStatus, 1) // 已支付
                    .in(OrderEntity::getOrderStatus, 1, 2); // 已支付或已使用
            
            List<OrderEntity> orders = orderMapper.selectList(orderWrapper);
            
            for (OrderEntity order : orders) {
                // 获取订单项
                LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
                itemWrapper.eq(OrderItem::getOrderId, order.getId())
                        .eq(OrderItem::getItemType, "hotel")
                        .eq(OrderItem::getItemId, roomId);
                List<OrderItem> items = orderItemMapper.selectList(itemWrapper);
                
                for (OrderItem item : items) {
                    if (item.getUseDate() != null) {
                        // 从备注中解析入住和退房日期
                        String remark = order.getRemark();
                        if (remark != null && remark.contains("入住：") && remark.contains("退房：")) {
                            try {
                                String[] parts = remark.split("，");
                                String inDateStr = parts[0].replace("入住：", "").trim();
                                String outDateStr = parts[1].replace("退房：", "").trim();
                                if (outDateStr.contains("。")) {
                                    outDateStr = outDateStr.split("。")[0];
                                }
                                
                                Date orderCheckIn = sdf.parse(inDateStr);
                                Date orderCheckOut = sdf.parse(outDateStr);
                                
                                // 检查日期是否冲突
                                if (!(checkOut.before(orderCheckIn) || checkIn.after(orderCheckOut))) {
                                    return false; // 有冲突
                                }
                            } catch (ParseException e) {
                                // 解析失败，跳过
                            }
                        }
                    }
                }
            }
            
            return true; // 无冲突
        } catch (ParseException e) {
            throw new BusinessException("日期格式错误");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHotelMinPrice(Long hotelId) {
        Hotel hotel = hotelMapper.selectById(hotelId);
        if (hotel == null) {
            return;  // 酒店不存在，直接返回
        }
        
        updateHotelMinPriceIfNeeded(hotelId, hotel);
    }
    
    /**
     * 根据房型价格更新酒店最低价格（如果酒店对象已存在，直接更新对象，避免重复查询）
     */
    private void updateHotelMinPriceIfNeeded(Long hotelId, Hotel hotel) {
        // 查询该酒店所有上架的房型，按价格升序排列
        LambdaQueryWrapper<HotelRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HotelRoom::getHotelId, hotelId);
        wrapper.eq(HotelRoom::getStatus, 1);  // 只查询上架的房型
        wrapper.orderByAsc(HotelRoom::getPrice);
        wrapper.last("LIMIT 1");  // 只取价格最低的一个
        
        HotelRoom cheapestRoom = hotelRoomMapper.selectOne(wrapper);
        
        BigDecimal calculatedMinPrice = null;
        if (cheapestRoom != null && cheapestRoom.getPrice() != null) {
            calculatedMinPrice = cheapestRoom.getPrice();
        }
        
        // 如果计算出的最低价格与数据库中的不一致，更新数据库
        if (hotel.getMinPrice() == null || 
            calculatedMinPrice == null || 
            !hotel.getMinPrice().equals(calculatedMinPrice)) {
            hotel.setMinPrice(calculatedMinPrice);
            hotelMapper.updateById(hotel);
        }
    }
}

