// api-service.js - Servicio para comunicación con la API
const API_SERVICE_BASE_URL = (() => {
    const hostname = window.location.hostname;
    const port = window.location.port;
    
    console.log(`API Service - Frontend ejecutándose en: ${hostname}:${port}`);
    
    // Si estamos en el contenedor Docker (puerto 80), usar localhost:8080
    if (port === '80' || port === '' || port === '3000') {
        console.log('API Service - Usando configuración para contenedor Docker');
        return 'http://localhost:8080';
    }
    
    // Para desarrollo local o Live Server
    if (hostname === 'localhost' || hostname === '127.0.0.1') {
        console.log('API Service - Usando configuración para desarrollo local');
        return 'http://localhost:8080';
    }
    
    // Para producción o otros entornos
    console.log('API Service - Usando configuración para producción');
    return `${window.location.protocol}//${hostname}:8080`;
})();

// Función para realizar peticiones HTTP
async function makeRequest(url, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
        },
    };
    
    const finalOptions = { ...defaultOptions, ...options };
    
    try {
        const response = await fetch(`${API_SERVICE_BASE_URL}${url}`, finalOptions);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('Error en petición API:', error);
        throw error;
    }
}

// Servicios para Pacientes
const pacienteService = {
    // Obtener todos los pacientes
    getAll: () => makeRequest('/api/pacientes'),
    
    // Obtener paciente por ID
    getById: (id) => makeRequest(`/api/pacientes/${id}`),
    
    // Crear nuevo paciente
    create: (paciente) => makeRequest('/api/pacientes', {
        method: 'POST',
        body: JSON.stringify(paciente)
    }),
    
    // Actualizar paciente
    update: (id, paciente) => makeRequest(`/api/pacientes/${id}`, {
        method: 'PUT',
        body: JSON.stringify(paciente)
    }),
    
    // Eliminar paciente
    delete: (id) => makeRequest(`/api/pacientes/${id}`, {
        method: 'DELETE'
    })
};

// Servicios para Médicos
const medicoService = {
    // Obtener todos los médicos
    getAll: () => makeRequest('/api/medicos'),
    
    // Obtener médico por ID
    getById: (id) => makeRequest(`/api/medicos/${id}`),
    
    // Crear nuevo médico
    create: (medico) => makeRequest('/api/medicos', {
        method: 'POST',
        body: JSON.stringify(medico)
    }),
    
    // Actualizar médico
    update: (id, medico) => makeRequest(`/api/medicos/${id}`, {
        method: 'PUT',
        body: JSON.stringify(medico)
    }),
    
    // Eliminar médico
    delete: (id) => makeRequest(`/api/medicos/${id}`, {
        method: 'DELETE'
    }),
    
    // Obtener médicos por especialidad
    getByEspecialidad: (especialidad) => makeRequest(`/api/medicos/especialidad/${especialidad}`)
};

// Servicios para Consultas
const consultaService = {
    // Obtener todas las consultas
    getAll: () => makeRequest('/api/consultas'),
    
    // Obtener consulta por ID
    getById: (id) => makeRequest(`/api/consultas/${id}`),
    
    // Crear nueva consulta
    create: (consulta) => makeRequest('/api/consultas', {
        method: 'POST',
        body: JSON.stringify(consulta)
    }),
    
    // Actualizar consulta
    update: (id, consulta) => makeRequest(`/api/consultas/${id}`, {
        method: 'PUT',
        body: JSON.stringify(consulta)
    }),
    
    // Eliminar consulta
    delete: (id) => makeRequest(`/api/consultas/${id}`, {
        method: 'DELETE'
    }),
    
    // Obtener consultas por paciente
    getByPaciente: (pacienteId) => makeRequest(`/api/consultas/paciente/${pacienteId}`),
    
    // Obtener consultas por médico
    getByMedico: (medicoId) => makeRequest(`/api/consultas/medico/${medicoId}`)
};

// Servicios de Autenticación
const authService = {
    // Login de paciente
    loginPaciente: (email, contrasena) => makeRequest('/api/auth/paciente/login', {
        method: 'POST',
        body: JSON.stringify({ email, contrasena })
    }),
    
    // Registro de paciente
    registerPaciente: (paciente) => makeRequest('/api/auth/paciente/register', {
        method: 'POST',
        body: JSON.stringify(paciente)
    }),
    
    // Login de médico
    loginMedico: (email, contrasena) => makeRequest('/api/auth/medico/login', {
        method: 'POST',
        body: JSON.stringify({ email, contrasena })
    }),
    
    // Registro de médico
    registerMedico: (medico) => makeRequest('/api/auth/medico/register', {
        method: 'POST',
        body: JSON.stringify(medico)
    })
};

// Exportar servicios
window.pacienteService = pacienteService;
window.medicoService = medicoService;
window.consultaService = consultaService;
window.authService = authService;