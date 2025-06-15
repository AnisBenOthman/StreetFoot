# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /discover-service

# Copy the built JAR file into the container
COPY target/*.jar discover-service.jar

# Expose the port the application runs on
EXPOSE 8761

# Run the JAR file
ENTRYPOINT ["java", "-jar", "discover-service.jar"]
