package com.tourism.controller;

import com.tourism.annotation.OperationLog;
import com.tourism.dto.CommentReplyAddDTO;
import com.tourism.entity.CommentReply;
import com.tourism.service.CommentReplyService;
import com.tourism.utils.JwtUtil;
import com.tourism.vo.CommentReplyVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 评论回复控制器
 */
@RestController
@RequestMapping("/comments/{commentId}/replies")
@Api(tags = "评论回复管理")
public class CommentReplyController {
    
    @Autowired
    private CommentReplyService replyService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping
    @ApiOperation("添加评论回复")
    @OperationLog(operationType = "新增", module = "评论管理", description = "添加评论回复")
    public Result<CommentReply> addReply(
            @PathVariable Long commentId,
            @Validated @RequestBody CommentReplyAddDTO dto,
            HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        CommentReply reply = replyService.addReply(commentId, userId, dto.getReplyToId(), dto.getContent());
        return Result.success("回复成功", reply);
    }
    
    @GetMapping
    @ApiOperation("获取评论回复列表")
    public Result<List<CommentReplyVO>> getReplies(@PathVariable Long commentId) {
        List<CommentReplyVO> replies = replyService.getReplyList(commentId);
        return Result.success(replies);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除评论回复")
    @OperationLog(operationType = "删除", module = "评论管理", description = "删除评论回复")
    public Result<?> deleteReply(@PathVariable Long commentId, @PathVariable Long id, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        replyService.deleteReply(commentId, id, userId);
        return Result.success("删除成功");
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

