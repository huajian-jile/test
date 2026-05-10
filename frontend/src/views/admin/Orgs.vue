<template>
  <el-card>
    <template #header>组织机构</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="levelType" label="层级" width="80" />
      <el-table-column prop="regionCode" label="区划代码" width="120" />
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
    const body = await http.get('/api/admin/orgs')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
})
</script>
