const reviewService = require('../services/reviewService');

exports.create = async (req, res, next) => {
  try {
    const created = await reviewService.create(req.body);
    res.status(201).json(created);
  } catch (err) {
    next(err);
  }
};

exports.listByService = async (req, res, next) => {
  try {
    const serviceId = parseInt(req.params.serviceId);
    const reviews = await reviewService.listByService(serviceId);
    res.json(reviews);
  } catch (err) {
    next(err);
  }
};