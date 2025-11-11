const express = require('express');
const router = express.Router();
const reviewController = require('../controllers/review.controller');

router.post('/', reviewController.create);
router.get('/service/:serviceId', reviewController.listByService);

module.exports = router;
