package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.Attraction;
import com.tourism.entity.Food;
import com.tourism.entity.Hotel;
import com.tourism.entity.HotelRoom;
import com.tourism.entity.Strategy;
import com.tourism.entity.Announcement;
import com.tourism.mapper.AttractionMapper;
import com.tourism.mapper.FoodMapper;
import com.tourism.mapper.HotelMapper;
import com.tourism.mapper.HotelRoomMapper;
import com.tourism.mapper.StrategyMapper;
import com.tourism.service.AiAssistantService;
import com.tourism.service.AiService;
import com.tourism.mapper.AnnouncementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * AI助手服务实现类
 */
@Service
public class AiAssistantServiceImpl implements AiAssistantService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiAssistantServiceImpl.class);
    
    @Autowired
    private AiService aiService;
    
    @Autowired
    private AttractionMapper attractionMapper;
    
    @Autowired
    private FoodMapper foodMapper;
    
    @Autowired
    private HotelMapper hotelMapper;
    
    @Autowired
    private HotelRoomMapper hotelRoomMapper;
    
    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private AnnouncementMapper announcementMapper;
    
    @Override
    public String chat(String message, List<Map<String, String>> conversationHistory) {
        try {
            // 构建系统提示词
            String systemPrompt = buildSystemPrompt();
            
            // 检测用户是否询问了特定酒店，如果是，动态查询该酒店的详细信息
            String additionalHotelInfo = extractHotelInfoFromMessage(message);
            
            // 构建完整的对话提示词
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append(systemPrompt).append("\n\n");
            
            // 如果检测到特定酒店查询，添加该酒店的详细信息
            if (additionalHotelInfo != null && !additionalHotelInfo.isEmpty()) {
                promptBuilder.append("## 用户询问的酒店详细信息\n");
                promptBuilder.append(additionalHotelInfo).append("\n\n");
            }
            
            // 添加对话历史
            if (conversationHistory != null && !conversationHistory.isEmpty()) {
                promptBuilder.append("以下是之前的对话历史：\n");
                for (Map<String, String> history : conversationHistory) {
                    String role = history.get("role");
                    String content = history.get("content");
                    if (role != null && content != null) {
                        if ("user".equals(role)) {
                            promptBuilder.append("用户：").append(content).append("\n");
                        } else if ("assistant".equals(role)) {
                            promptBuilder.append("助手：").append(content).append("\n");
                        }
                    }
                }
                promptBuilder.append("\n");
            }
            
            // 添加当前用户消息
            promptBuilder.append("用户：").append(message).append("\n");
            promptBuilder.append("助手：");
            
            String prompt = promptBuilder.toString();
            
            // 调用AI服务
            String response = aiService.generateContent(prompt);
            
            if (response == null || response.isEmpty()) {
                logger.warn("AI服务返回空响应");
                return "抱歉，我现在无法回答您的问题，请稍后再试。可能是AI服务暂时不可用，请检查AI配置是否正确。";
            }
            
            return response;
        } catch (Exception e) {
            logger.error("AI对话处理异常", e);
            throw new RuntimeException("AI助手服务异常：" + e.getMessage(), e);
        }
    }
    
    /**
     * 从用户消息中提取酒店名称，并查询该酒店的详细信息
     */
    private String extractHotelInfoFromMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return null;
        }
        
        try {
            // 查询所有酒店名称，检查用户消息中是否包含酒店名称
            LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Hotel::getStatus, 1);
            wrapper.select(Hotel::getId, Hotel::getName);
            List<Hotel> allHotels = hotelMapper.selectList(wrapper);
            
            if (allHotels == null || allHotels.isEmpty()) {
                return null;
            }
            
            // 检查用户消息中是否包含任何酒店名称
            for (Hotel hotel : allHotels) {
                if (hotel.getName() != null && message.contains(hotel.getName())) {
                    // 找到匹配的酒店，查询详细信息
                    return getHotelDetailedInfo(hotel.getId());
                }
            }
            
            return null;
        } catch (Exception e) {
            logger.warn("提取酒店信息失败", e);
            return null;
        }
    }
    
    /**
     * 获取酒店的详细信息（包括房型）
     */
    private String getHotelDetailedInfo(Long hotelId) {
        try {
            Hotel hotel = hotelMapper.selectById(hotelId);
            if (hotel == null || hotel.getStatus() != 1) {
                return null;
            }
            
            StringBuilder info = new StringBuilder();
            info.append(String.format("酒店名称：%s\n", hotel.getName()));
            info.append(String.format("描述：%s\n", hotel.getDescription() != null ? hotel.getDescription() : "暂无描述"));
            info.append(String.format("地址：%s\n", hotel.getAddress() != null ? hotel.getAddress() : "地址未知"));
            info.append(String.format("联系电话：%s\n", hotel.getPhone() != null ? hotel.getPhone() : "未知"));
            info.append(String.format("评分：%.1f\n", hotel.getRating() != null ? hotel.getRating() : 0.0));
            info.append(String.format("最低价格：%s元/晚\n", hotel.getMinPrice() != null ? hotel.getMinPrice().toString() : "价格未知"));
            
            // 查询房间信息
            LambdaQueryWrapper<HotelRoom> roomWrapper = new LambdaQueryWrapper<>();
            roomWrapper.eq(HotelRoom::getHotelId, hotelId);
            roomWrapper.eq(HotelRoom::getStatus, 1);
            roomWrapper.orderByAsc(HotelRoom::getPrice);
            List<HotelRoom> rooms = hotelRoomMapper.selectList(roomWrapper);
            
            if (rooms != null && !rooms.isEmpty()) {
                info.append("房型信息：\n");
                for (HotelRoom room : rooms) {
                    info.append(String.format("  - %s（%s）：面积%s㎡，%s，可住%d人，价格%s元/晚\n",
                        room.getRoomName() != null ? room.getRoomName() : "未知房型",
                        room.getRoomType() != null ? room.getRoomType() : "未知类型",
                        room.getArea() != null ? room.getArea().toString() : "未知",
                        room.getBedType() != null ? room.getBedType() : "未知床型",
                        room.getMaxOccupancy() != null ? room.getMaxOccupancy() : 0,
                        room.getPrice() != null ? room.getPrice().toString() : "未知"));
                }
            } else {
                info.append("房型信息：暂无房型数据\n");
            }
            
            return info.toString();
        } catch (Exception e) {
            logger.warn("获取酒店{}详细信息失败", hotelId, e);
            return null;
        }
    }
    
    @Override
    public String getWebsiteDataSummary() {
        try {
            StringBuilder summary = new StringBuilder();
            
            // 获取景点数据摘要
            try {
                LambdaQueryWrapper<Attraction> attractionWrapper = new LambdaQueryWrapper<>();
                attractionWrapper.eq(Attraction::getStatus, 1);
                attractionWrapper.orderByDesc(Attraction::getRating);
                attractionWrapper.last("LIMIT 10");
                List<Attraction> attractions = attractionMapper.selectList(attractionWrapper);
                
                summary.append("## 景点信息\n");
                if (attractions != null && !attractions.isEmpty()) {
                    for (Attraction attraction : attractions) {
                        summary.append(String.format("- %s：%s，评分%.1f，地址：%s\n", 
                            attraction.getName() != null ? attraction.getName() : "未知景点", 
                            attraction.getDescription() != null ? attraction.getDescription() : "暂无描述",
                            attraction.getRating() != null ? attraction.getRating() : 0.0,
                            attraction.getAddress() != null ? attraction.getAddress() : "地址未知"));
                    }
                } else {
                    summary.append("- 暂无景点数据\n");
                }
            } catch (Exception e) {
                logger.warn("获取景点数据失败", e);
                summary.append("## 景点信息\n- 数据获取失败\n");
            }
            
            // 获取美食数据摘要
            try {
                LambdaQueryWrapper<Food> foodWrapper = new LambdaQueryWrapper<>();
                foodWrapper.eq(Food::getStatus, 1);
                foodWrapper.orderByDesc(Food::getRating);
                foodWrapper.last("LIMIT 10");
                List<Food> foods = foodMapper.selectList(foodWrapper);
                
                summary.append("\n## 美食信息\n");
                if (foods != null && !foods.isEmpty()) {
                    for (Food food : foods) {
                        summary.append(String.format("- %s：%s，评分%.1f\n", 
                            food.getName() != null ? food.getName() : "未知美食",
                            food.getDescription() != null ? food.getDescription() : "暂无描述",
                            food.getRating() != null ? food.getRating() : 0.0));
                    }
                } else {
                    summary.append("- 暂无美食数据\n");
                }
            } catch (Exception e) {
                logger.warn("获取美食数据失败", e);
                summary.append("\n## 美食信息\n- 数据获取失败\n");
            }
            
            // 获取酒店数据摘要（增加到50个，确保覆盖更多酒店）
            try {
                LambdaQueryWrapper<Hotel> hotelWrapper = new LambdaQueryWrapper<>();
                hotelWrapper.eq(Hotel::getStatus, 1);
                hotelWrapper.orderByDesc(Hotel::getRating);
                hotelWrapper.orderByDesc(Hotel::getViewCount);
                hotelWrapper.last("LIMIT 50");
                List<Hotel> hotels = hotelMapper.selectList(hotelWrapper);
                
                summary.append("\n## 酒店信息\n");
                if (hotels != null && !hotels.isEmpty()) {
                    for (Hotel hotel : hotels) {
                        summary.append(String.format("- %s：%s，评分%.1f，最低价格：%s元/晚\n", 
                            hotel.getName() != null ? hotel.getName() : "未知酒店",
                            hotel.getDescription() != null ? hotel.getDescription() : "暂无描述",
                            hotel.getRating() != null ? hotel.getRating() : 0.0,
                            hotel.getMinPrice() != null ? hotel.getMinPrice().toString() : "价格未知"));
                        
                        // 查询该酒店的房间信息
                        try {
                            LambdaQueryWrapper<HotelRoom> roomWrapper = new LambdaQueryWrapper<>();
                            roomWrapper.eq(HotelRoom::getHotelId, hotel.getId());
                            roomWrapper.eq(HotelRoom::getStatus, 1);
                            roomWrapper.orderByAsc(HotelRoom::getPrice);
                            List<HotelRoom> rooms = hotelRoomMapper.selectList(roomWrapper);
                            
                            if (rooms != null && !rooms.isEmpty()) {
                                summary.append("  房型信息：\n");
                                for (HotelRoom room : rooms) {
                                    String roomInfo = String.format("    - %s（%s）：面积%s㎡，%s，可住%d人，价格%s元/晚",
                                        room.getRoomName() != null ? room.getRoomName() : "未知房型",
                                        room.getRoomType() != null ? room.getRoomType() : "未知类型",
                                        room.getArea() != null ? room.getArea().toString() : "未知",
                                        room.getBedType() != null ? room.getBedType() : "未知床型",
                                        room.getMaxOccupancy() != null ? room.getMaxOccupancy() : 0,
                                        room.getPrice() != null ? room.getPrice().toString() : "未知");
                                    summary.append(roomInfo).append("\n");
                                }
                            }
                        } catch (Exception e) {
                            logger.warn("获取酒店{}的房间信息失败", hotel.getId(), e);
                            // 房间信息获取失败不影响酒店基本信息
                        }
                    }
                } else {
                    summary.append("- 暂无酒店数据\n");
                }
            } catch (Exception e) {
                logger.warn("获取酒店数据失败", e);
                summary.append("\n## 酒店信息\n- 数据获取失败\n");
            }

            // 获取活动公告数据摘要
            try {
                LambdaQueryWrapper<Announcement> announcementWrapper = new LambdaQueryWrapper<>();
                announcementWrapper.eq(Announcement::getStatus, 1);
                announcementWrapper.orderByDesc(Announcement::getIsTop);
                announcementWrapper.orderByDesc(Announcement::getCreateTime);
                announcementWrapper.last("LIMIT 10");
                List<Announcement> announcements = announcementMapper.selectList(announcementWrapper);
                
                summary.append("\n## 活动公告信息\n");
                if (announcements != null && !announcements.isEmpty()) {
                    for (Announcement announcement : announcements) {
                        String categoryText = "";
                        if ("festival".equals(announcement.getCategory())) {
                            categoryText = "节庆活动";
                        } else if ("promotion".equals(announcement.getCategory())) {
                            categoryText = "优惠活动";
                        } else if ("news".equals(announcement.getCategory())) {
                            categoryText = "新闻资讯";
                        } else {
                            categoryText = announcement.getCategory() != null ? announcement.getCategory() : "其他";
                        }
                        
                        summary.append(String.format("- %s（%s）：%s\n", 
                            announcement.getTitle() != null ? announcement.getTitle() : "未知公告",
                            categoryText,
                            announcement.getContent() != null && announcement.getContent().length() > 50 
                                ? announcement.getContent().substring(0, 50) + "..." 
                                : (announcement.getContent() != null ? announcement.getContent() : "暂无描述")));
                    }
                } else {
                    summary.append("- 暂无活动公告数据\n");
                }
            } catch (Exception e) {
                logger.warn("获取活动公告数据失败", e);
                summary.append("\n## 活动公告信息\n- 数据获取失败\n");
            }
            
            // 获取攻略数据摘要
            try {
                LambdaQueryWrapper<Strategy> strategyWrapper = new LambdaQueryWrapper<>();
                strategyWrapper.eq(Strategy::getStatus, 1);
                strategyWrapper.eq(Strategy::getIsRecommend, 1);
                strategyWrapper.orderByDesc(Strategy::getViewCount);
                strategyWrapper.last("LIMIT 5");
                List<Strategy> strategies = strategyMapper.selectList(strategyWrapper);
                
                summary.append("\n## 推荐攻略\n");
                if (strategies != null && !strategies.isEmpty()) {
                    for (Strategy strategy : strategies) {
                        summary.append(String.format("- %s：%s\n", 
                            strategy.getTitle() != null ? strategy.getTitle() : "未知攻略",
                            strategy.getDescription() != null ? strategy.getDescription() : "暂无描述"));
                    }
                } else {
                    summary.append("- 暂无攻略数据\n");
                }
            } catch (Exception e) {
                logger.warn("获取攻略数据失败", e);
                summary.append("\n## 推荐攻略\n- 数据获取失败\n");
            }
            
            return summary.toString();
        } catch (Exception e) {
            logger.error("获取网站数据摘要失败", e);
            return "## 数据获取失败\n抱歉，暂时无法获取网站数据信息。";
        }
    }
    
    /**
     * 构建系统提示词
     */
    private String buildSystemPrompt() {
        try {
            String dataSummary = getWebsiteDataSummary();
            
            return "你是一个专业的旅游助手，专门为家乡文旅宣传平台提供咨询服务。\n\n" +
                   "你的职责是：\n" +
                   "1. 回答用户关于旅游景点、美食、酒店、攻略、活动公告等相关问题\n" +
                   "2. 根据用户需求推荐合适的旅游方案\n" +
                   "3. 帮助用户规划旅游路线\n" +
                   "4. 提供旅游建议和注意事项\n" +
                   "5. 介绍最新的活动公告和优惠信息\n\n" +
                   "以下是平台当前的数据信息，请基于这些信息回答用户的问题：\n\n" +
                   dataSummary + "\n\n" +
                   "重要注意事项：\n" +
                   "- 回答要准确、友好、专业，必须基于上述数据信息回答\n" +
                   "- 当用户询问酒店信息时，如果数据中包含该酒店的房型信息，必须详细说明房型、面积、床型、价格等信息\n" +
                   "- 当用户询问活动公告时，如果数据中有相关信息，必须准确介绍活动标题、分类和内容\n" +
                   "- 如果用户询问的信息不在上述数据中，可以基于常识回答，但要明确说明这是通用建议，不是平台数据\n" +
                   "- 如果用户想要生成攻略，可以引导用户使用AI攻略生成功能\n" +
                   "- 回答要简洁明了，避免冗长，但要包含关键信息\n" +
                   "- 对于酒店房型问题和活动公告问题，如果数据中有相关信息，必须准确回答，不要说不存在";
        } catch (Exception e) {
            logger.error("构建系统提示词失败", e);
            // 如果获取数据摘要失败，使用简化版的提示词
            return "你是一个专业的旅游助手，专门为家乡文旅宣传平台提供咨询服务。\n\n" +
                   "你的职责是：\n" +
                   "1. 回答用户关于旅游景点、美食、酒店、攻略等相关问题\n" +
                   "2. 根据用户需求推荐合适的旅游方案\n" +
                   "3. 帮助用户规划旅游路线\n" +
                   "4. 提供旅游建议和注意事项\n\n" +
                   "注意事项：\n" +
                   "- 回答要准确、友好、专业\n" +
                   "- 如果用户想要生成攻略，可以引导用户使用AI攻略生成功能\n" +
                   "- 回答要简洁明了，避免冗长";
        }
    }
}
