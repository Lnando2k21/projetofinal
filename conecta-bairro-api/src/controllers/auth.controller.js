const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const { registerUser, findUserByEmail } = require('../services/userService');

exports.register = async (req, res, next) => {
  try {
    const { name, email, password, whatsapp, cep, role, address, professional } = req.body;
    // validações simples
    if (!email || !password || !name) return res.status(400).json({ message: 'Campos obrigatórios faltando' });

    const existing = await findUserByEmail(email);
    if (existing) return res.status(409).json({ message: 'E-mail já registrado' });

    // registerUser agora aceita address e professional para providers
    const user = await registerUser({ name, email, password, whatsapp, cep, role, address, professional });

    res.status(201).json({ user: { id: user.id, email: user.email, name: user.name } });
  } catch (err) {
    next(err);
  }
};

exports.login = async (req, res, next) => {
  try {
    const { email, password } = req.body;
    const user = await findUserByEmail(email);
    if (!user) return res.status(401).json({ message: 'Credenciais inválidas' });

    const match = await bcrypt.compare(password, user.password);
    if (!match) return res.status(401).json({ message: 'Credenciais inválidas' });

    const token = jwt.sign({ userId: user.id, role: user.role }, process.env.JWT_SECRET || 'dev_secret', { expiresIn: '7d' });
    res.json({ token, user: { id: user.id, email: user.email, name: user.name } });
  } catch (err) {
    next(err);
  }
};