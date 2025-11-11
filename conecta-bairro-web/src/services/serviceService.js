import api from './api';

export async function getServices(params) {
  const res = await api.get('/services', { params });
  return res.data;
}

export async function getServiceById(id) {
  const res = await api.get(`/services/${id}`);
  return res.data;
}
