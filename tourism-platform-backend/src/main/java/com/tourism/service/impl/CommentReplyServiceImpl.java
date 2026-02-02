package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.Comment;
import com.tourism.entity.CommentReply;
import com.tourism.exception.BusinessException;
import com.tourism.mapper.CommentMapper;
import com.tourism.mapper.CommentReplyMapper;
import com.tourism.service.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论回复服务实现类
 */
@Service
public class CommentReplyServiceImpl implements CommentReplyService {
    
    @Autowired
    private CommentReplyMapper replyMapper;
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentReply addReply(Long commentId, Long userId, Long replyToId, String content) {
        // 检查评论是否存在
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        
        CommentReply reply = new CommentReply();
        reply.setCommentId(commentId);
        reply.setUserId(userId);
        reply.setReplyToId(replyToId);
        reply.setContent(content);
        reply.setStatus(1); // 默认通过审核
        
        replyMapper.insert(reply);
        
        // 更新评论的回复数
        comment.setReplyCount(comment.getReplyCount() + 1);
        commentMapper.updateById(comment);
        
        return reply;
    }
    
    @Override
    public List<CommentReply> getReplyList(Long commentId) {
        LambdaQueryWrapper<CommentReply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentReply::getCommentId, commentId);
        wrapper.eq(CommentReply::getStatus, 1);
        wrapper.orderByAsc(CommentReply::getCreateTime);
        return replyMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReply(Long replyId, Long userId) {
        CommentReply reply = replyMapper.selectById(replyId);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }
        if (!reply.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此回复");
        }
        replyMapper.deleteById(replyId);
        
        // 更新评论的回复数
        Comment comment = commentMapper.selectById(reply.getCommentId());
        if (comment != null && comment.getReplyCount() > 0) {
            comment.setReplyCount(comment.getReplyCount() - 1);
            commentMapper.updateById(comment);
        }
    }
}

