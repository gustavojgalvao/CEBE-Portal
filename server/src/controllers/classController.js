// In-memory data for demonstration purposes
const classes = [
  { id: 1, course: 'Confeitaria', shift: 'Manhã', maxCapacity: 20, currentEnrolled: 20, teacher: 'Chef Ana' },
  { id: 2, course: 'Confeitaria', shift: 'Tarde', maxCapacity: 20, currentEnrolled: 15, teacher: 'Chef Ana' },
  { id: 3, course: 'Administração', shift: 'Noite', maxCapacity: 30, currentEnrolled: 10, teacher: 'Prof. Carlos' },
  { id: 4, course: 'Informática Básica', shift: 'Manhã', maxCapacity: 15, currentEnrolled: 15, teacher: 'Prof. Marcos' },
];

// RF03: Listar turmas disponíveis e informar quais estão lotadas
exports.getAllClasses = (req, res) => {
  const classesStatus = classes.map(c => ({
    ...c,
    isFull: c.currentEnrolled >= c.maxCapacity,
    availableSpots: c.maxCapacity - c.currentEnrolled
  }));
  
  res.status(200).json(classesStatus);
};

// Auxiliar: Buscar turma específica
exports.getClassById = (req, res) => {
  const { id } = req.params;
  const classItem = classes.find(c => c.id === parseInt(id));
  
  if (!classItem) {
    return res.status(404).json({ error: 'Turma não encontrada.' });
  }
  
  res.status(200).json({
    ...classItem,
    isFull: classItem.currentEnrolled >= classItem.maxCapacity,
    availableSpots: classItem.maxCapacity - classItem.currentEnrolled
  });
};
