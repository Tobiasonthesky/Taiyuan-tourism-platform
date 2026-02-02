package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 用户浏览记录实体类
 */
@Data
@TableName("browse_history")
public class BrowseHistory {
    
    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 浏览对象类型
     */
    private String targetType;
    
    /**
     * 浏览对象ID
     */
    private Long targetId;
    
    /**
     * 浏览时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date browseTime;
}

