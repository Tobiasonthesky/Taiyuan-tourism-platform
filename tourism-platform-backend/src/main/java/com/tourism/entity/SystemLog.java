package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统日志实体类
 */
@Data
@TableName("system_log")
public class SystemLog {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String operationType;
    
    private String operationModule;
    
    private String operationDesc;
    
    private Long userId;
    
    private String username;
    
    private String ipAddress;
    
    private String userAgent;
    
    private String requestUrl;
    
    private String requestMethod;
    
    private String requestParams;
    
    private String result;
    
    private Integer success;
    
    private String errorMsg;
    
    private Long duration;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;
}