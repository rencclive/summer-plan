import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { getActivePinia } from 'pinia';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard',
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/plans',
    name: 'PlanList',
    component: () => import('@/views/plans/PlanList.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/plans/create',
    name: 'PlanCreate',
    component: () => import('@/views/plans/PlanForm.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/plans/:id',
    name: 'PlanDetail',
    component: () => import('@/views/plans/PlanDetail.vue'),
    meta: { requiresAuth: true },
    props: true,
  },
  {
    path: '/plans/:id/edit',
    name: 'PlanEdit',
    component: () => import('@/views/plans/PlanForm.vue'),
    meta: { requiresAuth: true },
    props: true,
  },
  {
    path: '/tasks',
    name: 'TaskBoard',
    component: () => import('@/views/tasks/TaskBoard.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/tasks/create',
    name: 'TaskCreate',
    component: () => import('@/views/tasks/TaskForm.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/tasks/:id',
    name: 'TaskDetail',
    component: () => import('@/views/tasks/TaskForm.vue'),
    meta: { requiresAuth: true },
    props: true,
  },
  {
    path: '/tasks/:id/progress',
    name: 'TaskProgress',
    component: () => import('@/views/tasks/TaskProgress.vue'),
    meta: { requiresAuth: true },
    props: true,
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { guest: true },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { guest: true },
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const pinia = getActivePinia();
  const userStore = useUserStore(pinia);
  if (to.meta.requiresAuth && !userStore.token) {
    next({ path: '/login' });
  } else if (to.meta.guest && userStore.token) {
    next({ path: '/plans' });
  } else {
    next();
  }
});

export default router; 