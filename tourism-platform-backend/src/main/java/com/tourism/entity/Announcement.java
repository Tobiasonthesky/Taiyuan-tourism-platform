package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 活动公告实体类
 */
@Data
@TableName("announcement")
public class Announcement {
    
    /**
     * 公告ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 分类：festival-节庆活动，promotion-优惠活动，news-新闻资讯
     */
    private String category;
    
    /**
     * 封面图片
     */
    private String coverImage;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date startTime;
    
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date endTime;
    
    /**
     * 是否置顶：0-否，1-是
     */
    private Integer isTop;
    
    /**
     * 是否轮播：0-否，1-是
     */
    private Integer isBanner;
    
    /**
     * 浏览次数
     */
    private Integer viewCount;
    
    /**
     * 状态：0-下架，1-上架
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

