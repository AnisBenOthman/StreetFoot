# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /scheduling-service

# Copy the built JAR file into the container
COPY target/*.jar scheduling-service.jar

# Expose the port the application runs on
EXPOSE 8083

# Run the JAR file
ENTRYPOINT ["java", "-jar", "scheduling-service.jar"]
