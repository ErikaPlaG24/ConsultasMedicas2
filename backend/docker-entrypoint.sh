#!/bin/sh

echo "Starting Spring Boot application with hot-reloading..."

# Background process to watch for file changes and recompile
while inotifywait -r -e modify /app/src/main/; 
do 
  echo "Source code changed, recompiling..."
  mvn compile -o -DskipTests; 
done >/dev/null 2>&1 &

# Start the Spring Boot application
echo "Starting Spring Boot application..."
mvn spring-boot:run
