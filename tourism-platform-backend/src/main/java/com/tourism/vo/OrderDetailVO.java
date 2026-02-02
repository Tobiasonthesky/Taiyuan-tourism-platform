package com.tourism.vo;

import com.tourism.entity.OrderEntity;
import lombok.Data;

import java.util.List;

/**
 * 订单详情视图对象
 */
@Data
public class OrderDetailVO {
    
    /**
     * 订单信息
     */
    private OrderEntity order;
    
    /**
     * 订单项列表（包含房间信息）
     */
    private List<OrderItemVO> items;
}

