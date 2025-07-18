import axios from 'axios';
import { useUserStore } from '@/stores/user';

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

// 请求拦截器：自动携带token
request.interceptors.request.use(config => {
  const userStore = useUserStore();
  if (userStore.token) {
    config.headers = config.headers || {};
    config.headers['Authorization'] = `Bearer ${userStore.token}`;
  }
  return config;
});

// 响应拦截器：统一错误处理
request.interceptors.response.use(
  response => {
    // 兼容 code 为 0 或 200
    if (response.data && response.data.code !== 0 && response.data.code !== 200) {
      return Promise.reject(response.data.message || '请求失败');
    }
    return response.data.data;
  },
  error => {
    return Promise.reject(error.response?.data?.message || error.message || '网络错误');
  }
);

export default request; 