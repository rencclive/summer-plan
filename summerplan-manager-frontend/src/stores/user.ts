import { defineStore } from 'pinia';
import { login, register, getUserInfo } from '@/api/auth';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: null as null | { id: number; username: string },
  }),
  actions: {
    async loginAction(payload: { username: string; password: string }) {
      const res = await login(payload);
      console.log('login res:', res); // 调试打印
      // 兼容 token 字段名
      const token = res.token || res.accessToken || '';
      this.token = token;
      if (token) {
        localStorage.setItem('token', token);
      }
      this.user = res.user || null;
    },
    async registerAction(payload: { username: string; password: string }) {
      await register(payload);
    },
    async fetchUserInfo() {
      const user = await getUserInfo();
      this.user = user;
    },
    logout() {
      this.token = '';
      this.user = null;
      localStorage.removeItem('token');
    },
  },
}); 