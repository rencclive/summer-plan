-- 任务表
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
    plan_id BIGINT NOT NULL COMMENT '计划ID',
    title VARCHAR(200) NOT NULL COMMENT '任务标题',
    description TEXT COMMENT '任务描述',
    due_date DATE COMMENT '截止日期',
    status VARCHAR(20) NOT NULL DEFAULT 'TODO' COMMENT '状态：TODO-待办，IN_PROGRESS-进行中，DONE-已完成',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' COMMENT '优先级：LOW-低，MEDIUM-中，HIGH-高',
    progress INT NOT NULL DEFAULT 0 COMMENT '进度百分比',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    CONSTRAINT fk_plan_id FOREIGN KEY (plan_id) REFERENCES summer_plans(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务表'; 