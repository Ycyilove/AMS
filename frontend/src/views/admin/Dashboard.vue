<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="8" v-for="stat in stats" :key="stat.title">
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
              <span>成绩分布</span>
              <el-radio-group v-model="chartType" size="small">
                <el-radio-button label="bar">柱状图</el-radio-button>
                <el-radio-button label="line">折线图</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-container">
            <!-- 这里可以集成 ECharts 图表 -->
            <div class="chart-placeholder">图表区域</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="recent-activities">
          <template #header>
            <div class="card-header">
              <span>最近活动</span>
              <el-button type="text">查看全部</el-button>
            </div>
          </template>
          <!-- <el-timeline>
            <el-timeline-item
              v-for="activity in recentActivities"
              :key="activity.id"
              :timestamp="activity.time"
              :type="activity.type"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline> -->
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="table-row">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近成绩录入</span>
              <el-button type="primary" size="small">添加成绩</el-button>
            </div>
          </template>
          <el-table :data="recentScores" style="width: 100%" stripe>
            <el-table-column prop="studentName" label="学生姓名" />
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column prop="score" label="分数" />
            <el-table-column prop="submitTime" label="提交时间" />
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="row.status === '已审核' ? 'success' : 'warning'">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { User, School, Reading, Medal } from "@element-plus/icons-vue";

const chartType = ref("bar");

const stats = ref([
  {
    title: "学生总数",
    value: "",
    icon: "User",
    type: "primary",
  },
  {
    title: "教师总数",
    value: "",
    icon: "School",
    type: "success",
  },
  {
    title: "课程总数",
    value: "",
    icon: "Reading",
    type: "warning",
  }
]);

const recentActivities = ref([]);

const recentScores = ref([]);

onMounted(() => {
  // 这里可以添加获取数据的逻辑
});
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

.table-row {
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
