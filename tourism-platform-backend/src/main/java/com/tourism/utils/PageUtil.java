package com.tourism.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页工具类
 */
public class PageUtil {
    
    /**
     * 创建分页对象
     */
    public static <T> Page<T> createPage(Integer current, Integer size) {
        if (current == null || current < 1) {
            current = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }
        return new Page<>(current, size);
    }
    
    /**
     * 转换分页结果为Map
     */
    public static <T> Map<String, Object> convertPageToMap(IPage<T> page) {
        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("pages", page.getPages());
        result.put("current", page.getCurrent());
        result.put("size", page.getSize());
        result.put("records", page.getRecords());
        return result;
    }
}

