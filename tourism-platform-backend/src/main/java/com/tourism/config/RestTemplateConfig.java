package com.tourism.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类
 * 用于调用第三方API（如地图API、AI API）
 */
@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时时间：10秒
        factory.setConnectTimeout(10000);
        // 读取超时时间：120秒（AI生成可能需要较长时间）
        factory.setReadTimeout(120000);
        
        return new RestTemplate(factory);
    }
}

