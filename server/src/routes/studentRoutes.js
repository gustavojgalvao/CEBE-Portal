const express = require('express');
const router = express.Router();
const studentController = require('../controllers/studentController');

router.get('/', studentController.getAllStudents);
router.post('/', studentController.createStudent);
router.get('/:id', studentController.getStudentDetails);
router.delete('/:id', studentController.deleteStudent);
router.put('/:id/change-class', studentController.changeClass);

module.exports = router;
