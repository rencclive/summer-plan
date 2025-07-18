package com.summerplan.service.impl;

import com.summerplan.dto.TaskRequest;
import com.summerplan.dto.TaskResponse;
import com.summerplan.entity.Task;
import com.summerplan.repository.TaskRepository;
import com.summerplan.service.TaskService;
import com.summerplan.service.SummerPlanService;
import com.summerplan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final SummerPlanService summerPlanService;
    private final UserService userService;

    @Override
    public List<TaskResponse> getTasksByPlanId(Long planId) {
        return taskRepository.findByPlanId(planId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse createTask(Long planId, TaskRequest request) {
        Task task = new Task();
        BeanUtils.copyProperties(request, task);
        task.setPlanId(planId);
        Task saved = taskRepository.save(task);
        // 新建任务后，更新计划进度
        summerPlanService.updatePlanProgressByTasks(planId);
        // 新建任务后，同步所属计划状态（包含新任务状态）
        List<Task> allTasks = taskRepository.findByPlanId(planId);
        // 确保包含新任务的最新状态
        boolean allNotStarted = allTasks.stream().allMatch(t -> "未开始".equals(t.getStatus()));
        boolean allCompleted = allTasks.stream().allMatch(t -> "已完成".equals(t.getStatus()));
        boolean anyInProgress = allTasks.stream().anyMatch(t -> "进行中".equals(t.getStatus()));
        com.summerplan.entity.SummerPlan plan = summerPlanService.getPlanById(planId).orElse(null);
        if (plan != null) {
            if (allNotStarted && !"未开始".equals(plan.getStatus())) {
                plan.setStatus("未开始");
                log.info("计划[{}]所有任务未开始，计划状态自动改为未开始", plan.getId());
                summerPlanService.updatePlan(plan.getId(), plan);
            } else if (allCompleted && !"已完成".equals(plan.getStatus())) {
                plan.setStatus("已完成");
                log.info("计划[{}]所有任务已完成，计划状态自动改为已完成", plan.getId());
                summerPlanService.updatePlan(plan.getId(), plan);
            } else if (!"进行中".equals(plan.getStatus())) {
                plan.setStatus("进行中");
                log.info("计划[{}]部分任务已开始，计划状态自动改为进行中", plan.getId());
                summerPlanService.updatePlan(plan.getId(), plan);
            }
        }
        return toResponse(saved);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("任务不存在"));
        BeanUtils.copyProperties(request, task);
        Task saved = taskRepository.save(task);
        // 修改任务后，立即同步更新计划进度
        summerPlanService.updatePlanProgressByTasks(task.getPlanId());
        // 任务状态变更后，同步所属计划状态
        List<Task> allTasks = taskRepository.findByPlanId(task.getPlanId());
        boolean allNotStarted = allTasks.stream().allMatch(t -> "未开始".equals(t.getStatus()));
        boolean allCompleted = allTasks.stream().allMatch(t -> "已完成".equals(t.getStatus()));
        boolean anyInProgress = allTasks.stream().anyMatch(t -> "进行中".equals(t.getStatus()));
        com.summerplan.entity.SummerPlan plan = summerPlanService.getPlanById(task.getPlanId()).orElse(null);
        if (plan != null) {
            String oldStatus = plan.getStatus();
            if (allNotStarted && !"未开始".equals(plan.getStatus())) {
                plan.setStatus("未开始");
                log.info("计划[{}]所有任务未开始，计划状态自动改为未开始", plan.getId());
                summerPlanService.updatePlan(plan.getId(), plan);
            } else if (allCompleted && !"已完成".equals(plan.getStatus())) {
                plan.setStatus("已完成");
                log.info("计划[{}]所有任务已完成，计划状态自动改为已完成", plan.getId());
                summerPlanService.updatePlan(plan.getId(), plan);
            } else if (anyInProgress && !"进行中".equals(plan.getStatus())) {
                plan.setStatus("进行中");
                log.info("计划[{}]存在进行中任务，计划状态自动改为进行中", plan.getId());
                summerPlanService.updatePlan(plan.getId(), plan);
            }
        }
        return toResponse(saved);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            Long planId = task.getPlanId();
            taskRepository.deleteById(id);
            // 删除任务后，更新计划进度
            summerPlanService.updatePlanProgressByTasks(planId);
        } else {
            taskRepository.deleteById(id);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByUserId(Long userId) {
        // 获取该用户所有计划ID
        List<com.summerplan.entity.SummerPlan> plans = summerPlanService.getPlansByUserId(userId);
        List<Long> planIds = plans.stream().map(com.summerplan.entity.SummerPlan::getId).collect(Collectors.toList());
        if (planIds.isEmpty()) return List.of();
        return taskRepository.findAll().stream()
                .filter(task -> planIds.contains(task.getPlanId()))
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("任务不存在"));
        return toResponse(task);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse resp = new TaskResponse();
        BeanUtils.copyProperties(task, resp);
        return resp;
    }
} 