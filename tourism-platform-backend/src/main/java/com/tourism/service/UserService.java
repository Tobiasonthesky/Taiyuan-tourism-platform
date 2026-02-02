package com.tourism.service;

import com.tourism.dto.UserLoginDTO;
import com.tourism.dto.UserRegisterDTO;
import com.tourism.dto.UserUpdateDTO;
import com.tourism.vo.LoginVO;
import com.tourism.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    UserVO register(UserRegisterDTO dto);
    
    /**
     * 用户登录
     */
    LoginVO login(UserLoginDTO dto);
    
    /**
     * 获取用户信息
     */
    UserVO getUserInfo(Long userId);
    
    /**
     * 更新用户信息
     */
    UserVO updateUserInfo(Long userId, UserUpdateDTO dto);
    
    /**
     * 根据用户名获取用户
     */
    UserVO getUserByUsername(String username);
}

