package com.tourism.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    
    /**
     * 操作类型：登录、查询、新增、修改、删除、其他
     */
    String operationType() default "其他";
    
    /**
     * 操作模块
     */
    String module() default "";
    
    /**
     * 操作描述
     */
    String description() default "";
}