import request from '@/utils/request';

export interface Task {
  id: number;
  planId: number;
  title: string;
  description: string;
  status: string;
  dueDate: string;
}

// 获取某计划下所有任务
export function getTasksByPlan(planId: number) {
  return request.get(`/plans/${planId}/tasks`);
}

// 创建新任务
export function createTask(planId: number, data: Partial<Task>) {
  return request.post(`/plans/${planId}/tasks`, data);
}

// 获取任务详情
export function getTaskDetail(id: number) {
  return request.get(`/tasks/${id}`);
}

// 更新任务
export function updateTask(id: number, data: Partial<Task>) {
  return request.put(`/tasks/${id}`, data);
}

// 删除任务
export function deleteTask(id: number) {
  return request.delete(`/tasks/${id}`);
}

// 获取任务进度历史
export function getTaskProgressHistory(taskId: number) {
  return request.get(`/tasks/${taskId}/progress`);
}

// 记录任务进度
export function recordTaskProgress(taskId: number, data: { progress: string }) {
  return request.post(`/tasks/${taskId}/progress`, data);
}

// 获取所有任务及其所属计划和进度
export function getAllTasks(): Promise<any[]> {
  return request.get('/tasks/all');
} 