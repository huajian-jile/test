<template>
  <el-card>
    <template #header>被扶贫对象 — 贫困户需求</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="householdId" label="户ID" width="80" />
      <el-table-column prop="category" label="类别" width="100" />
      <el-table-column prop="status" label="状态" width="100" />
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
    const body = await http.get('/api/beneficiary/needs')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
})
</script>
