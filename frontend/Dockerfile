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

# Installiere envsubst
RUN apk add --no-cache gettext

# Kopiere nur die gebauten Dateien
COPY --from=build /app/dist /usr/share/nginx/html

# Kopiere die Nginx-Konfigurationsvorlage und das Entrypoint-Skript
COPY nginx.conf.template /etc/nginx/nginx.conf.template
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh

# Setze Berechtigungen für das Entrypoint-Skript
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

# Erstelle notwendige Verzeichnisse und setze die richtigen Berechtigungen
RUN mkdir -p /var/cache/nginx /var/run /run && \
    chown -R nginx:nginx /var/cache/nginx /var/run /run /usr/share/nginx/html

# Switch to root to allow writing to /etc/nginx
USER root

# Exponiere Port
EXPOSE 80

# Starte nginx über das Entrypoint-Skript
ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]