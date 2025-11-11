// Serviço mínimo de serviços - substitua por consultas reais com Prisma
const services = [];
let idCounter = 1;

async function list(filters = {}) {
  // Em um DB real, aplique filtros por bairro e categoria
  return services;
}

async function getById(id) {
  return services.find(s => s.id === id);
}

async function create(data) {
  const record = { id: idCounter++, ...data };
  services.push(record);
  return record;
}

async function update(id, data) {
  const idx = services.findIndex(s => s.id === id);
  if (idx === -1) throw new Error('Not found');
  services[idx] = { ...services[idx], ...data };
  return services[idx];
}

async function remove(id) {
  const idx = services.findIndex(s => s.id === id);
  if (idx === -1) throw new Error('Not found');
  services.splice(idx, 1);
}

module.exports = { list, getById, create, update, remove };
