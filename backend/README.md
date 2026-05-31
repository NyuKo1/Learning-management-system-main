# SEC LMS — Backend Microservices

**SmartEduControl** (SEC) Learning Management System — Backend

## Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `password` |
| Any seeded user | `{email}@sec.kz` | `password` |

> Example: `tgridley@sec.kz` / `password`

## Services

| Service | Port | Database Port |
|---------|------|---------------|
| API Gateway | 8080 | — |
| Auth Service | 8081 | 3307 |
| Faculty Service | 8082 | 3308 |
| Subject Service | 8083 | 3309 |
| Exam Service | 8084 | 3310 |
| CRM Service | 8085 | 3311 |
| Notify Service | 8086 | 3312 |
| Config Server | 8888 | — |
| Discovery Server | 8761 | — |
| Redis | — | 6379 |

## Quick Start (Docker)

```bash
# 1. Copy environment template
cp .env.example .env

# 2. Edit .env — set JWT_SECRET to a long random string
# Optional: set TELEGRAM_BOT_TOKEN if you want notifications

# 3. Start everything
docker-compose -f docker-compose.prod.yml up -d

# 4. Wait ~60 seconds for all services to start
# 5. Access: LMS http://localhost:4200 | CRM http://localhost:4201
```

## Local Development (Databases Only)

```bash
docker-compose up -d
# All databases start on ports 3307-3312, Redis on 6379
# Run each Spring Boot service locally via IDE or ./mvnw spring-boot:run
```

## Seed Data

The system auto-seeds default users via `AppStartupRunner`. For full seed data (106 users, faculty, students), import the SQL files manually:

```bash
mysql -h 127.0.0.1 -P 3307 -u root -proot lms-auth < backend/auth-service/src/main/resources/database/lms-auth.sql
mysql -h 127.0.0.1 -P 3308 -u root -proot lms-faculty < backend/faculty-service/src/main/resources/database/lms-faculty.sql
mysql -h 127.0.0.1 -P 3309 -u root -proot lms-subject < backend/subject-service/src/main/resources/database/lms-subject.sql
mysql -h 127.0.0.1 -P 3310 -u root -proot lms-exam < backend/exam-service/src/main/resources/database/lms-exam.sql
```

## Environment Variables (.env)

| Variable | Description | Example |
|----------|-------------|---------|
| JWT_SECRET | Signing secret for all JWT tokens | `a-very-long-random-string-at-least-32-chars` |
| JWT_ACCESS_EXPIRATION | Access token TTL in seconds | `7200` |
| JWT_REFRESH_EXPIRATION | Refresh token TTL in seconds | `86400` |
| TELEGRAM_BOT_TOKEN | Telegram bot API token (optional) | `123456789:ABC...` |
| TELEGRAM_BOT_USERNAME | Bot username without @ (optional) | `my_notify_bot` |
| MYSQL_ROOT_PASSWORD | MySQL root password | `strongpassword` |
| SSO_ALLOWED_ORIGINS | Comma-separated allowed origins | `http://localhost:4200,http://localhost:4201` |

## Architecture

- **Spring Boot 2.7** with **Spring Cloud 2021.0.3**
- **Spring Cloud Gateway** — single entry point at port 8080
- **Eureka Service Discovery** — all services register themselves
- **Spring Cloud Config** — centralized configuration from `config-server/src/main/resources/config/`
- **MySQL 8** — separate database per service
- **Redis** — SSO authorization code storage
- Shared library: local Maven module `kz.sec.lms:shared-library:1.0.0` (in `backend/shared-library/`)
