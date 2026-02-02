package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 旅游攻略实体类
 */
@Data
@TableName("strategy")
public class Strategy {
    
    /**
     * 攻略ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 攻略标题
     */
    private String title;
    
    /**
     * 创建用户ID（可为空，系统创建）
     */
    private Long userId;
    
    /**
     * 分类：1day-1日游，2day-2日游，theme-主题游
     */
    private String category;
    
    /**
     * 主题：亲子、情侣、摄影等
     */
    private String theme;
    
    /**
     * 封面图片
     */
    private String coverImage;
    
    /**
     * 攻略描述
     */
    private String description;
    
    /**
     * 详细内容
     */
    private String content;
    
    /**
     * 游玩时长（天）
     */
    private Integer duration;
    
    /**
     * 预算（元）
     */
    private BigDecimal budget;
    
    /**
     * 最佳季节
     */
    private String bestSeason;
    
    /**
     * 注意事项
     */
    private String tips;
    
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
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 状态：0-待审核，1-已通过，2-已拒绝
     */
    private Integer status;
    
    /**
     * 是否推荐：0-否，1-是
     */
    private Integer isRecommend;
    
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

