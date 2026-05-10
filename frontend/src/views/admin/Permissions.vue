<template>
  <el-card>
    <template #header>菜单与权限</template>
    <el-table :data="rows" border stripe v-loading="loading" row-key="id" default-expand-all>
      <el-table-column prop="code" label="权限码" min-width="180" />
      <el-table-column prop="name" label="名称" width="140" />
      <el-table-column prop="permType" label="类型" width="80" />
      <el-table-column prop="path" label="路由" min-width="160" />
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
    const body = await http.get('/api/admin/permissions')
    rows.value = body.data || []
  } finally {
    loading.value = false
  }
})
</script>
