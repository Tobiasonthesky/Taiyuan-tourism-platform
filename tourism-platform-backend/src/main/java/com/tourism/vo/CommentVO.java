package com.tourism.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 评论视图对象
 */
@Data
public class CommentVO {
    
    /**
     * 评论ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 用户头像
     */
    private String userAvatar;
    
    /**
     * 评论对象类型
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
     * 多维度评分（JSON格式）
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
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}

