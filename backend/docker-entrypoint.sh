#!/bin/bash

echo "Starting Spring Boot application with hot-reloading..."

# Wait for dependencies to be available
echo "Waiting for database to be ready..."
sleep 10

# Background process to watch for file changes and recompile
echo "Setting up file watcher..."
while inotifywait -r -e modify /app/src/main/ 2>/dev/null; 
do 
  echo "Source code changed, recompiling..."
  mvn compile -o -DskipTests 2>/dev/null; 
done &

# Start the Spring Boot application
echo "Starting Spring Boot application..."
exec mvn spring-boot:run
