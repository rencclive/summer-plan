package com.summerplan.repository;

import com.summerplan.entity.TaskProgressHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskProgressHistoryRepository extends JpaRepository<TaskProgressHistory, Long> {
    List<TaskProgressHistory> findByTaskIdOrderByCreatedTimeAsc(Long taskId);
} 