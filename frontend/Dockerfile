# --- Stage 1: Build the React Application ---
FROM node:20-alpine AS build
WORKDIR /app

# Copy package descriptors and install dependencies
COPY package*.json ./
RUN npm ci

# Copy the rest of your app and compile it via Vite
COPY . .
RUN npm run build

# --- Stage 2: Serve the Static Files via Nginx ---
FROM nginx:alpine

# Copy the compiled HTML/CSS/JS exactly replacing Nginx's default content
COPY --from=build /app/dist /usr/share/nginx/html

# Replace Nginx default configuration mapped with our proxy settings
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
