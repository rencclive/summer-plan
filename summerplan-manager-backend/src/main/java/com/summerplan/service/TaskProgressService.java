package com.summerplan.service;

import com.summerplan.dto.TaskProgressRequest;
import com.summerplan.dto.TaskProgressResponse;

import java.util.List;

public interface TaskProgressService {
    void recordProgress(Long taskId, TaskProgressRequest request);
    List<TaskProgressResponse> getProgressHistory(Long taskId);
} 