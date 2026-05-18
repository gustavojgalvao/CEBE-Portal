import { getClasses, registerStudent, scheduleAppointment } from '../services/api.js';

document.addEventListener('DOMContentLoaded', () => {
    loadClasses();
    setupForms();
});

// Render Classes (RF03)
async function loadClasses() {
    const grid = document.getElementById('classes-grid');
    const courseSelect = document.getElementById('courseSelect');
    
    try {
        const classes = await getClasses();
        
        // Clear skeletons
        grid.innerHTML = '';
        // Clear default select
        courseSelect.innerHTML = '<option value="">Selecione...</option>';
        
        const uniqueCourses = new Set();
        
        classes.forEach(cls => {
            uniqueCourses.add(cls.course);
            
            // Build Card
            const isFull = cls.isFull;
            const card = document.createElement('div');
            card.className = 'class-card';
            
            card.innerHTML = `
                <div class="class-header">
                    <h4>${cls.course}</h4>
                    <span class="badge ${isFull ? 'full' : 'available'}">
                        ${isFull ? 'Lotada' : 'Vagas Abertas'}
                    </span>
                </div>
                <div class="class-info"><strong>Professor:</strong> ${cls.teacher}</div>
                <div class="class-info"><strong>Turno:</strong> ${cls.shift}</div>
                <div class="class-info"><strong>Vagas Disponíveis:</strong> ${cls.availableSpots} de ${cls.maxCapacity}</div>
            `;
            grid.appendChild(card);
        });
        
        // Populate form Selects
        uniqueCourses.forEach(course => {
            const option = document.createElement('option');
            option.value = course;
            option.textContent = course;
            courseSelect.appendChild(option);
        });
        
    } catch (error) {
        grid.innerHTML = '<p style="color:var(--danger)">Erro ao carregar as turmas. O servidor está rodando?</p>';
    }
}

// Setup Form Listeners (RF01, RF04)
function setupForms() {
    const enrollmentForm = document.getElementById('enrollment-form');
    const appointmentForm = document.getElementById('appointment-form');
    
    enrollmentForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const fb = document.getElementById('enrollment-feedback');
        fb.className = 'feedback-msg';
        fb.textContent = 'Enviando...';
        
        const data = {
            name: document.getElementById('studentName').value,
            phone: document.getElementById('studentPhone').value,
            courses: [document.getElementById('courseSelect').value],
            shifts: [document.getElementById('shiftSelect').value]
        };
        
        try {
            await registerStudent(data);
            fb.textContent = 'Matrícula realizada com sucesso!';
            fb.classList.add('success');
            enrollmentForm.reset();
        } catch (err) {
            fb.textContent = 'Erro ao processar matrícula.';
            fb.classList.add('error');
        }
    });

    appointmentForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const fb = document.getElementById('appointment-feedback');
        fb.className = 'feedback-msg';
        fb.textContent = 'Agendando...';
        
        const data = {
            name: document.getElementById('appName').value,
            type: document.getElementById('appType').value,
            date: document.getElementById('appDate').value,
            time: document.getElementById('appTime').value,
            phone: 'N/A', // Simplified for demo
            email: 'N/A'
        };
        
        try {
            await scheduleAppointment(data);
            fb.textContent = 'Agendamento confirmado com sucesso!';
            fb.classList.add('success');
            appointmentForm.reset();
        } catch (err) {
            fb.textContent = 'Erro ao processar agendamento.';
            fb.classList.add('error');
        }
    });
}
