package com.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tourism.entity.CommentReply;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论回复Mapper接口
 */
@Mapper
public interface CommentReplyMapper extends BaseMapper<CommentReply> {
}

