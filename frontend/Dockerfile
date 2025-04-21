# Build-Stage mit Alpine-Basis für minimale Größe
FROM node:20-alpine AS build

# Setze Arbeitsverzeichnis
WORKDIR /app

# Kopiere nur package.json und package-lock.json, um Layercaching zu nutzen
COPY package*.json ./

# Installiere Abhängigkeiten mit Production-Flag, ohne npm-Cache
RUN npm ci --omit=dev --no-audit --no-fund && npm cache clean --force

# Kopiere den Quellcode
COPY . .

# Baue die Anwendung
RUN npm run build

# Finale, extrem schlanke nginx-Stage
FROM nginx:alpine-slim

# Kopiere nur die gebauten Dateien und eine angepasste nginx.conf
COPY --from=build /app/dist /usr/share/nginx/html

# Optimiere nginx für Produktion
RUN rm -rf /var/cache/apk/* && \
    rm -rf /usr/share/nginx/html/index.html.gz && \
    chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

# Führe nginx als nicht-Root-Benutzer aus
USER nginx

# Exponiere Port
EXPOSE 80

# Starte nginx im Vordergrund
CMD ["nginx", "-g", "daemon off;"]