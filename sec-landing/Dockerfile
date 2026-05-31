FROM nginx:1.25-alpine

COPY . /usr/share/nginx/html

RUN printf 'server {\n\
    listen 80;\n\
    server_name localhost;\n\
\n\
    location / {\n\
        root /usr/share/nginx/html;\n\
        index index.html;\n\
        try_files $uri $uri/ /index.html;\n\
    }\n\
\n\
    # API gateway\n\
    location /api/ {\n\
        proxy_pass http://backend-api-gateway:8080/;\n\
        proxy_set_header Host              $host;\n\
        proxy_set_header X-Real-IP         $remote_addr;\n\
        proxy_set_header X-Forwarded-For   $proxy_add_x_forwarded_for;\n\
        proxy_set_header X-Forwarded-Proto $scheme;\n\
    }\n\
\n\
    # LMS frontend\n\
    location /lms/ {\n\
        proxy_pass http://frontend-lms:80/;\n\
        proxy_set_header Host              $host;\n\
        proxy_set_header X-Real-IP         $remote_addr;\n\
        proxy_set_header X-Forwarded-For   $proxy_add_x_forwarded_for;\n\
    }\n\
\n\
    # CRM frontend\n\
    location /crm/ {\n\
        proxy_pass http://frontend-crm:80/;\n\
        proxy_set_header Host              $host;\n\
        proxy_set_header X-Real-IP         $remote_addr;\n\
        proxy_set_header X-Forwarded-For   $proxy_add_x_forwarded_for;\n\
    }\n\
}\n' > /etc/nginx/conf.d/default.conf

EXPOSE 80
