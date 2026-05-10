<template>
  <template v-for="node in items" :key="node.id">
    <el-sub-menu v-if="node.children && node.children.length" :index="String(node.id)">
      <template #title>{{ node.name }}</template>
      <MenuTree :items="node.children" />
    </el-sub-menu>
    <el-menu-item v-else :index="menuIndex(node)">{{ node.name }}</el-menu-item>
  </template>
</template>

<script setup>
defineProps({
  items: { type: Array, default: () => [] },
})

function menuIndex(node) {
  if (node.path) return node.path.startsWith('/') ? node.path : '/' + node.path
  return '/' + (node.code || node.id)
}
</script>
<script>
export default { name: 'MenuTree' }
</script>
