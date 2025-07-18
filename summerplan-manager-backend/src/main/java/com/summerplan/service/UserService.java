package com.summerplan.service;

import com.summerplan.dto.LoginRequest;
import com.summerplan.dto.LoginResponse;
import com.summerplan.dto.RegisterRequest;
import com.summerplan.dto.RegisterResponse;
import com.summerplan.dto.UserInfoResponse;
import com.summerplan.entity.User;

import java.util.Optional;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据ID查找用户
     */
    Optional<User> findById(Long id);
    
    /**
     * 获取当前登录用户信息
     */
    User getCurrentUser();

    /**
     * 用户注册
     */
    RegisterResponse register(RegisterRequest request);

    /**
     * 获取当前登录用户信息（DTO）
     */
    UserInfoResponse getCurrentUserInfo();
} 