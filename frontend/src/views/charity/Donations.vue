<template>
  <el-card>
    <template #header>个人慈善 — 捐赠记录</template>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="donatedAt" label="捐赠日期" width="170" />
      <el-table-column prop="donationLocation" label="地点" width="140" show-overflow-tooltip />
      <el-table-column prop="recipientName" label="对方名称" width="120" />
      <el-table-column prop="recipientAddress" label="对方地址" min-width="160" show-overflow-tooltip />
      <el-table-column prop="recipientFamilySituation" label="对方家庭状况" min-width="200" show-overflow-tooltip />
      <el-table-column prop="amount" label="金额" width="100" />
      <el-table-column prop="groupPhotoUrl" label="合照照片" min-width="180" show-overflow-tooltip />
      <el-table-column prop="remark" label="备注" width="140" show-overflow-tooltip />
      <el-table-column prop="donorId" label="捐赠人ID" width="90" />
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
