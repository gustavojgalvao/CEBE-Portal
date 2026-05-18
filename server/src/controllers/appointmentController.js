// In-memory data for demonstration purposes
const appointments = [];

// RF04: Agendamentos
exports.createAppointment = (req, res) => {
  const { name, phone, email, date, time, type, observations } = req.body;
  
  if (!name || !date || !time || !type) {
    return res.status(400).json({ error: 'Nome, data, hora e tipo do agendamento são obrigatórios.' });
  }
  
  const newAppointment = {
    id: appointments.length + 1,
    name,
    phone,
    email,
    date,
    time,
    type, // 'visita' ou 'duvida'
    observations,
    status: 'Agendado'
  };
  
  appointments.push(newAppointment);
  res.status(201).json({ message: 'Agendamento realizado com sucesso.', appointment: newAppointment });
};

// Auxiliar: Listar agendamentos
exports.getAllAppointments = (req, res) => {
  res.status(200).json(appointments);
};
