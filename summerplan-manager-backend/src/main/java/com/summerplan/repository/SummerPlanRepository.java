package com.summerplan.repository;

import com.summerplan.entity.SummerPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SummerPlanRepository extends JpaRepository<SummerPlan, Long> {
    // 查询某个用户的所有计划
    List<SummerPlan> findByUserId(Long userId);
} 