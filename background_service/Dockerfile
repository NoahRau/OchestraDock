# Build-Stage
FROM python:3.11-alpine AS builder

# Installiere Build-Abhängigkeiten
RUN apk add --no-cache gcc musl-dev python3-dev

# Setze Arbeitsverzeichnis
WORKDIR /app

# Kopiere nur die Abhängigkeitsdatei
COPY requirements.txt .

# Installiere Abhängigkeiten in einem speziellen Verzeichnis
RUN pip install --no-cache-dir --user -r requirements.txt

# Finale, schlanke Stage
FROM python:3.11-alpine

# Kopiere Python-Pakete vom Builder
COPY --from=builder /root/.local /root/.local

# Stelle sicher, dass die Binaries im PATH sind
ENV PATH=/root/.local/bin:$PATH

# Setze Arbeitsverzeichnis
WORKDIR /app

# Kopiere den Anwendungscode
COPY . .

# Setze Umgebungsvariablen
ENV UVICORN_HOST=0.0.0.0
ENV UVICORN_PORT=8000

# Exponiere Port
EXPOSE 8000

# Führe die Anwendung als nicht-Root-Benutzer aus
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Starte die Anwendung
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]