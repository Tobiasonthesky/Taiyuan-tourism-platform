package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类
 * 注意：由于Order是Java关键字，使用OrderEntity作为类名
 */
@Data
@TableName("`order`")
public class OrderEntity {
    
    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单号，唯一
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 订单类型：ticket-门票，hotel-酒店，experience-体验
     */
    private String orderType;
    
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    
    /**
     * 实付金额
     */
    private BigDecimal payAmount;
    
    /**
     * 支付方式：alipay-支付宝，wechat-微信
     */
    private String payMethod;
    
    /**
     * 支付状态：0-未支付，1-已支付，2-已退款
     */
    private Integer payStatus;
    
    /**
     * 订单状态：0-待支付，1-已支付，2-已使用，3-已取消，4-已退款
     */
    private Integer orderStatus;
    
    /**
     * 联系人姓名
     */
    private String contactName;
    
    /**
     * 联系人电话
     */
    private String contactPhone;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 支付时间
     */
    private Date payTime;
    
    /**
     * 使用时间
     */
    private Date useTime;
    
    /**
     * 取消时间
     */
    private Date cancelTime;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}

