# Use a Java base image
FROM openjdk:17-jdk-slim

# Copy your Spring Boot jar (assuming you build it as "app.jar")
COPY target/ToDoAppCloudsync-0.0.1-SNAPSHOT.jar /ToDoAppCloudsync-0.0.1-SNAPSHOT.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/ToDoAppCloudsync-0.0.1-SNAPSHOT.jar"]
