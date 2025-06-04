<template>
  <div class="student-layout">
    <aside class="sidebar">
      <div class="logo">
        <h2>学生成绩管理系统</h2>
      </div>
      <nav class="nav-menu">
        <router-link to="/student" class="nav-item">
          <i class="fas fa-home"></i>
          <span>首页</span>
        </router-link>
        <router-link to="/student/courses" class="nav-item">
          <i class="fas fa-book"></i>
          <span>选课管理</span>
        </router-link>
        <router-link to="/student/scores" class="nav-item">
          <i class="fas fa-chart-bar"></i>
          <span>成绩查询</span>
        </router-link>
        <router-link to="/student/profile" class="nav-item">
          <i class="fas fa-user"></i>
          <span>个人信息</span>
        </router-link>
      </nav>
    </aside>
    <main class="main-content">
      <header class="header">
        <div class="user-info">
          <span>学生</span>
          <button @click="handleLogout" class="logout-btn">退出登录</button>
        </div>
      </header>
      <div class="content">
        <router-view></router-view>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userRole')
  router.push('/login')
}
</script>

<style scoped>
.student-layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 250px;
  background-color: #2c3e50;
  color: white;
  padding: 1rem;
}

.logo {
  padding: 1rem 0;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo h2 {
  margin: 0;
  font-size: 1.2rem;
}

.nav-menu {
  margin-top: 2rem;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  margin-bottom: 0.5rem;
  transition: background-color 0.3s;
}

.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.nav-item.router-link-active {
  background-color: #3498db;
}

.nav-item i {
  margin-right: 0.75rem;
}

.main-content {
  flex: 1;
  background-color: #f5f5f5;
}

.header {
  background-color: white;
  padding: 1rem 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: flex-end;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logout-btn {
  padding: 0.5rem 1rem;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #c0392b;
}

.content {
  padding: 2rem;
}
</style> 