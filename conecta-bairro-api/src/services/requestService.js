// Serviço de requests usando Prisma
const prisma = require('../config/database');

async function create(data) {
  const created = await prisma.request.create({ data });
  return created;
}

async function getById(id) {
  return prisma.request.findUnique({ where: { id: Number(id) } });
}

async function updateStatus(id, status) {
  return prisma.request.update({ where: { id: Number(id) }, data: { status } });
}

module.exports = { create, getById, updateStatus };
