#!/bin/bash

# Load environment variables from .env file
if [ -f .env ]; then
    echo "Loading environment variables from .env file..."
    set -a
    source .env
    set +a
    echo "Environment variables loaded successfully!"
else
    echo "Warning: .env file not found!"
    exit 1
fi

# Run the Spring Boot application
echo "Starting Spring Boot application..."
mvn spring-boot:run
