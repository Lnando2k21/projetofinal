import api from './api';

export const authService = {
  async login(email, password) {
    const response = await api.post('/auth/login', { email, password });
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      return response.data;
    }
    throw new Error('Falha na autenticação');
  },

  async register(payload) {
    const response = await api.post('/auth/register', payload);
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      return response.data;
    }
    throw new Error('Falha no registro');
  },

  async logout() {
    localStorage.removeItem('token');
  },

  async getCurrentUser() {
    try {
      const token = localStorage.getItem('token');
      if (!token) return null;
      
      const response = await api.get('/auth/me');
      return response.data;
    } catch (error) {
      return null;
    }
  },

  getToken() {
    return localStorage.getItem('token');
  }
};
