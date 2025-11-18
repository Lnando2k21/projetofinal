// Serviço de serviços usando Prisma
const prisma = require('../config/database');

async function list(filters = {}) {
  const where = {};
  // filters.category: nome da categoria
  if (filters.category) where.category = { is: { name: filters.category } };
  // filters.bairro: nome do bairro/neighbor nas áreas
  if (filters.bairro) where.areas = { some: { neighbor: filters.bairro } };
  return prisma.service.findMany({ where, include: { provider: true, category: true, areas: true } });
}

async function getById(id) {
  return prisma.service.findUnique({ where: { id: Number(id) }, include: { provider: true, category: true, areas: true } });
}

async function create(data) {
  const created = await prisma.service.create({ data });
  return created;
}

async function update(id, data) {
  return prisma.service.update({ where: { id: Number(id) }, data });
}

async function remove(id) {
  await prisma.service.delete({ where: { id: Number(id) } });
}

module.exports = { list, getById, create, update, remove };
