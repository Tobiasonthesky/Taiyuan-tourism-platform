package com.tourism.controller;

import com.tourism.dto.UserLoginDTO;
import com.tourism.dto.UserRegisterDTO;
import com.tourism.dto.UserUpdateDTO;
import com.tourism.service.UserService;
import com.tourism.utils.JwtUtil;
import com.tourism.utils.SecurityContextUtil;
import com.tourism.vo.LoginVO;
import com.tourism.vo.Result;
import com.tourism.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result<UserVO> register(@Validated @RequestBody UserRegisterDTO dto) {
        UserVO userVO = userService.register(dto);
        return Result.success("注册成功", userVO);
    }
    
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<LoginVO> login(@Validated @RequestBody UserLoginDTO dto) {
        LoginVO loginVO = userService.login(dto);
        return Result.success("登录成功", loginVO);
    }
    
    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public Result<UserVO> getUserInfo(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        UserVO userVO = userService.getUserInfo(userId);
        return Result.success(userVO);
    }
    
    @PutMapping("/info")
    @ApiOperation("更新用户信息")
    public Result<UserVO> updateUserInfo(@Validated @RequestBody UserUpdateDTO dto, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        UserVO userVO = userService.updateUserInfo(userId, dto);
        return Result.success("更新成功", userVO);
    }
    
    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

