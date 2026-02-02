package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 美食图片实体类
 */
@Data
@TableName("food_image")
public class FoodImage {
    
    /**
     * 图片ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 美食ID
     */
    private Long foodId;
    
    /**
     * 图片URL
     */
    private String imageUrl;
    
    /**
     * 图片类型
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

