// Serviço mínimo para gerenciar requests (jobs)
const requests = [];
let idCounter = 1;

async function create(data) {
  const record = { id: idCounter++, status: 'PENDING', ...data };
  requests.push(record);
  return record;
}

async function getById(id) {
  return requests.find(r => r.id === id);
}

async function updateStatus(id, status) {
  const r = requests.find(r => r.id === id);
  if (!r) throw new Error('Not found');
  r.status = status;
  return r;
}

module.exports = { create, getById, updateStatus };
