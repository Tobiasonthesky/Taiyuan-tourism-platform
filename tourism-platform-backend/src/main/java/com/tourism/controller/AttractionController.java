package com.tourism.controller;

import com.tourism.entity.Attraction;
import com.tourism.mapper.AttractionMapper;
import com.tourism.service.AttractionService;
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
 * 景点控制器
 */
@RestController
@RequestMapping("/attractions")
@Api(tags = "景点管理")
public class AttractionController {
    
    @Autowired
    private AttractionService attractionService;
    
    @Autowired
    private BrowseHistoryService browseHistoryService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AttractionMapper attractionMapper;
    
    @PostMapping("/submit")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("用户提交景点（待审核）")
    public Result<Attraction> submitAttraction(@RequestBody Map<String, Object> attractionData, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        // 将前端传来的Map转换为Attraction实体，处理address和经纬度字段
        Attraction attraction = convertMapToAttraction(attractionData);
        
        attraction.setUserId(userId);
        attraction.setStatus(0); // 待审核
        attraction.setViewCount(0);
        attraction.setCommentCount(0);
        attraction.setFavoriteCount(0);
        
        System.out.println("用户提交景点 - 接收到的数据: " + attractionData);
        System.out.println("用户提交景点 - 地址: " + attraction.getAddress() + ", 经度: " + attraction.getLongitude() + ", 纬度: " + attraction.getLatitude());
        
        attractionMapper.insert(attraction);
        
        return Result.success(attraction);
    }
    
    /**
     * 将前端传来的Map转换为Attraction实体，处理address和经纬度字段
     */
    private Attraction convertMapToAttraction(Map<String, Object> attractionData) {
        Attraction attraction = new Attraction();
        
        // 手动设置字段
        if (attractionData.containsKey("id") && attractionData.get("id") != null) {
            attraction.setId(Long.valueOf(attractionData.get("id").toString()));
        }
        if (attractionData.containsKey("name")) {
            attraction.setName(attractionData.get("name") != null ? attractionData.get("name").toString() : null);
        }
        if (attractionData.containsKey("description")) {
            attraction.setDescription(attractionData.get("description") != null ? attractionData.get("description").toString() : null);
        }
        if (attractionData.containsKey("coverImage")) {
            attraction.setCoverImage(attractionData.get("coverImage") != null ? attractionData.get("coverImage").toString() : null);
        }
        if (attractionData.containsKey("content")) {
            attraction.setContent(attractionData.get("content") != null ? attractionData.get("content").toString() : null);
        }
        if (attractionData.containsKey("categoryId") && attractionData.get("categoryId") != null) {
            attraction.setCategoryId(Long.valueOf(attractionData.get("categoryId").toString()));
        }
        if (attractionData.containsKey("videoUrl")) {
            attraction.setVideoUrl(attractionData.get("videoUrl") != null ? attractionData.get("videoUrl").toString() : null);
        }
        if (attractionData.containsKey("tags")) {
            attraction.setTags(attractionData.get("tags") != null ? attractionData.get("tags").toString() : null);
        }
        if (attractionData.containsKey("openingHours")) {
            attraction.setOpeningHours(attractionData.get("openingHours") != null ? attractionData.get("openingHours").toString() : null);
        }
        if (attractionData.containsKey("ticketPrice") && attractionData.get("ticketPrice") != null) {
            try {
                attraction.setTicketPrice(new BigDecimal(attractionData.get("ticketPrice").toString()));
            } catch (Exception e) {
                // 忽略转换错误
            }
        }
        
        // 处理address和经纬度字段
        String address = null;
        String longitude = null;
        String latitude = null;
        
        if (attractionData.containsKey("address")) {
            Object addressObj = attractionData.get("address");
            if (addressObj != null) {
                String addressStr = addressObj.toString().trim();
                if (!addressStr.isEmpty()) {
                    address = addressStr;
                }
            }
        }
        
        if (attractionData.containsKey("longitude")) {
            Object longitudeObj = attractionData.get("longitude");
            if (longitudeObj != null) {
                String longitudeStr = longitudeObj.toString().trim();
                if (!longitudeStr.isEmpty()) {
                    longitude = longitudeStr;
                }
            }
        }
        
        if (attractionData.containsKey("latitude")) {
            Object latitudeObj = attractionData.get("latitude");
            if (latitudeObj != null) {
                String latitudeStr = latitudeObj.toString().trim();
                if (!latitudeStr.isEmpty()) {
                    latitude = latitudeStr;
                }
            }
        }
        
        // 设置地址和经纬度
        attraction.setAddress(address);
        if (longitude != null && !longitude.isEmpty()) {
            try {
                attraction.setLongitude(new BigDecimal(longitude));
            } catch (Exception e) {
                System.err.println("转换经度失败: " + longitude);
            }
        }
        if (latitude != null && !latitude.isEmpty()) {
            try {
                attraction.setLatitude(new BigDecimal(latitude));
            } catch (Exception e) {
                System.err.println("转换纬度失败: " + latitude);
            }
        }
        
        return attraction;
    }
    
    @GetMapping
    @ApiOperation("获取景点列表")
    public Result<PageVO<Attraction>> getAttractions(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<Attraction> pageVO = attractionService.getAttractionList(categoryId, keyword, page, size);
        return Result.success(pageVO);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取景点详情")
    public Result<Attraction> getAttractionDetail(@PathVariable Long id, HttpServletRequest request) {
        Attraction attraction = attractionService.getAttractionDetail(id);
        
        // 添加浏览记录（如果用户已登录）
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                Long userId = jwtUtil.getUserIdFromToken(token);
                if (userId != null) {
                    browseHistoryService.addBrowseHistory(userId, "attraction", id);
                }
            } catch (Exception e) {
                // 忽略Token解析错误
            }
        }
        
        return Result.success(attraction);
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    @GetMapping("/search")
    @ApiOperation("搜索景点")
    public Result<PageVO<Attraction>> searchAttractions(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<Attraction> pageVO = attractionService.searchAttractions(keyword, page, size);
        return Result.success(pageVO);
    }
}

