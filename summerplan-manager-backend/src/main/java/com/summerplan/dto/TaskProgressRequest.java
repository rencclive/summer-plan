package com.summerplan.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 任务进度记录请求DTO
 */
@Data
public class TaskProgressRequest {
    @Min(0)
    @Max(100)
    private Integer progress;

    private String comment;
} 