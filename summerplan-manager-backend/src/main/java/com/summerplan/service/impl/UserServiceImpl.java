package com.summerplan.service.impl;

import com.summerplan.common.exception.BusinessException;
import com.summerplan.dto.LoginRequest;
import com.summerplan.dto.LoginResponse;
import com.summerplan.dto.RegisterRequest;
import com.summerplan.dto.RegisterResponse;
import com.summerplan.dto.UserInfoResponse;
import com.summerplan.entity.User;
import com.summerplan.repository.UserRepository;
import com.summerplan.service.UserService;
import com.summerplan.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 查找用户
        Optional<User> userOpt = userRepository.findByUsernameAndStatus(request.getUsername(), 1);
        if (userOpt.isEmpty()) {
            throw new BusinessException("用户名或密码错误");
        }

        User user = userOpt.get();

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getUsername());

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setRealName(user.getRealName());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setRole(user.getRole());

        response.setUser(userInfo);

        return response;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getCurrentUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            final String username;

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                username = userDetails.getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            } else {
                throw new BusinessException("无法获取用户认证信息");
            }

            if (username == null || username.isEmpty()) {
                throw new BusinessException("用户名不能为空");
            }

            return findByUsername(username)
                    .orElseThrow(() -> new BusinessException("用户不存在: " + username));
        } catch (Exception e) {
            throw new BusinessException("获取当前用户信息失败: " + e.getMessage());
        }
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException("用户名已存在");
        }
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(1); // 正常状态
        user = userRepository.save(user);
        return new RegisterResponse(user.getId(), user.getUsername());
    }

    @Override
    public UserInfoResponse getCurrentUserInfo() {
        User user = getCurrentUser();
        return new UserInfoResponse(user.getId(), user.getUsername());
    }
} 