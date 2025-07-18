export interface User {
  id: number
  username: string
  email?: string
  realName?: string
  phone?: string
  avatar?: string
  status: number
  role: string
  createdTime: string
  updatedTime: string
}

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  user: User
}

export interface RegisterRequest {
  username: string
  password: string
  email?: string
  realName?: string
  phone?: string
}

export interface UpdateProfileRequest {
  email?: string
  realName?: string
  phone?: string
  avatar?: string
}

export interface ChangePasswordRequest {
  oldPassword: string
  newPassword: string
} 