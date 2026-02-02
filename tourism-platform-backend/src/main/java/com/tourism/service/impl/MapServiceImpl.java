package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tourism.entity.Attraction;
import com.tourism.entity.Culture;
import com.tourism.entity.Food;
import com.tourism.entity.Hotel;
import com.tourism.mapper.AttractionMapper;
import com.tourism.mapper.CultureMapper;
import com.tourism.mapper.FoodMapper;
import com.tourism.mapper.HotelMapper;
import com.tourism.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 地图服务实现类
 */
@Service
public class MapServiceImpl implements MapService {
    
    @Autowired
    private AttractionMapper attractionMapper;
    
    @Autowired
    private FoodMapper foodMapper;
    
    @Autowired
    private HotelMapper hotelMapper;
    
    @Autowired
    private CultureMapper cultureMapper;
    
    @Autowired(required = false)
    private RestTemplate restTemplate;
    
    @Value("${map.api.key:}")
    private String mapApiKey;
    
    @Value("${map.api.type:amap}")
    private String mapApiType; // amap-高德, baidu-百度
    
    // 高德地图API基础URL
    private static final String AMAP_BASE_URL = "https://restapi.amap.com/v3";
    
    @Override
    public List<Map<String, Object>> getAttractionMapData() {
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attraction::getStatus, 1);
        wrapper.isNotNull(Attraction::getLongitude);
        wrapper.isNotNull(Attraction::getLatitude);
        List<Attraction> attractions = attractionMapper.selectList(wrapper);
        
        return attractions.stream().map(attraction -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", attraction.getId());
            data.put("name", attraction.getName());
            data.put("longitude", attraction.getLongitude());
            data.put("latitude", attraction.getLatitude());
            data.put("address", attraction.getAddress());
            data.put("coverImage", attraction.getCoverImage());
            data.put("type", "attraction");
            return data;
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<Map<String, Object>> getRestaurantMapData() {
        List<Map<String, Object>> restaurantList = new ArrayList<>();
        
        // 从美食的推荐餐厅中提取餐厅信息
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getStatus, 1);
        wrapper.isNotNull(Food::getRecommendedRestaurants);
        List<Food> foods = foodMapper.selectList(wrapper);
        
        for (Food food : foods) {
            try {
                if (food.getRecommendedRestaurants() != null && !food.getRecommendedRestaurants().isEmpty()) {
                    JSONArray restaurants = JSON.parseArray(food.getRecommendedRestaurants());
                    for (int i = 0; i < restaurants.size(); i++) {
                        JSONObject restaurant = restaurants.getJSONObject(i);
                        if (restaurant.containsKey("longitude") && restaurant.containsKey("latitude")) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("id", "restaurant_" + food.getId() + "_" + i);
                            data.put("name", restaurant.getString("name") != null ? 
                                    restaurant.getString("name") : food.getName() + "推荐餐厅");
                            data.put("longitude", restaurant.getBigDecimal("longitude"));
                            data.put("latitude", restaurant.getBigDecimal("latitude"));
                            data.put("address", restaurant.getString("address"));
                            data.put("coverImage", restaurant.getString("image") != null ? 
                                    restaurant.getString("image") : food.getCoverImage());
                            data.put("type", "restaurant");
                            restaurantList.add(data);
                        }
                    }
                }
            } catch (Exception e) {
                // 忽略JSON解析错误
                continue;
            }
        }
        
        return restaurantList;
    }
    
    @Override
    public List<Map<String, Object>> getHotelMapData() {
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Hotel::getStatus, 1);
        wrapper.isNotNull(Hotel::getLongitude);
        wrapper.isNotNull(Hotel::getLatitude);
        List<Hotel> hotels = hotelMapper.selectList(wrapper);
        
        return hotels.stream().map(hotel -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", hotel.getId());
            data.put("name", hotel.getName());
            data.put("longitude", hotel.getLongitude());
            data.put("latitude", hotel.getLatitude());
            data.put("address", hotel.getAddress());
            data.put("coverImage", hotel.getCoverImage());
            data.put("type", "hotel");
            data.put("starLevel", hotel.getStarLevel());
            data.put("minPrice", hotel.getMinPrice());
            return data;
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<Map<String, Object>> getCultureMapData() {
        LambdaQueryWrapper<Culture> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Culture::getStatus, 1);
        wrapper.isNotNull(Culture::getLongitude);
        wrapper.isNotNull(Culture::getLatitude);
        List<Culture> cultures = cultureMapper.selectList(wrapper);
        
        return cultures.stream().map(culture -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", culture.getId());
            data.put("name", culture.getName());
            data.put("longitude", culture.getLongitude());
            data.put("latitude", culture.getLatitude());
            data.put("address", culture.getActivityLocation());
            data.put("coverImage", culture.getCoverImage());
            data.put("type", "culture");
            data.put("activityTime", culture.getActivityTime());
            return data;
        }).collect(Collectors.toList());
    }
    
    @Override
    public Map<String, List<Map<String, Object>>> getAllMapData() {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("attractions", getAttractionMapData());
        result.put("restaurants", getRestaurantMapData());
        result.put("hotels", getHotelMapData());
        result.put("cultures", getCultureMapData());
        return result;
    }
    
    @Override
    public Map<String, Object> planRoute(Double originLng, Double originLat, 
                                        Double destLng, Double destLat, String mode) {
        Map<String, Object> result = new HashMap<>();
        
        // 如果没有配置地图API密钥，返回基础路线信息
        if (mapApiKey == null || mapApiKey.isEmpty()) {
            double distance = calculateDistance(originLat, originLng, destLat, destLng);
            result.put("distance", distance);
            result.put("distanceText", String.format("%.2f", distance) + "公里");
            
            // 根据导航模式使用不同的速度估算时间
            int estimatedMinutes;
            if ("walking".equals(mode)) {
                // 步行速度约5公里/小时
                estimatedMinutes = (int)(distance / 5.0 * 60);
            } else if ("riding".equals(mode) || "bicycling".equals(mode)) {
                // 骑行速度约15公里/小时
                estimatedMinutes = (int)(distance / 15.0 * 60);
            } else {
                // 驾车速度约50公里/小时
                estimatedMinutes = (int)(distance / 50.0 * 60);
            }
            result.put("duration", estimatedMinutes + "分钟");
            result.put("mode", mode != null ? mode : "driving");
            result.put("message", "请配置地图API密钥以获取详细路线规划");
            return result;
        }
        
        // 调用高德地图路线规划API
        try {
            String routeType = "driving"; // 默认驾车
            if ("walking".equals(mode)) {
                routeType = "walking";
            } else if ("riding".equals(mode) || "bicycling".equals(mode)) {
                routeType = "bicycling"; // 高德地图API使用bicycling表示骑行
            }
            
            String url = String.format("%s/direction/%s?key=%s&origin=%s,%s&destination=%s,%s&extensions=all",
                    AMAP_BASE_URL, routeType, mapApiKey, originLng, originLat, destLng, destLat);
            
            if (restTemplate != null) {
                String response = restTemplate.getForObject(url, String.class);
                JSONObject jsonResponse = JSON.parseObject(response);
                
                if ("1".equals(jsonResponse.getString("status"))) {
                    JSONObject route = jsonResponse.getJSONObject("route");
                    if (route != null) {
                        JSONArray paths = route.getJSONArray("paths");
                        if (paths != null && paths.size() > 0) {
                            JSONObject path = paths.getJSONObject(0);
                            int distance = path.getIntValue("distance"); // 米
                            int duration = path.getIntValue("duration"); // 秒
                            
                            result.put("distance", distance / 1000.0); // 转换为公里
                            result.put("distanceText", String.format("%.2f", distance / 1000.0) + "公里");
                            result.put("duration", duration / 60 + "分钟");
                            result.put("durationSeconds", duration);
                            result.put("mode", mode != null ? mode : "driving");
                            
                            // 处理steps数据，确保格式正确
                            JSONArray stepsArray = path.getJSONArray("steps");
                            if (stepsArray != null && stepsArray.size() > 0) {
                                List<Map<String, Object>> stepsList = new ArrayList<>();
                                for (int i = 0; i < stepsArray.size(); i++) {
                                    JSONObject step = stepsArray.getJSONObject(i);
                                    Map<String, Object> stepMap = new HashMap<>();
                                    
                                    // 提取instruction（路线指引）
                                    String instruction = step.getString("instruction");
                                    if (instruction == null || instruction.isEmpty()) {
                                        instruction = step.getString("road");
                                    }
                                    if (instruction == null || instruction.isEmpty()) {
                                        instruction = step.getString("action");
                                    }
                                    stepMap.put("instruction", instruction != null ? instruction : "");
                                    
                                    // 提取road（道路名称）
                                    String road = step.getString("road");
                                    if (road != null && !road.isEmpty()) {
                                        stepMap.put("road", road);
                                    }
                                    
                                    // 提取distance（距离，单位：米）
                                    Integer stepDistance = step.getInteger("distance");
                                    if (stepDistance != null) {
                                        stepMap.put("distance", stepDistance);
                                    }
                                    
                                    stepsList.add(stepMap);
                                }
                                result.put("steps", stepsList);
                                System.out.println("路线规划成功，模式: " + mode + ", 步骤数: " + stepsList.size());
                            } else {
                                result.put("steps", new ArrayList<>());
                                System.out.println("警告: 路线规划成功但未找到步骤数据，模式: " + mode);
                            }
                            
                            result.put("success", true);
                            return result;
                        }
                    }
                } else {
                    String info = jsonResponse.getString("info");
                    System.err.println("高德地图API返回错误，状态: " + jsonResponse.getString("status") + ", 信息: " + info);
                }
            }
        } catch (Exception e) {
            // 如果API调用失败，返回基础信息
            System.err.println("调用高德地图API失败: " + e.getMessage());
        }
        
        // 降级处理：返回基础信息（根据模式使用不同的速度估算）
        double distance = calculateDistance(originLat, originLng, destLat, destLng);
        result.put("distance", distance);
        result.put("distanceText", String.format("%.2f", distance) + "公里");
        
        // 根据导航模式使用不同的速度估算时间
        int estimatedMinutes;
        if ("walking".equals(mode)) {
            // 步行速度约5公里/小时
            estimatedMinutes = (int)(distance / 5.0 * 60);
        } else if ("riding".equals(mode) || "bicycling".equals(mode)) {
            // 骑行速度约15公里/小时
            estimatedMinutes = (int)(distance / 15.0 * 60);
        } else {
            // 驾车速度约50公里/小时
            estimatedMinutes = (int)(distance / 50.0 * 60);
        }
        result.put("duration", estimatedMinutes + "分钟");
        result.put("mode", mode != null ? mode : "driving");
        result.put("success", false);
        result.put("message", "路线规划失败，返回估算距离和时间");
        
        return result;
    }
    
    @Override
    public List<Map<String, Object>> searchPlace(String keyword, String city) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 检查参数
        if (keyword == null || keyword.isEmpty()) {
            System.err.println("搜索关键词为空");
            return result;
        }
        
        if (mapApiKey == null || mapApiKey.isEmpty()) {
            System.err.println("地图API密钥未配置，请在application.yml中配置map.api.key");
            return result;
        }
        
        if (restTemplate == null) {
            System.err.println("RestTemplate未注入，无法调用高德地图API");
            return result;
        }
        
        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());
            // 构建搜索URL，确保城市参数正确
            String url = String.format("%s/place/text?key=%s&keywords=%s",
                    AMAP_BASE_URL, mapApiKey, encodedKeyword);
            
            // 强制指定城市，避免搜索到其他城市的结果
            if (city != null && !city.isEmpty()) {
                String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
                url += "&city=" + encodedCity;
                url += "&citylimit=true"; // 限制在当前城市内搜索
            } else {
                // 如果没有指定城市，默认使用太原
                url += "&city=太原&citylimit=true";
            }
            
            // 添加其他参数以提高搜索准确性
            url += "&offset=20"; // 每页返回20条
            url += "&page=1"; // 第一页
            url += "&extensions=all"; // 返回详细信息
            
            System.out.println("调用高德地图搜索API: " + url.replace(mapApiKey, "***"));
            
            String response = restTemplate.getForObject(url, String.class);
            
            if (response == null || response.isEmpty()) {
                System.err.println("高德地图API返回空响应");
                return result;
            }
            
            JSONObject jsonResponse = JSON.parseObject(response);
            String status = jsonResponse.getString("status");
            String info = jsonResponse.getString("info");
            
            System.out.println("高德地图API响应状态: " + status + ", 信息: " + info);
            System.out.println("完整API响应: " + response);
            
            if ("1".equals(status)) {
                JSONArray pois = jsonResponse.getJSONArray("pois");
                if (pois != null && pois.size() > 0) {
                    System.out.println("找到 " + pois.size() + " 个搜索结果");
                    
                    // 太原市的大致范围（用于过滤，但不过度严格）
                    double taiyuanMinLng = 112.0;  // 稍微放宽范围
                    double taiyuanMaxLng = 113.0;
                    double taiyuanMinLat = 37.4;
                    double taiyuanMaxLat = 38.2;
                    
                    int filteredCount = 0;
                    int skippedCount = 0;
                    for (int i = 0; i < pois.size(); i++) {
                        JSONObject poi = pois.getJSONObject(i);
                        String poiName = poi.getString("name");
                        String poiAddress = poi.getString("address");
                        String poiCity = poi.getString("cityname"); // 获取POI所在城市
                        String poiAdname = poi.getString("adname"); // 区县名称
                        
                        Map<String, Object> place = new HashMap<>();
                        place.put("id", poi.getString("id"));
                        place.put("name", poiName);
                        place.put("address", poiAddress);
                        place.put("city", poiCity);
                        place.put("adname", poiAdname);
                        
                        String location = poi.getString("location");
                        if (location != null && location.contains(",")) {
                            String[] coords = location.split(",");
                            try {
                                double lng = Double.parseDouble(coords[0]);
                                double lat = Double.parseDouble(coords[1]);
                                
                                // 检查是否在太原范围内（放宽范围）
                                boolean inTaiyuan = (lng >= taiyuanMinLng && lng <= taiyuanMaxLng &&
                                                    lat >= taiyuanMinLat && lat <= taiyuanMaxLat);
                                
                                // 城市名称检查（更严格）
                                boolean cityMatch = false;
                                if (city != null && !city.isEmpty()) {
                                    // 精确匹配城市名或区县名包含城市
                                    if ((poiCity != null && (poiCity.equals(city) || poiCity.contains(city + "市"))) || 
                                        (poiAdname != null && poiAdname.contains(city))) {
                                        cityMatch = true;
                                    }
                                }
                                
                                // 如果明确指定了城市，必须满足城市匹配或在指定城市范围内
                                if (city != null && !city.isEmpty()) {
                                    if (!cityMatch && !inTaiyuan) {
                                        System.out.println("过滤非" + city + "结果: " + poiName + " (城市:" + poiCity + ", 区县:" + poiAdname + ", 坐标:" + lng + "," + lat + ")");
                                        skippedCount++;
                                        continue; // 跳过非目标城市的POI
                                    }
                                } else {
                                    // 未指定城市时，仅过滤明显不在太原范围内的结果
                                    if (!inTaiyuan) {
                                        System.out.println("过滤非太原结果: " + poiName + " (城市:" + poiCity + ", 区县:" + poiAdname + ", 坐标:" + lng + "," + lat + ")");
                                        skippedCount++;
                                        continue; // 跳过非太原的POI
                                    }
                                }
                                
                                place.put("longitude", new BigDecimal(lng));
                                place.put("latitude", new BigDecimal(lat));
                                
                                System.out.println("添加搜索结果: " + poiName + " (城市:" + poiCity + ", 区县:" + poiAdname + ", 坐标:" + lng + "," + lat + ")");
                                result.add(place);
                                filteredCount++;
                            } catch (Exception e) {
                                System.err.println("解析坐标失败: " + location + ", 错误: " + e.getMessage());
                                skippedCount++;
                                continue; // 跳过无效坐标的POI
                            }
                        } else {
                            System.err.println("POI缺少坐标信息: " + poiName);
                            skippedCount++;
                            continue; // 跳过没有坐标的POI
                        }
                        place.put("type", poi.getString("type"));
                        place.put("tel", poi.getString("tel"));
                        // 安全获取图片
                        try {
                            if (poi.containsKey("photos") && poi.get("photos") != null) {
                                JSONArray photos = poi.getJSONArray("photos");
                                if (photos != null && photos.size() > 0) {
                                    JSONObject photo = photos.getJSONObject(0);
                                    if (photo != null && photo.containsKey("url")) {
                                        place.put("image", photo.getString("url"));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // 忽略图片解析错误
                        }
                    }
                    System.out.println("过滤后有效结果: " + filteredCount + " 个，跳过: " + skippedCount + " 个");
                } else {
                    System.out.println("搜索结果为空（pois数组为空或不存在）");
                }
            } else {
                // API返回错误
                System.err.println("高德地图API错误: status=" + status + ", info=" + info);
                if (info != null && info.contains("USERKEY_PLAT_NOMATCH")) {
                    System.err.println("API密钥平台类型不匹配，请使用Web服务API类型的Key");
                } else if (info != null && info.contains("INVALID_USER_KEY")) {
                    System.err.println("API密钥无效，请检查application.yml中的map.api.key配置");
                } else if (info != null && info.contains("DAILY_QUERY_OVER_LIMIT")) {
                    System.err.println("API调用次数超限，请检查配额或等待重置");
                } else {
                    System.err.println("未知API错误，请查看高德地图API文档");
                }
            }
        } catch (Exception e) {
            System.err.println("搜索地点失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    @Override
    public Map<String, BigDecimal> geocodeAddress(String address, String city) {
        Map<String, BigDecimal> result = new HashMap<>();
        
        if (address == null || address.trim().isEmpty()) {
            return null;
        }
        
        if (mapApiKey == null || mapApiKey.isEmpty()) {
            System.err.println("地图API密钥未配置，无法进行地理编码");
            return null;
        }
        
        if (restTemplate == null) {
            System.err.println("RestTemplate未注入，无法调用高德地图API");
            return null;
        }
        
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String url = String.format("%s/geocode/geo?key=%s&address=%s",
                    AMAP_BASE_URL, mapApiKey, encodedAddress);
            
            // 如果指定了城市，添加到参数中
            if (city != null && !city.isEmpty()) {
                String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
                url += "&city=" + encodedCity;
            } else {
                // 默认使用太原
                url += "&city=太原";
            }
            
            System.out.println("调用地理编码API: " + url.replace(mapApiKey, "***"));
            
            String response = restTemplate.getForObject(url, String.class);
            
            if (response == null || response.isEmpty()) {
                System.err.println("地理编码API返回空响应");
                return null;
            }
            
            JSONObject jsonResponse = JSON.parseObject(response);
            String status = jsonResponse.getString("status");
            String info = jsonResponse.getString("info");
            
            System.out.println("地理编码API响应状态: " + status + ", 信息: " + info);
            
            if ("1".equals(status)) {
                JSONArray geocodes = jsonResponse.getJSONArray("geocodes");
                if (geocodes != null && geocodes.size() > 0) {
                    JSONObject geocode = geocodes.getJSONObject(0);
                    String location = geocode.getString("location");
                    if (location != null && location.contains(",")) {
                        String[] coords = location.split(",");
                        try {
                            BigDecimal longitude = new BigDecimal(coords[0]);
                            BigDecimal latitude = new BigDecimal(coords[1]);
                            result.put("longitude", longitude);
                            result.put("latitude", latitude);
                            System.out.println("地理编码成功 - 地址: " + address + ", 经度: " + longitude + ", 纬度: " + latitude);
                            return result;
                        } catch (Exception e) {
                            System.err.println("解析坐标失败: " + location + ", 错误: " + e.getMessage());
                        }
                    }
                }
            } else {
                System.err.println("地理编码API错误: status=" + status + ", info=" + info);
            }
        } catch (Exception e) {
            System.err.println("地理编码失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 计算两点间距离（公里）- 使用Haversine公式
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 地球半径（公里）
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}

