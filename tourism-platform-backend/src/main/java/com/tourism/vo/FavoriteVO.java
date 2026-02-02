package com.tourism.vo;

import lombok.Data;

import java.util.Date;

/**
 * 收藏视图对象（包含收藏项的详细信息）
 */
@Data
public class FavoriteVO {
    
    /**
     * 收藏ID
     */
    private Long id;
    
    /**
     * 收藏对象类型
     */
    private String targetType;
    
    /**
     * 收藏对象ID
     */
    private Long targetId;
    
    /**
     * 收藏项名称
     */
    private String name;
    
    /**
     * 收藏项标题（攻略使用）
     */
    private String title;
    
    /**
     * 收藏项图片
     */
    private String image;
    
    /**
     * 收藏项封面图片
     */
    private String coverImage;
    
    /**
     * 收藏项描述
     */
    private String description;
    
    /**
     * 收藏时间
     */
    private Date createTime;
}

