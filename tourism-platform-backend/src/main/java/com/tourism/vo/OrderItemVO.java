package com.tourism.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 订单项视图对象
 */
@Data
public class OrderItemVO {
    
    /**
     * 订单项ID
     */
    private Long id;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 项目类型：ticket-门票，hotel-酒店，experience-体验
     */
    private String itemType;
    
    /**
     * 项目ID
     */
    private Long itemId;
    
    /**
     * 项目名称
     */
    private String itemName;
    
    /**
     * 项目图片
     */
    private String itemImage;
    
    /**
     * 数量
     */
    private Integer quantity;
    
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    
    /**
     * 总价
     */
    private BigDecimal totalPrice;
    
    /**
     * 使用日期
     */
    private Date useDate;
    
    /**
     * 使用时间
     */
    private String useTime;
    
    /**
     * 额外信息（JSON格式，解析后的Map）
     */
    private Map<String, Object> extraInfo;
    
    // 酒店房间相关字段（从extraInfo中提取）
    /**
     * 房间类型
     */
    private String roomType;
    
    /**
     * 房间名称
     */
    private String roomName;
    
    /**
     * 房间价格
     */
    private BigDecimal roomPrice;
    
    /**
     * 入住日期
     */
    private String checkInDate;
    
    /**
     * 退房日期
     */
    private String checkOutDate;
}

