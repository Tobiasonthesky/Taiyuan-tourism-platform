package com.tourism.controller;

import com.tourism.service.MapService;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 地图控制器
 */
@RestController
@RequestMapping("/map")
@Api(tags = "地图服务")
public class MapController {
    
    @Autowired
    private MapService mapService;
    
    @GetMapping("/attractions")
    @ApiOperation("获取景点地图数据")
    public Result<List<Map<String, Object>>> getAttractionMapData() {
        List<Map<String, Object>> mapData = mapService.getAttractionMapData();
        return Result.success(mapData);
    }
    
    @GetMapping("/restaurants")
    @ApiOperation("获取餐厅地图数据")
    public Result<List<Map<String, Object>>> getRestaurantMapData() {
        List<Map<String, Object>> mapData = mapService.getRestaurantMapData();
        return Result.success(mapData);
    }
    
    @GetMapping("/hotels")
    @ApiOperation("获取酒店地图数据")
    public Result<List<Map<String, Object>>> getHotelMapData() {
        List<Map<String, Object>> mapData = mapService.getHotelMapData();
        return Result.success(mapData);
    }
    
    @GetMapping("/cultures")
    @ApiOperation("获取文化地图数据")
    public Result<List<Map<String, Object>>> getCultureMapData() {
        List<Map<String, Object>> mapData = mapService.getCultureMapData();
        return Result.success(mapData);
    }
    
    @GetMapping("/all")
    @ApiOperation("获取所有地图数据（景点、餐厅、酒店、文化）")
    public Result<Map<String, List<Map<String, Object>>>> getAllMapData() {
        Map<String, List<Map<String, Object>>> mapData = mapService.getAllMapData();
        return Result.success(mapData);
    }
    
    @PostMapping("/route")
    @ApiOperation("路线规划")
    public Result<Map<String, Object>> planRoute(
            @RequestParam Double originLng,
            @RequestParam Double originLat,
            @RequestParam Double destLng,
            @RequestParam Double destLat,
            @RequestParam(required = false, defaultValue = "driving") String mode) {
        Map<String, Object> route = mapService.planRoute(originLng, originLat, destLng, destLat, mode);
        return Result.success(route);
    }
    
    @GetMapping("/search")
    @ApiOperation("地点搜索")
    public Result<List<Map<String, Object>>> searchPlace(
            @RequestParam String keyword,
            @RequestParam(required = false) String city) {
        List<Map<String, Object>> places = mapService.searchPlace(keyword, city);
        return Result.success(places);
    }
}

