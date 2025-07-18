import request from '@/utils/request';

export interface LoginParams {
  username: string;
  password: string;
}

export interface RegisterParams {
  username: string;
  password: string;
}

export function login(data: LoginParams) {
  return request.post('/auth/login', data);
}

export function register(data: RegisterParams) {
  return request.post('/auth/register', data);
}

export function getUserInfo() {
  return request.get('/auth/me');
} 