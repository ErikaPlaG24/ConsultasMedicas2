// Configuración de la API - Detección automática del entorno
const API_BASE_URL = (() => {
    const hostname = window.location.hostname;
    const port = window.location.port;
    
    console.log(`Frontend ejecutándose en: ${hostname}:${port}`);
    
    // Si estamos en el contenedor Docker (puerto 80), usar localhost:8080
    if (port === '80' || port === '' || port === '3000') {
        console.log('Usando configuración para contenedor Docker');
        return 'http://localhost:8080';
    }
    
    // Para desarrollo local o Live Server
    if (hostname === 'localhost' || hostname === '127.0.0.1') {
        console.log('Usando configuración para desarrollo local');
        return 'http://localhost:8080';
    }
    
    // Para producción o otros entornos
    console.log('Usando configuración para producción');
    return `${window.location.protocol}//${hostname}:8080`;
})();

// Configuración de headers por defecto
const DEFAULT_HEADERS = {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
};

// Clase para manejar las llamadas a la API
class ApiService {
    
    // Método genérico para hacer peticiones HTTP
    async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}/api${endpoint}`;
        const config = {
            headers: DEFAULT_HEADERS,
            ...options
        };

        console.log(`Haciendo petición a: ${url}`);

        try {
            const response = await fetch(url, config);
            
            // Verificar si la respuesta es exitosa
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP ${response.status}: ${errorText}`);
            }

            // Verificar si hay contenido para parsear
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            } else {
                return await response.text();
            }
        } catch (error) {
            console.error('Error en la petición:', error);
            throw error;
        }
    }

    // ========== MÉTODOS PARA PACIENTES ==========
    
    // Obtener todos los pacientes
    async obtenerPacientes() {
        return await this.request('/pacientes');
    }

    // Obtener paciente por ID
    async obtenerPaciente(id) {
        return await this.request(`/pacientes/${id}`);
    }

    // Crear nuevo paciente
    async crearPaciente(pacienteData) {
        return await this.request('/pacientes', {
            method: 'POST',
            body: JSON.stringify(pacienteData)
        });
    }

    // Actualizar paciente
    async actualizarPaciente(id, pacienteData) {
        return await this.request(`/pacientes/${id}`, {
            method: 'PUT',
            body: JSON.stringify(pacienteData)
        });
    }

    // Eliminar paciente
    async eliminarPaciente(id) {
        return await this.request(`/pacientes/${id}`, {
            method: 'DELETE'
        });
    }

    // Buscar paciente por email
    async buscarPacientePorEmail(email) {
        return await this.request(`/pacientes/buscar/email?email=${encodeURIComponent(email)}`);
    }

    // ========== MÉTODOS PARA MÉDICOS ==========
    
    // Obtener todos los médicos
    async obtenerMedicos() {
        return await this.request('/medicos');
    }

    // Obtener médico por ID
    async obtenerMedico(id) {
        return await this.request(`/medicos/${id}`);
    }

    // Crear nuevo médico
    async crearMedico(medicoData) {
        return await this.request('/medicos', {
            method: 'POST',
            body: JSON.stringify(medicoData)
        });
    }

    // Actualizar médico
    async actualizarMedico(id, medicoData) {
        return await this.request(`/medicos/${id}`, {
            method: 'PUT',
            body: JSON.stringify(medicoData)
        });
    }

    // Eliminar médico
    async eliminarMedico(id) {
        return await this.request(`/medicos/${id}`, {
            method: 'DELETE'
        });
    }

    // Buscar médicos por especialidad
    async buscarMedicosPorEspecialidad(especialidad) {
        return await this.request(`/medicos/buscar/especialidad?especialidad=${encodeURIComponent(especialidad)}`);
    }

    // ========== MÉTODOS PARA CONSULTAS ==========
    
    // Obtener todas las consultas
    async obtenerConsultas() {
        return await this.request('/consultas');
    }

    // Obtener consulta por ID
    async obtenerConsulta(id) {
        return await this.request(`/consultas/${id}`);
    }

    // Crear nueva consulta
    async crearConsulta(consultaData) {
        return await this.request('/consultas', {
            method: 'POST',
            body: JSON.stringify(consultaData)
        });
    }

    // Actualizar consulta
    async actualizarConsulta(id, consultaData) {
        return await this.request(`/consultas/${id}`, {
            method: 'PUT',
            body: JSON.stringify(consultaData)
        });
    }

    // Eliminar consulta
    async eliminarConsulta(id) {
        return await this.request(`/consultas/${id}`, {
            method: 'DELETE'
        });
    }

    // Obtener consultas por paciente
    async obtenerConsultasPorPaciente(idPaciente) {
        return await this.request(`/consultas/paciente/${idPaciente}`);
    }

    // Obtener consultas por médico
    async obtenerConsultasPorMedico(idMedico) {
        return await this.request(`/consultas/medico/${idMedico}`);
    }

    // ========== MÉTODOS UTILITARIOS ==========
    
    // Verificar conexión con el backend
    async verificarConexion() {
        try {
            await this.request('/pacientes');
            return true;
        } catch (error) {
            console.error('No se pudo conectar con el backend:', error);
            return false;
        }
    }

    // Formatear fecha para el backend (YYYY-MM-DD)
    formatearFecha(fecha) {
        if (fecha instanceof Date) {
            return fecha.toISOString().split('T')[0];
        }
        return fecha;
    }

    // Formatear fecha y hora para el backend (YYYY-MM-DD HH:mm:ss)
    formatearFechaHora(fechaHora) {
        if (fechaHora instanceof Date) {
            return fechaHora.toISOString().slice(0, 19).replace('T', ' ');
        }
        return fechaHora;
    }
}

// Crear instancia global del servicio API
const apiService = new ApiService();

// Función para mostrar notificaciones
function mostrarNotificacion(mensaje, tipo = 'info') {
    // Crear elemento de notificación
    const notificacion = document.createElement('div');
    notificacion.className = `alert alert-${tipo} alert-dismissible fade show position-fixed`;
    notificacion.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    notificacion.innerHTML = `
        ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    // Agregar al body
    document.body.appendChild(notificacion);

    // Remover automáticamente después de 5 segundos
    setTimeout(() => {
        if (notificacion.parentNode) {
            notificacion.parentNode.removeChild(notificacion);
        }
    }, 5000);
}

// Función para manejar errores de la API
function manejarErrorAPI(error) {
    console.error('Error de API:', error);
    
    let mensaje = 'Ha ocurrido un error inesperado';
    
    if (error.message.includes('Failed to fetch')) {
        mensaje = `❌ Error de conexión: No se pudo conectar con el backend en ${API_BASE_URL}
        
        🔍 Posibles causas:
        1. El backend no está ejecutándose (verificar: docker ps)
        2. Problema de CORS (el frontend necesita acceder desde file:// o http://localhost)
        3. Puerto incorrecto (verificar que sea 8080)
        
        ✅ Solución: Asegúrate de que el backend esté corriendo con Docker`;
    } else if (error.message.includes('HTTP 404')) {
        mensaje = 'Recurso no encontrado';
    } else if (error.message.includes('HTTP 409')) {
        mensaje = 'El email ya está registrado';
    } else if (error.message.includes('HTTP 400')) {
        mensaje = 'Datos inválidos o incompletos';
    } else if (error.message.includes('HTTP 500')) {
        mensaje = 'Error interno del servidor';
    }
    
    mostrarNotificacion(mensaje, 'danger');
    return mensaje;
}

// Verificar conexión al cargar la página
document.addEventListener('DOMContentLoaded', async () => {
    const conectado = await apiService.verificarConexion();
    if (!conectado) {
        mostrarNotificacion('⚠️ No se pudo conectar con el backend. Asegúrese de que esté ejecutándose en http://localhost:8080', 'warning');
    } else {
        console.log('✅ Conexión con el backend establecida correctamente');
    }
});
