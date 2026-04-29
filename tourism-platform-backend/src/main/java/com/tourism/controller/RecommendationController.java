package com.tourism.controller;

import com.tourism.service.RecommendationService;
import com.tourism.utils.JwtUtil;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ?
 */
@RestController
@RequestMapping("/recommendations")
@Api(tags = "")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/hot")
    @ApiOperation("")
    public Result<Map<String, ?>> getHotRecommendations(
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        Map<String, ?> result = recommendationService.getHotRecommendations(type, limit);
        return Result.success(result);
    }
    
    @GetMapping("/personalized")
    @ApiOperation("")
    public Result<Map<String, ?>> getPersonalizedRecommendations(
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            // ?
            return Result.success(recommendationService.getHotRecommendations(null, limit));
        }
        
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId != null) {
                Map<String, ?> result = recommendationService.getPersonalizedRecommendations(userId, limit);
                return Result.success(result);
            }
        } catch (Exception e) {
            // Token?
        }
        
        return Result.success(recommendationService.getHotRecommendations(null, limit));
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

