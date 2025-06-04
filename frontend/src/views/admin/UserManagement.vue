<template>
  <div class="user-management">
    <div class="header">
      <h1>用户管理</h1>
      <button class="add-btn" @click="showAddUserModal = true">
        <i class="fas fa-plus"></i>
        添加用户
      </button>
    </div>

    <div class="filters">
      <div class="search-box">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="搜索用户..."
          @input="handleSearch"
        />
      </div>
      <div class="filter-group">
        <select v-model="roleFilter" @change="handleFilter">
          <option value="">所有角色</option>
          <option value="ADMIN">管理员</option>
          <option value="STUDENT">学生</option>
        </select>
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>角色</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in filteredUsers" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.role }}</td>
            <td>{{ formatDate(user.createdAt) }}</td>
            <td class="actions">
              <button class="edit-btn" @click="handleEdit(user)">
                <i class="fas fa-edit"></i>
              </button>
              <button class="delete-btn" @click="handleDelete(user)">
                <i class="fas fa-trash"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 添加/编辑用户模态框 -->
    <div v-if="showAddUserModal" class="modal">
      <div class="modal-content">
        <h2>{{ editingUser ? '编辑用户' : '添加用户' }}</h2>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="username">用户名</label>
            <input
              type="text"
              id="username"
              v-model="userForm.username"
              required
            />
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input
              type="password"
              id="password"
              v-model="userForm.password"
              :required="!editingUser"
            />
          </div>
          <div class="form-group">
            <label for="role">角色</label>
            <select id="role" v-model="userForm.role" required>
              <option value="ADMIN">管理员</option>
              <option value="STUDENT">学生</option>
            </select>
          </div>
          <div class="modal-actions">
            <button type="button" class="cancel-btn" @click="closeModal">取消</button>
            <button type="submit" class="submit-btn">确定</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface User {
  id: number
  username: string
  role: string
  createdAt: string
}

const users = ref<User[]>([])
const searchQuery = ref('')
const roleFilter = ref('')
const showAddUserModal = ref(false)
const editingUser = ref<User | null>(null)

const userForm = ref({
  username: '',
  password: '',
  role: 'STUDENT'
})

const filteredUsers = computed(() => {
  return users.value.filter(user => {
    const matchesSearch = user.username.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesRole = !roleFilter.value || user.role === roleFilter.value
    return matchesSearch && matchesRole
  })
})

const fetchUsers = async () => {
  try {
    const response = await fetch('/api/admin/users')
    if (response.ok) {
      users.value = await response.json()
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

const handleSearch = () => {
  // 搜索逻辑已通过计算属性实现
}

const handleFilter = () => {
  // 筛选逻辑已通过计算属性实现
}

const handleEdit = (user: User) => {
  editingUser.value = user
  userForm.value = {
    username: user.username,
    password: '',
    role: user.role
  }
  showAddUserModal.value = true
}

const handleDelete = async (user: User) => {
  if (confirm('确定要删除该用户吗？')) {
    try {
      const response = await fetch(`/api/admin/users/${user.id}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        users.value = users.value.filter(u => u.id !== user.id)
      }
    } catch (error) {
      console.error('删除用户失败:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    const url = editingUser.value
      ? `/api/admin/users/${editingUser.value.id}`
      : '/api/admin/users'
    
    const method = editingUser.value ? 'PUT' : 'POST'
    
    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userForm.value)
    })

    if (response.ok) {
      await fetchUsers()
      closeModal()
    }
  } catch (error) {
    console.error('保存用户失败:', error)
  }
}

const closeModal = () => {
  showAddUserModal.value = false
  editingUser.value = null
  userForm.value = {
    username: '',
    password: '',
    role: 'STUDENT'
  }
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleString()
}

// 初始化时获取用户列表
fetchUsers()
</script>

<style scoped>
.user-management {
  padding: 1rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.add-btn {
  padding: 0.5rem 1rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.search-box input {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 200px;
}

.filter-group select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

th {
  background-color: #f8f9fa;
  font-weight: 600;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.edit-btn, .delete-btn {
  padding: 0.25rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.edit-btn {
  background-color: #3498db;
  color: white;
}

.delete-btn {
  background-color: #e74c3c;
  color: white;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  max-width: 500px;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}

.cancel-btn,
.submit-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn {
  background-color: #95a5a6;
  color: white;
}

.submit-btn {
  background-color: #3498db;
  color: white;
}
</style> 