import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

interface ApiResponse<T = any> {
  code: number
  message?: string
  data: T
}

// 创建axios实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

// 验证token格式
const isValidToken = (token: string): boolean => {
  if (!token) return false
  try {
    // 只检查基本格式
    const parts = token.split('.')
    return parts.length === 3
  } catch (error) {
    console.error('Token validation error:', error)
    return false
  }
}

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      if (!isValidToken(token)) {
        // token格式不正确，清除token并跳转到登录页
        localStorage.removeItem('token')
        localStorage.removeItem('userRole')
        router.push('/login')
        ElMessage.error('登录已过期，请重新登录')
        return Promise.reject(new Error('Invalid token format'))
      }
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 未授权，清除token并跳转到登录页
          localStorage.removeItem('token')
          localStorage.removeItem('userRole')
          router.push('/login')
          ElMessage.error('登录已过期，请重新登录')
          break
        case 403:
          ElMessage.error('没有权限访问该资源')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误，请稍后重试')
          break
        default:
          ElMessage.error(error.response.data?.message || '请求失败')
      }
    } else {
      ElMessage.error('网络错误，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

export default request