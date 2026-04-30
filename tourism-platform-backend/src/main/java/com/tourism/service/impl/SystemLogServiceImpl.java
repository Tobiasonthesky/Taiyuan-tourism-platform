package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.SystemLog;
import com.tourism.mapper.SystemLogMapper;
import com.tourism.service.SystemLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统日志服务实现类
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {
    
    private final SystemLogMapper systemLogMapper;
    
    public SystemLogServiceImpl(SystemLogMapper systemLogMapper) {
        this.systemLogMapper = systemLogMapper;
    }
    
    @Override
    public Page<SystemLog> getSystemLogPage(Integer current, Integer size,
                                            String operationType, String operationModule,
                                            String username, Integer success,
                                            String startTime, String endTime) {
        Page<SystemLog> page = new Page<>(current, size);
        LambdaQueryWrapper<SystemLog> queryWrapper = new LambdaQueryWrapper<>();
        
        if (operationType != null && !operationType.isEmpty()) {
            queryWrapper.eq(SystemLog::getOperationType, operationType);
        }
        if (operationModule != null && !operationModule.isEmpty()) {
            queryWrapper.like(SystemLog::getOperationModule, operationModule);
        }
        if (username != null && !username.isEmpty()) {
            queryWrapper.like(SystemLog::getUsername, username);
        }
        if (success != null) {
            queryWrapper.eq(SystemLog::getSuccess, success);
        }
        if (startTime != null && !startTime.isEmpty()) {
            queryWrapper.ge(SystemLog::getCreateTime, startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            queryWrapper.le(SystemLog::getCreateTime, endTime);
        }
        
        queryWrapper.orderByDesc(SystemLog::getCreateTime);
        
        return systemLogMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public SystemLog getSystemLogById(Long id) {
        return systemLogMapper.selectById(id);
    }
    
    @Override
    public void recordSystemLog(SystemLog systemLog) {
        systemLogMapper.insert(systemLog);
    }
    
    @Override
    public void batchDeleteSystemLogs(List<Long> ids) {
        systemLogMapper.deleteBatchIds(ids);
    }
    
    @Override
    public void clearSystemLogs() {
        systemLogMapper.delete(new LambdaQueryWrapper<>());
    }
}