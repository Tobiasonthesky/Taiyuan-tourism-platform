package com.tourism.service;

import com.tourism.entity.CommentReply;

import java.util.List;

/**
 * 评论回复服务接口
 */
public interface CommentReplyService {
    
    /**
     * 添加回复
     */
    CommentReply addReply(Long commentId, Long userId, Long replyToId, String content);
    
    /**
     * 获取回复列表
     */
    List<CommentReply> getReplyList(Long commentId);
    
    /**
     * 删除回复
     */
    void deleteReply(Long replyId, Long userId);
}

