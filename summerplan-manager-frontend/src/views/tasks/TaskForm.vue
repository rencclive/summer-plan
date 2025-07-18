<template>
  <MainLayout>
    <template #default>
      <div class="task-form-view">
        <h2>{{ isEdit ? '编辑任务' : '新建任务' }}</h2>
        <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" style="max-width: 500px; margin: 0 auto;">
          <el-form-item v-if="!planId" label="所属计划" prop="planId">
            <el-select v-model="form.planId" placeholder="请选择计划" filterable>
              <el-option v-for="plan in planList.filter((p: SummerPlan) => p.id !== 0)" :key="plan.id" :label="plan.title" :value="plan.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" />
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input v-model="form.description" type="textarea" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态">
              <el-option label="未开始" value="未开始" />
              <el-option label="进行中" value="进行中" />
              <el-option label="已完成" value="已完成" />
            </el-select>
          </el-form-item>
          <el-form-item label="截止日期" prop="dueDate">
            <el-date-picker v-model="form.dueDate" type="date" placeholder="选择日期" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="form.priority" placeholder="请选择优先级">
              <el-option label="高" value="高" />
              <el-option label="中" value="中" />
              <el-option label="低" value="低" />
            </el-select>
          </el-form-item>
          <el-form-item label="进度" prop="progress">
            <el-input-number v-model="form.progress" :min="0" :max="100" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit" :loading="loading">{{ isEdit ? '保存' : '创建' }}</el-button>
            <el-button @click="onCancel">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </template>
  </MainLayout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { createTask, updateTask, getTaskDetail } from '@/api/task';
import { ElMessage } from 'element-plus';
import MainLayout from '@/layouts/MainLayout.vue';
import { getPlans } from '@/api/plan';
import type { SummerPlan } from '@/types/plan';
const planList = ref<SummerPlan[]>([]);

const route = useRoute();
const router = useRouter();
const isEdit = !!route.params.id;
const planId = computed(() => {
  const val = Number(route.query.planId);
  return isNaN(val) ? undefined : val;
});

const form = reactive({
  planId: undefined,
  title: '',
  description: '',
  status: '未开始',
  dueDate: '',
  priority: '',
  progress: 0,
});

const rules = {
  planId: [{ required: !planId.value, message: '请选择计划', trigger: 'change' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  dueDate: [{ required: true, message: '请选择截止日期', trigger: 'change' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
  progress: [
    { required: true, message: '请输入进度', trigger: 'blur' },
    { type: 'number', min: 0, max: 100, message: '进度范围0-100', trigger: 'blur' }
  ],
};

onMounted(async () => {
  if (!planId.value) {
    const res = await getPlans();
    planList.value = res.data || res;
  }
  if (isEdit) {
    const res = await getTaskDetail(Number(route.params.id));
    const data = res.data || res;
    // 字段映射，保证所有表单字段都被正确赋值
    form.planId = typeof data.planId === 'number' ? data.planId : undefined;
    form.title = data.title || '';
    form.description = data.description || '';
    form.status = data.status || '未开始';
    form.dueDate = data.dueDate || '';
    form.priority = data.priority || '';
    form.progress = typeof data.progress === 'number' ? data.progress : 0;
  }
});

// 监听状态变化，若为已完成则自动设进度为100
watch(() => form.status, (val) => {
  if (val === '已完成') {
    form.progress = 100;
  }
});

const onSubmit = async () => {
  await formRef.value.validate();
  loading.value = true;
  try {
    const submitPlanId = planId.value || form.planId;
    if (!submitPlanId) {
      ElMessage.error('请选择计划');
      loading.value = false;
      return;
    }
    if (isEdit) {
      await updateTask(Number(route.params.id), form);
      ElMessage.success('保存成功');
    } else {
      await createTask(submitPlanId, form);
      ElMessage.success('创建成功');
    }
    router.push({ path: '/tasks', query: { planId: String(submitPlanId) } });
  } finally {
    loading.value = false;
  }
};
const onCancel = () => {
  router.push({ path: '/tasks', query: { planId: String(planId.value) } });
};
</script>

<style scoped>
.task-form-view {
  padding: 24px;
}
</style> 