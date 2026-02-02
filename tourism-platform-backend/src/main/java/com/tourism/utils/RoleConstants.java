package com.tourism.utils;

/**
 * 角色常量类
 */
public class RoleConstants {
    
    /**
     * 普通用户
     */
    public static final String ROLE_USER = "user";
    
    /**
     * 管理员
     */
    public static final String ROLE_ADMIN = "admin";
    
    /**
     * 检查是否为管理员
     */
    public static boolean isAdmin(String role) {
        return ROLE_ADMIN.equals(role);
    }
    
    /**
     * 检查是否为普通用户
     */
    public static boolean isUser(String role) {
        return ROLE_USER.equals(role);
    }
}

