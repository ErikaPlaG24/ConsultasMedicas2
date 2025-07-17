# 🏥 Sistema de Consultas Médicas - Conexión Backend-Frontend

## 📁 Archivos Creados para la Conexión

### ✅ **Archivos principales agregados:**

1. **`frontend/js/api.js`** - Servicio API completo para conectar con el backend
2. **`frontend/js/utils.js`** - Utilidades mejoradas para la UI
3. **`frontend/pacientes.html`** - Página completa de gestión de pacientes
4. **`frontend/test-api.html`** - Página de pruebas para verificar la conexión
5. **`backend/src/main/java/com/example/backend/config/CorsConfig.java`** - Configuración CORS

## 🚀 **Instrucciones para Conectar Backend y Frontend**

### **1. Iniciar el Backend**
```bash
cd backend
mvn spring-boot:run
```
El backend estará disponible en: `http://localhost:8080`

### **2. Iniciar el Frontend**
- Abre `frontend/index.html` con Live Server en VS Code
- O simplemente abre el archivo en tu navegador

### **3. Probar la Conexión**
- Ve a `frontend/test-api.html` para probar todas las conexiones
- Verifica que aparezca "✅ Conexión exitosa!"

## 🔧 **Configuración CORS**

El archivo `CorsConfig.java` permite que el frontend se conecte con el backend desde:
- `http://localhost:3000` (React/development)
- `http://127.0.0.1:5500` (Live Server)
- `http://localhost:5500` (Live Server)

## 🎯 **Funcionalidades Disponibles**

### **API Service (`api.js`)**
```javascript
// Pacientes
await apiService.obtenerPacientes()
await apiService.crearPaciente(data)
await apiService.actualizarPaciente(id, data)
await apiService.eliminarPaciente(id)
await apiService.buscarPacientePorEmail(email)

// Médicos
await apiService.obtenerMedicos()
await apiService.crearMedico(data)
// ... más métodos

// Consultas
await apiService.obtenerConsultas()
await apiService.crearConsulta(data)
// ... más métodos
```

### **Utilidades UI (`utils.js`)**
```javascript
// Crear tarjetas
UIUtils.crearTarjetaPaciente(paciente)
UIUtils.crearTarjetaMedico(medico)

// Mostrar notificaciones
mostrarNotificacion('Mensaje', 'success|error|warning|info')

// Validaciones
UIUtils.validarEmail(email)

// Modal de confirmación
UIUtils.crearModalConfirmacion(titulo, mensaje, callback)
```

## 📋 **Páginas Actualizadas**

1. **`index.html`** - Agregada navegación a pacientes
2. **`pacientes.html`** - CRUD completo de pacientes conectado al backend
3. **`test-api.html`** - Pruebas de conexión y API

## 🔍 **Solución de Problemas**

### **Error de CORS**
- Asegúrate de que `CorsConfig.java` esté en el backend
- Reinicia el backend después de agregar CORS

### **Error de Conexión**
- Verifica que el backend esté ejecutándose en puerto 8080
- Usa `test-api.html` para diagnosticar problemas

### **Error 404**
- Confirma que todas las rutas del backend estén funcionando
- Prueba manualmente: `http://localhost:8080/api/pacientes`

## 🎉 **Siguientes Pasos**

1. **Probar**: Ve a `test-api.html` y ejecuta todas las pruebas
2. **Gestionar Pacientes**: Ve a `pacientes.html` y crea/edita pacientes
3. **Crear páginas similares**: Para médicos y consultas usando el mismo patrón

## 📞 **Endpoints Disponibles**

```
Backend: http://localhost:8080/api

GET    /pacientes           - Listar todos
GET    /pacientes/{id}      - Obtener por ID  
POST   /pacientes           - Crear nuevo
PUT    /pacientes/{id}      - Actualizar
DELETE /pacientes/{id}      - Eliminar
GET    /pacientes/buscar/email?email=...

GET    /medicos             - Listar todos
GET    /medicos/{id}        - Obtener por ID
POST   /medicos             - Crear nuevo
PUT    /medicos/{id}        - Actualizar
DELETE /medicos/{id}        - Eliminar
GET    /medicos/buscar/especialidad?especialidad=...

GET    /consultas           - Listar todas
GET    /consultas/{id}      - Obtener por ID
POST   /consultas           - Crear nueva
PUT    /consultas/{id}      - Actualizar
DELETE /consultas/{id}      - Eliminar
GET    /consultas/paciente/{id} - Por paciente
GET    /consultas/medico/{id}   - Por médico
```

¡Tu sistema frontend-backend ya está completamente conectado! 🎉
