from flask import Flask, request, jsonify
from flask_cors import CORS
import logging
from datetime import datetime

# Configurar logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

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
        
        # Mostrar datos en consola
        logger.info("=== DATOS DE CONTACTO RECIBIDOS ===")
        logger.info(f"Timestamp: {datetime.now().isoformat()}")
        logger.info(f"Datos completos: {data}")
        
        # Extraer campos específicos si existen
        nombre = data.get('nombre', 'No especificado')
        email = data.get('email', 'No especificado')
        telefono = data.get('telefono', 'No especificado')
        mensaje = data.get('mensaje', 'No especificado')
        
        logger.info(f"Nombre: {nombre}")
        logger.info(f"Email: {email}")
        logger.info(f"Teléfono: {telefono}")
        logger.info(f"Mensaje: {mensaje}")
        logger.info("================================")
        
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
    logger.info("Iniciando API de Consultas Médicas...")
    logger.info("Endpoints disponibles:")
    logger.info("- GET  /          - Página de inicio")
    logger.info("- POST /contacto  - Recibir datos de contacto")
    logger.info("- GET  /health    - Verificación de salud")
    
    # Ejecutar la aplicación
    app.run(
        host='0.0.0.0',  # Permitir conexiones desde cualquier IP
        port=5000,       # Puerto estándar de Flask
        debug=True       # Modo desarrollo
    )
