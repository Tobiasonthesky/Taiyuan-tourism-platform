package com.tourism.controller;

import com.tourism.entity.Favorite;
import com.tourism.service.FavoriteService;
import com.tourism.utils.JwtUtil;
import com.tourism.vo.PageVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/favorites")
@Api(tags = "收藏管理")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping
    @ApiOperation("添加收藏")
    public Result<?> addFavorite(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        favoriteService.addFavorite(userId, targetType, targetId);
        return Result.success("收藏成功");
    }
    
    @DeleteMapping
    @ApiOperation("取消收藏")
    public Result<?> removeFavorite(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        favoriteService.removeFavorite(userId, targetType, targetId);
        return Result.success("取消收藏成功");
    }
    
    @GetMapping("/check")
    @ApiOperation("检查是否已收藏")
    public Result<Boolean> checkFavorite(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        boolean isFavorite = favoriteService.isFavorite(userId, targetType, targetId);
        return Result.success(isFavorite);
    }
    
    @GetMapping
    @ApiOperation("获取收藏列表")
    public Result<PageVO<com.tourism.vo.FavoriteVO>> getFavorites(
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        PageVO<com.tourism.vo.FavoriteVO> pageVO = favoriteService.getFavoriteList(userId, targetType, page, size);
        return Result.success(pageVO);
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

