package com.summerplan.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务进度历史响应DTO
 */
@Data
public class TaskProgressResponse {
    private Long id;
    private Long userId;
    private Integer progress;
    private String comment;
    private LocalDateTime createdTime;
} 