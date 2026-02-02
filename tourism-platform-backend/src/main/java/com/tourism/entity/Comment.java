package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 评论实体类
 */
@Data
@TableName("comment")
public class Comment {
    
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 评论对象类型：attraction-景点，food-美食，hotel-酒店，culture-文化
     */
    private String targetType;
    
    /**
     * 评论对象ID
     */
    private Long targetId;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 评分（0-5）
     */
    private BigDecimal rating;
    
    /**
     * 多维度评分（JSON格式：环境、服务、性价比等）
     */
    private String ratingDetail;
    
    /**
     * 评论图片（JSON数组）
     */
    private String images;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 回复数
     */
    private Integer replyCount;
    
    /**
     * 状态：0-待审核，1-已通过，2-已拒绝
     */
    private Integer status;
    
    /**
     * 是否置顶：0-否，1-是
     */
    private Integer isTop;
    
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

