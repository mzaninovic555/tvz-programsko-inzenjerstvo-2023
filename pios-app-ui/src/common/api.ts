import axios from 'axios';

const api = axios.create({
  baseURL: process.env.SERVICE_API_URL || '/api',
});

export default api;
