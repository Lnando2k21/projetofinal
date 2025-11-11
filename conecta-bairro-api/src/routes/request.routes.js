const express = require('express');
const router = express.Router();
const requestController = require('../controllers/request.controller');

router.post('/', requestController.create);
router.get('/:id', requestController.getById);
router.put('/:id/status', requestController.updateStatus);

module.exports = router;
