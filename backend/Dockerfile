# Stage 1: Dependencies cache
FROM maven:3.9.4-eclipse-temurin-17-alpine AS deps

WORKDIR /app
COPY pom.xml /app

RUN mvn go-offline:resolve-dependencies

# Stage 2: Build stage (for production)
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY --from=deps /root/.m2/repository /root/.m2/repository
COPY . /app

RUN mvn package -DskipTests -o

# Stage 3: Development stage (with hot-reloading)
FROM maven:3.9.4-eclipse-temurin-17-alpine AS dev

WORKDIR /app
COPY --from=deps /root/.m2/repository /root/.m2/repository

# Install bash for script execution
RUN apk add --no-cache bash

# Copy everything including the entrypoint script
COPY . /app

# Make the entrypoint script executable
RUN chmod +x /app/entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/app/entrypoint.sh"]

# Stage 4: Production stage (optimized runtime)
FROM eclipse-temurin:17.0.5_8-jdk-alpine AS release

LABEL maintainer="mediconsultas"
WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

# Create non-root user for security
RUN addgroup --system app && adduser -S -s /bin/false -G app app
RUN chown -R app:app /app

USER app
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
