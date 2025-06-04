<template>
  <div class="statistics">
    <h1>统计分析</h1>

    <div class="stats-grid">
      <div class="stat-card">
        <h3>总体情况</h3>
        <div class="stat-content">
          <div class="stat-item">
            <span class="label">学生总数</span>
            <span class="value">{{ stats.totalStudents }}</span>
          </div>
          <div class="stat-item">
            <span class="label">课程总数</span>
            <span class="value">{{ stats.totalCourses }}</span>
          </div>
          <div class="stat-item">
            <span class="label">平均成绩</span>
            <span class="value">{{ stats.averageScore.toFixed(1) }}</span>
          </div>
        </div>
      </div>

      <div class="stat-card">
        <h3>成绩分布</h3>
        <div class="chart-container">
          <canvas ref="scoreDistributionChart"></canvas>
        </div>
      </div>

      <div class="stat-card">
        <h3>院系成绩对比</h3>
        <div class="chart-container">
          <canvas ref="departmentComparisonChart"></canvas>
        </div>
      </div>

      <div class="stat-card">
        <h3>课程成绩趋势</h3>
        <div class="chart-container">
          <canvas ref="courseTrendChart"></canvas>
        </div>
      </div>
    </div>

    <div class="filters">
      <div class="filter-group">
        <label>时间范围</label>
        <select v-model="timeRange" @change="updateCharts">
          <option value="semester">本学期</option>
          <option value="year">本学年</option>
          <option value="all">全部</option>
        </select>
      </div>
      <div class="filter-group">
        <label>院系</label>
        <select v-model="department" @change="updateCharts">
          <option value="">全部院系</option>
          <option v-for="dept in departments" :key="dept.id" :value="dept.id">
            {{ dept.name }}
          </option>
        </select>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from "vue";
import Chart from "chart.js/auto";
import { getStatistics, getDepartments, getChartData } from "@/services/statistics";
import type {
  Statistics,
  Department,
  ChartData,
  StatisticsFilter,
} from "@/types/statistics";

const stats = ref<Statistics>({
  totalStudents: 0,
  totalCourses: 0,
  averageScore: 0,
});

const timeRange = ref<StatisticsFilter["timeRange"]>("semester");
const department = ref("");
const departments = ref<Department[]>([]);

let scoreDistributionChart: Chart | null = null;
let departmentComparisonChart: Chart | null = null;
let courseTrendChart: Chart | null = null;

const scoreDistributionChartRef = ref<HTMLCanvasElement | null>(null);
const departmentComparisonChartRef = ref<HTMLCanvasElement | null>(null);
const courseTrendChartRef = ref<HTMLCanvasElement | null>(null);

const fetchStats = async () => {
  try {
    const filter: StatisticsFilter = {
      timeRange: timeRange.value,
      department: department.value || undefined,
    };
    stats.value = await getStatistics(filter);
    updateCharts();
  } catch (error) {
    console.error("获取统计数据失败:", error);
  }
};

const fetchDepartments = async () => {
  try {
    departments.value = await getDepartments();
  } catch (error) {
    console.error("获取院系列表失败:", error);
  }
};

const initScoreDistributionChart = () => {
  if (!scoreDistributionChartRef.value) return;

  const ctx = scoreDistributionChartRef.value.getContext("2d");
  if (!ctx) return;

  scoreDistributionChart = new Chart(ctx, {
    type: "bar",
    data: {
      labels: ["0-60", "60-70", "70-80", "80-90", "90-100"],
      datasets: [
        {
          label: "学生人数",
          data: [0, 0, 0, 0, 0],
          backgroundColor: "#3498db",
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "成绩分布",
        },
      },
    },
  });
};

const initDepartmentComparisonChart = () => {
  if (!departmentComparisonChartRef.value) return;

  const ctx = departmentComparisonChartRef.value.getContext("2d");
  if (!ctx) return;

  departmentComparisonChart = new Chart(ctx, {
    type: "bar",
    data: {
      labels: departments.value.map((dept) => dept.name),
      datasets: [
        {
          label: "平均成绩",
          data: departments.value.map((dept) => dept.averageScore || 0),
          backgroundColor: "#2ecc71",
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "院系平均成绩对比",
        },
      },
    },
  });
};

const initCourseTrendChart = () => {
  if (!courseTrendChartRef.value) return;

  const ctx = courseTrendChartRef.value.getContext("2d");
  if (!ctx) return;

  courseTrendChart = new Chart(ctx, {
    type: "line",
    data: {
      labels: ["第一周", "第二周", "第三周", "第四周", "第五周"],
      datasets: [
        {
          label: "平均成绩",
          data: [0, 0, 0, 0, 0],
          borderColor: "#e74c3c",
          tension: 0.1,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "课程成绩趋势",
        },
      },
    },
  });
};

const updateCharts = async () => {
  try {
    const filter: StatisticsFilter = {
      timeRange: timeRange.value,
      department: department.value || undefined,
    };
    const data = await getChartData(filter);

    if (scoreDistributionChart) {
      scoreDistributionChart.data.datasets[0].data = data.scoreDistribution;
      scoreDistributionChart.update();
    }

    if (departmentComparisonChart) {
      departmentComparisonChart.data.datasets[0].data = data.departmentComparison;
      departmentComparisonChart.update();
    }

    if (courseTrendChart) {
      courseTrendChart.data.datasets[0].data = data.courseTrend;
      courseTrendChart.update();
    }
  } catch (error) {
    console.error("更新图表数据失败:", error);
  }
};

onMounted(() => {
  fetchStats();
  fetchDepartments();
  initScoreDistributionChart();
  initDepartmentComparisonChart();
  initCourseTrendChart();
});

onUnmounted(() => {
  if (scoreDistributionChart) {
    scoreDistributionChart.destroy();
  }
  if (departmentComparisonChart) {
    departmentComparisonChart.destroy();
  }
  if (courseTrendChart) {
    courseTrendChart.destroy();
  }
});
</script>

<style scoped>
.statistics {
  padding: 1rem;
}

h1 {
  margin-bottom: 2rem;
  color: #2c3e50;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-card h3 {
  margin: 0 0 1rem;
  color: #2c3e50;
}

.stat-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 1rem;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.stat-item .label {
  font-size: 0.875rem;
  color: #666;
  margin-bottom: 0.5rem;
}

.stat-item .value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #2c3e50;
}

.chart-container {
  height: 300px;
  position: relative;
}

.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-group label {
  font-size: 0.875rem;
  color: #666;
}

.filter-group select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  min-width: 150px;
}
</style>
