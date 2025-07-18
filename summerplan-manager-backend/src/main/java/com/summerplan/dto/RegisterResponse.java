package com.summerplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户注册响应")
public class RegisterResponse {
    @Schema(description = "用户ID", example = "1")
    private Long id;
    @Schema(description = "用户名", example = "testuser")
    private String username;

    public RegisterResponse() {}
    public RegisterResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
} 