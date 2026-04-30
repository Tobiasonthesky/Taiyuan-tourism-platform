package com.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.SystemLog;
import com.tourism.service.SystemLogService;
import com.tourism.vo.PageVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统日志控制器
 */
@RestController
@RequestMapping("/admin/system-logs")
@Api(tags = "系统日志管理")
@PreAuthorize("hasRole('ADMIN')")
public class SystemLogController {
    
    private final SystemLogService systemLogService;
    
    public SystemLogController(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }
    
    @GetMapping
    @ApiOperation("分页查询系统日志")
    public Result<PageVO<SystemLog>> getSystemLogs(
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String operationModule,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer success,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Page<SystemLog> page = systemLogService.getSystemLogPage(
                current, size, operationType, operationModule, username, success, startTime, endTime);
        
        PageVO<SystemLog> pageVO = new PageVO<>(
                page.getTotal(),
                page.getPages(),
                page.getCurrent(),
                page.getSize(),
                page.getRecords()
        );
        
        return Result.success(pageVO);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询系统日志")
    public Result<SystemLog> getSystemLogById(@PathVariable("id") Long id) {
        SystemLog systemLog = systemLogService.getSystemLogById(id);
        if (systemLog == null) {
            return Result.error("日志不存在");
        }
        return Result.success(systemLog);
    }
    
    @DeleteMapping("/batch")
    @ApiOperation("批量删除系统日志")
    public Result<?> batchDeleteSystemLogs(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的日志");
        }
        systemLogService.batchDeleteSystemLogs(ids);
        return Result.success("删除成功");
    }
    
    @DeleteMapping("/clear")
    @ApiOperation("清空系统日志")
    public Result<?> clearSystemLogs() {
        systemLogService.clearSystemLogs();
        return Result.success("清空成功");
    }
}