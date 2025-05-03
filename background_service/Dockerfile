# Build-Stage
FROM python:3.11-alpine AS builder

# Installiere Build-Abhängigkeiten
RUN apk add --no-cache gcc musl-dev python3-dev

# Setze Arbeitsverzeichnis
WORKDIR /app

# Kopiere nur die Abhängigkeitsdatei
COPY requirements.txt .

# Installiere Abhängigkeiten in einem speziellen Verzeichnis
RUN pip install --no-cache-dir --prefix=/install -r requirements.txt

# Finale, schlanke Stage
FROM python:3.11-alpine

# Erstelle nicht-Root-Benutzer
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Kopiere Python-Pakete vom Builder
COPY --from=builder /install /usr/local

# Setze Arbeitsverzeichnis
WORKDIR /app

# Kopiere den Anwendungscode
COPY . .

# Ändere die Besitzrechte der Dateien auf den nicht-Root-Benutzer
RUN chown -R appuser:appgroup /usr/local /app

# Wechsle zum nicht-Root-Benutzer
USER appuser

# Setze Umgebungsvariablen
ENV UVICORN_HOST=0.0.0.0
ENV UVICORN_PORT=8000

# Exponiere Port
EXPOSE 8000

# Starte die Anwendung
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]