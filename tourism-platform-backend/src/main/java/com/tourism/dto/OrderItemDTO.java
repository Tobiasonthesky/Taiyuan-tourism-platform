package com.tourism.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单项DTO
 */
@Data
public class OrderItemDTO {
    
    /**
     * 项目类型：ticket-门票，hotel-酒店，experience-体验
     */
    @NotBlank(message = "项目类型不能为空")
    private String itemType;
    
    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long itemId;
    
    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;
    
    /**
     * 使用日期（支持Date或String格式）
     */
    private Object useDate;
    
    /**
     * 使用时间
     */
    private String useTime;
    
    /**
     * 房间类型（仅酒店订单）
     */
    private String roomType;
    
    /**
     * 房间名称（仅酒店订单）
     */
    private String roomName;
    
    /**
     * 房间价格（仅酒店订单）
     */
    private BigDecimal roomPrice;
    
    /**
     * 入住日期（仅酒店订单）
     */
    private String checkInDate;
    
    /**
     * 退房日期（仅酒店订单）
     */
    private String checkOutDate;
}

