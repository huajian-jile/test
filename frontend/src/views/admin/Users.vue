<template>
  <el-card>
    <template #header>用户管理（含明文密码列，便于管理员核对）</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="password" label="密码(明文)" min-width="120" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="status" label="状态" width="70" />
      <el-table-column prop="loginAt" label="最后登录" width="170" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="showPwd(row)">查看密码</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="pwdVisible" title="用户密码" width="400px">
      <p><strong>{{ pwdUser?.username }}</strong></p>
      <el-input :model-value="pwdText" readonly type="textarea" :rows="2" />
    </el-dialog>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import http from '@/api/http'
import { ElMessage } from 'element-plus'

const rows = ref([])
const loading = ref(false)
const pwdVisible = ref(false)
const pwdText = ref('')
const pwdUser = ref(null)

async function load() {
  loading.value = true
  try {
    const body = await http.get('/api/admin/users')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
}

async function showPwd(row) {
  pwdUser.value = row
  try {
    const body = await http.get(`/api/admin/users/${row.id}/password`)
    pwdText.value = body.data?.password || ''
    pwdVisible.value = true
  } catch {
    ElMessage.warning('无查看密码权限或接口失败')
  }
}

onMounted(load)
</script>
