package com.summerplan.service;

import com.summerplan.dto.TaskRequest;
import com.summerplan.dto.TaskResponse;
import com.summerplan.entity.Task;

import java.util.List;

public interface TaskService {
    List<TaskResponse> getTasksByPlanId(Long planId);
    TaskResponse createTask(Long planId, TaskRequest request);
    TaskResponse updateTask(Long id, TaskRequest request);
    void deleteTask(Long id);
    List<Task> getAllTasks();
    TaskResponse getTaskById(Long id);
    List<Task> getTasksByUserId(Long userId);
} 