package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 攻略图片实体类
 */
@Data
@TableName("strategy_image")
public class StrategyImage {
    
    /**
     * 图片ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 攻略ID
     */
    private Long strategyId;
    
    /**
     * 图片URL
     */
    private String imageUrl;
    
    /**
     * 图片类型：cover-封面，detail-详情
     */
    private String imageType;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}



