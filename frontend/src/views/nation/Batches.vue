<template>
  <el-card>
    <template #header>国家层面 — 拨款批次</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="batchNo" label="批次号" />
      <el-table-column prop="fundSourceId" label="资金来源ID" width="120" />
      <el-table-column prop="totalAmount" label="总金额" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column prop="title" label="标题" />
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
    const body = await http.get('/api/nation/batches')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
})
</script>
