package com.tourism.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 添加评论DTO
 */
@Data
public class CommentAddDTO {
    
    /**
     * 评论对象类型：attraction-景点，food-美食，hotel-酒店，culture-文化
     */
    @NotBlank(message = "评论对象类型不能为空")
    private String targetType;
    
    /**
     * 评论对象ID
     */
    @NotNull(message = "评论对象ID不能为空")
    private Long targetId;
    
    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
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
}

