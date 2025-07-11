# API de Consultas Médicas

## Descripción
API REST desarrollada con Flask para el sistema de consultas médicas.

## Estructura del Proyecto
```
api/
├── app.py              # Aplicación principal Flask
├── requirements.txt    # Dependencias Python
├── Dockerfile         # Configuración Docker
└── README.md          # Documentación
```

## Endpoints Disponibles

### 1. **GET /** 
- **Descripción**: Página de inicio/prueba
- **Respuesta**: Información básica de la API

### 2. **POST /contacto**
- **Descripción**: Recibe datos de contacto y los muestra en consola
- **Cuerpo**: JSON con datos de contacto
- **Ejemplo**:
```json
{
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "telefono": "123-456-7890",
    "mensaje": "Solicito información sobre consultas"
}
```

### 3. **GET /health**
- **Descripción**: Verificación de salud de la API
- **Respuesta**: Status de la aplicación

## Características Implementadas

### ✅ **Soporte CORS**
- Configurado con Flask-CORS
- Permite peticiones desde el frontend (puerto 80)
- Soporte para desarrollo local

### ✅ **Logging**
- Logs estructurados en consola
- Información detallada de requests
- Manejo de errores

### ✅ **Manejo de Errores**
- Respuestas JSON estandarizadas
- Códigos de estado HTTP apropiados
- Logging de errores para debugging

### ✅ **Validación de Datos**
- Validación de JSON en requests
- Mensajes de error descriptivos
- Estructura de respuesta consistente

## Instalación Local

### 1. Instalar dependencias:
```bash
pip install -r requirements.txt
```

### 2. Ejecutar la aplicación:
```bash
python app.py
```

### 3. La API estará disponible en:
- http://localhost:5000

## Uso con Docker

La API está configurada para ejecutarse en contenedores Docker como parte del stack completo del proyecto.

## Ejemplo de Uso

### Petición POST a /contacto:
```bash
curl -X POST http://localhost:5000/contacto \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Ana García",
    "email": "ana@example.com",
    "telefono": "987-654-3210",
    "mensaje": "Consulta sobre disponibilidad"
  }'
```

### Respuesta:
```json
{
    "message": "Datos de contacto recibidos correctamente",
    "success": true,
    "data_received": {
        "nombre": "Ana García",
        "email": "ana@example.com",
        "telefono": "987-654-3210",
        "mensaje": "Consulta sobre disponibilidad"
    },
    "timestamp": "2025-07-09T10:30:00"
}
```

## Logs en Consola
Los datos recibidos se muestran en la consola con este formato:
```
INFO:__main__:=== DATOS DE CONTACTO RECIBIDOS ===
INFO:__main__:Timestamp: 2025-07-09T10:30:00
INFO:__main__:Datos completos: {'nombre': 'Ana García', 'email': 'ana@example.com', ...}
INFO:__main__:Nombre: Ana García
INFO:__main__:Email: ana@example.com
INFO:__main__:Teléfono: 987-654-3210
INFO:__main__:Mensaje: Consulta sobre disponibilidad
INFO:__main__:================================
```
