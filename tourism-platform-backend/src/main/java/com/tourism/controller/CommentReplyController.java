package com.tourism.controller;

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
 * ?
 */
@RestController
@RequestMapping("/comments/{commentId}/replies")
@Api(tags = "")
public class CommentReplyController {
    
    @Autowired
    private CommentReplyService replyService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping
    @ApiOperation("")
    public Result<CommentReply> addReply(
            @PathVariable Long commentId,
            @Validated @RequestBody CommentReplyAddDTO dto,
            HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        CommentReply reply = replyService.addReply(commentId, userId, dto.getReplyToId(), dto.getContent());
        return Result.success("", reply);
    }
    
    @GetMapping
    @ApiOperation("")
    public Result<List<CommentReplyVO>> getReplies(@PathVariable Long commentId) {
        List<CommentReplyVO> replies = replyService.getReplyList(commentId);
        return Result.success(replies);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("")
    public Result<?> deleteReply(@PathVariable Long commentId, @PathVariable Long id, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        replyService.deleteReply(commentId, id, userId);
        return Result.success("");
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

