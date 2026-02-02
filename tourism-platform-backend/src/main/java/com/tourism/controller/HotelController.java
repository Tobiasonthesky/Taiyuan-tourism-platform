package com.tourism.controller;

import com.tourism.entity.Hotel;
import com.tourism.entity.HotelRoom;
import com.tourism.service.HotelService;
import com.tourism.service.BrowseHistoryService;
import com.tourism.utils.JwtUtil;
import com.tourism.vo.PageVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 酒店控制器
 */
@RestController
@RequestMapping("/hotels")
@Api(tags = "酒店管理")
public class HotelController {
    
    @Autowired
    private HotelService hotelService;
    
    @Autowired
    private BrowseHistoryService browseHistoryService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping
    @ApiOperation("获取酒店列表")
    public Result<PageVO<Hotel>> getHotels(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer starLevel,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<Hotel> pageVO = hotelService.getHotelList(keyword, starLevel, page, size);
        return Result.success(pageVO);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取酒店详情")
    public Result<Hotel> getHotelDetail(@PathVariable Long id, HttpServletRequest request) {
        Hotel hotel = hotelService.getHotelDetail(id);
        
        // 添加浏览记录（如果用户已登录）
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                Long userId = jwtUtil.getUserIdFromToken(token);
                if (userId != null) {
                    browseHistoryService.addBrowseHistory(userId, "hotel", id);
                }
            } catch (Exception e) {
                // 忽略Token解析错误
            }
        }
        
        return Result.success(hotel);
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    @GetMapping("/{id}/rooms")
    @ApiOperation("获取酒店房间列表")
    public Result<List<HotelRoom>> getHotelRooms(@PathVariable Long id) {
        List<HotelRoom> rooms = hotelService.getHotelRooms(id);
        return Result.success(rooms);
    }
    
    @GetMapping("/search")
    @ApiOperation("搜索酒店")
    public Result<PageVO<Hotel>> searchHotels(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<Hotel> pageVO = hotelService.searchHotels(keyword, page, size);
        return Result.success(pageVO);
    }
    
    @GetMapping("/rooms/{roomId}/availability")
    @ApiOperation("检查房间可用性")
    public Result<Boolean> checkRoomAvailability(
            @PathVariable Long roomId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate) {
        boolean available = hotelService.checkRoomAvailability(roomId, checkInDate, checkOutDate);
        return Result.success(available);
    }
}

