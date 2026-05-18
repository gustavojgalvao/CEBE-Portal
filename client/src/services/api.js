const API_BASE_URL = 'http://localhost:3000/api';

// Classes API
export const getClasses = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/classes`);
    if (!response.ok) throw new Error('Erro ao buscar turmas');
    return await response.json();
  } catch (error) {
    console.error('Erro getClasses:', error);
    return [];
  }
};

// Students API
export const registerStudent = async (studentData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/students`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(studentData),
    });
    return await response.json();
  } catch (error) {
    console.error('Erro registerStudent:', error);
    throw error;
  }
};

// Appointments API
export const scheduleAppointment = async (appointmentData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/appointments`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(appointmentData),
    });
    return await response.json();
  } catch (error) {
    console.error('Erro scheduleAppointment:', error);
    throw error;
  }
};
