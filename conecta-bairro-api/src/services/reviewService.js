// Serviço mínimo para reviews
const reviews = [];
let idCounter = 1;

async function create(data) {
  const record = { id: idCounter++, ...data };
  reviews.push(record);
  return record;
}

async function listByService(serviceId) {
  return reviews.filter(r => r.serviceId === serviceId);
}

module.exports = { create, listByService };
