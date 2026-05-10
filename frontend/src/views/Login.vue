<template>
  <div class="wrap">
    <el-card class="card">
      <h2>慈善资金追溯平台</h2>
      <el-form :model="form" @submit.prevent="onSubmit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" autocomplete="current-password" />
        </el-form-item>
        <el-button type="primary" native-type="submit" :loading="loading" style="width: 100%">登录</el-button>
      </el-form>
      <p class="hint">默认账号：<code>admin</code> 密码：<code>admin123</code></p>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const auth = useAuthStore()
const route = useRoute()
const router = useRouter()
const loading = ref(false)
const form = reactive({ username: 'admin', password: 'admin123' })

async function onSubmit() {
  loading.value = true
  try {
    await auth.login({ username: form.username, password: form.password })
    ElMessage.success('登录成功')
    const redir = route.query.redirect || '/'
    router.push(redir)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.wrap {
  min-height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a1a2e, #16213e);
}
.card {
  width: 380px;
}
.hint {
  margin-top: 12px;
  font-size: 12px;
  color: #888;
}
</style>
