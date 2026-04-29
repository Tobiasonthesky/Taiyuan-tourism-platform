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
 * ? */
@RestController
@RequestMapping("/map")
@Api(tags = "")
public class MapController {
    
    @Autowired
    private MapService mapService;
    
    @GetMapping("/attractions")
    @ApiOperation("")
    public Result<List<Map<String, Object>>> getAttractionMapData() {
        List<Map<String, Object>> mapData = mapService.getAttractionMapData();
        return Result.success(mapData);
    }
    
    @GetMapping("/restaurants")
    @ApiOperation("")
    public Result<List<Map<String, Object>>> getRestaurantMapData() {
        List<Map<String, Object>> mapData = mapService.getRestaurantMapData();
        return Result.success(mapData);
    }
    
    @GetMapping("/hotels")
    @ApiOperation("")
    public Result<List<Map<String, Object>>> getHotelMapData() {
        List<Map<String, Object>> mapData = mapService.getHotelMapData();
        return Result.success(mapData);
    }
    
    @GetMapping("/cultures")
    @ApiOperation("")
    public Result<List<Map<String, Object>>> getCultureMapData() {
        List<Map<String, Object>> mapData = mapService.getCultureMapData();
        return Result.success(mapData);
    }
    
    @GetMapping("/all")
    @ApiOperation("")
    public Result<Map<String, List<Map<String, Object>>>> getAllMapData() {
        Map<String, List<Map<String, Object>>> mapData = mapService.getAllMapData();
        return Result.success(mapData);
    }
    
    @PostMapping("/route")
    @ApiOperation("")
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
    @ApiOperation("")
    public Result<List<Map<String, Object>>> searchPlace(
            @RequestParam String keyword,
            @RequestParam(required = false) String city) {
        List<Map<String, Object>> places = mapService.searchPlace(keyword, city);
        return Result.success(places);
    }
}

