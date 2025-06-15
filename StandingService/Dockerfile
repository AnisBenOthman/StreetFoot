# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /standing-service

# Copy the built JAR file into the container
COPY target/*.jar standing-service.jar

# Expose the port the application runs on
EXPOSE 8085

# Run the JAR file
ENTRYPOINT ["java", "-jar", "standing-service.jar"]
