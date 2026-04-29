package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.Comment;
import com.tourism.entity.CommentReply;
import com.tourism.entity.User;
import com.tourism.exception.BusinessException;
import com.tourism.mapper.CommentMapper;
import com.tourism.mapper.CommentReplyMapper;
import com.tourism.mapper.UserMapper;
import com.tourism.service.CommentReplyService;
import com.tourism.vo.CommentReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 评论回复服务实现类
 */
@Service
public class CommentReplyServiceImpl implements CommentReplyService {
    
    @Autowired
    private CommentReplyMapper replyMapper;
    
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentReply addReply(Long commentId, Long userId, Long replyToId, String content) {
        // 检查评论是否存在
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        
        if (replyToId != null && replyToId > 0) {
            CommentReply replyTo = replyMapper.selectById(replyToId);
            if (replyTo == null || !commentId.equals(replyTo.getCommentId())) {
                throw new BusinessException("被回复内容不存在");
            }
        }

        CommentReply reply = new CommentReply();
        reply.setCommentId(commentId);
        reply.setUserId(userId);
        reply.setReplyToId(replyToId);
        reply.setContent(content);
        reply.setStatus(1); // 默认通过审核
        
        replyMapper.insert(reply);
        
        // 更新评论的回复数
        Integer replyCount = comment.getReplyCount() == null ? 0 : comment.getReplyCount();
        comment.setReplyCount(replyCount + 1);
        commentMapper.updateById(comment);
        
        return reply;
    }
    
    @Override
    public List<CommentReplyVO> getReplyList(Long commentId) {
        LambdaQueryWrapper<CommentReply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentReply::getCommentId, commentId);
        wrapper.eq(CommentReply::getStatus, 1);
        wrapper.orderByAsc(CommentReply::getCreateTime);
        List<CommentReply> replies = replyMapper.selectList(wrapper);
        if (replies.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        Set<Long> replyToIds = replies.stream()
                .map(CommentReply::getReplyToId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());

        Map<Long, CommentReply> replyIdMap = replyToIds.isEmpty()
                ? java.util.Collections.emptyMap()
                : replyMapper.selectBatchIds(replyToIds).stream()
                .collect(Collectors.toMap(CommentReply::getId, Function.identity(), (a, b) -> a));

        Set<Long> userIds = replies.stream()
                .map(CommentReply::getUserId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());

        replyIdMap.values().stream()
                .map(CommentReply::getUserId)
                .filter(id -> id != null && id > 0)
                .forEach(userIds::add);

        Map<Long, User> userMap = userIds.isEmpty()
                ? java.util.Collections.emptyMap()
                : userMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity(), (a, b) -> a));

        return replies.stream().map(reply -> {
            CommentReplyVO vo = new CommentReplyVO();
            vo.setId(reply.getId());
            vo.setCommentId(reply.getCommentId());
            vo.setUserId(reply.getUserId());
            vo.setReplyToId(reply.getReplyToId());
            vo.setContent(reply.getContent());
            vo.setCreateTime(reply.getCreateTime());

            User user = userMap.get(reply.getUserId());
            if (user != null) {
                vo.setUserName(user.getUsername());
                vo.setUserAvatar(user.getAvatar());
            }

            if (reply.getReplyToId() != null && reply.getReplyToId() > 0) {
                CommentReply replyTo = replyIdMap.get(reply.getReplyToId());
                if (replyTo != null) {
                    vo.setReplyToUserId(replyTo.getUserId());
                    User replyToUser = userMap.get(replyTo.getUserId());
                    if (replyToUser != null) {
                        vo.setReplyToUserName(replyToUser.getUsername());
                    }
                }
            }
            return vo;
        }).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReply(Long commentId, Long replyId, Long userId) {
        CommentReply reply = replyMapper.selectById(replyId);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }
        if (!commentId.equals(reply.getCommentId())) {
            throw new BusinessException("回复与评论不匹配");
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

