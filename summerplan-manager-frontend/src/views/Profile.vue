<template>
  <MainLayout>
    <template #default>
      <div class="profile-container">
        <el-card class="profile-card">
          <h2 class="title">个人信息</h2>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户ID">{{ user?.id }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ user?.username }}</el-descriptions-item>
          </el-descriptions>
          <el-button type="danger" style="margin-top: 24px; width: 100%;" @click="onLogout">退出登录</el-button>
        </el-card>
      </div>
    </template>
  </MainLayout>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useUserStore } from '@/stores/user';
import { useRouter } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';
import { ElMessage } from 'element-plus';

const userStore = useUserStore();
const router = useRouter();
const user = computed(() => userStore.user);

onMounted(async () => {
  if (!user.value) {
    try {
      await userStore.fetchUserInfo();
    } catch {
      router.push('/login');
    }
  }
});

const onLogout = () => {
  userStore.logout();
  ElMessage.success('已退出登录');
  router.push('/login');
};
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f6fa;
}
.profile-card {
  width: 400px;
  max-width: 95vw;
  margin: 0 auto;
  border-radius: 12px;
  box-shadow: 0 2px 12px #0001;
}
.title {
  text-align: center;
  margin-bottom: 24px;
}
</style> 