<template>
  <div class="course-selection">
    <h1>选课管理</h1>

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

    <div class="courses-grid">
      <div v-for="course in filteredCourses" :key="course.id" class="course-card">
        <div class="course-header">
          <h3>{{ course.name }}</h3>
          <span class="course-id">{{ course.courseId }}</span>
        </div>
        <div class="course-info">
          <p><i class="fas fa-user"></i> 教师：{{ course.teacher }}</p>
          <p><i class="fas fa-clock"></i> 时间：{{ course.time }}</p>
          <p><i class="fas fa-map-marker-alt"></i> 地点：{{ course.location }}</p>
          <p><i class="fas fa-graduation-cap"></i> 学分：{{ course.credits }}</p>
          <p><i class="fas fa-university"></i> 院系：{{ course.department }}</p>
        </div>
        <div class="course-actions">
          <button
            :class="['select-btn', { 'selected': course.selected }]"
            @click="handleSelect(course)"
          >
            {{ course.selected ? '退选' : '选课' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface Course {
  id: number
  courseId: string
  name: string
  teacher: string
  time: string
  location: string
  credits: number
  department: string
  selected: boolean
}

const courses = ref<Course[]>([])
const searchQuery = ref('')
const departmentFilter = ref('')

const departments = [
  '计算机科学与技术学院',
  '电子信息工程学院',
  '机械工程学院',
  '经济管理学院',
  '理学院'
]

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
    const response = await fetch('/api/student/courses')
    if (response.ok) {
      courses.value = await response.json()
    }
  } catch (error) {
    console.error('获取课程列表失败:', error)
  }
}

const handleSearch = () => {
  // 搜索逻辑已通过计算属性实现
}

const handleFilter = () => {
  // 筛选逻辑已通过计算属性实现
}

const handleSelect = async (course: Course) => {
  try {
    const url = course.selected
      ? `/api/student/courses/${course.id}/drop`
      : `/api/student/courses/${course.id}/select`
    
    const response = await fetch(url, {
      method: 'POST'
    })

    if (response.ok) {
      course.selected = !course.selected
    }
  } catch (error) {
    console.error('选课操作失败:', error)
  }
}

// 初始化时获取课程列表
fetchCourses()
</script>

<style scoped>
.course-selection {
  padding: 1rem;
}

h1 {
  margin-bottom: 2rem;
  color: #2c3e50;
}

.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
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

.courses-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.course-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.course-header {
  padding: 1rem;
  background-color: #f8f9fa;
  border-bottom: 1px solid #eee;
}

.course-header h3 {
  margin: 0;
  color: #2c3e50;
}

.course-id {
  font-size: 0.875rem;
  color: #666;
}

.course-info {
  padding: 1rem;
}

.course-info p {
  margin: 0.5rem 0;
  color: #666;
}

.course-info i {
  width: 20px;
  color: #3498db;
}

.course-actions {
  padding: 1rem;
  border-top: 1px solid #eee;
  text-align: right;
}

.select-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: #3498db;
  color: white;
}

.select-btn.selected {
  background-color: #e74c3c;
}

.select-btn:hover {
  opacity: 0.9;
}
</style> 