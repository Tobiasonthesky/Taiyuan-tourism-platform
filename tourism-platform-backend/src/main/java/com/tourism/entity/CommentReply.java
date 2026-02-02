package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 评论回复实体类
 */
@Data
@TableName("comment_reply")
public class CommentReply {
    
    /**
     * 回复ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 评论ID
     */
    private Long commentId;
    
    /**
     * 回复用户ID
     */
    private Long userId;
    
    /**
     * 回复的回复ID（二级回复）
     */
    private Long replyToId;
    
    /**
     * 回复内容
     */
    private String content;
    
    /**
     * 状态：0-待审核，1-已通过，2-已拒绝
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}

