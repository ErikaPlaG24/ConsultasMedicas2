// auth.js - Manejo de autenticación

// Función para mostrar alertas
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer');
    const alertId = 'alert-' + Date.now();
    
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.id = alertId;
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    alertContainer.appendChild(alert);
    
    // Remover automáticamente después de 5 segundos
    setTimeout(() => {
        const alertElement = document.getElementById(alertId);
        if (alertElement) {
            alertElement.remove();
        }
    }, 5000);
}

// Función para validar email
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

// Función para validar contraseña
function validatePassword(password) {
    return password.length >= 6;
}

// Función para manejar el login
async function handleLogin(email, password, role = null) {
    try {
        const loginData = {
            email: email,
            contrasena: password
        };
        
        // Solo incluir tipoUsuario si se especifica
        if (role) {
            loginData.tipoUsuario = role;
        }
        
        const response = await fetch(`${window.APP_CONFIG.API_BASE_URL}/api/auth/login`, {
            method: 'POST',
            headers: window.APP_CONFIG.DEFAULT_HEADERS,
            body: JSON.stringify(loginData)
        });
        
        const data = await response.json();
        
        if (response.ok) {
            // Guardar información del usuario en localStorage
            localStorage.setItem('user', JSON.stringify(data));
            localStorage.setItem('userRole', data.tipoUsuario);
            
            showAlert('¡Inicio de sesión exitoso!', 'success');
            
            // Redirigir según el rol
            setTimeout(() => {
                if (data.tipoUsuario === 'paciente') {
                    window.location.href = 'index.html';
                } else {
                    window.location.href = 'index.html';
                }
            }, 1500);
        } else {
            showAlert(data.message || 'Error al iniciar sesión', 'danger');
        }
    } catch (error) {
        console.error('Error:', error);
        showAlert('Error de conexión. Intenta nuevamente.', 'danger');
    }
}

// Función para manejar el registro
async function handleRegister(formData, role) {
    try {
        const endpoint = `/api/auth/register/${role}`;
        
        const response = await fetch(`${window.APP_CONFIG.API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: window.APP_CONFIG.DEFAULT_HEADERS,
            body: JSON.stringify(formData)
        });
        
        const data = await response.json();
        
        if (response.ok) {
            showAlert('¡Registro exitoso! Ahora puedes iniciar sesión.', 'success');
            
            // Redirigir al login después de 2 segundos
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 2000);
        } else {
            showAlert(data.message || 'Error al registrarse', 'danger');
        }
    } catch (error) {
        console.error('Error:', error);
        showAlert('Error de conexión. Intenta nuevamente.', 'danger');
    }
}

// Función para cerrar sesión
function logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('userRole');
    window.location.href = 'login.html';
}

// Función para verificar si el usuario está logueado
function isLoggedIn() {
    return localStorage.getItem('user') !== null;
}

// Función para obtener el usuario actual
function getCurrentUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
}

// Función para obtener el rol del usuario
function getUserRole() {
    return localStorage.getItem('userRole');
}

// Función para proteger páginas (requiere login)
function requireAuth() {
    if (!isLoggedIn()) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

// Inicialización cuando se carga la página
document.addEventListener('DOMContentLoaded', function() {
    // Manejar selección de rol en registro (si existe)
    const roleButtons = document.querySelectorAll('.role-btn');
    let selectedRole = 'paciente'; // Por defecto para registro
    
    roleButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            roleButtons.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            selectedRole = this.dataset.role;
            
            // Mostrar/ocultar campos específicos en registro
            if (document.getElementById('pacienteFields')) {
                const pacienteFields = document.getElementById('pacienteFields');
                const medicoFields = document.getElementById('medicoFields');
                
                if (selectedRole === 'paciente') {
                    pacienteFields.style.display = 'block';
                    medicoFields.style.display = 'none';
                    
                    // Hacer campos requeridos/no requeridos
                    document.getElementById('fechaNacimiento').required = true;
                    document.getElementById('especialidad').required = false;
                    document.getElementById('registroProfesional').required = false;
                } else {
                    pacienteFields.style.display = 'none';
                    medicoFields.style.display = 'block';
                    
                    // Hacer campos requeridos/no requeridos
                    document.getElementById('fechaNacimiento').required = false;
                    document.getElementById('especialidad').required = true;
                    document.getElementById('registroProfesional').required = true;
                }
            }
        });
    });

    // Manejar formulario de login
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const email = document.getElementById('email').value;
            const password = document.getElementById('contrasena').value;
            
            // Validaciones
            if (!validateEmail(email)) {
                showAlert('Por favor ingresa un email válido', 'warning');
                return;
            }
            
            if (!validatePassword(password)) {
                showAlert('La contraseña debe tener al menos 6 caracteres', 'warning');
                return;
            }
            
            // Llamar sin especificar rol para autenticación automática
            await handleLogin(email, password);
        });
    }
    
    // Manejar formulario de registro
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const formData = new FormData(registerForm);
            const data = Object.fromEntries(formData);
            
            // Validaciones
            if (!validateEmail(data.email)) {
                showAlert('Por favor ingresa un email válido', 'warning');
                return;
            }
            
            if (!validatePassword(data.contrasena)) {
                showAlert('La contraseña debe tener al menos 6 caracteres', 'warning');
                return;
            }
            
            if (data.contrasena !== data.confirmarContrasena) {
                showAlert('Las contraseñas no coinciden', 'warning');
                return;
            }
            
            // Remover confirmación de contraseña del objeto
            delete data.confirmarContrasena;
            
            // Convertir fecha de nacimiento al formato correcto para pacientes
            if (selectedRole === 'paciente' && data.fechaNacimiento) {
                data.fechaNacimiento = data.fechaNacimiento;
            }
            
            await handleRegister(data, selectedRole);
        });
    }
    
    // Verificar si hay un usuario logueado y mostrar información
    if (isLoggedIn()) {
        const user = getCurrentUser();
        const userRole = getUserRole();
        
        // Actualizar navbar si existe
        const navbarNav = document.querySelector('.navbar-nav');
        if (navbarNav) {
            const userInfo = document.createElement('li');
            userInfo.className = 'nav-item dropdown';
            userInfo.innerHTML = `
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                    <i class="fas fa-user"></i> ${user.nombre} ${user.apellido}
                </a>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="#" onclick="logout()">
                        <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                    </a></li>
                </ul>
            `;
            navbarNav.appendChild(userInfo);
        }
    }
});

// Función para actualizar la navbar con información del usuario
function updateNavbar() {
    if (isLoggedIn()) {
        const user = getCurrentUser();
        const userRole = getUserRole();
        
        // Buscar el botón de login y reemplazarlo
        const loginBtn = document.querySelector('a[href="login.html"]');
        if (loginBtn) {
            loginBtn.outerHTML = `
                <div class="dropdown">
                    <button class="btn btn-outline-light dropdown-toggle" type="button" data-bs-toggle="dropdown">
                        <i class="fas fa-user"></i> ${user.nombre}
                    </button>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#" onclick="logout()">
                            <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                        </a></li>
                    </ul>
                </div>
            `;
        }
    }
}

// Exportar funciones para uso global
window.handleLogin = handleLogin;
window.handleRegister = handleRegister;
window.logout = logout;
window.isLoggedIn = isLoggedIn;
window.getCurrentUser = getCurrentUser;
window.getUserRole = getUserRole;
window.requireAuth = requireAuth;
window.updateNavbar = updateNavbar;
