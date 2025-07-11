# Spring Boot Hot-Reloading with Docker

This setup provides hot-reloading capabilities for Spring Boot development using Docker containers.

## Features

- **Hot-Reloading**: Automatic compilation and restart when source code changes
- **Dependency Caching**: Faster builds by caching Maven dependencies
- **Multi-Stage Build**: Separate development and production configurations
- **Security**: Production image uses non-root user
- **Optimized**: Smaller production image size

## Development Setup

### Prerequisites
- Docker and Docker Compose installed
- Java 17 (for local development)
- Maven 3.9+ (for local development)

### Running in Development Mode (with Hot-Reloading)

```bash
# Start all services in development mode
docker-compose up --build

# Or start only the backend service
docker-compose up --build backend
```

The application will be available at:
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health Check: http://localhost:8080/actuator/health

### Testing Hot-Reloading

1. Start the application with `docker-compose up --build`
2. Make changes to any Java file in `src/main/java/`
3. Save the file
4. The application will automatically recompile and restart (watch the logs)
5. Test your changes without rebuilding the container

### Development Features

- **Automatic Compilation**: File changes trigger automatic recompilation
- **Spring DevTools**: Enables fast application restarts
- **Live Reload**: Browser refresh on changes (if using LiveReload browser extension)
- **Volume Mounting**: Source code is mounted for real-time changes

## Production Setup

### Building for Production

```bash
# Build production image
docker build --target release -t mediconsultas-backend:latest ./backend

# Run production container
docker run -d -p 8080:8080 mediconsultas-backend:latest
```

### Production Docker Compose

```bash
# Start all services in production mode
docker-compose -f docker-compose.prod.yml up --build
```

### Production Features

- **Optimized Image**: Smaller runtime image using Eclipse Temurin JDK
- **Security**: Non-root user execution
- **No Dev Dependencies**: Only production dependencies included
- **JAR Execution**: Direct Java execution instead of Maven

## Docker Stages

The Dockerfile includes four stages:

1. **deps**: Caches Maven dependencies
2. **build**: Compiles and packages the application
3. **dev**: Development stage with hot-reloading
4. **release**: Production-ready optimized stage

## Configuration

### Environment Variables

**Development:**
- `SPRING_PROFILES_ACTIVE=dev`
- `SPRING_DEVTOOLS_RESTART_ENABLED=true`
- `SPRING_DEVTOOLS_LIVERELOAD_ENABLED=true`

**Production:**
- `SPRING_PROFILES_ACTIVE=prod`

### DevTools Configuration

The application.properties includes DevTools configuration:

```properties
# DevTools configuration for hot-reloading
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true
spring.devtools.restart.additional-paths=src/main/java,src/main/resources
spring.devtools.restart.exclude=static/**,public/**,resources/**
```

## Troubleshooting

### Common Issues

1. **Dependencies not cached**: 
   - Ensure the go-offline-maven-plugin is properly configured
   - Check that pom.xml changes trigger dependency rebuild

2. **Hot-reloading not working**:
   - Verify volume mounts in docker-compose.yml
   - Check that inotify-tools is installed in the container
   - Ensure DevTools is in the classpath

3. **Performance issues**:
   - Use .dockerignore to exclude unnecessary files
   - Ensure proper volume mounting strategy

### Logs

Monitor application logs:
```bash
docker-compose logs -f backend
```

### Manual Testing

Test the hot-reloading:
```bash
# Make a request to test endpoint
curl http://localhost:8080/api/hello

# Edit HelloController.java and save
# Watch the logs for recompilation
# Make the request again to see changes
```

## Best Practices

1. **Use .dockerignore** to exclude unnecessary files
2. **Separate development and production configurations**
3. **Use non-root user in production**
4. **Cache dependencies properly**
5. **Monitor resource usage during development**

## Performance Notes

- Initial startup may take longer due to dependency resolution
- Subsequent builds are faster due to dependency caching
- Hot-reloading is faster than container rebuilds
- Production images are optimized for size and security
