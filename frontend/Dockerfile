# Use an official Node.js image to build the React app
FROM node:16 as build
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of your React source code and build
COPY . .
RUN npm run build

# Now use an Nginx container to serve the files
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]
