<template>
  <el-card>
    <template #header>被扶贫对象 — 户档案</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="householdNo" label="户编号" />
      <el-table-column prop="headName" label="户主" width="100" />
      <el-table-column prop="address" label="地址" />
      <el-table-column prop="povertyFlag" label="贫困户" width="80" />
      <el-table-column prop="status" label="状态" width="90" />
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
    const body = await http.get('/api/beneficiary/households')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
})
</script>
