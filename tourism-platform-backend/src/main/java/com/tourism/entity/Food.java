package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 美食实体类
 */
@Data
@TableName("food")
public class Food {
    
    /**
     * 美食ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 美食名称
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
     * 美食描述
     */
    private String description;
    
    /**
     * 详细内容（制作方法等）
     */
    private String content;
    
    /**
     * 封面图片
     */
    private String coverImage;
    
    /**
     * 制作视频URL
     */
    private String videoUrl;
    
    /**
     * 起源/历史
     */
    private String origin;
    
    /**
     * 主要食材
     */
    private String ingredients;
    
    /**
     * 制作方法
     */
    private String cookingMethod;
    
    /**
     * 推荐餐厅（JSON格式）
     */
    private String recommendedRestaurants;
    
    /**
     * 推荐餐厅名称（临时字段，用于前端显示，不持久化）
     */
    @TableField(exist = false)
    private String restaurant;
    
    /**
     * 餐厅地址（临时字段，用于前端显示，不持久化）
     */
    @TableField(exist = false)
    private String address;
    
    /**
     * 餐厅经度（临时字段，用于前端显示，不持久化）
     */
    @TableField(exist = false)
    private BigDecimal longitude;
    
    /**
     * 餐厅纬度（临时字段，用于前端显示，不持久化）
     */
    @TableField(exist = false)
    private BigDecimal latitude;
    
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

