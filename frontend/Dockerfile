# Build-Stage mit Alpine-Basis für minimale Größe
FROM node:20-alpine AS build

# Setze Arbeitsverzeichnis
WORKDIR /app

# Kopiere nur package.json und package-lock.json, um Layercaching zu nutzen
COPY package*.json ./

# Installiere Abhängigkeiten mit Production-Flag, ohne npm-Cache
RUN npm ci --no-audit --no-fund && npm cache clean --force

# Kopiere den Quellcode
COPY . .

# Baue die Anwendung
RUN npm run build

# Finale, extrem schlanke nginx-Stage
FROM nginx:alpine-slim

# Kopiere nur die gebauten Dateien
COPY --from=build /app/dist /usr/share/nginx/html

# Erstelle notwendige Verzeichnisse und setze die richtigen Berechtigungen
RUN mkdir -p /var/cache/nginx /var/run /run && \
    chown -R nginx:nginx /var/cache/nginx /var/run /run /usr/share/nginx/html

# Führe nginx als nicht-Root-Benutzer aus
USER nginx

# Exponiere Port
EXPOSE 80

# Starte nginx im Vordergrund
CMD ["nginx", "-g", "daemon off;"]