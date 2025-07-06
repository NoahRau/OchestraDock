#!/bin/sh

# Replace environment variables in nginx config
envsubst '${VITE_BACKEND_URL}' < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf

# Start Nginx
exec nginx -g 'daemon off;'
