import { defineStore } from 'pinia';
import { getPlans, getPlanDetail, createPlan, updatePlan, deletePlan, Plan } from '@/api/plan';

export const usePlanStore = defineStore('plan', {
  state: () => ({
    plans: [] as Plan[],
    currentPlan: null as Plan | null,
  }),
  actions: {
    async fetchPlans() {
      this.plans = await getPlans();
    },
    async fetchPlanDetail(id: number) {
      this.currentPlan = await getPlanDetail(id);
    },
    async createPlanAction(data: Partial<Plan>) {
      await createPlan(data);
      await this.fetchPlans();
    },
    async updatePlanAction(id: number, data: Partial<Plan>) {
      await updatePlan(id, data);
      await this.fetchPlans();
    },
    async deletePlanAction(id: number) {
      await deletePlan(id);
      await this.fetchPlans();
    },
  },
}); 