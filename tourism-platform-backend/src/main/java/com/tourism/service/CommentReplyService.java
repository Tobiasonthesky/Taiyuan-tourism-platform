package com.tourism.service;

import com.tourism.entity.CommentReply;
import com.tourism.vo.CommentReplyVO;

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
    List<CommentReplyVO> getReplyList(Long commentId);
    
    /**
     * 删除回复
     */
    void deleteReply(Long commentId, Long replyId, Long userId);
}

