<template>
  <div class="score-query">
    <h1>成绩查询</h1>

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
        <select v-model="semesterFilter" @change="handleFilter">
          <option value="">所有学期</option>
          <option v-for="semester in semesters" :key="semester" :value="semester">
            {{ semester }}
          </option>
        </select>
      </div>
    </div>

    <div class="score-table">
      <table>
        <thead>
          <tr>
            <th>课程编号</th>
            <th>课程名称</th>
            <th>学分</th>
            <th>成绩</th>
            <th>教师</th>
            <th>学期</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="score in filteredScores" :key="score.id">
            <td>{{ score.courseId }}</td>
            <td>{{ score.courseName }}</td>
            <td>{{ score.credits }}</td>
            <td :class="getScoreClass(score.score)">{{ score.score }}</td>
            <td>{{ score.teacher }}</td>
            <td>{{ score.semester }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="summary">
      <div class="summary-card">
        <h3>总学分</h3>
        <p>{{ totalCredits }}</p>
      </div>
      <div class="summary-card">
        <h3>平均成绩</h3>
        <p>{{ averageScore }}</p>
      </div>
      <div class="summary-card">
        <h3>已修课程</h3>
        <p>{{ totalCourses }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface Score {
  id: number
  courseId: string
  courseName: string
  credits: number
  score: number
  teacher: string
  semester: string
}

const scores = ref<Score[]>([])
const searchQuery = ref('')
const semesterFilter = ref('')

const semesters = [
  '2023-2024-1',
  '2023-2024-2',
  '2022-2023-1',
  '2022-2023-2'
]

const filteredScores = computed(() => {
  return scores.value.filter(score => {
    const matchesSearch = 
      score.courseName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      score.courseId.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesSemester = !semesterFilter.value || score.semester === semesterFilter.value
    return matchesSearch && matchesSemester
  })
})

const totalCredits = computed(() => {
  return filteredScores.value.reduce((sum, score) => sum + score.credits, 0)
})

const averageScore = computed(() => {
  if (filteredScores.value.length === 0) return 0
  const sum = filteredScores.value.reduce((sum, score) => sum + score.score, 0)
  return (sum / filteredScores.value.length).toFixed(1)
})

const totalCourses = computed(() => {
  return filteredScores.value.length
})

const getScoreClass = (score: number) => {
  if (score >= 90) return 'score-a'
  if (score >= 80) return 'score-b'
  if (score >= 70) return 'score-c'
  if (score >= 60) return 'score-d'
  return 'score-f'
}

const fetchScores = async () => {
  try {
    const response = await fetch('/api/student/scores')
    if (response.ok) {
      scores.value = await response.json()
    }
  } catch (error) {
    console.error('获取成绩列表失败:', error)
  }
}

const handleSearch = () => {
  // 搜索逻辑已通过计算属性实现
}

const handleFilter = () => {
  // 筛选逻辑已通过计算属性实现
}

// 初始化时获取成绩列表
fetchScores()
</script>

<style scoped>
.score-query {
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

.score-table {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  margin-bottom: 2rem;
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

.score-a {
  color: #27ae60;
  font-weight: bold;
}

.score-b {
  color: #2980b9;
  font-weight: bold;
}

.score-c {
  color: #f39c12;
  font-weight: bold;
}

.score-d {
  color: #e67e22;
  font-weight: bold;
}

.score-f {
  color: #e74c3c;
  font-weight: bold;
}

.summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
}

.summary-card {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.summary-card h3 {
  margin: 0;
  color: #666;
  font-size: 1rem;
}

.summary-card p {
  margin: 0.5rem 0 0;
  font-size: 1.5rem;
  font-weight: bold;
  color: #2c3e50;
}
</style> 