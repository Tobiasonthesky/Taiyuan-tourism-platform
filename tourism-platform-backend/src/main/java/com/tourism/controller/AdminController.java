package com.tourism.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.*;
import com.tourism.mapper.*;
import com.tourism.service.HotelService;
import com.tourism.service.MapService;
import com.tourism.utils.JwtUtil;
import com.tourism.utils.PageUtil;
import com.tourism.utils.RoleConstants;
import com.tourism.vo.PageVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "管理员管理")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private AttractionMapper attractionMapper;
    
    @Autowired
    private FoodMapper foodMapper;
    
    @Autowired
    private CultureMapper cultureMapper;
    
    @Autowired
    private StrategyMapper strategyMapper;
    
    @Autowired
    private HotelMapper hotelMapper;
    
    @Autowired
    private ExperienceMapper experienceMapper;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private CommentReplyMapper commentReplyMapper;
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Autowired
    private BrowseHistoryMapper browseHistoryMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private MapService mapService;
    
    // ==================== 用户管理 ====================
    
    @GetMapping("/users")
    @ApiOperation("获取用户列表")
    public Result<PageVO<User>> getUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        
        Page<User> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getNickname, keyword)
                    .or().like(User::getEmail, keyword));
        }
        if (role != null && !role.isEmpty()) {
            wrapper.eq(User::getRole, role);
        }
        wrapper.orderByDesc(User::getCreateTime);
        
        Page<User> result = userMapper.selectPage(pageParam, wrapper);
        PageVO<User> pageVO = new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
        
        return Result.success(pageVO);
    }
    
    @PutMapping("/users/{id}/role")
    @ApiOperation("修改用户角色")
    public Result<?> updateUserRole(@PathVariable("id") Long id, @RequestParam("role") String role) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.notFound("用户不存在");
        }
        
        if (!RoleConstants.ROLE_USER.equals(role) && !RoleConstants.ROLE_ADMIN.equals(role)) {
            return Result.error("角色参数错误");
        }
        
        user.setRole(role);
        userMapper.updateById(user);
        return Result.success("修改成功");
    }
    
    @PutMapping("/users/{id}/status")
    @ApiOperation("修改用户状态")
    public Result<?> updateUserStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.notFound("用户不存在");
        }
        
        if (status != 0 && status != 1) {
            return Result.error("状态参数错误");
        }
        
        user.setStatus(status);
        userMapper.updateById(user);
        return Result.success("修改成功");
    }

    @DeleteMapping("/users/{id}")
    @ApiOperation("删除用户")
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public Result<?> deleteUser(@PathVariable("id") Long id) {
        // 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 检查是否为管理员，防止删除最后一个管理员
        if (RoleConstants.ROLE_ADMIN.equals(user.getRole())) {
            long adminCount = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getRole, RoleConstants.ROLE_ADMIN)
            );
            if (adminCount <= 1) {
                return Result.error("不能删除最后一个管理员");
            }
        }
        
        // 1. 先删除该用户的所有评论回复（因为评论回复可能引用评论，需要先删除）
        LambdaQueryWrapper<CommentReply> replyWrapper = new LambdaQueryWrapper<>();
        replyWrapper.eq(CommentReply::getUserId, id);
        commentReplyMapper.delete(replyWrapper);
        
        // 2. 删除该用户的所有评论
        // 注意：如果评论回复表有外键约束引用评论，需要先删除评论下的所有回复
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getUserId, id);
        List<Comment> userComments = commentMapper.selectList(commentWrapper);
        
        // 删除这些评论下的所有回复（包括其他用户的回复）
        for (Comment comment : userComments) {
            LambdaQueryWrapper<CommentReply> commentReplyWrapper = new LambdaQueryWrapper<>();
            commentReplyWrapper.eq(CommentReply::getCommentId, comment.getId());
            commentReplyMapper.delete(commentReplyWrapper);
        }
        
        // 然后删除评论本身
        commentMapper.delete(commentWrapper);
        
        // 3. 删除该用户的所有收藏
        LambdaQueryWrapper<Favorite> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(Favorite::getUserId, id);
        favoriteMapper.delete(favoriteWrapper);
        
        // 4. 删除该用户的所有浏览历史
        LambdaQueryWrapper<BrowseHistory> historyWrapper = new LambdaQueryWrapper<>();
        historyWrapper.eq(BrowseHistory::getUserId, id);
        browseHistoryMapper.delete(historyWrapper);
        
        // 5. 处理订单：由于订单表有外键约束不允许NULL，需要先删除订单项，再删除订单
        try {
            List<OrderEntity> orders = orderMapper.selectList(
                new LambdaQueryWrapper<OrderEntity>().eq(OrderEntity::getUserId, id)
            );
            for (OrderEntity order : orders) {
                // 先删除订单项
                LambdaQueryWrapper<OrderItem> orderItemWrapper = new LambdaQueryWrapper<>();
                orderItemWrapper.eq(OrderItem::getOrderId, order.getId());
                orderItemMapper.delete(orderItemWrapper);
                
                // 然后删除订单
                orderMapper.deleteById(order.getId());
            }
        } catch (Exception e) {
            // 如果删除订单失败，记录错误并抛出异常，阻止删除用户
            System.err.println("删除用户订单时出错: " + e.getMessage());
            throw new RuntimeException("删除用户订单失败: " + e.getMessage(), e);
        }
        
        // 6. 处理用户创建的内容（景点、美食、文化、攻略等）
        // 将这些内容的userId设为NULL，表示系统创建
        try {
            // 景点
            List<Attraction> attractions = attractionMapper.selectList(
                new LambdaQueryWrapper<Attraction>().eq(Attraction::getUserId, id)
            );
            for (Attraction attraction : attractions) {
                attraction.setUserId(null);
                attractionMapper.updateById(attraction);
            }
            
            // 美食
            List<Food> foods = foodMapper.selectList(
                new LambdaQueryWrapper<Food>().eq(Food::getUserId, id)
            );
            for (Food food : foods) {
                food.setUserId(null);
                foodMapper.updateById(food);
            }
            
            // 文化
            List<Culture> cultures = cultureMapper.selectList(
                new LambdaQueryWrapper<Culture>().eq(Culture::getUserId, id)
            );
            for (Culture culture : cultures) {
                culture.setUserId(null);
                cultureMapper.updateById(culture);
            }
            
            // 攻略
            List<Strategy> strategies = strategyMapper.selectList(
                new LambdaQueryWrapper<Strategy>().eq(Strategy::getUserId, id)
            );
            for (Strategy strategy : strategies) {
                strategy.setUserId(null);
                strategyMapper.updateById(strategy);
            }
        } catch (Exception e) {
            System.err.println("处理用户创建的内容时出错，但继续删除用户: " + e.getMessage());
        }
        
        // 7. 最后删除用户
        userMapper.deleteById(id);
        
        return Result.success("删除成功");
    }


    
    // ==================== 景点管理 ====================
    
    @PostMapping("/attractions")
    @ApiOperation("创建景点")
    public Result<Attraction> createAttraction(@RequestBody Attraction attraction) {
        attractionMapper.insert(attraction);
        return Result.success(attraction);
    }
    
    @PutMapping("/attractions/{id}")
    @ApiOperation("更新景点")
    public Result<Attraction> updateAttraction(@PathVariable("id") Long id, @RequestBody Attraction attraction) {
        attraction.setId(id);
        attractionMapper.updateById(attraction);
        return Result.success(attraction);
    }
    
    @DeleteMapping("/attractions/{id}")
    @ApiOperation("删除景点")
    public Result<?> deleteAttraction(@PathVariable("id") Long id) {
        attractionMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    @PutMapping("/attractions/{id}/audit")
    @ApiOperation("审核景点")
    public Result<?> auditAttraction(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction == null) {
            return Result.notFound("景点不存在");
        }
        if (status != 1 && status != 2) {
            return Result.error("状态参数错误，1-通过，2-拒绝");
        }
        
        // 如果审核通过，且地址有值但经纬度为空，尝试自动获取经纬度
        if (status == 1 && attraction.getAddress() != null && !attraction.getAddress().trim().isEmpty()) {
            if (attraction.getLongitude() == null || attraction.getLatitude() == null) {
                System.out.println("景点审核通过 - 尝试自动获取经纬度，地址: " + attraction.getAddress());
                Map<String, BigDecimal> location = mapService.geocodeAddress(attraction.getAddress(), "太原");
                if (location != null && location.containsKey("longitude") && location.containsKey("latitude")) {
                    attraction.setLongitude(location.get("longitude"));
                    attraction.setLatitude(location.get("latitude"));
                    System.out.println("景点审核通过 - 成功获取经纬度: " + attraction.getLongitude() + ", " + attraction.getLatitude());
                } else {
                    System.out.println("景点审核通过 - 无法获取经纬度，但继续审核通过");
                }
            }
        }
        
        attraction.setStatus(status);
        attractionMapper.updateById(attraction);
        return Result.success(status == 1 ? "审核通过" : "审核拒绝");
    }
    
    // ==================== 美食管理 ====================
    
    @PostMapping("/foods")
    @ApiOperation("创建美食")
    public Result<Food> createFood(@RequestBody Map<String, Object> foodData) {
        System.out.println("创建美食 - 接收到的数据: " + JSON.toJSONString(foodData));
        Food food = convertMapToFood(foodData);
        System.out.println("创建美食 - 转换后的Food对象 - recommendedRestaurants: " + food.getRecommendedRestaurants());
        foodMapper.insert(food);
        // 重新查询以确保获取最新数据
        Food savedFood = foodMapper.selectById(food.getId());
        System.out.println("创建美食 - 保存后的Food对象 - recommendedRestaurants: " + (savedFood != null ? savedFood.getRecommendedRestaurants() : "null"));
        // 返回时添加restaurant和address字段以便前端显示
        Food result = convertFoodToMap(savedFood != null ? savedFood : food);
        System.out.println("创建美食 - 返回的Food对象 - restaurant: " + result.getRestaurant() + ", address: " + result.getAddress());
        return Result.success(result);
    }
    
    @PutMapping("/foods/{id}")
    @ApiOperation("更新美食")
    public Result<Food> updateFood(@PathVariable("id") Long id, @RequestBody Map<String, Object> foodData) {
        System.out.println("更新美食 - ID: " + id + ", 接收到的数据: " + JSON.toJSONString(foodData));
        
        // 先查询原有数据
        Food existingFood = foodMapper.selectById(id);
        if (existingFood == null) {
            return Result.notFound("美食不存在");
        }
        
        Food food = convertMapToFood(foodData, existingFood);
        food.setId(id);
        System.out.println("更新美食 - 转换后的Food对象 - recommendedRestaurants: " + food.getRecommendedRestaurants());
        foodMapper.updateById(food);
        // 重新查询以确保获取最新数据
        Food updatedFood = foodMapper.selectById(id);
        System.out.println("更新美食 - 更新后的Food对象 - recommendedRestaurants: " + (updatedFood != null ? updatedFood.getRecommendedRestaurants() : "null"));
        // 返回时添加restaurant和address字段以便前端显示
        Food result = convertFoodToMap(updatedFood != null ? updatedFood : food);
        System.out.println("更新美食 - 返回的Food对象 - restaurant: " + result.getRestaurant() + ", address: " + result.getAddress());
        return Result.success(result);
    }
    
    /**
     * 将前端传来的Map转换为Food实体，处理restaurant和address字段
     */
    private Food convertMapToFood(Map<String, Object> foodData) {
        return convertMapToFood(foodData, null);
    }
    
    /**
     * 将前端传来的Map转换为Food实体，处理restaurant和address字段
     * @param foodData 前端传来的数据
     * @param existingFood 已存在的美食数据（更新时使用，用于保留未更新的字段）
     */
    private Food convertMapToFood(Map<String, Object> foodData, Food existingFood) {
        Food food = new Food();
        
        // 手动设置字段，确保正确处理所有类型
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
        if (foodData.containsKey("status")) {
            Object statusObj = foodData.get("status");
            if (statusObj != null) {
                food.setStatus(Integer.valueOf(statusObj.toString()));
            }
        }
        if (foodData.containsKey("categoryId") && foodData.get("categoryId") != null) {
            food.setCategoryId(Long.valueOf(foodData.get("categoryId").toString()));
        }
        if (foodData.containsKey("userId") && foodData.get("userId") != null) {
            food.setUserId(Long.valueOf(foodData.get("userId").toString()));
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
        if (foodData.containsKey("rating") && foodData.get("rating") != null) {
            try {
                food.setRating(new BigDecimal(foodData.get("rating").toString()));
            } catch (Exception e) {
                // 忽略转换错误
            }
        }
        if (foodData.containsKey("sortOrder") && foodData.get("sortOrder") != null) {
            try {
                food.setSortOrder(Integer.valueOf(foodData.get("sortOrder").toString()));
            } catch (Exception e) {
                // 忽略转换错误
            }
        }
        
        // 处理restaurant和address字段，转换为recommendedRestaurants JSON
        String restaurant = null;
        String address = null;
        String longitude = null;
        String latitude = null;
        
        // 检查并提取字段值，处理空字符串的情况
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
        
        System.out.println("提取的字段值 - restaurant: [" + restaurant + "], address: [" + address + "], longitude: [" + longitude + "], latitude: [" + latitude + "]");
        
        // 检查前端是否明确传了address字段（用于区分是更新时清空还是未传）
        boolean hasAddressField = foodData.containsKey("address");
        
        // 如果有地址（地址是必需的），就保存到recommendedRestaurants
        // 餐厅名称可以为空，使用美食名称作为默认值
        if (address != null && !address.isEmpty()) {
            JSONArray restaurantsArray = new JSONArray();
            JSONObject restaurantObj = new JSONObject();
            
            // 如果餐厅名称为空，使用美食名称
            String restaurantName = (restaurant != null && !restaurant.isEmpty()) ? restaurant : 
                (food.getName() != null ? food.getName() + "推荐餐厅" : "推荐餐厅");
            restaurantObj.put("name", restaurantName);
            restaurantObj.put("address", address);
            
            if (longitude != null && !longitude.isEmpty()) {
                try {
                    restaurantObj.put("longitude", new BigDecimal(longitude));
                } catch (Exception e) {
                    System.err.println("转换经度失败: " + longitude + ", 错误: " + e.getMessage());
                }
            }
            if (latitude != null && !latitude.isEmpty()) {
                try {
                    restaurantObj.put("latitude", new BigDecimal(latitude));
                } catch (Exception e) {
                    System.err.println("转换纬度失败: " + latitude + ", 错误: " + e.getMessage());
                }
            }
            restaurantsArray.add(restaurantObj);
            food.setRecommendedRestaurants(restaurantsArray.toJSONString());
            System.out.println("设置recommendedRestaurants: " + food.getRecommendedRestaurants());
        } else if (hasAddressField && (address == null || address.isEmpty())) {
            // 如果前端明确传了address字段但值为空，说明用户要清空地址
            food.setRecommendedRestaurants(null);
            System.out.println("用户清空了地址，recommendedRestaurants设置为null");
        } else if (foodData.containsKey("recommendedRestaurants")) {
            // 如果前端直接传了recommendedRestaurants，使用它
            Object recRest = foodData.get("recommendedRestaurants");
            if (recRest != null) {
                if (recRest instanceof String) {
                    String recRestStr = recRest.toString().trim();
                    if (!recRestStr.isEmpty() && !"null".equalsIgnoreCase(recRestStr)) {
                        food.setRecommendedRestaurants(recRestStr);
                    } else {
                        food.setRecommendedRestaurants(null);
                    }
                } else {
                    food.setRecommendedRestaurants(JSON.toJSONString(recRest));
                }
            } else {
                food.setRecommendedRestaurants(null);
            }
        } else if (existingFood != null) {
            // 更新时，如果前端没有传address字段，保留原有的recommendedRestaurants
            food.setRecommendedRestaurants(existingFood.getRecommendedRestaurants());
            System.out.println("保留原有的recommendedRestaurants: " + existingFood.getRecommendedRestaurants());
        } else {
            // 创建时，如果没有地址，设置为null
            food.setRecommendedRestaurants(null);
            System.out.println("没有地址信息，recommendedRestaurants设置为null");
        }
        
        return food;
    }
    
    /**
     * 从recommendedRestaurants JSON中解析出restaurant和address字段
     */
    private void parseRecommendedRestaurants(Food food) {
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
                // JSON解析失败，忽略
            }
        }
    }
    
    /**
     * 将Food实体转换为Map，添加restaurant和address字段以便前端显示
     */
    private Food convertFoodToMap(Food food) {
        // 从recommendedRestaurants JSON中解析出第一个餐厅的restaurant和address
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
                // JSON解析失败，忽略
            }
        }
        return food;
    }
    
    @DeleteMapping("/foods/{id}")
    @ApiOperation("删除美食")
    public Result<?> deleteFood(@PathVariable("id") Long id) {
        foodMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    @PutMapping("/foods/{id}/audit")
    @ApiOperation("审核美食")
    public Result<?> auditFood(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        Food food = foodMapper.selectById(id);
        if (food == null) {
            return Result.notFound("美食不存在");
        }
        if (status != 1 && status != 2) {
            return Result.error("状态参数错误，1-通过，2-拒绝");
        }
        
        // 如果审核通过，且推荐餐厅中有地址但缺少经纬度，尝试自动获取经纬度
        if (status == 1 && food.getRecommendedRestaurants() != null && !food.getRecommendedRestaurants().isEmpty()) {
            try {
                JSONArray restaurants = JSON.parseArray(food.getRecommendedRestaurants());
                if (restaurants != null && restaurants.size() > 0) {
                    boolean needUpdate = false;
                    for (int i = 0; i < restaurants.size(); i++) {
                        JSONObject restaurant = restaurants.getJSONObject(i);
                        String address = restaurant.getString("address");
                        if (address != null && !address.trim().isEmpty()) {
                            // 检查是否缺少经纬度
                            if (!restaurant.containsKey("longitude") || !restaurant.containsKey("latitude") ||
                                restaurant.get("longitude") == null || restaurant.get("latitude") == null) {
                                System.out.println("美食审核通过 - 尝试自动获取餐厅经纬度，地址: " + address);
                                Map<String, BigDecimal> location = mapService.geocodeAddress(address, "太原");
                                if (location != null && location.containsKey("longitude") && location.containsKey("latitude")) {
                                    restaurant.put("longitude", location.get("longitude"));
                                    restaurant.put("latitude", location.get("latitude"));
                                    needUpdate = true;
                                    System.out.println("美食审核通过 - 成功获取餐厅经纬度: " + location.get("longitude") + ", " + location.get("latitude"));
                                }
                            }
                        }
                    }
                    if (needUpdate) {
                        food.setRecommendedRestaurants(restaurants.toJSONString());
                    }
                }
            } catch (Exception e) {
                System.err.println("处理美食推荐餐厅JSON失败: " + e.getMessage());
            }
        }
        
        food.setStatus(status);
        foodMapper.updateById(food);
        return Result.success(status == 1 ? "审核通过" : "审核拒绝");
    }
    
    // ==================== 文化管理 ====================
    
    @PostMapping("/cultures")
    @ApiOperation("创建文化")
    public Result<Culture> createCulture(@RequestBody Map<String, Object> cultureData) {
        System.out.println("创建文化 - 接收到的数据: " + JSON.toJSONString(cultureData));
        Culture culture = convertMapToCulture(cultureData);
        System.out.println("创建文化 - 活动地点: " + culture.getActivityLocation() + ", 经度: " + culture.getLongitude() + ", 纬度: " + culture.getLatitude());
        cultureMapper.insert(culture);
        // 重新查询以确保获取最新数据
        Culture savedCulture = cultureMapper.selectById(culture.getId());
        return Result.success(savedCulture != null ? savedCulture : culture);
    }
    
    @PutMapping("/cultures/{id}")
    @ApiOperation("更新文化")
    public Result<Culture> updateCulture(@PathVariable("id") Long id, @RequestBody Map<String, Object> cultureData) {
        System.out.println("更新文化 - ID: " + id + ", 接收到的数据: " + JSON.toJSONString(cultureData));
        
        // 先查询原有数据
        Culture existingCulture = cultureMapper.selectById(id);
        if (existingCulture == null) {
            return Result.notFound("文化不存在");
        }
        
        Culture culture = convertMapToCulture(cultureData, existingCulture);
        culture.setId(id);
        System.out.println("更新文化 - 活动地点: " + culture.getActivityLocation() + ", 经度: " + culture.getLongitude() + ", 纬度: " + culture.getLatitude());
        cultureMapper.updateById(culture);
        // 重新查询以确保获取最新数据
        Culture updatedCulture = cultureMapper.selectById(id);
        return Result.success(updatedCulture != null ? updatedCulture : culture);
    }
    
    /**
     * 将前端传来的Map转换为Culture实体，处理activityLocation和经纬度字段
     */
    private Culture convertMapToCulture(Map<String, Object> cultureData) {
        return convertMapToCulture(cultureData, null);
    }
    
    /**
     * 将前端传来的Map转换为Culture实体，处理activityLocation和经纬度字段
     * @param cultureData 前端传来的数据
     * @param existingCulture 已存在的文化数据（更新时使用，用于保留未更新的字段）
     */
    private Culture convertMapToCulture(Map<String, Object> cultureData, Culture existingCulture) {
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
        if (cultureData.containsKey("tags")) {
            culture.setTags(cultureData.get("tags") != null ? cultureData.get("tags").toString() : null);
        }
        if (cultureData.containsKey("status")) {
            Object statusObj = cultureData.get("status");
            if (statusObj != null) {
                culture.setStatus(Integer.valueOf(statusObj.toString()));
            }
        }
        if (cultureData.containsKey("sortOrder") && cultureData.get("sortOrder") != null) {
            try {
                culture.setSortOrder(Integer.valueOf(cultureData.get("sortOrder").toString()));
            } catch (Exception e) {
                // 忽略转换错误
            }
        }
        
        // 处理activityTime字段
        if (cultureData.containsKey("activityTime")) {
            Object activityTimeObj = cultureData.get("activityTime");
            if (activityTimeObj != null) {
                String activityTimeStr = activityTimeObj.toString().trim();
                culture.setActivityTime(activityTimeStr.isEmpty() ? null : activityTimeStr);
            }
        } else if (existingCulture != null) {
            culture.setActivityTime(existingCulture.getActivityTime());
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
        
        // 检查前端是否明确传了activityLocation字段（用于区分是更新时清空还是未传）
        boolean hasActivityLocationField = cultureData.containsKey("activityLocation");
        
        // 设置活动地点和经纬度
        if (activityLocation != null && !activityLocation.isEmpty()) {
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
        } else if (hasActivityLocationField && (activityLocation == null || activityLocation.isEmpty())) {
            // 如果前端明确传了activityLocation字段但值为空，说明用户要清空地点
            culture.setActivityLocation(null);
            culture.setLongitude(null);
            culture.setLatitude(null);
            System.out.println("用户清空了活动地点，经纬度设置为null");
        } else if (existingCulture != null) {
            // 更新时，如果前端没有传activityLocation字段，保留原有的数据
            culture.setActivityLocation(existingCulture.getActivityLocation());
            culture.setLongitude(existingCulture.getLongitude());
            culture.setLatitude(existingCulture.getLatitude());
            System.out.println("保留原有的活动地点和经纬度");
        }
        
        return culture;
    }
    
    @DeleteMapping("/cultures/{id}")
    @ApiOperation("删除文化")
    public Result<?> deleteCulture(@PathVariable("id") Long id) {
        cultureMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    @PutMapping("/cultures/{id}/audit")
    @ApiOperation("审核文化")
    public Result<?> auditCulture(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        Culture culture = cultureMapper.selectById(id);
        if (culture == null) {
            return Result.notFound("文化不存在");
        }
        if (status != 1 && status != 2) {
            return Result.error("状态参数错误，1-通过，2-拒绝");
        }
        
        // 如果审核通过，且活动地点有值但经纬度为空，尝试自动获取经纬度
        if (status == 1 && culture.getActivityLocation() != null && !culture.getActivityLocation().trim().isEmpty()) {
            if (culture.getLongitude() == null || culture.getLatitude() == null) {
                System.out.println("文化审核通过 - 尝试自动获取经纬度，活动地点: " + culture.getActivityLocation());
                Map<String, BigDecimal> location = mapService.geocodeAddress(culture.getActivityLocation(), "太原");
                if (location != null && location.containsKey("longitude") && location.containsKey("latitude")) {
                    culture.setLongitude(location.get("longitude"));
                    culture.setLatitude(location.get("latitude"));
                    System.out.println("文化审核通过 - 成功获取经纬度: " + culture.getLongitude() + ", " + culture.getLatitude());
                } else {
                    System.out.println("文化审核通过 - 无法获取经纬度，但继续审核通过");
                }
            }
        }
        
        culture.setStatus(status);
        cultureMapper.updateById(culture);
        return Result.success(status == 1 ? "审核通过" : "审核拒绝");
    }
    
    // ==================== 酒店管理 ====================
    
    @PostMapping("/hotels")
    @ApiOperation("创建酒店")
    public Result<Hotel> createHotel(@RequestBody Hotel hotel) {
        hotelMapper.insert(hotel);
        return Result.success(hotel);
    }
    
    @PutMapping("/hotels/{id}")
    @ApiOperation("更新酒店")
    public Result<Hotel> updateHotel(@PathVariable("id") Long id, @RequestBody Hotel hotel) {
        hotel.setId(id);
        hotelMapper.updateById(hotel);
        return Result.success(hotel);
    }
    
    @DeleteMapping("/hotels/{id}")
    @ApiOperation("删除酒店")
    public Result<?> deleteHotel(@PathVariable("id") Long id) {
        hotelMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    // ==================== 酒店房间管理 ====================
    
    @Autowired
    private HotelRoomMapper hotelRoomMapper;
    
    @Autowired
    private HotelService hotelService;
    
    @PostMapping("/hotel-rooms")
    @ApiOperation("创建酒店房间")
    public Result<HotelRoom> createHotelRoom(@RequestBody HotelRoom room) {
        hotelRoomMapper.insert(room);
        // 创建房型后，更新酒店最低价格
        if (room.getHotelId() != null) {
            hotelService.updateHotelMinPrice(room.getHotelId());
        }
        return Result.success(room);
    }
    
    @PutMapping("/hotel-rooms/{id}")
    @ApiOperation("更新酒店房间")
    public Result<HotelRoom> updateHotelRoom(@PathVariable("id") Long id, @RequestBody HotelRoom room) {
        // 先查询原有房型，获取酒店ID
        HotelRoom oldRoom = hotelRoomMapper.selectById(id);
        Long hotelId = oldRoom != null ? oldRoom.getHotelId() : room.getHotelId();
        
        room.setId(id);
        hotelRoomMapper.updateById(room);
        
        // 更新房型后，更新酒店最低价格
        if (hotelId != null) {
            hotelService.updateHotelMinPrice(hotelId);
        }
        return Result.success(room);
    }
    
    @DeleteMapping("/hotel-rooms/{id}")
    @ApiOperation("删除酒店房间")
    public Result<?> deleteHotelRoom(@PathVariable("id") Long id) {
        // 先查询房型，获取酒店ID
        HotelRoom room = hotelRoomMapper.selectById(id);
        Long hotelId = room != null ? room.getHotelId() : null;
        
        hotelRoomMapper.deleteById(id);
        
        // 删除房型后，更新酒店最低价格
        if (hotelId != null) {
            hotelService.updateHotelMinPrice(hotelId);
        }
        return Result.success("删除成功");
    }
    
    // ==================== 活动公告管理 ====================
    
    @Autowired
    private AnnouncementMapper announcementMapper;
    
    @Autowired
    private AttractionImageMapper attractionImageMapper;
    
    @Autowired
    private FoodImageMapper foodImageMapper;
    
    @Autowired
    private CultureImageMapper cultureImageMapper;
    
    @Autowired
    private StrategyImageMapper strategyImageMapper;
    
    @Autowired
    private ExperienceImageMapper experienceImageMapper;
    
    @Autowired
    private HotelImageMapper hotelImageMapper;
    
    @PostMapping("/announcements")
    @ApiOperation("创建活动公告")
    public Result<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        announcementMapper.insert(announcement);
        return Result.success(announcement);
    }
    
    @PutMapping("/announcements/{id}")
    @ApiOperation("更新活动公告")
    public Result<Announcement> updateAnnouncement(@PathVariable("id") Long id, @RequestBody Announcement announcement) {
        announcement.setId(id);
        announcementMapper.updateById(announcement);
        return Result.success(announcement);
    }
    
    @DeleteMapping("/announcements/{id}")
    @ApiOperation("删除活动公告")
    public Result<?> deleteAnnouncement(@PathVariable("id") Long id) {
        announcementMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    @GetMapping("/announcements")
    @ApiOperation("获取活动公告列表（管理员）")
    public Result<PageVO<Announcement>> getAnnouncements(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        
        Page<Announcement> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Announcement::getTitle, keyword)
                    .or().like(Announcement::getContent, keyword));
        }
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Announcement::getCategory, category);
        }
        if (status != null) {
            wrapper.eq(Announcement::getStatus, status);
        }
        wrapper.orderByDesc(Announcement::getIsTop);
        wrapper.orderByDesc(Announcement::getSortOrder);
        wrapper.orderByDesc(Announcement::getCreateTime);
        
        Page<Announcement> result = announcementMapper.selectPage(pageParam, wrapper);
        return Result.success(new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        ));
    }
    
    // ==================== 攻略管理 ====================
    
    @PostMapping("/strategies")
    @ApiOperation("创建攻略")
    public Result<Strategy> createStrategy(@RequestBody Strategy strategy) {
        // 限制 bestSeason 字段长度（数据库字段限制为50字符）
        if (strategy.getBestSeason() != null && strategy.getBestSeason().length() > 50) {
            strategy.setBestSeason(strategy.getBestSeason().substring(0, 50));
        }
        strategyMapper.insert(strategy);
        return Result.success(strategy);
    }
    
    @PutMapping("/strategies/{id}")
    @ApiOperation("更新攻略")
    public Result<Strategy> updateStrategy(@PathVariable("id") Long id, @RequestBody Strategy strategy) {
        strategy.setId(id);
        // 限制 bestSeason 字段长度（数据库字段限制为50字符）
        if (strategy.getBestSeason() != null && strategy.getBestSeason().length() > 50) {
            strategy.setBestSeason(strategy.getBestSeason().substring(0, 50));
        }
        strategyMapper.updateById(strategy);
        return Result.success(strategy);
    }
    
    @DeleteMapping("/strategies/{id}")
    @ApiOperation("删除攻略")
    public Result<?> deleteStrategy(@PathVariable("id") Long id) {
        strategyMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    @PutMapping("/strategies/{id}/audit")
    @ApiOperation("审核攻略")
    public Result<?> auditStrategy(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        Strategy strategy = strategyMapper.selectById(id);
        if (strategy == null) {
            return Result.notFound("攻略不存在");
        }
        if (status != 1 && status != 2) {
            return Result.error("状态参数错误，1-通过，2-拒绝");
        }
        strategy.setStatus(status);
        strategyMapper.updateById(strategy);
        return Result.success(status == 1 ? "审核通过" : "审核拒绝");
    }
    
    // ==================== 待审核内容管理 ====================
    
    @GetMapping("/pending/attractions")
    @ApiOperation("获取待审核景点列表")
    public Result<PageVO<Attraction>> getPendingAttractions(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<Attraction> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attraction::getStatus, 0);
        wrapper.orderByDesc(Attraction::getCreateTime);
        
        Page<Attraction> result = attractionMapper.selectPage(pageParam, wrapper);
        return Result.success(new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        ));
    }
    
    @GetMapping("/pending/foods")
    @ApiOperation("获取待审核美食列表")
    public Result<PageVO<Food>> getPendingFoods(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<Food> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getStatus, 0);
        wrapper.orderByDesc(Food::getCreateTime);
        
        Page<Food> result = foodMapper.selectPage(pageParam, wrapper);
        
        // 解析recommendedRestaurants字段，填充restaurant和address字段
        List<Food> records = result.getRecords();
        for (Food food : records) {
            parseRecommendedRestaurants(food);
        }
        
        return Result.success(new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                records
        ));
    }
    
    @GetMapping("/pending/cultures")
    @ApiOperation("获取待审核文化列表")
    public Result<PageVO<Culture>> getPendingCultures(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<Culture> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Culture> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Culture::getStatus, 0);
        wrapper.orderByDesc(Culture::getCreateTime);
        
        Page<Culture> result = cultureMapper.selectPage(pageParam, wrapper);
        return Result.success(new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        ));
    }
    
    @GetMapping("/pending/strategies")
    @ApiOperation("获取待审核攻略列表")
    public Result<PageVO<Strategy>> getPendingStrategies(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<Strategy> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strategy::getStatus, 0);
        wrapper.orderByDesc(Strategy::getCreateTime);
        
        Page<Strategy> result = strategyMapper.selectPage(pageParam, wrapper);
        return Result.success(new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        ));
    }
    
    // ==================== 图片管理 ====================
    
    // 景点图片管理
    @PostMapping("/attraction-images")
    @ApiOperation("创建景点图片")
    public Result<AttractionImage> createAttractionImage(@RequestBody AttractionImage image) {
        attractionImageMapper.insert(image);
        return Result.success(image);
    }
    
    @PutMapping("/attraction-images/{id}")
    @ApiOperation("更新景点图片")
    public Result<AttractionImage> updateAttractionImage(@PathVariable("id") Long id, @RequestBody AttractionImage image) {
        image.setId(id);
        attractionImageMapper.updateById(image);
        return Result.success(image);
    }
    
    @DeleteMapping("/attraction-images/{id}")
    @ApiOperation("删除景点图片")
    public Result<?> deleteAttractionImage(@PathVariable("id") Long id) {
        attractionImageMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    // 美食图片管理
    @PostMapping("/food-images")
    @ApiOperation("创建美食图片")
    public Result<FoodImage> createFoodImage(@RequestBody FoodImage image) {
        foodImageMapper.insert(image);
        return Result.success(image);
    }
    
    @PutMapping("/food-images/{id}")
    @ApiOperation("更新美食图片")
    public Result<FoodImage> updateFoodImage(@PathVariable("id") Long id, @RequestBody FoodImage image) {
        image.setId(id);
        foodImageMapper.updateById(image);
        return Result.success(image);
    }
    
    @DeleteMapping("/food-images/{id}")
    @ApiOperation("删除美食图片")
    public Result<?> deleteFoodImage(@PathVariable("id") Long id) {
        foodImageMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    // 文化图片管理
    @PostMapping("/culture-images")
    @ApiOperation("创建文化图片")
    public Result<CultureImage> createCultureImage(@RequestBody CultureImage image) {
        cultureImageMapper.insert(image);
        return Result.success(image);
    }
    
    @PutMapping("/culture-images/{id}")
    @ApiOperation("更新文化图片")
    public Result<CultureImage> updateCultureImage(@PathVariable("id") Long id, @RequestBody CultureImage image) {
        image.setId(id);
        cultureImageMapper.updateById(image);
        return Result.success(image);
    }
    
    @DeleteMapping("/culture-images/{id}")
    @ApiOperation("删除文化图片")
    public Result<?> deleteCultureImage(@PathVariable("id") Long id) {
        cultureImageMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    // 攻略图片管理
    @PostMapping("/strategy-images")
    @ApiOperation("创建攻略图片")
    public Result<StrategyImage> createStrategyImage(@RequestBody StrategyImage image) {
        strategyImageMapper.insert(image);
        return Result.success(image);
    }
    
    @PutMapping("/strategy-images/{id}")
    @ApiOperation("更新攻略图片")
    public Result<StrategyImage> updateStrategyImage(@PathVariable("id") Long id, @RequestBody StrategyImage image) {
        image.setId(id);
        strategyImageMapper.updateById(image);
        return Result.success(image);
    }
    
    @DeleteMapping("/strategy-images/{id}")
    @ApiOperation("删除攻略图片")
    public Result<?> deleteStrategyImage(@PathVariable("id") Long id) {
        strategyImageMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    // 体验项目图片管理
    @PostMapping("/experience-images")
    @ApiOperation("创建体验项目图片")
    public Result<ExperienceImage> createExperienceImage(@RequestBody ExperienceImage image) {
        experienceImageMapper.insert(image);
        return Result.success(image);
    }
    
    @PutMapping("/experience-images/{id}")
    @ApiOperation("更新体验项目图片")
    public Result<ExperienceImage> updateExperienceImage(@PathVariable("id") Long id, @RequestBody ExperienceImage image) {
        image.setId(id);
        experienceImageMapper.updateById(image);
        return Result.success(image);
    }
    
    @DeleteMapping("/experience-images/{id}")
    @ApiOperation("删除体验项目图片")
    public Result<?> deleteExperienceImage(@PathVariable("id") Long id) {
        experienceImageMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    // 酒店图片管理
    @PostMapping("/hotel-images")
    @ApiOperation("创建酒店图片")
    public Result<HotelImage> createHotelImage(@RequestBody HotelImage image) {
        hotelImageMapper.insert(image);
        return Result.success(image);
    }
    
    @PutMapping("/hotel-images/{id}")
    @ApiOperation("更新酒店图片")
    public Result<HotelImage> updateHotelImage(@PathVariable("id") Long id, @RequestBody HotelImage image) {
        image.setId(id);
        hotelImageMapper.updateById(image);
        return Result.success(image);
    }
    
    @DeleteMapping("/hotel-images/{id}")
    @ApiOperation("删除酒店图片")
    public Result<?> deleteHotelImage(@PathVariable("id") Long id) {
        hotelImageMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    // ==================== 体验项目管理 ====================
    
    @PostMapping("/experiences")
    @ApiOperation("创建体验项目")
    public Result<Experience> createExperience(@RequestBody Experience experience) {
        experienceMapper.insert(experience);
        return Result.success(experience);
    }
    
    @PutMapping("/experiences/{id}")
    @ApiOperation("更新体验项目")
    public Result<Experience> updateExperience(@PathVariable("id") Long id, @RequestBody Experience experience) {
        experience.setId(id);
        experienceMapper.updateById(experience);
        return Result.success(experience);
    }
    
    @DeleteMapping("/experiences/{id}")
    @ApiOperation("删除体验项目")
    public Result<?> deleteExperience(@PathVariable("id") Long id) {
        experienceMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    // ==================== 订单管理 ====================
    
    @GetMapping("/orders")
    @ApiOperation("获取订单列表")
    public Result<PageVO<OrderEntity>> getOrders(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        
        Page<OrderEntity> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<OrderEntity> wrapper = new LambdaQueryWrapper<>();
        
        if (orderNo != null && !orderNo.isEmpty()) {
            wrapper.like(OrderEntity::getOrderNo, orderNo);
        }
        if (orderStatus != null) {
            wrapper.eq(OrderEntity::getOrderStatus, orderStatus);
        }
        wrapper.orderByDesc(OrderEntity::getCreateTime);
        
        Page<OrderEntity> result = orderMapper.selectPage(pageParam, wrapper);
        PageVO<OrderEntity> pageVO = new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
        
        return Result.success(pageVO);
    }
    
    @GetMapping("/statistics")
    @ApiOperation("获取统计数据")
    public Result<?> getStatistics() {
        // 统计用户数
        long userCount = userMapper.selectCount(null);
        long adminCount = userMapper.selectCount(
            new LambdaQueryWrapper<User>().eq(User::getRole, RoleConstants.ROLE_ADMIN)
        );
        
        // 统计景点数
        long attractionCount = attractionMapper.selectCount(null);
        
        // 统计美食数
        long foodCount = foodMapper.selectCount(null);
        
        // 统计订单数
        long orderCount = orderMapper.selectCount(null);
        
        // 统计待审核数量
        // 景点待审核
        long pendingAttractionCount = attractionMapper.selectCount(
            new LambdaQueryWrapper<Attraction>().eq(Attraction::getStatus, 0)
        );
        
        // 美食待审核
        long pendingFoodCount = foodMapper.selectCount(
            new LambdaQueryWrapper<Food>().eq(Food::getStatus, 0)
        );
        
        // 文化待审核
        long pendingCultureCount = cultureMapper.selectCount(
            new LambdaQueryWrapper<Culture>().eq(Culture::getStatus, 0)
        );
        
        // 攻略待审核
        long pendingStrategyCount = strategyMapper.selectCount(
            new LambdaQueryWrapper<Strategy>().eq(Strategy::getStatus, 0)
        );
        
        // 评论待审核
        long pendingCommentCount = commentMapper.selectCount(
            new LambdaQueryWrapper<Comment>().eq(Comment::getStatus, 0)
        );
        
        // 评论回复待审核
        long pendingCommentReplyCount = commentReplyMapper.selectCount(
            new LambdaQueryWrapper<CommentReply>().eq(CommentReply::getStatus, 0)
        );
        
        // 总待审核数量（所有待审核内容的总数）
        long pendingCount = pendingAttractionCount + pendingFoodCount + pendingCultureCount 
                + pendingStrategyCount + pendingCommentCount + pendingCommentReplyCount;
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("userCount", userCount);
        statistics.put("adminCount", adminCount);
        statistics.put("attractionCount", attractionCount);
        statistics.put("foodCount", foodCount);
        statistics.put("orderCount", orderCount);
        statistics.put("pendingCount", pendingCount);
        statistics.put("pendingAttractionCount", pendingAttractionCount);
        statistics.put("pendingFoodCount", pendingFoodCount);
        statistics.put("pendingCultureCount", pendingCultureCount);
        statistics.put("pendingStrategyCount", pendingStrategyCount);
        statistics.put("pendingCommentCount", pendingCommentCount);
        statistics.put("pendingCommentReplyCount", pendingCommentReplyCount);
        
        return Result.success(statistics);
    }
}

