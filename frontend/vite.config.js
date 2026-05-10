import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

/**
 * 后端 API 固定：http://127.0.0.1:9000（与 application.yml 一致）
 *
 * Windows 上 Node 常无法绑定 8090/9000（系统/Hyper-V 排除端口 → EACCES），
 * 故 Vite 默认用 5173；浏览器打开 http://127.0.0.1:5173 即可联调。
 *
 * 可选：若你机器上 9080 可用，可执行 npm run dev:ui9080
 * 或 PowerShell：$env:VITE_DEV_PORT=9080; npm run dev
 */
const API_PORT = Number(process.env.VITE_API_PORT || 9000)
const DEV_PORT = Number(process.env.PORT || process.env.VITE_DEV_PORT || 5173)

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    host: '127.0.0.1',
    port: DEV_PORT,
    strictPort: false,
    proxy: {
      '/api': {
        target: `http://127.0.0.1:${API_PORT}`,
        changeOrigin: true,
      },
    },
  },
  preview: {
    host: '127.0.0.1',
    port: DEV_PORT,
    strictPort: false,
  },
})
