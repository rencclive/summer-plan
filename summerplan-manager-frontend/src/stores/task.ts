import { defineStore } from 'pinia';
import { getTasks, getTaskDetail, createTask, updateTask, deleteTask, recordTaskProgress, Task } from '@/api/task';

export const useTaskStore = defineStore('task', {
  state: () => ({
    tasks: [] as Task[],
    currentTask: null as Task | null,
  }),
  actions: {
    async fetchTasks(planId?: number) {
      this.tasks = await getTasks(planId);
    },
    async fetchTaskDetail(id: number) {
      this.currentTask = await getTaskDetail(id);
    },
    async createTaskAction(data: Partial<Task>) {
      await createTask(data);
      await this.fetchTasks(data.planId);
    },
    async updateTaskAction(id: number, data: Partial<Task>) {
      await updateTask(id, data);
      await this.fetchTasks(data.planId);
    },
    async deleteTaskAction(id: number, planId?: number) {
      await deleteTask(id);
      await this.fetchTasks(planId);
    },
    async recordTaskProgressAction(taskId: number, progress: string) {
      await recordTaskProgress(taskId, progress);
      await this.fetchTaskDetail(taskId);
    },
  },
}); 