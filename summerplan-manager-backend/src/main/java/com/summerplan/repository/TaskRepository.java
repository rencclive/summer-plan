package com.summerplan.repository;

import com.summerplan.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByPlanId(Long planId);
    void deleteByPlanId(Long planId);
} 