package com.tourism.aspect;

import com.alibaba.fastjson.JSON;
import com.tourism.annotation.OperationLog;
import com.tourism.entity.SystemLog;
import com.tourism.service.SystemLogService;
import com.tourism.utils.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 操作日志AOP切面
 * 自动拦截带有@OperationLog注解的方法，记录操作日志到数据库
 */
@Aspect
@Component
public class OperationLogAspect {
    
    private final SystemLogService systemLogService;
    
    private final JwtUtil jwtUtil;
    
    public OperationLogAspect(SystemLogService systemLogService, JwtUtil jwtUtil) {
        this.systemLogService = systemLogService;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * 定义切点：所有带有@OperationLog注解的方法
     */
    @Pointcut("@annotation(com.tourism.annotation.OperationLog)")
    public void operationLogPointcut() {
    }
    
    /**
     * 环绕通知：拦截方法执行，记录日志
     */
    @Around("operationLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("===== AOP拦截器被触发 =====");
        long startTime = System.currentTimeMillis();
        
        HttpServletRequest request = getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog annotation = method.getAnnotation(OperationLog.class);
        
        SystemLog systemLog = new SystemLog();
        systemLog.setOperationType(annotation.operationType());
        systemLog.setOperationModule(annotation.module());
        systemLog.setOperationDesc(annotation.description());
        systemLog.setRequestUrl(request.getRequestURI());
        systemLog.setRequestMethod(request.getMethod());
        systemLog.setIpAddress(getIpAddress(request));
        systemLog.setUserAgent(request.getHeader("User-Agent"));
        systemLog.setCreateTime(LocalDateTime.now());
        
        System.out.println("准备记录日志: type=" + annotation.operationType() + ", module=" + annotation.module());
        
        try {
            // 获取当前用户信息
            String token = getTokenFromRequest(request);
            if (token != null) {
                Long userId = jwtUtil.getUserIdFromToken(token);
                String username = jwtUtil.getUsernameFromToken(token);
                systemLog.setUserId(userId);
                systemLog.setUsername(username);
                System.out.println("用户信息: userId=" + userId + ", username=" + username);
            }
            
            // 记录请求参数
            try {
                String params = JSON.toJSONString(joinPoint.getArgs());
                if (params.length() > 2000) {
                    params = params.substring(0, 2000) + "...(已截断)";
                }
                systemLog.setRequestParams(params);
            } catch (Exception e) {
                systemLog.setRequestParams("参数序列化失败");
            }
            
            // 执行目标方法
            Object result = joinPoint.proceed();
            
            // 记录执行结果
            systemLog.setDuration(System.currentTimeMillis() - startTime);
            systemLog.setSuccess(1);
            systemLog.setResult("成功"); // 简化，数据库字段只有50
            
            return result;
            
        } catch (Throwable e) {
            // 记录异常信息
            systemLog.setDuration(System.currentTimeMillis() - startTime);
            systemLog.setSuccess(0);
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.length() > 1000) {
                errorMsg = errorMsg.substring(0, 1000) + "...(已截断)";
            }
            systemLog.setErrorMsg(errorMsg);
            systemLog.setResult("失败"); // 简化，数据库字段只有50
            
            System.err.println("操作执行失败: " + e.getMessage());
            throw e;
            
        } finally {
            // 同步保存日志到数据库
            saveLog(systemLog);
        }
    }
    
    /**
     * 保存日志到数据库（同步方式，确保日志不丢失）
     */
    private void saveLog(SystemLog systemLog) {
        try {
            System.out.println("===== 开始保存日志 =====");
            System.out.println("operationType: " + systemLog.getOperationType());
            System.out.println("operationModule: " + systemLog.getOperationModule());
            System.out.println("operationDesc: " + systemLog.getOperationDesc());
            System.out.println("username: " + systemLog.getUsername());
            System.out.println("===== 正在调用Service =====");
            systemLogService.recordSystemLog(systemLog);
            System.out.println("===== 日志保存成功 =====");
        } catch (Exception e) {
            System.err.println("===== 保存操作日志失败 =====");
            e.printStackTrace();
        }
    }
    
    /**
     * 获取HttpServletRequest
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        throw new RuntimeException("无法获取HttpServletRequest");
    }
    
    /**
     * 获取客户端真实IP
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
    
    /**
     * 从请求头中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}