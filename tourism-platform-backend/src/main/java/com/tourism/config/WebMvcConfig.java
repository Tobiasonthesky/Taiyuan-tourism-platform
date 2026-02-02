package com.tourism.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Web MVC配置类
 * 配置跨域、静态资源等
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Value("${file.upload.path:./uploads/}")
    private String uploadPath;
    
    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
    
    /**
     * 静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解析上传路径（与FileUtil中的逻辑保持一致）
        String resolvedPath;
        Path path = Paths.get(uploadPath);
        if (!path.isAbsolute()) {
            String baseDir = System.getProperty("user.dir");
            resolvedPath = Paths.get(baseDir, uploadPath).normalize().toString();
        } else {
            resolvedPath = uploadPath;
        }
        
        // 确保路径以分隔符结尾
        String separator = System.getProperty("file.separator");
        if (!resolvedPath.endsWith(separator)) {
            resolvedPath += separator;
        }
        
        // 确保路径格式正确（file:协议需要正斜杠）
        String filePath = resolvedPath.replace("\\", "/");
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }
        
        // 文件上传路径映射
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + filePath);
    }
    
    /**
     * 配置Jackson日期格式，支持多种日期格式
     */
    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        ObjectMapper objectMapper = builder.build();
        // 添加自定义日期反序列化器，支持多种格式
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new CustomDateDeserializer());
        objectMapper.registerModule(module);
        
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}

