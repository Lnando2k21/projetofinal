const requestService = require('../services/requestService');

exports.create = async (req, res, next) => {
  try {
    const created = await requestService.create(req.body);
    res.status(201).json(created);
  } catch (err) {
    next(err);
  }
};

exports.getById = async (req, res, next) => {
  try {
    const reqObj = await requestService.getById(parseInt(req.params.id));
    if (!reqObj) return res.status(404).json({ message: 'Solicitação não encontrada' });
    res.json(reqObj);
  } catch (err) {
    next(err);
  }
};

exports.updateStatus = async (req, res, next) => {
  try {
    const updated = await requestService.updateStatus(parseInt(req.params.id), req.body.status);
    res.json(updated);
  } catch (err) {
    next(err);
  }
};