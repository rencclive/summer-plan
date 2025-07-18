<template>
  <MainLayout>
    <template #default>
      <div class="task-detail-view" v-if="task">
        <h2>任务详情</h2>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="ID">{{ task.id }}</el-descriptions-item>
          <el-descriptions-item label="标题">{{ task.title }}</el-descriptions-item>
          <el-descriptions-item label="描述">{{ task.description }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ task.status }}</el-descriptions-item>
          <el-descriptions-item label="截止日期">{{ task.dueDate }}</el-descriptions-item>
          <el-descriptions-item label="所属计划">{{ task.planId }}</el-descriptions-item>
        </el-descriptions>
        <div class="task-detail-actions">
          <el-button type="primary" @click="onEdit">编辑</el-button>
          <el-button type="danger" @click="onDelete" :loading="deleteLoading">删除</el-button>
          <el-button type="warning" @click="onProgress">进度记录</el-button>
        </div>
      </div>
    </template>
  </MainLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useTaskStore } from '@/stores/task';
import { ElMessage, ElMessageBox } from 'element-plus';
import MainLayout from '@/layouts/MainLayout.vue';

const route = useRoute();
const router = useRouter();
const taskStore = useTaskStore();
const task = ref<any>(null);
const deleteLoading = ref(false);

const fetchTask = async () => {
  await taskStore.fetchTaskDetail(Number(route.params.id));
  task.value = taskStore.currentTask;
};

onMounted(fetchTask);

const onEdit = () => {
  router.push(`/tasks/${task.value.id}/edit`);
};
const onDelete = () => {
  ElMessageBox.confirm('确定要删除该任务吗？', '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消',
  })
    .then(async () => {
      deleteLoading.value = true;
      await taskStore.deleteTaskAction(task.value.id);
      ElMessage.success('删除成功');
      router.push('/tasks');
    })
    .finally(() => {
      deleteLoading.value = false;
    });
};
const onProgress = () => {
  router.push(`/tasks/${task.value.id}/progress`);
};
</script>

<style scoped>
.task-detail-view {
  padding: 24px;
  max-width: 600px;
  margin: 0 auto;
}
.task-detail-actions {
  margin-top: 24px;
  display: flex;
  gap: 16px;
}
</style> 