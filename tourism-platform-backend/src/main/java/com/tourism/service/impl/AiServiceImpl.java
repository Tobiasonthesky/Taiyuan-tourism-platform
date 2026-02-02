package com.tourism.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tourism.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * AI服务实现类（DeepSeek API）
 */
@Service
public class AiServiceImpl implements AiService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);
    
    @Value("${ai.deepseek.api-key:}")
    private String apiKey;
    
    @Value("${ai.deepseek.base-url:https://api.deepseek.com}")
    private String baseUrl;
    
    @Value("${ai.deepseek.enabled:false}")
    private boolean enabled;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Override
    public String generateContent(String prompt) {
        if (!enabled || apiKey == null || apiKey.isEmpty()) {
            logger.warn("AI服务未启用或API密钥未配置");
            throw new RuntimeException("AI服务未启用或API密钥未配置，请检查配置文件");
        }
        
        try {
            String url = baseUrl + "/v1/chat/completions";
            
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-chat");
            
            // 构建消息数组
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            requestBody.put("messages", new Object[]{message});
            
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2000);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JSONObject jsonResponse = JSON.parseObject(response.getBody());
                if (jsonResponse.containsKey("choices") && jsonResponse.getJSONArray("choices").size() > 0) {
                    JSONObject choice = jsonResponse.getJSONArray("choices").getJSONObject(0);
                    JSONObject messageObj = choice.getJSONObject("message");
                    String content = messageObj.getString("content");
                    if (content == null || content.isEmpty()) {
                        throw new RuntimeException("AI返回内容为空");
                    }
                    return content;
                } else {
                    logger.error("AI API返回数据格式异常: {}", response.getBody());
                    throw new RuntimeException("AI API返回数据格式异常，请稍后重试");
                }
            }
            
            logger.error("AI API返回异常状态码: {}, 响应体: {}", response.getStatusCode(), response.getBody());
            throw new RuntimeException("AI API返回异常，状态码: " + response.getStatusCode());
            
        } catch (org.springframework.web.client.ResourceAccessException e) {
            logger.error("AI服务网络连接失败", e);
            throw new RuntimeException("无法连接到AI服务，请检查网络连接或AI服务配置");
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            logger.error("AI API请求错误: {}", e.getResponseBodyAsString());
            throw new RuntimeException("AI API请求错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("调用AI服务失败", e);
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("调用AI服务失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String generateStrategy(String prompt) {
        return generateContent(prompt);
    }
}
