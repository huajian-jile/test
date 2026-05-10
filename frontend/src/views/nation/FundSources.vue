<template>
  <el-card>
    <template #header>国家层面 — 资金来源</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="code" label="编码" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="remark" label="备注" />
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
    const body = await http.get('/api/nation/fund-sources')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
})
</script>
