package com.tourism.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 添加评论回复DTO
 */
@Data
public class CommentReplyAddDTO {
    
    /**
     * 回复的回复ID（二级回复，可为空）
     */
    private Long replyToId;
    
    /**
     * 回复内容
     */
    @NotBlank(message = "回复内容不能为空")
    private String content;
}

