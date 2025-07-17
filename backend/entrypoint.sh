#!/bin/bash
echo "Starting Spring Boot application..."
echo "Waiting for database to be ready..."
sleep 15
echo "Starting application..."
exec mvn spring-boot:run
