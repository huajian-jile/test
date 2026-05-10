<template>
  <el-card>
    <template #header>国家层面 — 划拨流水</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="bizNo" label="业务单号" />
      <el-table-column prop="batchId" label="批次ID" width="90" />
      <el-table-column prop="amount" label="金额" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column prop="transferAt" label="划转时间" width="170" />
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
    const body = await http.get('/api/nation/allocations')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
})
</script>
