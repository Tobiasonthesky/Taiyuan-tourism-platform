package com.tourism.controller;

import com.tourism.service.AiAssistantService;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * AI助手控制器
 */
@RestController
@RequestMapping("/ai-assistant")
@Api(tags = "AI智能助手")
public class AiAssistantController {
    
    @Autowired
    private AiAssistantService aiAssistantService;
    
    @PostMapping("/chat")
    @ApiOperation("AI助手对话")
    public Result<String> chat(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
        try {
            String message = (String) request.get("message");
            @SuppressWarnings("unchecked")
            List<Map<String, String>> conversationHistory = (List<Map<String, String>>) request.get("conversationHistory");
            
            if (message == null || message.trim().isEmpty()) {
                return Result.error("消息内容不能为空");
            }
            
            String response = aiAssistantService.chat(message, conversationHistory);
            return Result.success("回复成功", response);
            
        } catch (RuntimeException e) {
            // 如果是RuntimeException，可能是业务异常，返回友好提示
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("AI助手服务异常")) {
                return Result.error(errorMsg);
            }
            return Result.error("AI助手服务异常，请稍后再试：" + (errorMsg != null ? errorMsg : "未知错误"));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("AI助手服务异常，请稍后再试：" + (e.getMessage() != null ? e.getMessage() : "未知错误"));
        }
    }
    
    @GetMapping("/summary")
    @ApiOperation("获取网站数据摘要")
    public Result<String> getSummary() {
        try {
            String summary = aiAssistantService.getWebsiteDataSummary();
            return Result.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取数据摘要失败：" + e.getMessage());
        }
    }
}
