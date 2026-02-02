package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 民俗文化实体类
 */
@Data
@TableName("culture")
public class Culture {
    
    /**
     * 文化ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 文化名称
     */
    private String name;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 创建用户ID（可为空，系统创建）
     */
    private Long userId;
    
    /**
     * 文化描述
     */
    private String description;
    
    /**
     * 详细内容
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
     * 历史背景
     */
    private String history;
    
    /**
     * 活动时间
     */
    private String activityTime;
    
    /**
     * 活动地点
     */
    private String activityLocation;
    
    /**
     * 活动地点经度
     */
    private BigDecimal longitude;
    
    /**
     * 活动地点纬度
     */
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

