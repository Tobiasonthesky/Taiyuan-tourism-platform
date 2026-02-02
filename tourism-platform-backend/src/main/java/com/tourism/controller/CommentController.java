package com.tourism.controller;

import com.tourism.dto.CommentAddDTO;
import com.tourism.entity.Comment;
import com.tourism.service.CommentService;
import com.tourism.utils.JwtUtil;
import com.tourism.vo.CommentVO;
import com.tourism.vo.PageVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/comments")
@Api(tags = "评论管理")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping
    @ApiOperation("添加评论")
    public Result<Comment> addComment(@Validated @RequestBody CommentAddDTO dto, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        Comment comment = commentService.addComment(
                userId,
                dto.getTargetType(),
                dto.getTargetId(),
                dto.getContent(),
                dto.getRating(),
                dto.getRatingDetail(),
                dto.getImages()
        );
        return Result.success("评论成功", comment);
    }
    
    @GetMapping
    @ApiOperation("获取评论列表")
    public Result<PageVO<CommentVO>> getComments(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<CommentVO> pageVO = commentService.getCommentList(targetType, targetId, page, size);
        return Result.success(pageVO);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除评论")
    public Result<?> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        commentService.deleteComment(id, userId);
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

