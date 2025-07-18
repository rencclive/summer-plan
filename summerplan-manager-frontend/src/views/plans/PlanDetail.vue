<template>
  <MainLayout>
    <template #default>
      <div class="plan-detail-view" v-if="plan">
        <h2>计划详情</h2>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="ID">{{ plan.id }}</el-descriptions-item>
          <el-descriptions-item label="名称">{{ plan.name }}</el-descriptions-item>
          <el-descriptions-item label="描述">{{ plan.description }}</el-descriptions-item>
          <el-descriptions-item label="开始日期">{{ plan.startDate }}</el-descriptions-item>
          <el-descriptions-item label="结束日期">{{ plan.endDate }}</el-descriptions-item>
        </el-descriptions>
        <div class="plan-detail-actions">
          <el-button type="primary" @click="onEdit">编辑</el-button>
          <el-button type="danger" @click="onDelete" :loading="deleteLoading">删除</el-button>
          <el-button type="warning" @click="onTasks">查看任务</el-button>
        </div>
      </div>
    </template>
  </MainLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { usePlanStore } from '@/stores/plan';
import { ElMessage, ElMessageBox } from 'element-plus';
import MainLayout from '@/layouts/MainLayout.vue';

const route = useRoute();
const router = useRouter();
const planStore = usePlanStore();
const plan = ref<any>(null);
const deleteLoading = ref(false);

const fetchPlan = async () => {
  await planStore.fetchPlanDetail(Number(route.params.id));
  plan.value = planStore.currentPlan;
};

onMounted(fetchPlan);

const onEdit = () => {
  router.push(`/plans/${plan.value.id}/edit`);
};
const onDelete = () => {
  ElMessageBox.confirm('删除计划会同时删除该计划下的所有任务，是否确认？', '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消',
  })
    .then(async () => {
      deleteLoading.value = true;
      await planStore.deletePlanAction(plan.value.id);
      ElMessage.success('删除成功');
      router.push('/plans');
    })
    .finally(() => {
      deleteLoading.value = false;
    });
};
const onTasks = () => {
  router.push({ path: '/tasks', query: { planId: plan.value.id } });
};
</script>

<style scoped>
.plan-detail-view {
  padding: 24px;
  max-width: 600px;
  margin: 0 auto;
}
.plan-detail-actions {
  margin-top: 24px;
  display: flex;
  gap: 16px;
}
</style> 