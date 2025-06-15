# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /tournament-service

# Copy the built JAR file into the container
COPY target/*.jar tournament-service.jar

# Expose the port the application runs on
EXPOSE 8082

# Run the JAR file
ENTRYPOINT ["java", "-jar", "tournament-service.jar"]
