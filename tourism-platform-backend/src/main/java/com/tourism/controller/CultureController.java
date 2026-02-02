package com.tourism.controller;

import com.tourism.entity.Culture;
import com.tourism.mapper.CultureMapper;
import com.tourism.service.CultureService;
import com.tourism.service.BrowseHistoryService;
import com.tourism.utils.JwtUtil;
import com.tourism.vo.PageVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 民俗文化控制器
 */
@RestController
@RequestMapping("/cultures")
@Api(tags = "民俗文化管理")
public class CultureController {
    
    @Autowired
    private CultureService cultureService;
    
    @Autowired
    private BrowseHistoryService browseHistoryService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private CultureMapper cultureMapper;
    
    @PostMapping("/submit")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("用户提交文化（待审核）")
    public Result<Culture> submitCulture(@RequestBody Map<String, Object> cultureData, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        // 将前端传来的Map转换为Culture实体，处理activityLocation和经纬度字段
        Culture culture = convertMapToCulture(cultureData);
        
        culture.setUserId(userId);
        culture.setStatus(0); // 待审核
        culture.setViewCount(0);
        culture.setCommentCount(0);
        culture.setFavoriteCount(0);
        
        System.out.println("用户提交文化 - 接收到的数据: " + cultureData);
        System.out.println("用户提交文化 - 活动地点: " + culture.getActivityLocation() + ", 经度: " + culture.getLongitude() + ", 纬度: " + culture.getLatitude());
        
        cultureMapper.insert(culture);
        
        return Result.success(culture);
    }
    
    /**
     * 将前端传来的Map转换为Culture实体，处理activityLocation和经纬度字段
     */
    private Culture convertMapToCulture(Map<String, Object> cultureData) {
        Culture culture = new Culture();
        
        // 手动设置字段
        if (cultureData.containsKey("id") && cultureData.get("id") != null) {
            culture.setId(Long.valueOf(cultureData.get("id").toString()));
        }
        if (cultureData.containsKey("name")) {
            culture.setName(cultureData.get("name") != null ? cultureData.get("name").toString() : null);
        }
        if (cultureData.containsKey("description")) {
            culture.setDescription(cultureData.get("description") != null ? cultureData.get("description").toString() : null);
        }
        if (cultureData.containsKey("coverImage")) {
            culture.setCoverImage(cultureData.get("coverImage") != null ? cultureData.get("coverImage").toString() : null);
        }
        if (cultureData.containsKey("content")) {
            culture.setContent(cultureData.get("content") != null ? cultureData.get("content").toString() : null);
        }
        if (cultureData.containsKey("categoryId") && cultureData.get("categoryId") != null) {
            culture.setCategoryId(Long.valueOf(cultureData.get("categoryId").toString()));
        }
        if (cultureData.containsKey("videoUrl")) {
            culture.setVideoUrl(cultureData.get("videoUrl") != null ? cultureData.get("videoUrl").toString() : null);
        }
        if (cultureData.containsKey("history")) {
            culture.setHistory(cultureData.get("history") != null ? cultureData.get("history").toString() : null);
        }
        if (cultureData.containsKey("activityTime")) {
            culture.setActivityTime(cultureData.get("activityTime") != null ? cultureData.get("activityTime").toString() : null);
        }
        if (cultureData.containsKey("tags")) {
            culture.setTags(cultureData.get("tags") != null ? cultureData.get("tags").toString() : null);
        }
        
        // 处理activityLocation和经纬度字段
        String activityLocation = null;
        String longitude = null;
        String latitude = null;
        
        if (cultureData.containsKey("activityLocation")) {
            Object locationObj = cultureData.get("activityLocation");
            if (locationObj != null) {
                String locationStr = locationObj.toString().trim();
                if (!locationStr.isEmpty()) {
                    activityLocation = locationStr;
                }
            }
        }
        
        if (cultureData.containsKey("longitude")) {
            Object longitudeObj = cultureData.get("longitude");
            if (longitudeObj != null) {
                String longitudeStr = longitudeObj.toString().trim();
                if (!longitudeStr.isEmpty()) {
                    longitude = longitudeStr;
                }
            }
        }
        
        if (cultureData.containsKey("latitude")) {
            Object latitudeObj = cultureData.get("latitude");
            if (latitudeObj != null) {
                String latitudeStr = latitudeObj.toString().trim();
                if (!latitudeStr.isEmpty()) {
                    latitude = latitudeStr;
                }
            }
        }
        
        // 设置活动地点和经纬度
        culture.setActivityLocation(activityLocation);
        if (longitude != null && !longitude.isEmpty()) {
            try {
                culture.setLongitude(new BigDecimal(longitude));
            } catch (Exception e) {
                System.err.println("转换经度失败: " + longitude);
            }
        }
        if (latitude != null && !latitude.isEmpty()) {
            try {
                culture.setLatitude(new BigDecimal(latitude));
            } catch (Exception e) {
                System.err.println("转换纬度失败: " + latitude);
            }
        }
        
        return culture;
    }
    
    @GetMapping
    @ApiOperation("获取文化列表")
    public Result<PageVO<Culture>> getCultures(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<Culture> pageVO = cultureService.getCultureList(categoryId, keyword, page, size);
        return Result.success(pageVO);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取文化详情")
    public Result<Culture> getCultureDetail(@PathVariable Long id, HttpServletRequest request) {
        Culture culture = cultureService.getCultureDetail(id);
        
        // 添加浏览记录（如果用户已登录）
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                Long userId = jwtUtil.getUserIdFromToken(token);
                if (userId != null) {
                    browseHistoryService.addBrowseHistory(userId, "culture", id);
                }
            } catch (Exception e) {
                // 忽略Token解析错误
            }
        }
        
        return Result.success(culture);
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

