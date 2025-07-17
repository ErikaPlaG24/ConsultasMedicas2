from flask import Flask, request, jsonify
from flask_cors import CORS
import logging
import sys
from datetime import datetime

# Configurar logging personalizado
class CustomFormatter(logging.Formatter):
    def format(self, record):
        if record.levelno == logging.INFO and 'CONSULTA_MEDICA' in record.getMessage():
            return record.getMessage()
        return super().format(record)

# Configurar el logger
logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)

# Crear handler para consola
console_handler = logging.StreamHandler(sys.stdout)
console_handler.setLevel(logging.INFO)
console_handler.setFormatter(CustomFormatter())

# Agregar handler al logger
logger.addHandler(console_handler)

# Configurar logging básico para otros componentes
logging.basicConfig(level=logging.INFO, format='%(levelname)s:%(name)s:%(message)s')

# Crear aplicación Flask
app = Flask(__name__)

# Configurar CORS para permitir peticiones desde el frontend
CORS(app, origins=["http://localhost:80", "http://localhost:3000", "*"])

# Configuración de la aplicación
app.config['DEBUG'] = True

@app.route('/', methods=['GET'])
def home():
    """Endpoint de prueba para verificar que la API funciona"""
    return jsonify({
        'message': 'API de Consultas Médicas funcionando correctamente',
        'timestamp': datetime.now().isoformat(),
        'version': '1.0.0'
    })

@app.route('/contacto', methods=['POST'])
def contacto():
    """
    Endpoint POST /contacto
    Recibe datos de contacto y los muestra en consola
    """
    try:
        # Obtener datos JSON del request
        data = request.get_json()
        
        # Validar que se recibieron datos
        if not data:
            logger.warning("No se recibieron datos en el request")
            return jsonify({
                'error': 'No se recibieron datos',
                'success': False
            }), 400
        
        # Mostrar datos en consola con formato bonito
        timestamp = datetime.now().isoformat()
        
        # Extraer campos específicos si existen
        nombre = data.get('nombre', 'No especificado')
        email = data.get('email', 'No especificado')
        telefono = data.get('telefono', 'No especificado')
        mensaje = data.get('mensaje', 'No especificado')
        
        # Crear mensaje formateado
        mensaje_bonito = f"""
CONSULTA_MEDICA
{"="*80}
🏥 NUEVA CONSULTA MÉDICA RECIBIDA 🏥
{"="*80}
📅 Fecha y Hora: {timestamp}
👤 Nombre:       {nombre}
📧 Email:        {email}
📞 Teléfono:     {telefono}
📝 Mensaje:
   {mensaje}

{"-"*50}
📋 DATOS COMPLETOS RECIBIDOS:"""

        # Agregar todos los campos recibidos
        for key, value in data.items():
            mensaje_bonito += f"\n   {key}: {value}"
        
        mensaje_bonito += f"""
{"="*80}
✅ DATOS PROCESADOS CORRECTAMENTE
{"="*80}
"""
        
        # Imprimir usando el logger personalizado
        logger.info(mensaje_bonito)
        
        # Log adicional para tracking
        logger.info(f"Contacto procesado - Nombre: {nombre}, Email: {email}")
        
        # Respuesta exitosa
        return jsonify({
            'message': 'Datos de contacto recibidos correctamente',
            'success': True,
            'data_received': data,
            'timestamp': datetime.now().isoformat()
        }), 200
        
    except Exception as e:
        logger.error(f"Error procesando contacto: {str(e)}")
        return jsonify({
            'error': 'Error interno del servidor',
            'success': False,
            'details': str(e)
        }), 500

@app.route('/health', methods=['GET'])
def health_check():
    """Endpoint de verificación de salud de la API"""
    return jsonify({
        'status': 'healthy',
        'timestamp': datetime.now().isoformat(),
        'service': 'API Consultas Médicas'
    })

# Manejo de errores
@app.errorhandler(404)
def not_found(error):
    return jsonify({
        'error': 'Endpoint no encontrado',
        'success': False
    }), 404

@app.errorhandler(500)
def internal_error(error):
    return jsonify({
        'error': 'Error interno del servidor',
        'success': False
    }), 500

if __name__ == '__main__':
    # Mensaje de inicio bonito
    inicio_msg = f"""
CONSULTA_MEDICA
{"="*80}
🚀 INICIANDO API DE CONSULTAS MÉDICAS 🚀
{"="*80}
📍 Servidor:     http://0.0.0.0:5000
🌐 Endpoints disponibles:
   • GET  /          - Página de inicio
   • POST /contacto  - Recibir datos de contacto
   • GET  /health    - Verificación de salud
{"="*80}
✅ SERVIDOR LISTO PARA RECIBIR CONSULTAS
{"="*80}
"""
    logger.info(inicio_msg)
    
    # Ejecutar la aplicación
    app.run(
        host='0.0.0.0',  # Permitir conexiones desde cualquier IP
        port=5000,       # Puerto estándar de Flask
        debug=True       # Modo desarrollo
    )
