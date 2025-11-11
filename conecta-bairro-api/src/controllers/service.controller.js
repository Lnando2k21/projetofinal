// Controller simples para operações CRUD de Service (skeleton)
const serviceService = require('../services/serviceService');

exports.list = async (req, res, next) => {
  try {
    const { bairro, category } = req.query;
    const services = await serviceService.list({ bairro, category });
    res.json(services);
  } catch (err) {
    next(err);
  }
};

exports.getById = async (req, res, next) => {
  try {
    const service = await serviceService.getById(parseInt(req.params.id));
    if (!service) return res.status(404).json({ message: 'Serviço não encontrado' });
    res.json(service);
  } catch (err) {
    next(err);
  }
};

exports.create = async (req, res, next) => {
  try {
    const created = await serviceService.create(req.body);
    res.status(201).json(created);
  } catch (err) {
    next(err);
  }
};

exports.update = async (req, res, next) => {
  try {
    const updated = await serviceService.update(parseInt(req.params.id), req.body);
    res.json(updated);
  } catch (err) {
    next(err);
  }
};

exports.remove = async (req, res, next) => {
  try {
    await serviceService.remove(parseInt(req.params.id));
    res.status(204).end();
  } catch (err) {
    next(err);
  }
};