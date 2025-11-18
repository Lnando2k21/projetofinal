// Serviço de reviews usando Prisma
const prisma = require('../config/database');

async function create(data) {
  return prisma.review.create({ data });
}

async function listByService(serviceId) {
  return prisma.review.findMany({ where: { serviceId: Number(serviceId) } });
}

module.exports = { create, listByService };
