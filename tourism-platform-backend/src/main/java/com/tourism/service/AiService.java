package com.tourism.service;

/**
 * AI服务接口
 */
public interface AiService {
    
    /**
     * 调用AI生成内容
     * @param prompt 提示词
     * @return AI生成的内容
     */
    String generateContent(String prompt);
    
    /**
     * 生成旅游攻略
     * @param prompt 攻略生成提示词
     * @return 生成的攻略内容（JSON格式）
     */
    String generateStrategy(String prompt);
}
