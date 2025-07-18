package com.summerplan.service.impl;

import com.summerplan.dto.TaskProgressRequest;
import com.summerplan.dto.TaskProgressResponse;
import com.summerplan.entity.Task;
import com.summerplan.entity.TaskProgressHistory;
import com.summerplan.repository.TaskProgressHistoryRepository;
import com.summerplan.repository.TaskRepository;
import com.summerplan.service.TaskProgressService;
import com.summerplan.service.SummerPlanService;
import com.summerplan.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskProgressServiceImpl implements TaskProgressService {
    private final TaskProgressHistoryRepository progressHistoryRepository;
    private final TaskRepository taskRepository;
    private final SummerPlanService summerPlanService;

    @Override
    @Transactional
    public void recordProgress(Long taskId, TaskProgressRequest request) {
        TaskProgressHistory history = new TaskProgressHistory();
        history.setTaskId(taskId);
        history.setProgress(request.getProgress());
        history.setComment(request.getComment());
        // 获取当前用户ID（假设已集成Spring Security，用户ID存储在Principal中）
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        if (userId == null) {
            throw new RuntimeException("无法获取当前用户ID");
        }
        history.setUserId(userId);
        progressHistoryRepository.save(history);
        // 同步更新Task表的progress字段
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            task.setProgress(request.getProgress());
            taskRepository.save(task);
            // 进度变更后，更新计划进度
            summerPlanService.updatePlanProgressByTasks(task.getPlanId());
        }
    }

    @Override
    public List<TaskProgressResponse> getProgressHistory(Long taskId) {
        List<TaskProgressHistory> list = progressHistoryRepository.findByTaskIdOrderByCreatedTimeAsc(taskId);
        return list.stream().map(entity -> {
            TaskProgressResponse resp = new TaskProgressResponse();
            BeanUtils.copyProperties(entity, resp);
            return resp;
        }).collect(Collectors.toList());
    }
} 