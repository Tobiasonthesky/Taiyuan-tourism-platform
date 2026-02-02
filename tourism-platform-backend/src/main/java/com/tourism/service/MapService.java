package com.tourism.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 地图服务接口
 */
public interface MapService {
    
    /**
     * 获取景点地图数据
     */
    List<Map<String, Object>> getAttractionMapData();
    
    /**
     * 获取餐厅地图数据
     */
    List<Map<String, Object>> getRestaurantMapData();
    
    /**
     * 获取酒店地图数据
     */
    List<Map<String, Object>> getHotelMapData();
    
    /**
     * 获取文化地图数据
     */
    List<Map<String, Object>> getCultureMapData();
    
    /**
     * 获取所有地图数据（景点、餐厅、酒店、文化）
     */
    Map<String, List<Map<String, Object>>> getAllMapData();
    
    /**
     * 路线规划（调用高德地图API）
     * @param originLng 起点经度
     * @param originLat 起点纬度
     * @param destLng 终点经度
     * @param destLat 终点纬度
     * @param mode 出行方式：driving-驾车, walking-步行, transit-公交
     * @return 路线规划结果
     */
    Map<String, Object> planRoute(Double originLng, Double originLat, 
                                  Double destLng, Double destLat, String mode);
    
    /**
     * 地点搜索（调用高德地图API）
     * @param keyword 搜索关键词
     * @param city 城市名称（可选）
     * @return 搜索结果
     */
    List<Map<String, Object>> searchPlace(String keyword, String city);
    
    /**
     * 地理编码：根据地址获取经纬度
     * @param address 地址
     * @param city 城市名称（可选，默认为太原）
     * @return 包含经纬度的Map，如果失败返回null
     */
    Map<String, BigDecimal> geocodeAddress(String address, String city);
}

