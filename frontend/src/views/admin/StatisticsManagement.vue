<template>
  <div class="statistics-management">
    <div class="header">
      <h1>统计管理</h1>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="统计类型">
          <el-select v-model="queryParams.type" placeholder="请选择统计类型" clearable>
            <el-option label="教师统计" value="TEACHER" />
            <el-option label="课程统计" value="COURSE" />
            <el-option label="学生统计" value="STUDENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标ID">
          <el-input v-model="queryParams.targetId" placeholder="请输入目标ID" clearable />
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
        :data="statisticsList"
        border
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="type" label="统计类型" width="120">
          <template #default="{ row }">
            {{ getTypeLabel(row.type) }}
          </template>
        </el-table-column>
        <el-table-column prop="targetId" label="目标ID" width="100" />
        <el-table-column prop="statisticsData" label="统计数据" min-width="300">
          <template #default="{ row }">
            <el-button type="primary" link @click="showStatisticsDetail(row)">
              查看详情
            </el-button>
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
            <el-button type="primary" link @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
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

    <!-- 统计数据详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="统计数据详情"
      width="50%"
    >
      <pre class="statistics-detail">{{ JSON.stringify(currentStatistics?.statisticsData, null, 2) }}</pre>
    </el-dialog>

    <!-- 编辑统计数据对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑统计数据"
      width="50%"
    >
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="统计类型">
          <el-select v-model="editForm.type" placeholder="请选择统计类型">
            <el-option label="教师统计" value="TEACHER" />
            <el-option label="课程统计" value="COURSE" />
            <el-option label="学生统计" value="STUDENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标ID">
          <el-input v-model="editForm.targetId" placeholder="请输入目标ID" />
        </el-form-item>
        <el-form-item label="统计数据">
          <el-input
            v-model="editForm.statisticsData"
            type="textarea"
            :rows="10"
            placeholder="请输入JSON格式的统计数据"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Edit, Delete } from '@element-plus/icons-vue'
import request from '@/utils/request'

interface QueryParams {
  current: number
  size: number
  type?: string
  targetId?: string
}

interface Statistics {
  id: number
  type: string
  targetId: number
  statisticsData: string
  createdTime: string
  updatedTime: string
}

const loading = ref(false)
const statisticsList = ref<Statistics[]>([])
const total = ref(0)
const detailDialogVisible = ref(false)
const editDialogVisible = ref(false)
const currentStatistics = ref<Statistics | null>(null)

const queryParams = reactive<QueryParams>({
  current: 1,
  size: 10
})

const editForm = reactive({
  type: '',
  targetId: '',
  statisticsData: ''
})

const getList = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/statistics', { params: queryParams })
    statisticsList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('获取统计数据列表失败:', error)
    ElMessage.error('获取统计数据列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.current = 1
  getList()
}

const resetQuery = () => {
  queryParams.type = undefined
  queryParams.targetId = undefined
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

const getTypeLabel = (type: string) => {
  const typeMap: Record<string, string> = {
    TEACHER: '教师统计',
    COURSE: '课程统计',
    STUDENT: '学生统计'
  }
  return typeMap[type] || type
}

const showStatisticsDetail = (row: Statistics) => {
  currentStatistics.value = row
  detailDialogVisible.value = true
}

const handleEdit = (row: Statistics) => {
  editForm.type = row.type
  editForm.targetId = row.targetId.toString()
  editForm.statisticsData = row.statisticsData
  currentStatistics.value = row
  editDialogVisible.value = true
}

const handleSave = async () => {
  try {
    await request.put(
      `/api/statistics/type/${editForm.type}/target/${editForm.targetId}`,
      null,
      { params: { statisticsData: editForm.statisticsData } }
    )
    ElMessage.success('更新统计数据成功')
    editDialogVisible.value = false
    getList()
  } catch (error) {
    console.error('更新统计数据失败:', error)
    ElMessage.error('更新统计数据失败')
  }
}

const handleDelete = (row: Statistics) => {
  ElMessageBox.confirm('确认删除该统计数据吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/api/statistics/type/${row.type}/target/${row.targetId}`)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除统计数据失败:', error)
      ElMessage.error('删除统计数据失败')
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
.statistics-management {
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

.statistics-detail {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  white-space: pre-wrap;
  word-break: break-all;
  max-height: 400px;
  overflow-y: auto;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 