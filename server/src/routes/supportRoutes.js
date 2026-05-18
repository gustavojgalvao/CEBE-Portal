const express = require('express');
const router = express.Router();
const supportController = require('../controllers/supportController');

router.get('/', supportController.getAllSupportRequests);
router.post('/', supportController.createSupportRequest);

module.exports = router;
