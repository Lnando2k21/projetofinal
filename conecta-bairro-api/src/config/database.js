// Inicializa o cliente Prisma para uso por serviços/controles
const { PrismaClient } = require('@prisma/client');

const prisma = new PrismaClient();

module.exports = prisma;
