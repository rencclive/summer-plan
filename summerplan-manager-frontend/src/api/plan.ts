import request from '@/utils/request';

export interface Plan {
  id: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
}

export function getPlans() {
  return request.get('/plans');
}

export function getPlanDetail(id: number) {
  return request.get(`/plans/${id}`);
}

export function createPlan(data: Partial<Plan>) {
  return request.post('/plans', data);
}

export function updatePlan(id: number, data: Partial<Plan>) {
  return request.put(`/plans/${id}`, data);
}

export function deletePlan(id: number) {
  return request.delete(`/plans/${id}`);
} 