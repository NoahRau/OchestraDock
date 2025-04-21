# Build-Stage mit Maven
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Kopiere nur pom.xml und lade Abhängigkeiten herunter (für Layer-Caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Kopiere den Quellcode und baue das Projekt
COPY src ./src
RUN mvn clean package -DskipTests && \
    # Extrahiere die Abhängigkeiten für eine optimierte JRE
    jdeps --ignore-missing-deps \
    --print-module-deps \
    --multi-release 21 \
    --class-path "/app/target/dependency/*" \
    --module-path "/app/target/dependency/*" \
    target/ToDoAppCloudsync-0.0.1-SNAPSHOT.jar > jre-deps.info

# Runtime-Stage mit minimaler JRE
FROM eclipse-temurin:21-jre-alpine

# Erstelle nicht-Root-Benutzer
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Installiere nur notwendige Tools für Health-Checks
RUN apk add --no-cache curl && \
    rm -rf /var/cache/apk/*

# Setze Arbeitsverzeichnis
WORKDIR /app

# Kopiere nur die kompilierte JAR-Datei vom Builder
COPY --from=builder /app/target/ToDoAppCloudsync-0.0.1-SNAPSHOT.jar app.jar

# Wechsle zum nicht-Root-Benutzer
USER appuser

# Exponiere Port
EXPOSE 8080

# Starte die Anwendung mit optimierten JVM-Einstellungen
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]