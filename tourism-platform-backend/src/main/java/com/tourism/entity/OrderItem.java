package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单项实体类
 */
@Data
@TableName("order_item")
public class OrderItem {
    
    /**
     * 订单项ID
     */
    @TableId(type = IdType.AUTO)
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
     * 项目ID（景点ID、酒店ID等）
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
     * 额外信息（JSON格式）
     */
    private String extraInfo;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}

