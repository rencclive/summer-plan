package com.summerplan.common.constant;

/**
 * 系统常量
 */
public class Constants {
    
    /**
     * 成功状态码
     */
    public static final int SUCCESS = 200;
    
    /**
     * 失败状态码
     */
    public static final int FAIL = 500;
    
    /**
     * 未授权状态码
     */
    public static final int UNAUTHORIZED = 401;
    
    /**
     * 禁止访问状态码
     */
    public static final int FORBIDDEN = 403;
    
    /**
     * 未找到状态码
     */
    public static final int NOT_FOUND = 404;
    
    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NUMBER = 1;
    
    /**
     * 默认页面大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 最大页面大小
     */
    public static final int MAX_PAGE_SIZE = 100;
    
    /**
     * JWT令牌前缀
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    
    /**
     * JWT令牌头
     */
    public static final String JWT_TOKEN_HEADER = "Authorization";
    
    /**
     * 用户ID字段
     */
    public static final String USER_ID_FIELD = "userId";
    
    /**
     * 用户名字段
     */
    public static final String USERNAME_FIELD = "username";
    
    /**
     * 角色字段
     */
    public static final String ROLE_FIELD = "role";
    
    /**
     * 时间格式
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
} 