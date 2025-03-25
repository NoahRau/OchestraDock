FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy only pom.xml first and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the source code and build the project
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
RUN apt-get update -y \
    && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*
COPY --from=builder /app/target/ToDoAppCloudsync-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
