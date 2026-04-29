package com.tourism.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tourism.entity.Food;
import com.tourism.mapper.FoodMapper;
import com.tourism.service.FoodService;
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
 * 美食控制器
 */
@RestController
@RequestMapping("/foods")
@Api(tags = "美食管理")
public class FoodController {
    
    @Autowired
    private FoodService foodService;
    
    @Autowired
    private BrowseHistoryService browseHistoryService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private FoodMapper foodMapper;
    
    @PostMapping("/submit")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("用户提交美食（待审核）")
    public Result<Food> submitFood(@RequestBody Map<String, Object> foodData, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        Food food = convertMapToFood(foodData);
        
        food.setUserId(userId);
        food.setStatus(0);
        food.setViewCount(0);
        food.setCommentCount(0);
        food.setFavoriteCount(0);
        
        foodMapper.insert(food);
        
        if (food.getRecommendedRestaurants() != null && !food.getRecommendedRestaurants().isEmpty()) {
            try {
                JSONArray restaurants = JSON.parseArray(food.getRecommendedRestaurants());
                if (restaurants != null && restaurants.size() > 0) {
                    JSONObject firstRestaurant = restaurants.getJSONObject(0);
                    food.setRestaurant(firstRestaurant.getString("name"));
                    food.setAddress(firstRestaurant.getString("address"));
                    if (firstRestaurant.containsKey("longitude")) {
                        food.setLongitude(firstRestaurant.getBigDecimal("longitude"));
                    }
                    if (firstRestaurant.containsKey("latitude")) {
                        food.setLatitude(firstRestaurant.getBigDecimal("latitude"));
                    }
                }
            } catch (Exception e) {
            }
        }
        
        return Result.success(food);
    }
    
    private Food convertMapToFood(Map<String, Object> foodData) {
        Food food = new Food();
        
        if (foodData.containsKey("id") && foodData.get("id") != null) {
            food.setId(Long.valueOf(foodData.get("id").toString()));
        }
        if (foodData.containsKey("name")) {
            food.setName(foodData.get("name") != null ? foodData.get("name").toString() : null);
        }
        if (foodData.containsKey("description")) {
            food.setDescription(foodData.get("description") != null ? foodData.get("description").toString() : null);
        }
        if (foodData.containsKey("coverImage")) {
            food.setCoverImage(foodData.get("coverImage") != null ? foodData.get("coverImage").toString() : null);
        }
        if (foodData.containsKey("ingredients")) {
            food.setIngredients(foodData.get("ingredients") != null ? foodData.get("ingredients").toString() : null);
        }
        if (foodData.containsKey("cookingMethod")) {
            food.setCookingMethod(foodData.get("cookingMethod") != null ? foodData.get("cookingMethod").toString() : null);
        }
        if (foodData.containsKey("content")) {
            food.setContent(foodData.get("content") != null ? foodData.get("content").toString() : null);
        }
        if (foodData.containsKey("categoryId") && foodData.get("categoryId") != null) {
            food.setCategoryId(Long.valueOf(foodData.get("categoryId").toString()));
        }
        if (foodData.containsKey("videoUrl")) {
            food.setVideoUrl(foodData.get("videoUrl") != null ? foodData.get("videoUrl").toString() : null);
        }
        if (foodData.containsKey("origin")) {
            food.setOrigin(foodData.get("origin") != null ? foodData.get("origin").toString() : null);
        }
        if (foodData.containsKey("tags")) {
            food.setTags(foodData.get("tags") != null ? foodData.get("tags").toString() : null);
        }
        
        String restaurant = null;
        String address = null;
        String longitude = null;
        String latitude = null;
        
        if (foodData.containsKey("restaurant")) {
            Object restaurantObj = foodData.get("restaurant");
            if (restaurantObj != null) {
                String restaurantStr = restaurantObj.toString().trim();
                if (!restaurantStr.isEmpty()) {
                    restaurant = restaurantStr;
                }
            }
        }
        
        if (foodData.containsKey("address")) {
            Object addressObj = foodData.get("address");
            if (addressObj != null) {
                String addressStr = addressObj.toString().trim();
                if (!addressStr.isEmpty()) {
                    address = addressStr;
                }
            }
        }
        
        if (foodData.containsKey("longitude")) {
            Object longitudeObj = foodData.get("longitude");
            if (longitudeObj != null) {
                String longitudeStr = longitudeObj.toString().trim();
                if (!longitudeStr.isEmpty()) {
                    longitude = longitudeStr;
                }
            }
        }
        
        if (foodData.containsKey("latitude")) {
            Object latitudeObj = foodData.get("latitude");
            if (latitudeObj != null) {
                String latitudeStr = latitudeObj.toString().trim();
                if (!latitudeStr.isEmpty()) {
                    latitude = latitudeStr;
                }
            }
        }
        
        if (address != null && !address.isEmpty()) {
            JSONArray restaurantsArray = new JSONArray();
            JSONObject restaurantObj = new JSONObject();
            
            String restaurantName = (restaurant != null && !restaurant.isEmpty()) ? restaurant : 
                (food.getName() != null ? food.getName() : "");
            restaurantObj.put("name", restaurantName);
            restaurantObj.put("address", address);
            
            if (longitude != null && !longitude.isEmpty()) {
                try {
                    restaurantObj.put("longitude", new BigDecimal(longitude));
                } catch (Exception e) {
                }
            }
            if (latitude != null && !latitude.isEmpty()) {
                try {
                    restaurantObj.put("latitude", new BigDecimal(latitude));
                } catch (Exception e) {
                }
            }
            restaurantsArray.add(restaurantObj);
            food.setRecommendedRestaurants(restaurantsArray.toJSONString());
        } else if (foodData.containsKey("recommendedRestaurants")) {
            Object recRest = foodData.get("recommendedRestaurants");
            if (recRest != null) {
                if (recRest instanceof String) {
                    String recRestStr = recRest.toString().trim();
                    if (!recRestStr.isEmpty() && !"null".equalsIgnoreCase(recRestStr)) {
                        food.setRecommendedRestaurants(recRestStr);
                    }
                } else {
                    food.setRecommendedRestaurants(JSON.toJSONString(recRest));
                }
            }
        }
        
        return food;
    }
    
    @GetMapping
    @ApiOperation("获取美食列表")
    public Result<PageVO<Food>> getFoods(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<Food> pageVO = foodService.getFoodList(categoryId, keyword, page, size);
        return Result.success(pageVO);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取美食详情")
    public Result<Food> getFoodDetail(@PathVariable Long id, HttpServletRequest request) {
        Food food = foodService.getFoodDetail(id);
        
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                Long userId = jwtUtil.getUserIdFromToken(token);
                if (userId != null) {
                    browseHistoryService.addBrowseHistory(userId, "food", id);
                }
            } catch (Exception e) {
            }
        }
        
        return Result.success(food);
    }
    
    @GetMapping("/search")
    @ApiOperation("搜索美食")
    public Result<PageVO<Food>> searchFoods(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageVO<Food> pageVO = foodService.searchFoods(keyword, page, size);
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
