<template>
  <div class="student-management">
    <div class="header">
      <h1>学生管理</h1>
      <el-button type="primary" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        添加学生
      </el-button>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="学号">
          <el-input v-model="queryParams.studentNo" placeholder="请输入学号" clearable />
        </el-form-item>
        <el-form-item label="班级">
          <el-input v-model="queryParams.class_" placeholder="请输入班级" clearable />
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="queryParams.major" placeholder="请输入专业" clearable />
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
        :data="studentList"
        border
        style="width: 100%"
      >
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="user.realName" label="姓名" width="120" />
        <el-table-column prop="class_" label="班级" width="120" />
        <el-table-column prop="major" label="专业" width="180" />
        <el-table-column prop="grade" label="年级" width="100" />
        <el-table-column prop="user.email" label="邮箱" width="180" />
        <el-table-column prop="user.phone" label="电话" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
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

    <!-- 添加/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="showAddDialog"
      width="500px"
      append-to-body
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="学号" prop="studentNo">
          <el-input v-model="form.studentNo" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="姓名" prop="user.realName">
          <el-input v-model="form.user.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="班级" prop="class_">
          <el-input v-model="form.class_" placeholder="请输入班级" />
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="form.major" placeholder="请输入专业" />
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-input v-model="form.grade" placeholder="请输入年级" />
        </el-form-item>
        <el-form-item label="邮箱" prop="user.email">
          <el-input v-model="form.user.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="电话" prop="user.phone">
          <el-input v-model="form.user.phone" placeholder="请输入电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showAddDialog = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, Delete } from '@element-plus/icons-vue'
import request from '@/utils/request'

interface QueryParams {
  current: number
  size: number
  studentNo?: string
  class_?: string
  major?: string
}

interface Student {
  id: number
  studentNo: string
  class_: string
  major: string
  grade: string
  user: {
    realName: string
    email: string
    phone: string
  }
}

const loading = ref(false)
const studentList = ref<Student[]>([])
const total = ref(0)
const showAddDialog = ref(false)
const dialogTitle = ref('')
const formRef = ref()

const queryParams = reactive<QueryParams>({
  current: 1,
  size: 10
})

const form = reactive({
  id: undefined,
  studentNo: '',
  class_: '',
  major: '',
  grade: '',
  user: {
    realName: '',
    email: '',
    phone: ''
  }
})

const rules = {
  studentNo: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  'user.realName': [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  class_: [{ required: true, message: '请输入班级', trigger: 'blur' }],
  major: [{ required: true, message: '请输入专业', trigger: 'blur' }],
  grade: [{ required: true, message: '请输入年级', trigger: 'blur' }],
  'user.email': [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  'user.phone': [{ required: true, message: '请输入电话', trigger: 'blur' }]
}

const getList = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/students', { params: queryParams })
    studentList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('获取学生列表失败:', error)
    ElMessage.error('获取学生列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.current = 1
  getList()
}

const resetQuery = () => {
  queryParams.studentNo = ''
  queryParams.class_ = ''
  queryParams.major = ''
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

const resetForm = () => {
  form.id = undefined
  form.studentNo = ''
  form.class_ = ''
  form.major = ''
  form.grade = ''
  form.user.realName = ''
  form.user.email = ''
  form.user.phone = ''
}

const handleAdd = () => {
  resetForm()
  dialogTitle.value = '添加学生'
  showAddDialog.value = true
}

const handleEdit = (row: Student) => {
  resetForm()
  dialogTitle.value = '编辑学生'
  Object.assign(form, row)
  showAddDialog.value = true
}

const handleDelete = (row: Student) => {
  ElMessageBox.confirm('确认删除该学生吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/api/students/${row.id}`)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除学生失败:', error)
      ElMessage.error('删除学生失败')
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        if (form.id) {
          await request.put(`/api/students/${form.id}`, form)
          ElMessage.success('更新成功')
        } else {
          await request.post('/api/students', form)
          ElMessage.success('添加成功')
        }
        showAddDialog.value = false
        getList()
      } catch (error) {
        console.error('保存学生信息失败:', error)
        ElMessage.error('保存学生信息失败')
      }
    }
  })
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.student-management {
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 