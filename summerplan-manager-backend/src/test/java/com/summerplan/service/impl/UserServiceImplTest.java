package com.summerplan.service.impl;

import com.summerplan.common.exception.BusinessException;
import com.summerplan.dto.LoginRequest;
import com.summerplan.dto.RegisterRequest;
import com.summerplan.dto.RegisterResponse;
import com.summerplan.entity.User;
import com.summerplan.repository.UserRepository;
import com.summerplan.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("123456");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encoded");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        RegisterResponse response = userService.register(request);
        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
    }

    @Test
    void register_usernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));
        assertThrows(BusinessException.class, () -> userService.register(request));
    }

    @Test
    void login_success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("123456");
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encoded");
        user.setStatus(1);
        when(userRepository.findByUsernameAndStatus("testuser", 1)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken("testuser")).thenReturn("token");
        var response = userService.login(request);
        assertEquals("token", response.getToken());
        assertEquals("testuser", response.getUser().getUsername());
    }

    @Test
    void login_wrongPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrong");
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encoded");
        user.setStatus(1);
        when(userRepository.findByUsernameAndStatus("testuser", 1)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);
        assertThrows(BusinessException.class, () -> userService.login(request));
    }

    @Test
    void login_userNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("notfound");
        when(userRepository.findByUsernameAndStatus("notfound", 1)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> userService.login(request));
    }
} 