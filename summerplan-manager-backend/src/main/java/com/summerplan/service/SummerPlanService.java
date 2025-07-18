package com.summerplan.service;

import com.summerplan.entity.SummerPlan;

import java.util.List;
import java.util.Optional;

public interface SummerPlanService {
    List<SummerPlan> getPlansByUserId(Long userId);
    Optional<SummerPlan> getPlanById(Long id);
    SummerPlan createPlan(SummerPlan plan);
    SummerPlan updatePlan(Long id, SummerPlan plan);
    void deletePlan(Long id);

    /**
     * 根据任务进度平均值更新计划进度
     */
    void updatePlanProgressByTasks(Long planId);

    List<SummerPlan> getAllPlans();
} 