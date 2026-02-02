package com.tourism.service;

import com.tourism.dto.StrategyGenerateDTO;
import com.tourism.entity.Attraction;
import com.tourism.entity.Food;
import com.tourism.entity.Hotel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Prompt模板构建器
 * 用于构建AI提示词模板
 */
@Component
public class PromptTemplateBuilder {
    
    @Value("${ai.strategy.prompt.system-role:你是一个专业的旅游攻略生成助手。请根据以下信息生成一份详细的旅游攻略。}")
    private String systemRole;
    
    @Value("${ai.strategy.prompt.requirement-section:【需求信息】}")
    private String requirementSection;
    
    @Value("${ai.strategy.prompt.attractions-section:【可用景点】}")
    private String attractionsSection;
    
    @Value("${ai.strategy.prompt.foods-section:【推荐美食】}")
    private String foodsSection;
    
    @Value("${ai.strategy.prompt.hotels-section:【推荐酒店】}")
    private String hotelsSection;
    
    @Value("${ai.strategy.prompt.output-section:【输出要求】}")
    private String outputSection;
    
    @Value("${ai.strategy.prompt.json-template:{\n  \"title\": \"攻略标题（20字以内）\",\n  \"description\": \"攻略简介（100-200字）\",\n  \"content\": \"详细攻略内容（包含每天的行程安排，景点推荐，美食推荐，住宿建议等，800-1500字）\",\n  \"tips\": \"注意事项和实用建议（200-300字）\",\n  \"bestSeason\": \"最佳旅游季节\"\n}}")
    private String jsonTemplate;
    
    @Value("${ai.strategy.prompt.requirements:1. 攻略内容要详细、实用，符合用户需求\n2. 合理规划每天的行程，考虑时间和路线\n3. 推荐具体的景点、美食和酒店（从提供的列表中选择）\n4. 如果提供了预算，要合理分配预算\n5. 内容要生动有趣，吸引人\n6. 只返回JSON格式，不要其他文字说明}")
    private String requirements;
    
    @Value("${ai.strategy.prompt.attraction-description-length:100}")
    private int attractionDescriptionLength;
    
    @Value("${ai.strategy.prompt.food-description-length:80}")
    private int foodDescriptionLength;
    
    /**
     * 构建攻略生成提示词
     */
    public String buildStrategyPrompt(StrategyGenerateDTO dto, 
                                      List<Attraction> attractions, 
                                      List<Food> foods, 
                                      List<Hotel> hotels) {
        StringBuilder prompt = new StringBuilder();
        
        // 系统角色
        prompt.append(systemRole).append("\n\n");
        
        // 需求信息
        prompt.append(requirementSection).append("\n");
        prompt.append("游玩天数：").append(dto.getDuration()).append("天\n");
        if (dto.getBudget() != null) {
            prompt.append("预算：").append(dto.getBudget()).append("元\n");
        }
        if (dto.getTheme() != null && !dto.getTheme().isEmpty()) {
            prompt.append("主题：").append(dto.getTheme()).append("\n");
        }
        if (dto.getInterests() != null && !dto.getInterests().isEmpty()) {
            prompt.append("兴趣偏好：").append(dto.getInterests()).append("\n");
        }
        if (dto.getBestSeason() != null && !dto.getBestSeason().isEmpty()) {
            prompt.append("最佳季节：").append(dto.getBestSeason()).append("\n");
        }
        if (dto.getRequirements() != null && !dto.getRequirements().isEmpty()) {
            prompt.append("特殊要求：").append(dto.getRequirements()).append("\n");
        }
        
        // 可用景点
        prompt.append("\n").append(attractionsSection).append("\n");
        for (Attraction attraction : attractions) {
            prompt.append("- ").append(attraction.getName());
            if (attraction.getDescription() != null && !attraction.getDescription().isEmpty()) {
                String desc = attraction.getDescription();
                int length = Math.min(attractionDescriptionLength, desc.length());
                prompt.append("：").append(desc.substring(0, length));
            }
            prompt.append("\n");
        }
        
        // 推荐美食
        prompt.append("\n").append(foodsSection).append("\n");
        for (Food food : foods) {
            prompt.append("- ").append(food.getName());
            if (food.getDescription() != null && !food.getDescription().isEmpty()) {
                String desc = food.getDescription();
                int length = Math.min(foodDescriptionLength, desc.length());
                prompt.append("：").append(desc.substring(0, length));
            }
            prompt.append("\n");
        }
        
        // 推荐酒店
        prompt.append("\n").append(hotelsSection).append("\n");
        for (Hotel hotel : hotels) {
            prompt.append("- ").append(hotel.getName());
            if (hotel.getAddress() != null && !hotel.getAddress().isEmpty()) {
                prompt.append("：").append(hotel.getAddress());
            }
            prompt.append("\n");
        }
        
        // 输出要求
        prompt.append("\n").append(outputSection).append("\n");
        prompt.append("请生成一份JSON格式的攻略，包含以下字段：\n");
        prompt.append(jsonTemplate).append("\n\n");
        prompt.append("要求：\n");
        prompt.append(requirements);
        
        return prompt.toString();
    }
}
