package com.summerplan.controller;

import com.summerplan.common.response.ApiResponse;
import com.summerplan.dto.LoginRequest;
import com.summerplan.dto.LoginResponse;
import com.summerplan.dto.RegisterRequest;
import com.summerplan.dto.RegisterResponse;
import com.summerplan.dto.UserInfoResponse;
import com.summerplan.entity.User;
import com.summerplan.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "用户认证相关接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @Operation(summary = "用户注册", description = "用户注册接口")
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = userService.register(request);
        return ApiResponse.success("注册成功", response);
    }

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户信息")
    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getCurrentUserInfo() {
        UserInfoResponse response = userService.getCurrentUserInfo();
        return ApiResponse.success(response);
    }
    
    @Operation(summary = "用户登录", description = "用户登录接口")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ApiResponse.success("登录成功", response);
    }
    
    @Operation(summary = "获取用户信息", description = "获取当前登录用户信息")
    @GetMapping("/profile")
    public ApiResponse<User> getProfile() {
        User user = userService.getCurrentUser();
        return ApiResponse.success(user);
    }
    
    @Operation(summary = "用户登出", description = "用户登出接口")
    @PostMapping("/logout")
    public ApiResponse<String> logout() {
        return ApiResponse.success("登出成功");
    }
} 