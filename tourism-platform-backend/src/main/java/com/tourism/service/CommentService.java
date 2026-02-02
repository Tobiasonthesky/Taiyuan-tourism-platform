package com.tourism.service;

import com.tourism.entity.Comment;
import com.tourism.vo.CommentVO;
import com.tourism.vo.PageVO;

/**
 * 评论服务接口
 */
public interface CommentService {
    
    /**
     * 添加评论
     */
    Comment addComment(Long userId, String targetType, Long targetId, String content, 
                      java.math.BigDecimal rating, String ratingDetail, String images);
    
    /**
     * 获取评论列表（包含用户名）
     */
    PageVO<CommentVO> getCommentList(String targetType, Long targetId, Integer page, Integer size);
    
    /**
     * 删除评论
     */
    void deleteComment(Long commentId, Long userId);
}

