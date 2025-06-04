<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="stat in stats" :key="stat.title">
        <el-card class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-content">
            <el-icon class="stat-icon" :class="stat.type">
              <component :is="stat.icon" />
            </el-icon>
            <div class="stat-info">
              <h3>{{ stat.title }}</h3>
              <p class="stat-value">{{ stat.value }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>成绩趋势</span>
              <el-radio-group v-model="chartType" size="small">
                <el-radio-button label="bar">柱状图</el-radio-button>
                <el-radio-button label="line">折线图</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-container">
            <!-- 这里可以集成 ECharts 图表 -->
            <div class="chart-placeholder">
              图表区域
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="recent-activities">
          <template #header>
            <div class="card-header">
              <span>最近通知</span>
              <el-button type="text">查看全部</el-button>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="notice in recentNotices"
              :key="notice.id"
              :timestamp="notice.time"
              :type="notice.type"
            >
              {{ notice.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="table-row">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近成绩</span>
              <el-button type="primary" size="small">查看全部成绩</el-button>
            </div>
          </template>
          <el-table :data="recentScores" style="width: 100%" stripe>
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column prop="score" label="分数">
              <template #default="{ row }">
                <el-tag :type="getScoreTagType(row.score)">
                  {{ row.score }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="credit" label="学分" />
            <el-table-column prop="semester" label="学期" />
            <el-table-column prop="updateTime" label="更新时间" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="course-row">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>已选课程</span>
              <el-button type="primary" size="small">选课</el-button>
            </div>
          </template>
          <el-table :data="selectedCourses" style="width: 100%" stripe>
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column prop="teacher" label="任课教师" />
            <el-table-column prop="credit" label="学分" />
            <el-table-column prop="time" label="上课时间" />
            <el-table-column prop="location" label="上课地点" />
            <el-table-column label="操作">
              <template #default="{ row }">
                <el-button type="danger" size="small" @click="handleDropCourse(row)">
                  退选
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import {
  Reading,
  Medal,
  Timer,
  Star
} from '@element-plus/icons-vue'

const chartType = ref('bar')

const stats = ref([
  {
    title: '已修学分',
    value: '24',
    icon: 'Reading',
    type: 'primary'
  },
  {
    title: '平均成绩',
    value: '85.6',
    icon: 'Medal',
    type: 'success'
  },
  {
    title: '在修课程',
    value: '6',
    icon: 'Timer',
    type: 'warning'
  },
  {
    title: '绩点',
    value: '3.8',
    icon: 'Star',
    type: 'danger'
  }
])

const recentNotices = ref([
  {
    id: 1,
    content: '高等数学成绩已发布',
    time: '2024-01-20 10:00',
    type: 'primary'
  },
  {
    id: 2,
    content: '选课系统开放通知',
    time: '2024-01-20 09:30',
    type: 'success'
  },
  {
    id: 3,
    content: '期末考试安排已发布',
    time: '2024-01-20 09:00',
    type: 'warning'
  }
])

const recentScores = ref([
  {
    courseName: '高等数学',
    score: 95,
    credit: 4,
    semester: '2023-2024-1',
    updateTime: '2024-01-20 10:00'
  },
  {
    courseName: '大学物理',
    score: 88,
    credit: 3,
    semester: '2023-2024-1',
    updateTime: '2024-01-20 09:30'
  },
  {
    courseName: '程序设计',
    score: 92,
    credit: 4,
    semester: '2023-2024-1',
    updateTime: '2024-01-20 09:00'
  }
])

const selectedCourses = ref([
  {
    courseName: '高等数学',
    teacher: '张教授',
    credit: 4,
    time: '周一 1-2节',
    location: '教学楼A101'
  },
  {
    courseName: '大学物理',
    teacher: '李教授',
    credit: 3,
    time: '周二 3-4节',
    location: '教学楼B203'
  },
  {
    courseName: '程序设计',
    teacher: '王教授',
    credit: 4,
    time: '周三 5-6节',
    location: '教学楼C305'
  }
])

const getScoreTagType = (score: number) => {
  if (score >= 90) return 'success'
  if (score >= 80) return 'primary'
  if (score >= 60) return 'warning'
  return 'danger'
}

const handleDropCourse = (course: any) => {
  // 处理退选课程的逻辑
  console.log('退选课程:', course)
}

onMounted(() => {
  // 这里可以添加获取数据的逻辑
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-card {
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  font-size: 48px;
  margin-right: 20px;
  padding: 10px;
  border-radius: 8px;
}

.stat-icon.primary {
  background-color: #ecf5ff;
  color: #409eff;
}

.stat-icon.success {
  background-color: #f0f9eb;
  color: #67c23a;
}

.stat-icon.warning {
  background-color: #fdf6ec;
  color: #e6a23c;
}

.stat-icon.danger {
  background-color: #fef0f0;
  color: #f56c6c;
}

.stat-info h3 {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.stat-value {
  margin: 5px 0 0;
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  height: 400px;
}

.chart-container {
  height: 320px;
}

.chart-placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  border-radius: 4px;
  color: #909399;
}

.recent-activities {
  height: 400px;
  overflow-y: auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-row,
.course-row {
  margin-bottom: 20px;
}

:deep(.el-card__header) {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
  background-color: #f5f7fa;
}

:deep(.el-timeline-item__content) {
  color: #606266;
}

:deep(.el-timeline-item__timestamp) {
  color: #909399;
}
</style> 