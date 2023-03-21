import axios from 'axios';

const api = axios.create({
  baseURL: process.env.SERVICE_API_URL || '/api',
});

// api.defaults.headers.common['Authorization'] = `Bearer ${token}`;

export default api;
