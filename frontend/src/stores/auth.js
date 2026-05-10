import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import http from '@/api/http'

const TOKEN_KEY = 'charity_token'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem(TOKEN_KEY) || '')
  const username = ref('')
  const userId = ref(null)

  const isLoggedIn = computed(() => !!token.value)

  function setSession(t, user) {
    token.value = t
    username.value = user?.username || ''
    userId.value = user?.userId ?? null
    if (t) localStorage.setItem(TOKEN_KEY, t)
    else localStorage.removeItem(TOKEN_KEY)
  }

  function clear() {
    token.value = ''
    username.value = ''
    userId.value = null
    localStorage.removeItem(TOKEN_KEY)
  }

  async function login(payload) {
    const body = await http.post('/api/auth/login', payload)
    const d = body.data
    setSession(d.token, { username: d.username, userId: d.userId })
    return d
  }

  async function fetchMenus() {
    const body = await http.get('/api/auth/menus')
    return body.data || []
  }

  return { token, username, userId, isLoggedIn, login, clear, fetchMenus }
})
