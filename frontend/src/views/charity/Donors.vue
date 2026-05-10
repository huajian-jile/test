<template>
  <el-card>
    <template #header>个人慈善 — 捐赠人</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="donorName" label="姓名" width="120" />
      <el-table-column prop="gender" label="性别" width="80" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
      <el-table-column prop="donorType" label="类型" width="90" />
    </el-table>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import http from '@/api/http'

const rows = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const body = await http.get('/api/charity/donors')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
})
</script>
