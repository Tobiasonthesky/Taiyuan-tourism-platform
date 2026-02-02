package com.tourism.controller;

import com.tourism.entity.Experience;
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
 * 特色体验项目控制器
 */
@RestController
@RequestMapping("/experiences")
@Api(tags = "特色体验项目管理")
public class ExperienceController {
    
    @Autowired
    private com.tourism.service.ExperienceService experienceService;
    
    @Autowired
    private BrowseHistoryService browseHistoryService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping
    @ApiOperation("获取体验项目列表")
    public Result<PageVO<Experience>> getExperiences(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<Experience> pageVO = experienceService.getExperienceList(keyword, page, size);
        return Result.success(pageVO);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取体验项目详情")
    public Result<Experience> getExperienceDetail(@PathVariable Long id, HttpServletRequest request) {
        Experience experience = experienceService.getExperienceDetail(id);
        
        // 添加浏览记录（如果用户已登录）
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                Long userId = jwtUtil.getUserIdFromToken(token);
                if (userId != null) {
                    browseHistoryService.addBrowseHistory(userId, "experience", id);
                }
            } catch (Exception e) {
                // 忽略Token解析错误
            }
        }
        
        return Result.success(experience);
    }
    
    @GetMapping("/search")
    @ApiOperation("搜索体验项目")
    public Result<PageVO<Experience>> searchExperiences(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<Experience> pageVO = experienceService.searchExperiences(keyword, page, size);
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

