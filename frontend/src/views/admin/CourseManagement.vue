<template>
  <div class="course-management">
    <div class="header">
      <h1>课程管理</h1>
      <button class="add-btn" @click="showAddCourseModal = true">
        <i class="fas fa-plus"></i>
        添加课程
      </button>
    </div>

    <div class="filters">
      <div class="search-box">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="搜索课程..."
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
            <th>课程编号</th>
            <th>课程名称</th>
            <th>学分</th>
            <th>学时</th>
            <th>开课院系</th>
            <th>授课教师</th>
            <th>上课时间</th>
            <th>上课地点</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="course in filteredCourses" :key="course.id">
            <td>{{ course.courseId }}</td>
            <td>{{ course.name }}</td>
            <td>{{ course.credits }}</td>
            <td>{{ course.hours }}</td>
            <td>{{ course.department }}</td>
            <td>{{ course.teacher }}</td>
            <td>{{ course.time }}</td>
            <td>{{ course.location }}</td>
            <td class="actions">
              <button class="edit-btn" @click="handleEdit(course)">
                <i class="fas fa-edit"></i>
              </button>
              <button class="delete-btn" @click="handleDelete(course)">
                <i class="fas fa-trash"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 添加/编辑课程模态框 -->
    <div v-if="showAddCourseModal" class="modal">
      <div class="modal-content">
        <h2>{{ editingCourse ? '编辑课程' : '添加课程' }}</h2>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="courseId">课程编号</label>
            <input
              type="text"
              id="courseId"
              v-model="courseForm.courseId"
              required
            />
          </div>
          <div class="form-group">
            <label for="name">课程名称</label>
            <input
              type="text"
              id="name"
              v-model="courseForm.name"
              required
            />
          </div>
          <div class="form-group">
            <label for="credits">学分</label>
            <input
              type="number"
              id="credits"
              v-model="courseForm.credits"
              required
              min="0"
              max="10"
            />
          </div>
          <div class="form-group">
            <label for="hours">学时</label>
            <input
              type="number"
              id="hours"
              v-model="courseForm.hours"
              required
              min="0"
            />
          </div>
          <div class="form-group">
            <label for="department">开课院系</label>
            <select id="department" v-model="courseForm.department" required>
              <option v-for="dept in departments" :key="dept" :value="dept">
                {{ dept }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="teacher">授课教师</label>
            <select id="teacher" v-model="courseForm.teacher" required>
              <option v-for="teacher in teachers" :key="teacher.id" :value="teacher.name">
                {{ teacher.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="time">上课时间</label>
            <input
              type="text"
              id="time"
              v-model="courseForm.time"
              required
              placeholder="例如：周一 1-2节"
            />
          </div>
          <div class="form-group">
            <label for="location">上课地点</label>
            <input
              type="text"
              id="location"
              v-model="courseForm.location"
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
import { ref, computed, onMounted } from 'vue'

interface Course {
  id: number
  courseId: string
  name: string
  credits: number
  hours: number
  department: string
  teacher: string
  time: string
  location: string
}

interface Teacher {
  id: number
  name: string
}

const courses = ref<Course[]>([])
const teachers = ref<Teacher[]>([])
const searchQuery = ref('')
const departmentFilter = ref('')
const showAddCourseModal = ref(false)
const editingCourse = ref<Course | null>(null)

const departments = [
  '计算机科学与技术学院',
  '电子信息工程学院',
  '机械工程学院',
  '经济管理学院',
  '理学院'
]

const courseForm = ref({
  courseId: '',
  name: '',
  credits: 0,
  hours: 0,
  department: departments[0],
  teacher: '',
  time: '',
  location: ''
})

const filteredCourses = computed(() => {
  return courses.value.filter(course => {
    const matchesSearch = 
      course.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      course.courseId.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesDepartment = !departmentFilter.value || course.department === departmentFilter.value
    return matchesSearch && matchesDepartment
  })
})

const fetchCourses = async () => {
  try {
    const response = await fetch('/api/admin/courses')
    if (response.ok) {
      courses.value = await response.json()
    }
  } catch (error) {
    console.error('获取课程列表失败:', error)
  }
}

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

const handleEdit = (course: Course) => {
  editingCourse.value = course
  courseForm.value = { ...course }
  showAddCourseModal.value = true
}

const handleDelete = async (course: Course) => {
  if (confirm('确定要删除该课程吗？')) {
    try {
      const response = await fetch(`/api/admin/courses/${course.id}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        courses.value = courses.value.filter(c => c.id !== course.id)
      }
    } catch (error) {
      console.error('删除课程失败:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    const url = editingCourse.value
      ? `/api/admin/courses/${editingCourse.value.id}`
      : '/api/admin/courses'
    
    const method = editingCourse.value ? 'PUT' : 'POST'
    
    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(courseForm.value)
    })

    if (response.ok) {
      await fetchCourses()
      closeModal()
    }
  } catch (error) {
    console.error('保存课程失败:', error)
  }
}

const closeModal = () => {
  showAddCourseModal.value = false
  editingCourse.value = null
  courseForm.value = {
    courseId: '',
    name: '',
    credits: 0,
    hours: 0,
    department: departments[0],
    teacher: '',
    time: '',
    location: ''
  }
}

onMounted(() => {
  fetchCourses()
  fetchTeachers()
})
</script>

<style scoped>
.course-management {
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