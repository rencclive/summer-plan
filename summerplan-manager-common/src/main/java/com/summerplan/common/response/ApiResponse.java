package com.summerplan.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.summerplan.common.constant.Constants;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 统一API响应结果
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    /**
     * 状态码
     */
    private int code;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private LocalDateTime timestamp;
    
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiResponse(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public ApiResponse(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }
    
    /**
     * 成功响应
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(Constants.SUCCESS, "操作成功");
    }
    
    /**
     * 成功响应（带数据）
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(Constants.SUCCESS, "操作成功", data);
    }
    
    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(Constants.SUCCESS, message, data);
    }
    
    /**
     * 失败响应
     */
    public static <T> ApiResponse<T> fail() {
        return new ApiResponse<>(Constants.FAIL, "操作失败");
    }
    
    /**
     * 失败响应（自定义消息）
     */
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(Constants.FAIL, message);
    }
    
    /**
     * 失败响应（自定义状态码和消息）
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message);
    }
    
    /**
     * 未授权响应
     */
    public static <T> ApiResponse<T> unauthorized() {
        return new ApiResponse<>(Constants.UNAUTHORIZED, "未授权访问");
    }
    
    /**
     * 禁止访问响应
     */
    public static <T> ApiResponse<T> forbidden() {
        return new ApiResponse<>(Constants.FORBIDDEN, "禁止访问");
    }
    
    /**
     * 未找到响应
     */
    public static <T> ApiResponse<T> notFound() {
        return new ApiResponse<>(Constants.NOT_FOUND, "资源未找到");
    }
} 