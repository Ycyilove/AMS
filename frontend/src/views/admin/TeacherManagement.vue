<template>
  <div class="teacher-management">
    <div class="header">
      <h1>教师管理</h1>
      <button class="add-btn" @click="showAddTeacherModal = true">
        <i class="fas fa-plus"></i>
        添加教师
      </button>
    </div>

    <div class="filters">
      <div class="search-box">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="搜索教师..."
          @input="handleSearch"
        />
      </div>
      <div class="filter-group">
        <select v-model="departmentFilter" @change="handleFilter">
          <option value="">所有院系</option>
          <option v-for="dept in departments" :key="dept" :value="dept">
            {{ dept }}
          </option>
        </select>
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>工号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>院系</th>
            <th>职称</th>
            <th>联系电话</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="teacher in filteredTeachers" :key="teacher.id">
            <td>{{ teacher.teacherId }}</td>
            <td>{{ teacher.name }}</td>
            <td>{{ teacher.gender }}</td>
            <td>{{ teacher.department }}</td>
            <td>{{ teacher.title }}</td>
            <td>{{ teacher.phone }}</td>
            <td class="actions">
              <button class="edit-btn" @click="handleEdit(teacher)">
                <i class="fas fa-edit"></i>
              </button>
              <button class="delete-btn" @click="handleDelete(teacher)">
                <i class="fas fa-trash"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 添加/编辑教师模态框 -->
    <div v-if="showAddTeacherModal" class="modal">
      <div class="modal-content">
        <h2>{{ editingTeacher ? '编辑教师' : '添加教师' }}</h2>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="teacherId">工号</label>
            <input
              type="text"
              id="teacherId"
              v-model="teacherForm.teacherId"
              required
            />
          </div>
          <div class="form-group">
            <label for="name">姓名</label>
            <input
              type="text"
              id="name"
              v-model="teacherForm.name"
              required
            />
          </div>
          <div class="form-group">
            <label for="gender">性别</label>
            <select id="gender" v-model="teacherForm.gender" required>
              <option value="男">男</option>
              <option value="女">女</option>
            </select>
          </div>
          <div class="form-group">
            <label for="department">院系</label>
            <select id="department" v-model="teacherForm.department" required>
              <option v-for="dept in departments" :key="dept" :value="dept">
                {{ dept }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="title">职称</label>
            <select id="title" v-model="teacherForm.title" required>
              <option value="教授">教授</option>
              <option value="副教授">副教授</option>
              <option value="讲师">讲师</option>
              <option value="助教">助教</option>
            </select>
          </div>
          <div class="form-group">
            <label for="phone">联系电话</label>
            <input
              type="tel"
              id="phone"
              v-model="teacherForm.phone"
              required
            />
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

interface Teacher {
  id: number
  teacherId: string
  name: string
  gender: string
  department: string
  title: string
  phone: string
}

const teachers = ref<Teacher[]>([])
const searchQuery = ref('')
const departmentFilter = ref('')
const showAddTeacherModal = ref(false)
const editingTeacher = ref<Teacher | null>(null)

const departments = [
  '计算机科学与技术学院',
  '电子信息工程学院',
  '机械工程学院',
  '经济管理学院',
  '理学院'
]

const teacherForm = ref({
  teacherId: '',
  name: '',
  gender: '男',
  department: departments[0],
  title: '讲师',
  phone: ''
})

const filteredTeachers = computed(() => {
  return teachers.value.filter(teacher => {
    const matchesSearch = 
      teacher.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      teacher.teacherId.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesDepartment = !departmentFilter.value || teacher.department === departmentFilter.value
    return matchesSearch && matchesDepartment
  })
})

const fetchTeachers = async () => {
  try {
    const response = await fetch('/api/admin/teachers')
    if (response.ok) {
      teachers.value = await response.json()
    }
  } catch (error) {
    console.error('获取教师列表失败:', error)
  }
}

const handleSearch = () => {
  // 搜索逻辑已通过计算属性实现
}

const handleFilter = () => {
  // 筛选逻辑已通过计算属性实现
}

const handleEdit = (teacher: Teacher) => {
  editingTeacher.value = teacher
  teacherForm.value = { ...teacher }
  showAddTeacherModal.value = true
}

const handleDelete = async (teacher: Teacher) => {
  if (confirm('确定要删除该教师吗？')) {
    try {
      const response = await fetch(`/api/admin/teachers/${teacher.id}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        teachers.value = teachers.value.filter(t => t.id !== teacher.id)
      }
    } catch (error) {
      console.error('删除教师失败:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    const url = editingTeacher.value
      ? `/api/admin/teachers/${editingTeacher.value.id}`
      : '/api/admin/teachers'
    
    const method = editingTeacher.value ? 'PUT' : 'POST'
    
    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(teacherForm.value)
    })

    if (response.ok) {
      await fetchTeachers()
      closeModal()
    }
  } catch (error) {
    console.error('保存教师失败:', error)
  }
}

const closeModal = () => {
  showAddTeacherModal.value = false
  editingTeacher.value = null
  teacherForm.value = {
    teacherId: '',
    name: '',
    gender: '男',
    department: departments[0],
    title: '讲师',
    phone: ''
  }
}

// 初始化时获取教师列表
fetchTeachers()
</script>

<style scoped>
.teacher-management {
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