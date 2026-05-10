<template>
  <el-card>
    <template #header>个人慈善 — 捐赠记录</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="donorId" label="捐赠人ID" width="100" />
      <el-table-column prop="amount" label="金额" />
      <el-table-column prop="donatedAt" label="捐赠时间" width="170" />
      <el-table-column prop="channel" label="渠道" width="100" />
      <el-table-column prop="publicVisible" label="公示" width="80" />
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
    const body = await http.get('/api/charity/donations')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
})
</script>
