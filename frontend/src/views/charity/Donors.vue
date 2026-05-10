<template>
  <el-card>
    <template #header>个人慈善 — 捐赠人</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="displayName" label="展示名" />
      <el-table-column prop="donorType" label="类型" width="100" />
      <el-table-column prop="phone" label="手机" width="130" />
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
