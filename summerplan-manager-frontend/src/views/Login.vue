<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="title">登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="60px" @keyup.enter="onSubmit">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="username" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" autocomplete="current-password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="onSubmit" style="width:100%">登录</el-button>
        </el-form-item>
        <el-form-item>
          <span>没有账号？<router-link to="/register">注册</router-link></span>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { ElMessage } from 'element-plus';

const router = useRouter();
const userStore = useUserStore();
const loading = ref(false);
const formRef = ref();
const form = ref({
  username: '',
  password: '',
});
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

const onSubmit = async () => {
  console.log('onSubmit called');
  if (!formRef.value) return;
  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    loading.value = true;
    try {
      await userStore.loginAction(form.value);
      ElMessage.success('登录成功');
      window.location.href = '/profile';
    } catch (e: any) {
      console.error('login error:', e);
      ElMessage.error(e || '登录失败');
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f6fa;
}
.login-card {
  width: 350px;
  max-width: 90vw;
  margin: 0 auto;
  border-radius: 12px;
  box-shadow: 0 2px 12px #0001;
}
.title {
  text-align: center;
  margin-bottom: 24px;
}
</style> 