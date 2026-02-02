package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.*;
import com.tourism.exception.BusinessException;
import com.tourism.mapper.*;
import com.tourism.service.CommentService;
import com.tourism.utils.PageUtil;
import com.tourism.vo.CommentVO;
import com.tourism.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private AttractionMapper attractionMapper;
    
    @Autowired
    private FoodMapper foodMapper;
    
    @Autowired
    private HotelMapper hotelMapper;
    
    @Autowired
    private CultureMapper cultureMapper;
    
    @Autowired
    private StrategyMapper strategyMapper;
    
    @Autowired
    private ExperienceMapper experienceMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment addComment(Long userId, String targetType, Long targetId, String content,
                              BigDecimal rating, String ratingDetail, String images) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setTargetType(targetType);
        comment.setTargetId(targetId);
        comment.setContent(content);
        comment.setRating(rating);
        comment.setRatingDetail(ratingDetail);
        comment.setImages(images);
        comment.setStatus(1); // 默认通过审核
        comment.setIsTop(0);
        comment.setLikeCount(0);
        comment.setReplyCount(0);
        
        commentMapper.insert(comment);
        
        // 更新对应实体的评论数和评分
        updateTargetCommentCount(targetType, targetId, rating);
        
        return comment;
    }
    
    /**
     * 更新目标实体的评论数和评分
     */
    private void updateTargetCommentCount(String targetType, Long targetId, BigDecimal rating) {
        switch (targetType) {
            case "attraction":
                Attraction attraction = attractionMapper.selectById(targetId);
                if (attraction != null) {
                    attraction.setCommentCount((attraction.getCommentCount() == null ? 0 : attraction.getCommentCount()) + 1);
                    // 更新评分（简单平均）
                    updateRating(attraction, rating, "attraction");
                    attractionMapper.updateById(attraction);
                }
                break;
            case "food":
                Food food = foodMapper.selectById(targetId);
                if (food != null) {
                    food.setCommentCount((food.getCommentCount() == null ? 0 : food.getCommentCount()) + 1);
                    updateRating(food, rating, "food");
                    foodMapper.updateById(food);
                }
                break;
            case "hotel":
                Hotel hotel = hotelMapper.selectById(targetId);
                if (hotel != null) {
                    hotel.setCommentCount((hotel.getCommentCount() == null ? 0 : hotel.getCommentCount()) + 1);
                    updateRating(hotel, rating, "hotel");
                    hotelMapper.updateById(hotel);
                }
                break;
            case "culture":
                Culture culture = cultureMapper.selectById(targetId);
                if (culture != null) {
                    culture.setCommentCount((culture.getCommentCount() == null ? 0 : culture.getCommentCount()) + 1);
                    updateRating(culture, rating, "culture");
                    cultureMapper.updateById(culture);
                }
                break;
            case "strategy":
                Strategy strategy = strategyMapper.selectById(targetId);
                if (strategy != null) {
                    strategy.setCommentCount((strategy.getCommentCount() == null ? 0 : strategy.getCommentCount()) + 1);
                    // Strategy没有rating字段，只更新评论数
                    strategyMapper.updateById(strategy);
                }
                break;
            case "experience":
                Experience experience = experienceMapper.selectById(targetId);
                if (experience != null) {
                    experience.setCommentCount((experience.getCommentCount() == null ? 0 : experience.getCommentCount()) + 1);
                    updateRating(experience, rating, "experience");
                    experienceMapper.updateById(experience);
                }
                break;
        }
    }
    
    /**
     * 更新评分（计算平均分）
     */
    private void updateRating(Object entity, BigDecimal newRating, String type) {
        if (newRating == null) {
            return;
        }
        
        // 获取当前评论数
        int commentCount = 0;
        BigDecimal currentRating = BigDecimal.ZERO;
        
        if (entity instanceof Attraction) {
            Attraction att = (Attraction) entity;
            commentCount = att.getCommentCount() == null ? 0 : att.getCommentCount();
            currentRating = att.getRating() == null ? BigDecimal.ZERO : att.getRating();
            if (commentCount > 0) {
                // 计算新的平均分：(当前平均分 * (评论数-1) + 新评分) / 评论数
                BigDecimal newAvg = currentRating.multiply(BigDecimal.valueOf(commentCount - 1))
                        .add(newRating)
                        .divide(BigDecimal.valueOf(commentCount), 2, BigDecimal.ROUND_HALF_UP);
                att.setRating(newAvg);
            } else {
                att.setRating(newRating);
            }
        } else if (entity instanceof Food) {
            Food food = (Food) entity;
            commentCount = food.getCommentCount() == null ? 0 : food.getCommentCount();
            currentRating = food.getRating() == null ? BigDecimal.ZERO : food.getRating();
            if (commentCount > 0) {
                BigDecimal newAvg = currentRating.multiply(BigDecimal.valueOf(commentCount - 1))
                        .add(newRating)
                        .divide(BigDecimal.valueOf(commentCount), 2, BigDecimal.ROUND_HALF_UP);
                food.setRating(newAvg);
            } else {
                food.setRating(newRating);
            }
        } else if (entity instanceof Hotel) {
            Hotel hotel = (Hotel) entity;
            commentCount = hotel.getCommentCount() == null ? 0 : hotel.getCommentCount();
            currentRating = hotel.getRating() == null ? BigDecimal.ZERO : hotel.getRating();
            if (commentCount > 0) {
                BigDecimal newAvg = currentRating.multiply(BigDecimal.valueOf(commentCount - 1))
                        .add(newRating)
                        .divide(BigDecimal.valueOf(commentCount), 2, BigDecimal.ROUND_HALF_UP);
                hotel.setRating(newAvg);
            } else {
                hotel.setRating(newRating);
            }
        } else if (entity instanceof Culture) {
            Culture culture = (Culture) entity;
            commentCount = culture.getCommentCount() == null ? 0 : culture.getCommentCount();
            currentRating = culture.getRating() == null ? BigDecimal.ZERO : culture.getRating();
            if (commentCount > 0) {
                BigDecimal newAvg = currentRating.multiply(BigDecimal.valueOf(commentCount - 1))
                        .add(newRating)
                        .divide(BigDecimal.valueOf(commentCount), 2, BigDecimal.ROUND_HALF_UP);
                culture.setRating(newAvg);
            } else {
                culture.setRating(newRating);
            }
        } else if (entity instanceof Experience) {
            Experience experience = (Experience) entity;
            commentCount = experience.getCommentCount() == null ? 0 : experience.getCommentCount();
            currentRating = experience.getRating() == null ? BigDecimal.ZERO : experience.getRating();
            if (commentCount > 0) {
                BigDecimal newAvg = currentRating.multiply(BigDecimal.valueOf(commentCount - 1))
                        .add(newRating)
                        .divide(BigDecimal.valueOf(commentCount), 2, BigDecimal.ROUND_HALF_UP);
                experience.setRating(newAvg);
            } else {
                experience.setRating(newRating);
            }
        }
    }
    
    @Override
    public PageVO<CommentVO> getCommentList(String targetType, Long targetId, Integer page, Integer size) {
        Page<Comment> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getTargetType, targetType);
        wrapper.eq(Comment::getTargetId, targetId);
        wrapper.eq(Comment::getStatus, 1);
        wrapper.orderByDesc(Comment::getIsTop);
        wrapper.orderByDesc(Comment::getCreateTime);
        
        Page<Comment> result = commentMapper.selectPage(pageParam, wrapper);
        
        // 转换为CommentVO，填充用户名
        List<CommentVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                voList
        );
    }
    
    /**
     * 将Comment转换为CommentVO，填充用户名
     */
    private CommentVO convertToVO(Comment comment) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setUserId(comment.getUserId());
        vo.setTargetType(comment.getTargetType());
        vo.setTargetId(comment.getTargetId());
        vo.setContent(comment.getContent());
        vo.setRating(comment.getRating());
        vo.setRatingDetail(comment.getRatingDetail());
        vo.setImages(comment.getImages());
        vo.setLikeCount(comment.getLikeCount());
        vo.setReplyCount(comment.getReplyCount());
        vo.setStatus(comment.getStatus());
        vo.setIsTop(comment.getIsTop());
        vo.setCreateTime(comment.getCreateTime());
        vo.setUpdateTime(comment.getUpdateTime());
        
        // 查询用户信息，填充用户名和头像
        if (comment.getUserId() != null) {
            User user = userMapper.selectById(comment.getUserId());
            if (user != null) {
                vo.setUserName(user.getUsername()); // 使用用户名
                vo.setUserAvatar(user.getAvatar());
            }
        }
        
        return vo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此评论");
        }
        
        // 减少对应实体的评论数
        decreaseTargetCommentCount(comment.getTargetType(), comment.getTargetId());
        
        commentMapper.deleteById(commentId);
    }
    
    /**
     * 减少目标实体的评论数
     */
    private void decreaseTargetCommentCount(String targetType, Long targetId) {
        switch (targetType) {
            case "attraction":
                Attraction attraction = attractionMapper.selectById(targetId);
                if (attraction != null && attraction.getCommentCount() != null && attraction.getCommentCount() > 0) {
                    attraction.setCommentCount(attraction.getCommentCount() - 1);
                    attractionMapper.updateById(attraction);
                }
                break;
            case "food":
                Food food = foodMapper.selectById(targetId);
                if (food != null && food.getCommentCount() != null && food.getCommentCount() > 0) {
                    food.setCommentCount(food.getCommentCount() - 1);
                    foodMapper.updateById(food);
                }
                break;
            case "hotel":
                Hotel hotel = hotelMapper.selectById(targetId);
                if (hotel != null && hotel.getCommentCount() != null && hotel.getCommentCount() > 0) {
                    hotel.setCommentCount(hotel.getCommentCount() - 1);
                    hotelMapper.updateById(hotel);
                }
                break;
            case "culture":
                Culture culture = cultureMapper.selectById(targetId);
                if (culture != null && culture.getCommentCount() != null && culture.getCommentCount() > 0) {
                    culture.setCommentCount(culture.getCommentCount() - 1);
                    cultureMapper.updateById(culture);
                }
                break;
            case "strategy":
                Strategy strategy = strategyMapper.selectById(targetId);
                if (strategy != null && strategy.getCommentCount() != null && strategy.getCommentCount() > 0) {
                    strategy.setCommentCount(strategy.getCommentCount() - 1);
                    strategyMapper.updateById(strategy);
                }
                break;
            case "experience":
                Experience experience = experienceMapper.selectById(targetId);
                if (experience != null && experience.getCommentCount() != null && experience.getCommentCount() > 0) {
                    experience.setCommentCount(experience.getCommentCount() - 1);
                    experienceMapper.updateById(experience);
                }
                break;
        }
    }
}

