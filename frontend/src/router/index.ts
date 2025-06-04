import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/login',
            name: 'Login',
            component: () => import('@/views/Login.vue'),
            meta: { requiresAuth: false }
        },
        {
            path: '/admin',
            name: 'AdminLayout',
            component: () => import('../layouts/AdminLayout.vue'),
            meta: { requiresAuth: true, role: 'ADMIN' },
            children: [
                {
                    path: 'dashboard',
                    name: 'AdminDashboard',
                    component: () => import('../views/admin/Dashboard.vue')
                },
                {
                    path: 'users',
                    name: 'UserManagement',
                    component: () => import('../views/admin/UserManagement.vue')
                },
                {
                    path: 'teachers',
                    name: 'TeacherManagement',
                    component: () => import('../views/admin/TeacherManagement.vue')
                },
                {
                    path: 'courses',
                    name: 'CourseManagement',
                    component: () => import('../views/admin/CourseManagement.vue')
                },
                {
                    path: 'scores',
                    name: 'ScoreManagement',
                    component: () => import('../views/admin/ScoreManagement.vue')
                },
                {
                    path: 'statistics',
                    name: 'Statistics',
                    component: () => import('../views/admin/Statistics.vue')
                }
            ]
        },
        {
            path: '/student',
            name: 'StudentLayout',
            component: () => import('../layouts/StudentLayout.vue'),
            meta: { requiresAuth: true, role: 'STUDENT' },
            children: [
                {
                    path: 'dashboard',
                    name: 'StudentDashboard',
                    component: () => import('../views/student/Dashboard.vue')
                },
                {
                    path: 'courses',
                    name: 'CourseSelection',
                    component: () => import('../views/student/CourseSelection.vue')
                },
                {
                    path: 'scores',
                    name: 'ScoreQuery',
                    component: () => import('../views/student/ScoreQuery.vue')
                },
                {
                    path: 'profile',
                    name: 'Profile',
                    component: () => import('../views/student/Profile.vue')
                }
            ]
        }
    ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()
    const token = localStorage.getItem('token')
    const userRole = localStorage.getItem('userRole')

    // 检查是否需要认证
    if (to.meta.requiresAuth) {
        if (!token || !authStore.isAuthenticated()) {
            ElMessage.error('请先登录')
            next('/login')
            return
        }

        // 检查角色权限
        if (to.meta.role && to.meta.role !== userRole) {
            ElMessage.error('没有权限访问该页面')
            if (userRole === 'ADMIN') {
                next('/admin/dashboard')
            } else {
                next('/student/dashboard')
            }
            return
        }
    }

    // 如果已登录且访问登录页，重定向到对应角色的首页
    if (to.path === '/login' && token && authStore.isAuthenticated()) {
        if (userRole === 'ADMIN') {
            next('/admin/dashboard')
        } else {
            next('/student/dashboard')
        }
        return
    }

    next()
})

export default router