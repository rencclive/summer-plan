<template>
  <MainLayout>
    <template #default>
      <div class="task-progress-view">
        <h2>任务进度</h2>
        <el-timeline style="margin-bottom: 24px;">
          <el-timeline-item
            v-for="item in progressList"
            :key="item.id"
            :timestamp="item.timestamp"
            :color="item.status === '完成' ? 'green' : 'blue'"
          >
            {{ item.progress }}
          </el-timeline-item>
        </el-timeline>
        <el-form :model="form" ref="formRef" label-width="80px" style="max-width: 500px; margin: 0 auto;">
          <el-form-item label="进度" prop="progress">
            <el-input v-model="form.progress" type="textarea" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit" :loading="loading">记录进度</el-button>
            <el-button @click="onBack">返回</el-button>
          </el-form-item>
        </el-form>
      </div>
    </template>
  </MainLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getTaskProgressHistory, recordTaskProgress } from '@/api/task';
import { ElMessage } from 'element-plus';
import MainLayout from '@/layouts/MainLayout.vue';

interface ProgressItem {
  id: number;
  timestamp: string;
  status: string;
  progress: string;
}

const route = useRoute();
const router = useRouter();
const planId = Number(route.query.planId);
const taskId = Number(route.params.id);
const progressList = ref<ProgressItem[]>([]);
const formRef = ref();
const form = ref({ progress: '' });
const loading = ref(false);

const fetchProgress = async () => {
  const res = await getTaskProgressHistory(taskId);
  progressList.value = res.data || res;
};

onMounted(fetchProgress);

const onSubmit = async () => {
  await formRef.value.validate();
  loading.value = true;
  try {
    await recordTaskProgress(taskId, form.value);
    ElMessage.success('进度记录成功');
    form.value.progress = '';
    await fetchProgress();
  } finally {
    loading.value = false;
  }
};
const onBack = () => {
  router.push({ path: '/tasks', query: { planId } });
};
</script>

<style scoped>
.task-progress-view {
  padding: 24px;
}
</style> 