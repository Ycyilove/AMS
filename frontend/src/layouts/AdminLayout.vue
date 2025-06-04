<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="250px">
        <div class="logo">
          <h2>学生成绩管理系统</h2>
        </div>
        <el-menu
          :default-active="$route.path"
          class="nav-menu"
          background-color="#2c3e50"
          text-color="#fff"
          active-text-color="#409EFF"
          router
        >
          <el-menu-item index="dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="teachers">
            <el-icon><School /></el-icon>
            <span>教师管理</span>
          </el-menu-item>
          <el-menu-item index="courses">
            <el-icon><Reading /></el-icon>
            <span>课程管理</span>
          </el-menu-item>
          <el-menu-item index="scores">
            <el-icon><DataLine /></el-icon>
            <span>成绩管理</span>
          </el-menu-item>
          <el-menu-item index="statistics">
            <el-icon><TrendCharts /></el-icon>
            <span>统计分析</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div class="user-info">
            <span>管理员</span>
            <el-button type="danger" @click="handleLogout">退出登录</el-button>
          </div>
        </el-header>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import {
  HomeFilled,
  User,
  School,
  Reading,
  DataLine,
  TrendCharts
} from '@element-plus/icons-vue'

const router = useRouter()

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userRole')
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
}

.el-aside {
  background-color: #2c3e50;
  color: white;
}

.logo {
  padding: 1rem 0;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo h2 {
  margin: 0;
  font-size: 1.2rem;
  color: white;
}

.nav-menu {
  border-right: none;
}

.el-header {
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 2rem;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.el-main {
  background-color: #f5f5f5;
  padding: 2rem;
}

:deep(.el-menu-item) {
  display: flex;
  align-items: center;
}

:deep(.el-menu-item .el-icon) {
  margin-right: 0.75rem;
}
</style> 