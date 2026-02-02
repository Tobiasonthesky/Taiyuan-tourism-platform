package com.tourism.service;

import java.util.List;
import java.util.Map;

/**
 * AI助手服务接口
 */
public interface AiAssistantService {
    
    /**
     * AI助手对话
     * @param message 用户消息
     * @param conversationHistory 对话历史（可选）
     * @return AI回复
     */
    String chat(String message, List<Map<String, String>> conversationHistory);
    
    /**
     * 获取网站数据摘要（用于AI上下文）
     * @return 网站数据摘要
     */
    String getWebsiteDataSummary();
}
