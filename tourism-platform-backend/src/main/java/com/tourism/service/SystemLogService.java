package com.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.SystemLog;

import java.util.List;

/**
 * 系统日志服务接口
 */
public interface SystemLogService {
    
    /**
     * 分页查询系统日志
     */
    Page<SystemLog> getSystemLogPage(Integer current, Integer size, 
                                     String operationType, String operationModule, 
                                     String username, Integer success,
                                     String startTime, String endTime);
    
    /**
     * 根据ID查询系统日志
     */
    SystemLog getSystemLogById(Long id);
    
    /**
     * 记录系统日志
     */
    void recordSystemLog(SystemLog systemLog);
    
    /**
     * 批量删除系统日志
     */
    void batchDeleteSystemLogs(List<Long> ids);
    
    /**
     * 清空系统日志
     */
    void clearSystemLogs();
}