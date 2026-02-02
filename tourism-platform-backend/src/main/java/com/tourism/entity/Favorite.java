package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 收藏实体类
 */
@Data
@TableName("favorite")
public class Favorite {
    
    /**
     * 收藏ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 收藏对象类型：attraction-景点，food-美食，strategy-攻略，culture-文化，hotel-酒店，experience-体验项目
     */
    private String targetType;
    
    /**
     * 收藏对象ID
     */
    private Long targetId;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}

