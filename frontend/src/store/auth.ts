import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

interface User {
  id: number
  username: string
  role: 'ADMIN' | 'STUDENT'
}

// 验证token格式
const isValidToken = (token: string): boolean => {
  if (!token) return false
  const parts = token.split('.')
  return parts.length === 3
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userRole = ref(localStorage.getItem('userRole') || '')
  const user = ref<User | null>(null)

  const login = async (username: string, password: string, role: string) => {
    try {
      console.log('Login attempt:', { username, role });
      
      const response = await request.post('/auth/login', { 
        username, 
        password,
        role
      });
      console.log('Login response:', response.data);
      const { token: newToken, user: userData } = response.data.data;
      console.log('token:', newToken, 'user:', userData);
      // 验证 token 格式
      if (!isValidToken(newToken)) {
        console.error('Invalid token format:', newToken);
        ElMessage.error('登录失败：无效的 token 格式');
        return false;
      }

      // 更新状态
      token.value = newToken;
      userRole.value = userData.role;
      user.value = userData;

      // 保存到 localStorage
      localStorage.setItem('token', newToken);
      localStorage.setItem('userRole', userData.role);
      
      return true;
    } catch (error) {
      console.error('Login failed:', error);
      ElMessage.error('登录失败，请检查用户名和密码');
      return false;
    }
  };

  const logout = () => {
    token.value = ''
    userRole.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userRole')
  }

  const isAuthenticated = () => {
    return !!token.value && isValidToken(token.value)
  }

  const isAdmin = () => {
    return userRole.value === 'ADMIN'
  }

  return {
    token,
    userRole,
    user,
    login,
    logout,
    isAuthenticated,
    isAdmin
  }
})