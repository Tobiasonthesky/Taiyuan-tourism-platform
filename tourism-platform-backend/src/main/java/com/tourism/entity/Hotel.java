package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 酒店实体类
 */
@Data
@TableName("hotel")
public class Hotel {
    
    /**
     * 酒店ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 酒店名称
     */
    private String name;
    
    /**
     * 酒店描述
     */
    private String description;
    
    /**
     * 封面图片
     */
    private String coverImage;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 经度
     */
    private BigDecimal longitude;
    
    /**
     * 纬度
     */
    private BigDecimal latitude;
    
    /**
     * 联系电话
     */
    private String phone;
    
    /**
     * 星级（1-5）
     */
    private Integer starLevel;
    
    /**
     * 评分（0-5）
     */
    private BigDecimal rating;
    
    /**
     * 设施（JSON格式）
     */
    private String facilities;
    
    /**
     * 最低价格
     */
    private BigDecimal minPrice;
    
    /**
     * 浏览次数
     */
    private Integer viewCount;
    
    /**
     * 评论数
     */
    private Integer commentCount;
    
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

