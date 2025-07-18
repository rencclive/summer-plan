CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    due_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'TODO',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    progress INT NOT NULL DEFAULT 0,
    created_time DATETIME NOT NULL,
    updated_time DATETIME,
    CONSTRAINT fk_plan_id FOREIGN KEY (plan_id) REFERENCES summer_plans(id) ON DELETE CASCADE
); 