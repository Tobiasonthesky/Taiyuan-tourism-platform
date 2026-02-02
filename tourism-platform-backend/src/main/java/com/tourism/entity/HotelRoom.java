package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 酒店房间实体类
 */
@Data
@TableName("hotel_room")
public class HotelRoom {
    
    /**
     * 房间ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 酒店ID
     */
    private Long hotelId;
    
    /**
     * 房间类型
     */
    private String roomType;
    
    /**
     * 房间名称
     */
    private String roomName;
    
    /**
     * 房间描述
     */
    private String description;
    
    /**
     * 房间图片
     */
    private String image;
    
    /**
     * 面积（平方米）
     */
    private BigDecimal area;
    
    /**
     * 床型
     */
    private String bedType;
    
    /**
     * 最大入住人数
     */
    private Integer maxOccupancy;
    
    /**
     * 房间设施（JSON格式）
     */
    private String facilities;
    
    /**
     * 价格
     */
    private BigDecimal price;
    
    /**
     * 库存数量
     */
    private Integer stock;
    
    /**
     * 状态：0-下架，1-上架
     */
    private Integer status;
    
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

