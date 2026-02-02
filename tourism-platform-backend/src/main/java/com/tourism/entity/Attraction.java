package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 景点实体类
 */
@Data
@TableName("attraction")
public class Attraction {
    
    /**
     * 景点ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 景点名称
     */
    private String name;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称（临时字段，用于前端显示，不持久化）
     */
    @TableField(exist = false)
    private String categoryName;
    
    /**
     * 创建用户ID（可为空，系统创建）
     */
    private Long userId;
    
    /**
     * 景点描述
     */
    private String description;
    
    /**
     * 详细内容（富文本）
     */
    private String content;
    
    /**
     * 封面图片
     */
    private String coverImage;
    
    /**
     * 视频URL
     */
    private String videoUrl;
    
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
     * 开放时间
     */
    private String openingHours;
    
    /**
     * 门票价格
     */
    private BigDecimal ticketPrice;
    
    /**
     * 评分（0-5）
     */
    private BigDecimal rating;
    
    /**
     * 浏览次数
     */
    private Integer viewCount;
    
    /**
     * 评论数
     */
    private Integer commentCount;
    
    /**
     * 收藏数
     */
    private Integer favoriteCount;
    
    /**
     * 标签（逗号分隔）
     */
    private String tags;
    
    /**
     * 状态：0-待审核，1-已通过，2-已拒绝
     */
    private Integer status;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
    
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

