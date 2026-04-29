package com.tourism;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 家乡文旅宣传平台启动类
 */
@SpringBootApplication
@MapperScan("com.tourism.mapper")
@EnableAsync
public class TourismApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TourismApplication.class, args);
    }
}
