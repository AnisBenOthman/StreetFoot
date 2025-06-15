# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /gateway

# Copy the built JAR file into the container
COPY target/*.jar gateway.jar

# Expose the port the application runs on
EXPOSE 8084

# Run the JAR file
ENTRYPOINT ["java", "-jar", "gateway.jar"]
