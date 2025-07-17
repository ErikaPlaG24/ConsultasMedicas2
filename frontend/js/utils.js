// Utilidades mejoradas para conectar frontend con backend
class UIUtils {
    
    // Mostrar spinner de carga
    static mostrarCargando(elemento, texto = 'Cargando...') {
        if (typeof elemento === 'string') {
            elemento = document.getElementById(elemento);
        }
        
        if (elemento) {
            elemento.innerHTML = `
                <div class="d-flex justify-content-center align-items-center p-3">
                    <div class="spinner-border text-primary me-2" role="status">
                        <span class="visually-hidden">Cargando...</span>
                    </div>
                    <span>${texto}</span>
                </div>
            `;
        }
    }

    // Crear tarjeta de paciente
    static crearTarjetaPaciente(paciente) {
        return `
            <div class="col-md-6 col-lg-4 mb-3">
                <div class="card h-100 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">
                            <i class="fas fa-user me-2"></i>
                            ${paciente.nombre} ${paciente.apellido}
                        </h5>
                        <p class="card-text">
                            <small class="text-muted">
                                <i class="fas fa-envelope me-1"></i> ${paciente.email}<br>
                                <i class="fas fa-phone me-1"></i> ${paciente.telefono || 'No especificado'}<br>
                                <i class="fas fa-calendar me-1"></i> ${this.formatearFecha(paciente.fecha_nacimiento)}
                            </small>
                        </p>
                    </div>
                    <div class="card-footer bg-transparent">
                        <div class="btn-group w-100" role="group">
                            <button class="btn btn-outline-primary btn-sm" onclick="editarPaciente(${paciente.id_paciente})">
                                <i class="fas fa-edit"></i> Editar
                            </button>
                            <button class="btn btn-outline-danger btn-sm" onclick="eliminarPaciente(${paciente.id_paciente})">
                                <i class="fas fa-trash"></i> Eliminar
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    // Crear tarjeta de médico
    static crearTarjetaMedico(medico) {
        return `
            <div class="col-md-6 col-lg-4 mb-3">
                <div class="card h-100 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">
                            <i class="fas fa-user-md me-2"></i>
                            Dr. ${medico.nombre} ${medico.apellido}
                        </h5>
                        <p class="card-text">
                            <span class="badge bg-primary mb-2">${medico.especialidad}</span><br>
                            <small class="text-muted">
                                <i class="fas fa-envelope me-1"></i> ${medico.email}<br>
                                <i class="fas fa-phone me-1"></i> ${medico.telefono || 'No especificado'}<br>
                                <i class="fas fa-id-card me-1"></i> ${medico.registro_profesional}
                            </small>
                        </p>
                    </div>
                    <div class="card-footer bg-transparent">
                        <div class="btn-group w-100" role="group">
                            <button class="btn btn-outline-success btn-sm" onclick="agendarCita(${medico.id_medico})">
                                <i class="fas fa-calendar-plus"></i> Agendar
                            </button>
                            <button class="btn btn-outline-primary btn-sm" onclick="editarMedico(${medico.id_medico})">
                                <i class="fas fa-edit"></i> Editar
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    // Formatear fecha (YYYY-MM-DD a DD/MM/YYYY)
    static formatearFecha(fecha) {
        if (!fecha) return 'No especificada';
        
        try {
            const fechaObj = new Date(fecha);
            return fechaObj.toLocaleDateString('es-ES');
        } catch (error) {
            return fecha;
        }
    }

    // Validar email
    static validarEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }

    // Crear modal de confirmación
    static crearModalConfirmacion(titulo, mensaje, onConfirmar) {
        const modalId = 'modalConfirmacion';
        
        // Remover modal existente si existe
        const modalExistente = document.getElementById(modalId);
        if (modalExistente) {
            modalExistente.remove();
        }

        const modalHTML = `
            <div class="modal fade" id="${modalId}" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">${titulo}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            ${mensaje}
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="button" class="btn btn-danger" id="confirmarAccion">Confirmar</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', modalHTML);
        
        const modal = new bootstrap.Modal(document.getElementById(modalId));
        
        document.getElementById('confirmarAccion').addEventListener('click', () => {
            modal.hide();
            onConfirmar();
        });

        modal.show();
    }
}

// Utilidades originales (mantenidas para compatibilidad)
var MedicalConsultUtils = {
    // Formatear fecha en español
    formatDate: function(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('es-ES', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    // Validar formato de fecha
    isValidDate: function(dateString) {
        const date = new Date(dateString);
        return date instanceof Date && !isNaN(date);
    },

    // Verificar si una fecha es futura
    isFutureDate: function(dateString) {
        const date = new Date(dateString);
        const now = new Date();
        return date > now;
    },

    // Verificar horario de atención
    isWorkingHour: function(dateString) {
        const date = new Date(dateString);
        const hour = date.getHours();
        return hour >= 9 && hour <= 18;
    },

    // Obtener icono según especialidad
    getSpecialtyIcon: function(specialty) {
        const icons = {
            'Cardiología': 'fas fa-heart',
            'Pediatría': 'fas fa-baby',
            'Dermatología': 'fas fa-eye',
            'Neurología': 'fas fa-brain',
            'Traumatología': 'fas fa-bone',
            'Ginecología': 'fas fa-female',
            'Medicina General': 'fas fa-user-md',
            'Neumología': 'fas fa-lungs'
        };
        return icons[specialty] || 'fas fa-user-md';
    },

    // Obtener color según especialidad
    getSpecialtyColor: function(specialty) {
        const colors = {
            'Cardiología': '#e63946',
            'Pediatría': '#06d6a0',
            'Dermatología': '#ffd60a',
            'Neurología': '#7209b7',
            'Traumatología': '#f77f00',
            'Ginecología': '#d00000',
            'Medicina General': '#2c5aa0',
            'Neumología': '#023e8a'
        };
        return colors[specialty] || '#2c5aa0';
    },

    // Mostrar notificación
    showNotification: function(message, type = 'success') {
        const notification = document.createElement('div');
        notification.className = `alert alert-${type} position-fixed`;
        notification.style.cssText = `
            top: 20px;
            right: 20px;
            z-index: 9999;
            min-width: 300px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        `;
        notification.innerHTML = `
            <div class="d-flex align-items-center">
                <i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'} me-2"></i>
                ${message}
                <button type="button" class="btn-close ms-auto" onclick="this.parentElement.parentElement.remove()"></button>
            </div>
        `;
        document.body.appendChild(notification);
        
        // Auto-remover después de 5 segundos
        setTimeout(() => {
            if (notification.parentElement) {
                notification.remove();
            }
        }, 5000);
    },

    // Validar formulario de cita
    validateAppointment: function(appointment) {
        const errors = [];
        
        if (!appointment.medico) {
            errors.push('El nombre del médico es requerido');
        }
        
        if (!appointment.especialidad) {
            errors.push('La especialidad es requerida');
        }
        
        if (!appointment.fecha) {
            errors.push('La fecha y hora son requeridas');
        } else {
            if (!this.isValidDate(appointment.fecha)) {
                errors.push('La fecha no es válida');
            } else if (!this.isFutureDate(appointment.fecha)) {
                errors.push('La fecha debe ser futura');
            } else if (!this.isWorkingHour(appointment.fecha)) {
                errors.push('La cita debe ser en horario de atención (9:00 AM - 6:00 PM)');
            }
        }
        
        return errors;
    },

    // Guardar en localStorage
    saveToStorage: function(key, data) {
        try {
            localStorage.setItem(key, JSON.stringify(data));
            return true;
        } catch (error) {
            console.error('Error saving to localStorage:', error);
            return false;
        }
    },

    // Cargar desde localStorage
    loadFromStorage: function(key) {
        try {
            const data = localStorage.getItem(key);
            return data ? JSON.parse(data) : null;
        } catch (error) {
            console.error('Error loading from localStorage:', error);
            return null;
        }
    },

    // Generar ID único
    generateId: function() {
        return Date.now().toString(36) + Math.random().toString(36).substr(2);
    }
};

// Configuración de la aplicación
const AppConfig = {
    STORAGE_KEYS: {
        APPOINTMENTS: 'misCitas',
        DOCTORS: 'medicos',
        USER_PREFERENCES: 'preferenciasUsuario'
    },
    
    WORKING_HOURS: {
        START: 9,
        END: 18
    },
    
    SPECIALTIES: [
        'Cardiología',
        'Pediatría',
        'Dermatología',
        'Neurología',
        'Traumatología',
        'Ginecología',
        'Medicina General',
        'Neumología'
    ]
};

// Exportar para uso global
window.MedicalConsultUtils = MedicalConsultUtils;
window.AppConfig = AppConfig;
