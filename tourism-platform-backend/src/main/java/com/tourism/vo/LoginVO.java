package com.tourism.vo;

import lombok.Data;

/**
 * 登录响应视图对象
 */
@Data
public class LoginVO {
    
    /**
     * Token
     */
    private String token;
    
    /**
     * 用户信息
     */
    private UserVO userInfo;
}

