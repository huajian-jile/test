<template>
  <el-card>
    <template #header>被扶贫对象 — 到户资金</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="householdId" label="户ID" width="80" />
      <el-table-column prop="amount" label="金额" />
      <el-table-column prop="receivedAt" label="到账时间" width="170" />
      <el-table-column prop="purpose" label="用途" />
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
    const body = await http.get('/api/beneficiary/receipts')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
})
</script>
