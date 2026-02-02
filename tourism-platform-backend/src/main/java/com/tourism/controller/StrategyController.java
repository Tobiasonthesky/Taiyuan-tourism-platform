package com.tourism.controller;

import com.tourism.dto.StrategyGenerateDTO;
import com.tourism.entity.Strategy;
import com.tourism.entity.User;
import com.tourism.mapper.StrategyMapper;
import com.tourism.mapper.UserMapper;
import com.tourism.service.StrategyService;
import com.tourism.service.StrategyGenerateService;
import com.tourism.service.BrowseHistoryService;
import com.tourism.utils.JwtUtil;
import com.tourism.utils.RoleConstants;
import com.tourism.vo.PageVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 旅游攻略控制器
 */
@RestController
@RequestMapping("/strategies")
@Api(tags = "旅游攻略管理")
public class StrategyController {
    
    @Autowired
    private StrategyService strategyService;
    
    @Autowired
    private BrowseHistoryService browseHistoryService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private StrategyMapper strategyMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StrategyGenerateService strategyGenerateService;
    
    @PostMapping("/generate")
    @ApiOperation("AI生成攻略")
    public Result<Strategy> generateStrategy(@Validated @RequestBody StrategyGenerateDTO dto, 
                                              HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = null;
        if (token != null) {
            try {
                userId = jwtUtil.getUserIdFromToken(token);
            } catch (Exception e) {
                // 忽略Token解析错误，允许未登录用户使用
            }
        }
        
        Strategy strategy = strategyGenerateService.generateStrategy(dto, userId);
        return Result.success("攻略生成成功", strategy);
    }
    
    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("提交攻略（管理员直接通过，用户待审核）")
    public Result<Strategy> submitStrategy(@RequestBody Strategy strategy, HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            if (token == null) {
                return Result.error("未登录，请先登录");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error("用户信息无效，请重新登录");
            }
            
            // 查询用户信息，判断是否为管理员
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 设置用户ID
            strategy.setUserId(userId);
            
            // 根据用户角色设置状态：管理员直接通过（status=1），普通用户待审核（status=0）
            boolean isAdmin = RoleConstants.isAdmin(user.getRole());
            strategy.setStatus(isAdmin ? 1 : 0);
            
            // 设置统计字段
            if (strategy.getViewCount() == null) {
                strategy.setViewCount(0);
            }
            if (strategy.getCommentCount() == null) {
                strategy.setCommentCount(0);
            }
            if (strategy.getFavoriteCount() == null) {
                strategy.setFavoriteCount(0);
            }
            if (strategy.getLikeCount() == null) {
                strategy.setLikeCount(0);
            }
            if (strategy.getIsRecommend() == null) {
                strategy.setIsRecommend(0);
            }
            
            // 确保分类不为空（安全处理 null 和空字符串）
            String category = strategy.getCategory();
            if (category == null || category.isEmpty() || category.trim().isEmpty()) {
                if (strategy.getDuration() != null) {
                    if (strategy.getDuration() == 1) {
                        strategy.setCategory("1day");
                    } else if (strategy.getDuration() == 2) {
                        strategy.setCategory("2day");
                    } else {
                        strategy.setCategory("theme");
                    }
                } else {
                    strategy.setCategory("theme");
                }
            } else {
                // 确保分类值有效
                category = category.trim();
                if (!"1day".equals(category) && !"2day".equals(category) && !"theme".equals(category)) {
                    // 如果分类值无效，根据时长设置
                    if (strategy.getDuration() != null) {
                        if (strategy.getDuration() == 1) {
                            strategy.setCategory("1day");
                        } else if (strategy.getDuration() == 2) {
                            strategy.setCategory("2day");
                        } else {
                            strategy.setCategory("theme");
                        }
                    } else {
                        strategy.setCategory("theme");
                    }
                } else {
                    strategy.setCategory(category);
                }
            }
            
            // 处理可能为空的字符串字段，将空字符串转为 null
            if (strategy.getTheme() != null) {
                String theme = strategy.getTheme().trim();
                strategy.setTheme(theme.isEmpty() ? null : theme);
            }
            if (strategy.getCoverImage() != null) {
                String coverImage = strategy.getCoverImage().trim();
                strategy.setCoverImage(coverImage.isEmpty() ? null : coverImage);
            }
            if (strategy.getDescription() != null) {
                String description = strategy.getDescription().trim();
                strategy.setDescription(description.isEmpty() ? null : description);
            }
            if (strategy.getContent() != null) {
                String content = strategy.getContent().trim();
                strategy.setContent(content.isEmpty() ? null : content);
            }
            if (strategy.getTips() != null) {
                String tips = strategy.getTips().trim();
                strategy.setTips(tips.isEmpty() ? null : tips);
            }
            
            // 限制 bestSeason 字段长度（数据库字段限制为50字符）
            if (strategy.getBestSeason() != null) {
                String bestSeason = strategy.getBestSeason().trim();
                if (bestSeason.isEmpty()) {
                    strategy.setBestSeason(null);
                } else if (bestSeason.length() > 50) {
                    strategy.setBestSeason(bestSeason.substring(0, 50));
                } else {
                    strategy.setBestSeason(bestSeason);
                }
            }
            
            // 插入数据库
            strategyMapper.insert(strategy);
            
            // 根据用户角色返回不同的提示信息
            String message = isAdmin ? "攻略保存成功，已直接发布" : "攻略提交成功，等待审核";
            return Result.success(message, strategy);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提交失败：" + e.getMessage());
        }
    }
    
    @GetMapping
    @ApiOperation("获取攻略列表")
    public Result<PageVO<Strategy>> getStrategies(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) Boolean isRecommend,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<Strategy> pageVO = strategyService.getStrategyList(category, theme, isRecommend, page, size);
        return Result.success(pageVO);
    }
    
    @GetMapping("/{id:[0-9]+}")
    @ApiOperation("获取攻略详情")
    public Result<Strategy> getStrategyDetail(@PathVariable Long id, HttpServletRequest request) {
        Strategy strategy = strategyService.getStrategyDetail(id);
        
        // 添加浏览记录（如果用户已登录）
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                Long userId = jwtUtil.getUserIdFromToken(token);
                if (userId != null) {
                    browseHistoryService.addBrowseHistory(userId, "strategy", id);
                }
            } catch (Exception e) {
                // 忽略Token解析错误
            }
        }
        
        return Result.success(strategy);
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

