package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 酒店图片实体类
 */
@Data
@TableName("hotel_image")
public class HotelImage {
    
    /**
     * 图片ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 酒店ID
     */
    private Long hotelId;
    
    /**
     * 图片URL
     */
    private String imageUrl;
    
    /**
     * 图片类型：cover-封面，room-房间，facility-设施，environment-环境
     */
    private String imageType;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}



