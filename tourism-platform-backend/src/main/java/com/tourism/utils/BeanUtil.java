package com.tourism.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean工具类
 */
public class BeanUtil {
    
    /**
     * 复制Bean属性
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Bean复制失败", e);
        }
    }
    
    /**
     * 复制Bean列表
     */
    public static <T, S> List<T> copyList(List<S> sourceList, Class<T> targetClass) {
        List<T> targetList = new ArrayList<>();
        if (sourceList != null && !sourceList.isEmpty()) {
            for (S source : sourceList) {
                targetList.add(copyProperties(source, targetClass));
            }
        }
        return targetList;
    }
}

