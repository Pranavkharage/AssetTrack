import axios from 'axios'

// Production is same-origin: Spring Boot serves the built React application.
// During local development, Vite proxies /api to the Spring Boot server.
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('assettrack_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

export default api
