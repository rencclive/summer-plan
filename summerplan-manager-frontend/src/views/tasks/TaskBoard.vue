<template>
  <MainLayout>
    <template #default>
      <div class="task-board-view">
        <div class="task-board-header">
          <h2>任务看板</h2>
          <el-button type="primary" @click="onCreate">新建任务</el-button>
        </div>
        <el-table :data="tasks" style="width: 100%; margin-top: 16px;">
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="title" label="任务标题" />
          <el-table-column prop="planTitle" label="所属计划" />
          <el-table-column prop="description" label="描述" />
          <el-table-column prop="status" label="状态" width="100" />
          <el-table-column prop="priority" label="优先级" width="80" />
          <el-table-column prop="dueDate" label="截止日期" width="120" />
          <el-table-column prop="progress" label="进度" width="100">
            <template #default="scope">
              <span v-if="scope.row.progress !== undefined">{{ scope.row.progress }}%</span>
              <span v-else>--</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="320">
            <template #default="scope">
              <el-button size="small" type="primary" @click="onEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="warning" @click="onProgress(scope.row)">进度</el-button>
              <el-button size="small" type="danger" @click="onDelete(scope.row)" :loading="deleteLoadingId === scope.row.id">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </template>
  </MainLayout>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getAllTasks, deleteTask } from '@/api/task';
import { ElMessage, ElMessageBox } from 'element-plus';
import MainLayout from '@/layouts/MainLayout.vue';

const route = useRoute();
const router = useRouter();
const planId = ref<number | null>(null);
const tasks = ref([]);
const deleteLoadingId = ref<number | null>(null);

const fetchTasks = async () => {
  const res = await getAllTasks();
  tasks.value = res.data || res;
};

onMounted(fetchTasks);

watch(() => route.query.planId, fetchTasks);

const onCreate = () => {
  router.push({ path: '/tasks/create', query: { planId: planId.value } });
};
const onEdit = (task: any) => {
  router.push({ path: `/tasks/${task.id}`, query: { planId: planId.value } });
};
const onDetail = (task: any) => {
  router.push({ path: `/tasks/${task.id}`, query: { planId: planId.value } });
};
const onProgress = (task: any) => {
  router.push({ path: `/tasks/${task.id}/progress`, query: { planId: planId.value } });
};
const onDelete = (task: any) => {
  ElMessageBox.confirm(`确定要删除任务「${task.title}」吗？`, '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消',
  })
    .then(async () => {
      deleteLoadingId.value = task.id;
      await deleteTask(task.id);
      ElMessage.success('删除成功');
      await fetchTasks();
      deleteLoadingId.value = null;
    })
    .catch(() => {
      deleteLoadingId.value = null;
    });
};
const onBackToPlan = () => {
  if (planId.value) router.push(`/plans/${planId.value}`);
};
</script>

<style scoped>
.task-board-view {
  padding: 24px;
}
.task-board-header {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 12px;
  margin-bottom: 12px;
}
</style> 