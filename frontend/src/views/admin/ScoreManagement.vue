<template>
  <div class="score-management">
    <div class="header">
      <h1>成绩管理</h1>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="选课ID">
          <el-input v-model="queryParams.courseSelectionId" placeholder="请输入选课ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="resetQuery">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="scoreList"
        border
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="courseSelectionId" label="选课ID" width="100" />
        <el-table-column prop="score" label="成绩" width="100">
          <template #default="{ row }">
            <el-input-number
              v-model="row.score"
              :min="0"
              :max="100"
              :precision="1"
              :step="0.5"
              @change="(value) => handleScoreChange(row, value)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="updatedTime" label="更新时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.updatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.size"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Delete } from '@element-plus/icons-vue'
import request from '@/utils/request'

interface QueryParams {
  current: number
  size: number
  courseSelectionId?: number
}

interface Score {
  id: number
  courseSelectionId: number
  score: number
  createdTime: string
  updatedTime: string
}

const loading = ref(false)
const scoreList = ref<Score[]>([])
const total = ref(0)

const queryParams = reactive<QueryParams>({
  current: 1,
  size: 10
})

const getList = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/scores', { params: queryParams })
    scoreList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('获取成绩列表失败:', error)
    ElMessage.error('获取成绩列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.current = 1
  getList()
}

const resetQuery = () => {
  queryParams.courseSelectionId = undefined
  handleQuery()
}

const handleSizeChange = (val: number) => {
  queryParams.size = val
  getList()
}

const handleCurrentChange = (val: number) => {
  queryParams.current = val
  getList()
}

const handleScoreChange = async (row: Score, value: number) => {
  try {
    await request.put(`/api/scores/course-selection/${row.courseSelectionId}`, null, {
      params: { score: value }
    })
    ElMessage.success('更新成绩成功')
  } catch (error) {
    console.error('更新成绩失败:', error)
    ElMessage.error('更新成绩失败')
    getList() // 刷新列表以恢复原值
  }
}

const handleDelete = (row: Score) => {
  ElMessageBox.confirm('确认删除该成绩记录吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/api/scores/course-selection/${row.courseSelectionId}`)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除成绩失败:', error)
      ElMessage.error('删除成绩失败')
    }
  })
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleString()
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.score-management {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 