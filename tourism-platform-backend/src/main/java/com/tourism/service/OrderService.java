package com.tourism.service;

import com.tourism.dto.OrderItemDTO;
import com.tourism.entity.OrderEntity;
import com.tourism.entity.OrderItem;
import com.tourism.vo.OrderDetailVO;
import com.tourism.vo.OrderItemVO;
import com.tourism.vo.PageVO;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 创建订单
     */
    OrderEntity createOrder(Long userId, String orderType, List<OrderItemDTO> items,
                           String contactName, String contactPhone, String remark);
    
    /**
     * 获取订单列表
     */
    PageVO<OrderEntity> getOrderList(Long userId, Integer orderStatus, Integer page, Integer size);
    
    /**
     * 获取订单详情（包含房间信息）
     */
    OrderDetailVO getOrderDetail(Long orderId, Long userId);
    
    /**
     * 获取订单项列表（包含房间信息）
     */
    List<OrderItemVO> getOrderItems(Long orderId);
    
    /**
     * 取消订单
     */
    void cancelOrder(Long orderId, Long userId);
    
    /**
     * 支付订单
     */
    void payOrder(Long orderId, String payMethod);
}
