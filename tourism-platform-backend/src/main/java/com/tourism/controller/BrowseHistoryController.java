package com.tourism.controller;

import com.tourism.entity.BrowseHistory;
import com.tourism.service.BrowseHistoryService;
import com.tourism.utils.JwtUtil;
import com.tourism.vo.PageVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 浏览记录控制器
 */
@RestController
@RequestMapping("/browse-history")
@Api(tags = "浏览记录管理")
public class BrowseHistoryController {
    
    @Autowired
    private BrowseHistoryService browseHistoryService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping
    @ApiOperation("添加浏览记录")
    public Result<?> addBrowseHistory(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        browseHistoryService.addBrowseHistory(userId, targetType, targetId);
        return Result.success("记录成功");
    }
    
    @GetMapping
    @ApiOperation("获取浏览记录列表")
    public Result<PageVO<BrowseHistory>> getBrowseHistory(
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        PageVO<BrowseHistory> pageVO = browseHistoryService.getBrowseHistoryList(userId, targetType, page, size);
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

