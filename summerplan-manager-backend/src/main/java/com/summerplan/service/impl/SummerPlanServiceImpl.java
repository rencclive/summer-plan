package com.summerplan.service.impl;

import com.summerplan.entity.SummerPlan;
import com.summerplan.repository.SummerPlanRepository;
import com.summerplan.repository.TaskRepository;
import com.summerplan.service.SummerPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummerPlanServiceImpl implements SummerPlanService {
    private final SummerPlanRepository summerPlanRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<SummerPlan> getPlansByUserId(Long userId) {
        return summerPlanRepository.findByUserId(userId);
    }

    @Override
    public Optional<SummerPlan> getPlanById(Long id) {
        return summerPlanRepository.findById(id);
    }

    @Override
    public SummerPlan createPlan(SummerPlan plan) {
        return summerPlanRepository.save(plan);
    }

    @Override
    public SummerPlan updatePlan(Long id, SummerPlan plan) {
        plan.setId(id);
        // 检查是否需要批量更新任务
        SummerPlan oldPlan = summerPlanRepository.findById(id).orElse(null);
        if (oldPlan != null && !"已完成".equals(oldPlan.getStatus()) && "已完成".equals(plan.getStatus())) {
            // 计划状态由非“已完成”变为“已完成”
            List<com.summerplan.entity.Task> tasks = taskRepository.findByPlanId(id);
            boolean needUpdate = false;
            for (com.summerplan.entity.Task task : tasks) {
                if (!"已完成".equals(task.getStatus()) || task.getProgress() != 100) {
                    log.info("计划[{}]变为已完成，任务[{}]状态/进度将被强制设为已完成/100", id, task.getId());
                    task.setStatus("已完成");
                    task.setProgress(100);
                    needUpdate = true;
                }
            }
            if (needUpdate) {
                log.info("计划[{}]变为已完成，批量更新{}个任务为已完成/100", id, tasks.size());
                taskRepository.saveAll(tasks);
            }
        }
        return summerPlanRepository.save(plan);
    }

    @Override
    @Transactional
    public void deletePlan(Long id) {
        // 先删除该计划下所有任务
        taskRepository.deleteByPlanId(id);
        // 再删除计划本身
        summerPlanRepository.deleteById(id);
    }

    @Override
    public List<SummerPlan> getAllPlans() {
        return summerPlanRepository.findAll();
    }

    /**
     * 根据任务进度平均值更新计划进度
     */
    @Transactional
    public void updatePlanProgressByTasks(Long planId) {
        List<com.summerplan.entity.Task> tasks = taskRepository.findByPlanId(planId);
        if (tasks == null || tasks.isEmpty()) {
            return;
        }
        int avgProgress = (int) Math.round(tasks.stream().mapToInt(com.summerplan.entity.Task::getProgress).average().orElse(0));
        SummerPlan plan = summerPlanRepository.findById(planId).orElse(null);
        if (plan != null) {
            plan.setProgress(avgProgress);
            summerPlanRepository.save(plan);
        }
    }
} 