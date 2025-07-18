<template>
  <MainLayout>
    <template #default>
      <div class="plan-form-view">
        <h2>{{ isEdit ? '编辑计划' : '新建计划' }}</h2>
        <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" style="max-width: 500px; margin: 0 auto;">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" />
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input v-model="form.description" type="textarea" />
          </el-form-item>
          <el-form-item label="开始日期" prop="startDate">
            <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="结束日期" prop="endDate">
            <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" :disabled="form.status === '已完成'">
              <el-option label="草稿" value="草稿" />
              <el-option label="未开始" value="未开始" />
              <el-option label="进行中" value="进行中" />
              <el-option label="已完成" value="已完成" />
            </el-select>
          </el-form-item>
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="form.priority" placeholder="请选择优先级">
              <el-option label="高" value="HIGH" />
              <el-option label="中" value="MEDIUM" />
              <el-option label="低" value="LOW" />
            </el-select>
          </el-form-item>
          <el-form-item label="标签" prop="tags">
            <el-input v-model="form.tags" placeholder="多个标签用逗号分隔" />
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
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { usePlanStore } from '@/stores/plan';
import { ElMessage } from 'element-plus';
import MainLayout from '@/layouts/MainLayout.vue';

const route = useRoute();
const router = useRouter();
const planStore = usePlanStore();
const isEdit = !!route.params.id;
const formRef = ref();
const loading = ref(false);
const form = ref({
  title: '',
  description: '',
  startDate: '',
  endDate: '',
  status: '草稿',
  priority: 'MEDIUM',
  tags: '',
});
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
};

onMounted(async () => {
  if (isEdit) {
    await planStore.fetchPlanDetail(Number(route.params.id));
    Object.assign(form.value, planStore.currentPlan);
  }
});

const onSubmit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    loading.value = true;
    try {
      if (isEdit) {
        await planStore.updatePlanAction(Number(route.params.id), form.value);
        ElMessage.success('修改成功');
      } else {
        await planStore.createPlanAction(form.value);
        ElMessage.success('创建成功');
      }
      router.push('/plans');
    } finally {
      loading.value = false;
    }
  });
};
const onCancel = () => {
  router.back();
};
</script>

<style scoped>
.plan-form-view {
  padding: 24px;
}
</style> 