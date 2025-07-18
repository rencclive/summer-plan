export interface Plan {
  id: number;
  userId: number;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  status: string;
  priority: string;
  progress: number;
  tags: string;
  createdTime: string;
  updatedTime: string;
}

export type SummerPlan = Plan; 