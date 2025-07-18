package com.summerplan.controller;

import com.summerplan.common.response.ApiResponse;
import com.summerplan.entity.SummerPlan;
import com.summerplan.entity.User;
import com.summerplan.service.SummerPlanService;
import com.summerplan.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "计划管理", description = "暑假计划管理接口")
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {
    private final SummerPlanService summerPlanService;
    private final UserService userService;

    @Operation(summary = "获取用户所有计划")
    @GetMapping
    public ApiResponse<List<SummerPlan>> getPlans() {
        User user = userService.getCurrentUser();
        List<SummerPlan> plans;
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            plans = summerPlanService.getAllPlans();
        } else {
            plans = summerPlanService.getPlansByUserId(user.getId());
        }
        return ApiResponse.success(plans);
    }

    @Operation(summary = "创建新计划")
    @PostMapping
    public ApiResponse<SummerPlan> createPlan(@Valid @RequestBody SummerPlan plan) {
        plan.setUserId(userService.getCurrentUser().getId());
        SummerPlan created = summerPlanService.createPlan(plan);
        return ApiResponse.success("创建成功", created);
    }

    @Operation(summary = "获取计划详情")
    @GetMapping("/{id}")
    public ApiResponse<SummerPlan> getPlan(@PathVariable Long id) {
        return summerPlanService.getPlanById(id)
                .map(ApiResponse::success)
                .orElseGet(() -> ApiResponse.fail("计划不存在"));
    }

    @Operation(summary = "更新计划")
    @PutMapping("/{id}")
    public ApiResponse<SummerPlan> updatePlan(@PathVariable Long id, @Valid @RequestBody SummerPlan plan) {
        plan.setUserId(userService.getCurrentUser().getId());
        SummerPlan updated = summerPlanService.updatePlan(id, plan);
        return ApiResponse.success("更新成功", updated);
    }

    @Operation(summary = "删除计划")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePlan(@PathVariable Long id) {
        summerPlanService.deletePlan(id);
        return ApiResponse.success("删除成功");
    }
} 