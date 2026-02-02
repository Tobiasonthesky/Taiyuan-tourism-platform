package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 文化图片实体类
 */
@Data
@TableName("culture_image")
public class CultureImage {
    
    /**
     * 图片ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 文化ID
     */
    private Long cultureId;
    
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



