package com.summerplan.controller;

import com.summerplan.common.response.ApiResponse;
import com.summerplan.dto.TaskRequest;
import com.summerplan.dto.TaskResponse;
import com.summerplan.dto.TaskProgressRequest;
import com.summerplan.dto.TaskProgressResponse;
import com.summerplan.service.TaskService;
import com.summerplan.service.TaskProgressService;
import com.summerplan.entity.Task;
import com.summerplan.entity.SummerPlan;
import com.summerplan.entity.User;
import com.summerplan.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import com.summerplan.service.SummerPlanService;

@Tag(name = "任务管理", description = "计划任务管理接口")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskProgressService taskProgressService;
    private final UserService userService;
    private final SummerPlanService summerPlanService;

    @Operation(summary = "获取计划下所有任务")
    @GetMapping("/api/plans/{planId}/tasks")
    public ApiResponse<List<TaskResponse>> getTasks(@PathVariable Long planId) {
        User user = userService.getCurrentUser();
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            // 校验该计划是否属于当前用户
            SummerPlan plan = null;
            for (SummerPlan p : userService.getCurrentUser().getRole().equalsIgnoreCase("ADMIN") ? summerPlanService.getAllPlans() : summerPlanService.getPlansByUserId(user.getId())) {
                if (p.getId().equals(planId)) {
                    plan = p;
                    break;
                }
            }
            if (plan == null) {
                return ApiResponse.fail("无权访问该计划下的任务");
            }
        }
        return ApiResponse.success(taskService.getTasksByPlanId(planId));
    }

    @Operation(summary = "创建新任务")
    @PostMapping("/api/plans/{planId}/tasks")
    public ApiResponse<TaskResponse> createTask(@PathVariable Long planId, @Valid @RequestBody TaskRequest request) {
        return ApiResponse.success("创建成功", taskService.createTask(planId, request));
    }

    @Operation(summary = "更新任务")
    @PutMapping("/api/tasks/{id}")
    public ApiResponse<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return ApiResponse.success("更新成功", taskService.updateTask(id, request));
    }

    @Operation(summary = "删除任务")
    @DeleteMapping("/api/tasks/{id}")
    public ApiResponse<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ApiResponse.success("删除成功");
    }

    @Operation(summary = "记录任务进度")
    @PostMapping("/api/tasks/{taskId}/progress")
    public ApiResponse<String> recordTaskProgress(@PathVariable Long taskId, @Valid @RequestBody TaskProgressRequest request) {
        taskProgressService.recordProgress(taskId, request);
        return ApiResponse.success("进度记录成功");
    }

    @Operation(summary = "获取任务进度历史")
    @GetMapping("/api/tasks/{taskId}/progress")
    public ApiResponse<List<TaskProgressResponse>> getTaskProgressHistory(@PathVariable Long taskId) {
        return ApiResponse.success(taskProgressService.getProgressHistory(taskId));
    }

    @Operation(summary = "获取所有任务及其所属计划和进度")
    @GetMapping("/api/tasks/all")
    public ApiResponse<List<Map<String, Object>>> getAllTasksWithPlanAndProgress() {
        User user = userService.getCurrentUser();
        List<Task> tasks;
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            tasks = taskService.getAllTasks();
        } else {
            tasks = taskService.getTasksByUserId(user.getId());
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("title", task.getTitle());
            map.put("description", task.getDescription());
            map.put("status", task.getStatus());
            map.put("priority", task.getPriority());
            map.put("progress", task.getProgress());
            map.put("dueDate", task.getDueDate());
            // 获取所属计划
            SummerPlan plan = task.getSummerPlan();
            if (plan != null) {
                map.put("planId", plan.getId());
                map.put("planTitle", plan.getTitle());
            }
            result.add(map);
        }
        return ApiResponse.success(result);
    }

    @Operation(summary = "获取任务详情")
    @GetMapping("/api/tasks/{id}")
    public ApiResponse<TaskResponse> getTaskDetail(@PathVariable Long id) {
        return ApiResponse.success(taskService.getTaskById(id));
    }
} 