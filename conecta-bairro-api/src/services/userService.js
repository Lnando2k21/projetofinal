// Serviço mínimo para manipular usuários - aqui você integra com Prisma ou outro ORM
const bcrypt = require('bcrypt');

// Placeholder: em produção troque por consultas ao DB (Prisma client)
const users = []; // só para exemplo
let idCounter = 1;

async function registerUser({ name, email, password, whatsapp, cep, role }) {
  const hashed = await bcrypt.hash(password, 10);
  const user = { id: idCounter++, name, email, password: hashed, whatsapp, cep, role };
  users.push(user);
  return user;
}

async function findUserByEmail(email) {
  return users.find(u => u.email === email);
}

module.exports = { registerUser, findUserByEmail };
