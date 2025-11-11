const express = require('express');
const router = express.Router();

const authRoutes = require('./auth.routes');
const serviceRoutes = require('./service.routes');
const requestRoutes = require('./request.routes');
const reviewRoutes = require('./review.routes');

router.use('/auth', authRoutes);
router.use('/services', serviceRoutes);
router.use('/requests', requestRoutes);
router.use('/reviews', reviewRoutes);

router.get('/', (req, res) => {
  res.json({ message: 'API Conecta Bairro' });
});

module.exports = router;
