// Serviço de usuários usando Prisma
const bcrypt = require('bcrypt');
const prisma = require('../config/database');

async function registerUser({ name, email, password, whatsapp, cep, role, address, professional }) {
  const hashed = await bcrypt.hash(password, 10);
  const user = await prisma.user.create({
    data: {
      name,
      email,
      password: hashed,
      whatsapp,
      cep,
      role
    }
  });

  // Se for prestador, crie perfil de serviço básico (categoria + service + area)
  if ((role === 'PROVIDER' || role === 'provider' || role === 'PROFESSIONAL') && professional) {
    try {
      const categories = Array.isArray(professional.categories) ? professional.categories : [];
      const firstCategory = categories.length > 0 ? categories[0] : 'Geral';

      // upsert category
      const category = await prisma.category.upsert({
        where: { name: firstCategory },
        update: {},
        create: { name: firstCategory }
      });

      // criar um Service vinculado ao provider (apenas um por provider por causa da constraint unique)
      const serviceData = {
        title: `${name} - ${category.name}`,
        description: professional.description || '',
        priceRange: professional.hourlyRate ? String(professional.hourlyRate) : 'A combinar',
        providerId: user.id,
        categoryId: category.id
      };

      const service = await prisma.service.create({ data: serviceData });

      // criar área de atendimento a partir do endereço (neighborhood)
      const neighbor = address?.neighborhood || address?.bairro || 'Não informado';
      await prisma.serviceArea.create({ data: { serviceId: service.id, city: address?.city || '', neighbor } });
    } catch (err) {
      // não interromper o registro do usuário se falhar a criação das entidades relacionadas
      console.error('Erro criando perfil de provider:', err);
    }
  }

  return user;
}

async function findUserByEmail(email) {
  return prisma.user.findUnique({ where: { email } });
}

module.exports = { registerUser, findUserByEmail };
