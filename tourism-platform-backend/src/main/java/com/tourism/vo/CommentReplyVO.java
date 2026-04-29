package com.tourism.vo;

import lombok.Data;

import java.util.Date;

/**
 * 评论回复视图对象
 */
@Data
public class CommentReplyVO {

    /**
     * 回复ID
     */
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
     * 回复用户名
     */
    private String userName;

    /**
     * 回复用户头像
     */
    private String userAvatar;

    /**
     * 回复的回复ID
     */
    private Long replyToId;

    /**
     * 被回复用户ID
     */
    private Long replyToUserId;

    /**
     * 被回复用户名
     */
    private String replyToUserName;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;
}
