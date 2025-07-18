package com.summerplan.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private Long planId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;
    private String priority;
    private Integer progress;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
} 