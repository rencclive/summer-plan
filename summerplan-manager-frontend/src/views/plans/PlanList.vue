<template>
  <MainLayout>
    <template #default>
      <div class="plan-list-view">
        <div class="plan-list-header">
          <h2>计划列表</h2>
          <el-button type="primary" @click="onCreate">新建计划</el-button>
        </div>
        <el-table :data="plans" style="width: 100%; margin-top: 16px;">
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="title" label="计划名称" />
          <el-table-column prop="description" label="描述" />
          <el-table-column prop="startDate" label="开始日期" width="120" />
          <el-table-column prop="endDate" label="结束日期" width="120" />
          <el-table-column prop="status" label="计划状态" width="100" />
          <el-table-column prop="progress" label="计划进度" width="100">
            <template #default="scope">
              <span v-if="scope.row.progress !== undefined">{{ scope.row.progress }}%</span>
              <span v-else>--</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160">
            <template #default="scope">
              <el-button size="small" type="primary" @click="onEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="onDelete(scope.row)" :loading="deleteLoadingId === scope.row.id">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </template>
  </MainLayout>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { usePlanStore } from '@/stores/plan';
import { ElMessage, ElMessageBox } from 'element-plus';
import MainLayout from '@/layouts/MainLayout.vue';

const planStore = usePlanStore();
const router = useRouter();
const plans = ref([]);
const deleteLoadingId = ref<number | null>(null);

const fetchPlans = async () => {
  await planStore.fetchPlans();
  plans.value = planStore.plans;
};

onMounted(fetchPlans);

const onCreate = () => {
  router.push('/plans/create');
};
const onEdit = (plan: any) => {
  router.push(`/plans/${plan.id}/edit`);
};
const onDetail = (plan: any) => {
  router.push(`/plans/${plan.id}`);
};
const onDelete = (plan: any) => {
  ElMessageBox.confirm(`删除计划会同时删除该计划下的所有任务，是否确认？`, '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消',
  })
    .then(async () => {
      deleteLoadingId.value = plan.id;
      await planStore.deletePlanAction(plan.id);
      ElMessage.success('删除成功');
      await fetchPlans();
      deleteLoadingId.value = null;
    })
    .catch(() => {
      deleteLoadingId.value = null;
    });
};
</script>

<style scoped>
.plan-list-view {
  padding: 24px;
}
.plan-list-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}
</style> 