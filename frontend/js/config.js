// config.js - Configuración global para la aplicación
(function() {
    'use strict';
    
    // Detectar el entorno automáticamente
    const detectEnvironment = () => {
        const hostname = window.location.hostname;
        const port = window.location.port;
        
        console.log(`Aplicación ejecutándose en: ${hostname}:${port}`);
        
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
    };
    
    // Configuración global
    window.APP_CONFIG = {
        API_BASE_URL: detectEnvironment(),
        DEFAULT_HEADERS: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    };
    
    console.log('Configuración cargada:', window.APP_CONFIG);
})();
