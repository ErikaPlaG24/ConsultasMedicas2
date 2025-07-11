# Sistema de Consultas Médicas Online 🏥

Un sistema web moderno y amigable para la gestión de citas médicas, desarrollado con HTML, CSS, JavaScript y Bootstrap.

## 📋 Características

- **Interfaz Moderna**: Diseño atractivo con gradientes, efectos glassmorphism y animaciones suaves
- **Responsive**: Adaptado para dispositivos móviles y tablets
- **Gestión de Citas**: Agenda, visualiza y cancela citas médicas
- **Catálogo de Médicos**: Busca y filtra médicos por especialidad
- **Persistencia Local**: Los datos se guardan en localStorage del navegador
- **Validaciones**: Validación de fechas, horarios y campos obligatorios

## 🚀 Tecnologías Utilizadas

- **Frontend**: HTML5, CSS3, JavaScript ES6+
- **Framework CSS**: Bootstrap 5.3.0
- **Iconos**: Font Awesome 6.0.0
- **Tipografía**: Google Fonts (Inter)
- **JavaScript Framework**: AngularJS 1.8.2 (para la página de citas)

## 📁 Estructura del Proyecto

```
frontend/
├── index.html          # Página principal
├── medicos.html        # Listado de médicos
├── citas.html          # Gestión de citas
├── about.html          # Información sobre el sistema
├── css/
│   └── style.css       # Estilos compartidos
└── js/
    ├── medicos.json    # Datos de médicos
    └── utils.js        # Utilidades JavaScript
```

## 🎨 Características de Diseño

### Paleta de Colores
- **Primario**: #2c5aa0 (Azul médico)
- **Secundario**: #48cae4 (Azul claro)
- **Éxito**: #06d6a0 (Verde)
- **Peligro**: #e63946 (Rojo)

### Efectos Visuales
- **Glassmorphism**: Transparencias con blur effects
- **Gradientes**: Colores suaves y modernos
- **Animaciones**: Hover effects y transiciones
- **Responsive**: Diseño adaptativo

## 🔧 Funcionalidades Principales

### 1. Página Principal (index.html)
- Hero section con información destacada
- Estadísticas del sistema
- Tarjetas de características
- Catálogo de especialidades médicas

### 2. Médicos (medicos.html)
- Listado de médicos con tarjetas interactivas
- Búsqueda por nombre o especialidad
- Filtrado por especialidad
- Información de horarios
- Botón directo para agendar cita

### 3. Citas (citas.html)
- Formulario de agendamiento
- Selección rápida de médicos
- Validación de fechas y horarios
- Lista de citas programadas
- Cancelación de citas

### 4. Validaciones Implementadas
- Fechas futuras únicamente
- Horarios de atención (9:00 AM - 6:00 PM)
- Campos obligatorios
- Duplicación de citas
- Formato de datos

## 🛠️ Instalación y Uso

1. **Clona o descarga el proyecto**
2. **Abre index.html en tu navegador**
3. **Navega por las diferentes secciones**

No requiere instalación de dependencias adicionales, funciona directamente en el navegador.

## 📱 Características Responsive

- **Desktop**: Diseño completo con todas las funcionalidades
- **Tablet**: Adaptación de columnas y espaciado
- **Mobile**: Navegación colapsible y diseño vertical

## 🔒 Persistencia de Datos

Los datos se almacenan localmente en el navegador usando localStorage:
- `misCitas`: Citas programadas por el usuario
- `medicos`: Información de médicos (cargada desde JSON)
- `preferenciasUsuario`: Configuraciones del usuario

## 🎯 Mejoras Futuras Sugeridas

- Integración con backend real
- Sistema de notificaciones push
- Autenticación de usuarios
- Historial médico
- Pagos en línea
- Videoconsultas
- Recordatorios por email/SMS

## 📞 Soporte

Para soporte técnico o consultas:
- Email: info@consultamedica.com
- Teléfono: +1 (555) 123-4567

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.

---

**Desarrollado con ❤️ para mejorar el acceso a servicios médicos**
