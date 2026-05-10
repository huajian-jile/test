import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  { path: '/login', name: 'login', component: () => import('@/views/Login.vue'), meta: { public: true } },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/nation/batches' },
      { path: 'nation/fund-sources', component: () => import('@/views/nation/FundSources.vue') },
      { path: 'nation/batches', component: () => import('@/views/nation/Batches.vue') },
      { path: 'nation/allocations', component: () => import('@/views/nation/Allocations.vue') },
      { path: 'charity/donors', component: () => import('@/views/charity/Donors.vue') },
      { path: 'charity/donations', component: () => import('@/views/charity/Donations.vue') },
      { path: 'beneficiary/households', component: () => import('@/views/beneficiary/Households.vue') },
      { path: 'beneficiary/receipts', component: () => import('@/views/beneficiary/Receipts.vue') },
      { path: 'beneficiary/needs', component: () => import('@/views/beneficiary/Needs.vue') },
      { path: 'admin/users', component: () => import('@/views/admin/Users.vue') },
      { path: 'admin/roles', component: () => import('@/views/admin/Roles.vue') },
      { path: 'admin/permissions', component: () => import('@/views/admin/Permissions.vue') },
      { path: 'admin/orgs', component: () => import('@/views/admin/Orgs.vue') },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.public) return true
  if (to.meta.requiresAuth && !auth.isLoggedIn) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  return true
})

export default router
