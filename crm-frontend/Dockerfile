FROM node:18-alpine AS build

RUN apk add --no-cache python3 make g++

WORKDIR /app

COPY package*.json ./
RUN npm install --legacy-peer-deps

COPY ./ ./

ENV NODE_OPTIONS="--max_old_space_size=4096"
RUN npm run build -- --configuration production


FROM nginx:1.25-alpine

COPY .nginx/default.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist/sec-crm /usr/share/nginx/html

EXPOSE 80
