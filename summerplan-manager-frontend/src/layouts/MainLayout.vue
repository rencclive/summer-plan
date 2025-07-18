<template>
  <el-container class="main-layout">
    <el-header class="main-header">
      <div class="logo">暑假计划管理</div>
      <el-menu
        v-if="user"
        mode="horizontal"
        :default-active="activeMenu"
        background-color="#409eff"
        text-color="#fff"
        active-text-color="#ffd04b"
        class="main-menu"
        @select="onMenuSelect"
      >
        <el-menu-item index="/dashboard">仪表盘</el-menu-item>
        <el-menu-item index="/plans">计划管理</el-menu-item>
        <el-menu-item index="/tasks">任务看板</el-menu-item>
        <el-menu-item index="/profile">个人信息</el-menu-item>
      </el-menu>
      <div class="spacer"></div>
      <div v-if="user" class="user-info">
        <el-icon><User /></el-icon>
        <span class="username">{{ user.username }}</span>
        <el-button type="text" @click="onLogout">退出</el-button>
      </div>
    </el-header>
    <el-main class="main-content">
      <slot />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useUserStore } from '@/stores/user';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { User } from '@element-plus/icons-vue';

const userStore = useUserStore();
const router = useRouter();
const route = useRoute();
const user = computed(() => userStore.user);

const activeMenu = computed(() => {
  // 只高亮主菜单
  if (route.path.startsWith('/plans')) return '/plans';
  if (route.path.startsWith('/tasks')) return '/tasks';
  if (route.path.startsWith('/profile')) return '/profile';
  if (route.path.startsWith('/settings')) return '/settings';
  if (route.path.startsWith('/apidocs')) return '/apidocs';
  return '/dashboard';
});

const onMenuSelect = (index: string) => {
  if (index !== route.path) {
    router.push(index);
  }
};

const onLogout = () => {
  userStore.logout();
  ElMessage.success('已退出登录');
  router.push('/login');
};

onMounted(() => {
  if (userStore.token && !userStore.user) {
    userStore.fetchUserInfo();
  }
});
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
  background: #f5f6fa;
  display: flex;
  flex-direction: column;
}
.main-header {
  display: flex;
  align-items: center;
  height: 56px;
  background: #409eff;
  color: #fff;
  padding: 0 24px;
  box-shadow: 0 2px 8px #0001;
}
.main-menu {
  flex: none;
  margin-left: 32px;
  min-width: 600px;
  background: transparent;
  border-bottom: none;
}
.logo {
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 2px;
}
.spacer {
  flex: 1;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}
.username {
  font-weight: 500;
}
.main-content {
  flex: 1;
  padding: 24px 0;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
@media (max-width: 600px) {
  .main-header {
    flex-direction: column;
    height: auto;
    padding: 8px 8px;
  }
  .main-content {
    padding: 8px 0;
  }
  .logo {
    font-size: 16px;
  }
}
</style> 