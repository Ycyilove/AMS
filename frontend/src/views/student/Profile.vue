<template>
  <div class="profile">
    <h1>个人信息</h1>

    <div class="profile-container">
      <div class="profile-header">
        <div class="avatar">
          <i class="fas fa-user-circle"></i>
        </div>
        <div class="basic-info">
          <h2>{{ student.name }}</h2>
          <p class="student-id">学号：{{ student.studentId }}</p>
        </div>
      </div>

      <div class="profile-content">
        <div class="info-section">
          <h3>基本信息</h3>
          <div class="info-grid">
            <div class="info-item">
              <label>性别</label>
              <span>{{ student.gender }}</span>
            </div>
            <div class="info-item">
              <label>年龄</label>
              <span>{{ student.age }}</span>
            </div>
            <div class="info-item">
              <label>院系</label>
              <span>{{ student.department }}</span>
            </div>
            <div class="info-item">
              <label>专业</label>
              <span>{{ student.major }}</span>
            </div>
            <div class="info-item">
              <label>班级</label>
              <span>{{ student.class }}</span>
            </div>
            <div class="info-item">
              <label>入学年份</label>
              <span>{{ student.enrollmentYear }}</span>
            </div>
          </div>
        </div>

        <div class="info-section">
          <h3>联系方式</h3>
          <div class="info-grid">
            <div class="info-item">
              <label>手机号码</label>
              <span>{{ student.phone }}</span>
            </div>
            <div class="info-item">
              <label>邮箱</label>
              <span>{{ student.email }}</span>
            </div>
            <div class="info-item">
              <label>家庭住址</label>
              <span>{{ student.address }}</span>
            </div>
          </div>
        </div>

        <div class="info-section">
          <h3>学习情况</h3>
          <div class="info-grid">
            <div class="info-item">
              <label>已修学分</label>
              <span>{{ student.earnedCredits }}</span>
            </div>
            <div class="info-item">
              <label>平均成绩</label>
              <span>{{ student.averageScore }}</span>
            </div>
            <div class="info-item">
              <label>在读状态</label>
              <span>{{ student.status }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="profile-actions">
        <button class="edit-btn" @click="handleEdit">
          <i class="fas fa-edit"></i> 编辑信息
        </button>
        <button class="change-password-btn" @click="handleChangePassword">
          <i class="fas fa-key"></i> 修改密码
        </button>
      </div>
    </div>

    <!-- 编辑信息模态框 -->
    <div v-if="showEditModal" class="modal">
      <div class="modal-content">
        <h3>编辑个人信息</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label>手机号码</label>
            <input type="tel" v-model="editForm.phone" required>
          </div>
          <div class="form-group">
            <label>邮箱</label>
            <input type="email" v-model="editForm.email" required>
          </div>
          <div class="form-group">
            <label>家庭住址</label>
            <input type="text" v-model="editForm.address" required>
          </div>
          <div class="modal-actions">
            <button type="submit" class="submit-btn">保存</button>
            <button type="button" class="cancel-btn" @click="showEditModal = false">取消</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 修改密码模态框 -->
    <div v-if="showPasswordModal" class="modal">
      <div class="modal-content">
        <h3>修改密码</h3>
        <form @submit.prevent="handlePasswordSubmit">
          <div class="form-group">
            <label>当前密码</label>
            <input type="password" v-model="passwordForm.currentPassword" required>
          </div>
          <div class="form-group">
            <label>新密码</label>
            <input type="password" v-model="passwordForm.newPassword" required>
          </div>
          <div class="form-group">
            <label>确认新密码</label>
            <input type="password" v-model="passwordForm.confirmPassword" required>
          </div>
          <div class="modal-actions">
            <button type="submit" class="submit-btn">确认修改</button>
            <button type="button" class="cancel-btn" @click="showPasswordModal = false">取消</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Student {
  studentId: string
  name: string
  gender: string
  age: number
  department: string
  major: string
  class: string
  enrollmentYear: string
  phone: string
  email: string
  address: string
  earnedCredits: number
  averageScore: number
  status: string
}

const student = ref<Student>({
  studentId: '',
  name: '',
  gender: '',
  age: 0,
  department: '',
  major: '',
  class: '',
  enrollmentYear: '',
  phone: '',
  email: '',
  address: '',
  earnedCredits: 0,
  averageScore: 0,
  status: ''
})

const showEditModal = ref(false)
const showPasswordModal = ref(false)

const editForm = ref({
  phone: '',
  email: '',
  address: ''
})

const passwordForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const fetchStudentInfo = async () => {
  try {
    const response = await fetch('/api/student/profile')
    if (response.ok) {
      student.value = await response.json()
      // 初始化编辑表单
      editForm.value = {
        phone: student.value.phone,
        email: student.value.email,
        address: student.value.address
      }
    }
  } catch (error) {
    console.error('获取学生信息失败:', error)
  }
}

const handleEdit = () => {
  showEditModal.value = true
}

const handleChangePassword = () => {
  showPasswordModal.value = true
}

const handleSubmit = async () => {
  try {
    const response = await fetch('/api/student/profile', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(editForm.value)
    })

    if (response.ok) {
      // 更新本地数据
      student.value = {
        ...student.value,
        ...editForm.value
      }
      showEditModal.value = false
    }
  } catch (error) {
    console.error('更新个人信息失败:', error)
  }
}

const handlePasswordSubmit = async () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    alert('两次输入的新密码不一致')
    return
  }

  try {
    const response = await fetch('/api/student/change-password', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        currentPassword: passwordForm.value.currentPassword,
        newPassword: passwordForm.value.newPassword
      })
    })

    if (response.ok) {
      showPasswordModal.value = false
      passwordForm.value = {
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    }
  } catch (error) {
    console.error('修改密码失败:', error)
  }
}

onMounted(() => {
  fetchStudentInfo()
})
</script>

<style scoped>
.profile {
  padding: 1rem;
}

h1 {
  margin-bottom: 2rem;
  color: #2c3e50;
}

.profile-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.profile-header {
  padding: 2rem;
  background-color: #f8f9fa;
  display: flex;
  align-items: center;
  gap: 2rem;
}

.avatar {
  font-size: 4rem;
  color: #3498db;
}

.basic-info h2 {
  margin: 0;
  color: #2c3e50;
}

.student-id {
  margin: 0.5rem 0 0;
  color: #666;
}

.profile-content {
  padding: 2rem;
}

.info-section {
  margin-bottom: 2rem;
}

.info-section h3 {
  margin: 0 0 1rem;
  color: #2c3e50;
  border-bottom: 2px solid #eee;
  padding-bottom: 0.5rem;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.info-item label {
  color: #666;
  font-size: 0.875rem;
}

.info-item span {
  color: #2c3e50;
  font-weight: 500;
}

.profile-actions {
  padding: 1rem 2rem;
  border-top: 1px solid #eee;
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.edit-btn,
.change-password-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.edit-btn {
  background-color: #3498db;
  color: white;
}

.change-password-btn {
  background-color: #2ecc71;
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
  align-items: center;
  justify-content: center;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  max-width: 500px;
}

.modal-content h3 {
  margin: 0 0 1.5rem;
  color: #2c3e50;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #666;
}

.form-group input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 1.5rem;
}

.submit-btn,
.cancel-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn {
  background-color: #3498db;
  color: white;
}

.cancel-btn {
  background-color: #e74c3c;
  color: white;
}
</style> 