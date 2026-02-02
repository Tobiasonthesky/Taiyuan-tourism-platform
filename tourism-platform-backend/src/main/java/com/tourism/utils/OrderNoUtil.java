package com.tourism.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单号生成工具类
 */
public class OrderNoUtil {
    
    private static final AtomicLong counter = new AtomicLong(0);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * 生成订单号
     * 格式：年月日时分秒 + 4位自增序号
     */
    public static String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        long sequence = counter.incrementAndGet() % 10000;
        return timestamp + String.format("%04d", sequence);
    }
}

