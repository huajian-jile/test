<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="brand">慈善资金追溯</div>
      <el-scrollbar>
        <el-menu
          :default-active="active"
          router
          background-color="#1d1e1f"
          text-color="#eee"
          active-text-color="#ffd04b"
        >
          <MenuTree :items="menus" />
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="title">工作台</span>
        <div class="right">
          <span class="user">{{ auth.username }}</span>
          <el-button type="primary" link @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import MenuTree from './MenuTree.vue'

const auth = useAuthStore()
const route = useRoute()
const router = useRouter()
const menus = ref([])
const active = computed(() => route.path)

onMounted(async () => {
  menus.value = await auth.fetchMenus()
})

function logout() {
  auth.clear()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  height: 100%;
}
.aside {
  background: #1d1e1f;
  color: #fff;
}
.brand {
  padding: 16px;
  font-weight: 600;
  border-bottom: 1px solid #333;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #eee;
}
.title {
  font-size: 16px;
}
.right {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user {
  color: #666;
}
.main {
  background: #f6f7f9;
}
</style>
