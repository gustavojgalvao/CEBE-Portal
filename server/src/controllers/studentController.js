// In-memory data for demonstration purposes
let students = [
  { id: 1, name: 'João Silva', courses: ['Confeitaria'], shifts: ['Manhã'], enrollmentId: '2023001', phone: '11999999999' }
];

// RF01: Cadastro de alunos
exports.createStudent = (req, res) => {
  const { name, courses, shifts, phone } = req.body;
  
  // RF02: Validar turnos caso haja múltiplos cursos
  if (courses.length > 1 && shifts.length !== courses.length) {
    return res.status(400).json({ error: 'Número de turnos deve ser igual ao número de cursos escolhidos, e em turnos diferentes.' });
  }
  
  // TODO: Add logic to check for distinct shifts if multiple courses
  
  const newStudent = {
    id: students.length + 1,
    name,
    courses,
    shifts,
    enrollmentId: `202300${students.length + 1}`,
    phone
  };
  
  students.push(newStudent);
  res.status(201).json(newStudent);
};

// RF01: Remoção de alunos
exports.deleteStudent = (req, res) => {
  const { id } = req.params;
  const initialLength = students.length;
  students = students.filter(student => student.id !== parseInt(id));
  
  if (students.length === initialLength) {
    return res.status(404).json({ error: 'Aluno não encontrado.' });
  }
  
  res.status(200).json({ message: 'Aluno removido com sucesso.' });
};

// RF05: Exibir informações atreladas ao aluno
exports.getStudentDetails = (req, res) => {
  const { id } = req.params;
  const student = students.find(s => s.id === parseInt(id));
  
  if (!student) {
    return res.status(404).json({ error: 'Aluno não encontrado.' });
  }
  
  // Informações simuladas de professor atrelado ao curso
  const studentDetails = {
    ...student,
    teachers: student.courses.map(course => `${course} - Prof. Padrão`)
  };
  
  res.status(200).json(studentDetails);
};

// RF06: Troca de turmas
exports.changeClass = (req, res) => {
  const { id } = req.params;
  const { oldCourse, newCourse, newShift } = req.body;
  
  let student = students.find(s => s.id === parseInt(id));
  if (!student) {
    return res.status(404).json({ error: 'Aluno não encontrado.' });
  }
  
  const courseIndex = student.courses.indexOf(oldCourse);
  if (courseIndex === -1) {
    return res.status(400).json({ error: 'Aluno não está matriculado neste curso.' });
  }
  
  // Troca
  student.courses[courseIndex] = newCourse;
  student.shifts[courseIndex] = newShift;
  
  res.status(200).json({ message: 'Troca de turma realizada com sucesso.', student });
};

// Auxiliar: Listar todos
exports.getAllStudents = (req, res) => {
  res.status(200).json(students);
};
