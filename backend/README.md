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
| Audit Service | 8087 | 3313 |
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
| AUDIT_INTERNAL_TOKEN | Shared secret services use to authenticate to audit-service | `openssl rand -hex 32` |
| AUDIT_RETENTION_DAYS | Days to keep audit events before cleanup | `90` |
| BRAND_NAME | Short brand name in toolbar | `AcmeSchool` |
| BRAND_PRODUCT_NAME | Long product name (title, footer) | `AcmeSchool LMS` |
| BRAND_PRIMARY_COLOR | CSS primary color (hex) | `#2563eb` |
| BRAND_LOGO_URL | Path/URL to logo image | `/branding/logo.png` |
| BRAND_FAVICON_URL | Path/URL to favicon | `/branding/favicon.ico` |
| BRAND_SUPPORT_EMAIL | Footer support contact | `support@acme.kz` |

## Customizing branding per customer

Each customer deployment is configured via env vars in `.env`. The defaults render
as "SmartEduControl" with the SEC indigo color. Override per customer:

```bash
BRAND_NAME=AcmeSchool
BRAND_PRODUCT_NAME="AcmeSchool LMS"
BRAND_PRIMARY_COLOR=#2563eb
BRAND_LOGO_URL=/branding/acme-logo.png
BRAND_FAVICON_URL=/branding/acme-favicon.ico
BRAND_SUPPORT_EMAIL=support@acme.kz
```

Logo and favicon files live on the host at `/srv/sec/branding/` and are mounted
read-only into both the LMS and CRM nginx containers (see `docker-compose.prod.yml`).
Drop the customer's `logo.png` and `favicon.ico` there before bringing the stack up.

## Audit log

Every write (POST/PUT/PATCH/DELETE) on every backend service is logged to the
`audit-service` (port 8087) and stored in `lms-audit` MySQL on port 3313.
Sensitive GET endpoints are logged via the `@Audited(sensitive=true)` annotation
in the controller (already applied on user/client/lead/payment/subscription endpoints).

- **Retention:** configurable via `AUDIT_RETENTION_DAYS` (default 90).
- **Cleanup:** daily job at 03:00 server-local time deletes events older than retention.
- **Admin UI:** `/admin-panel/audit-log` in the LMS (requires `ROLE_ADMIN`).
- **Internal auth:** services authenticate to audit-service via the shared
  `AUDIT_INTERNAL_TOKEN` env (generate a strong random value per deployment).
- **Failure mode:** if audit-service is unreachable, events are written to
  `/var/log/sec/audit-fallback.log` inside the calling container and the user
  request still succeeds.
- **PII redaction:** request bodies are redacted before persistence — fields
  named `password`, `*secret*`, `*token*`, `card_number` are replaced with `***`.

## Architecture

- **Spring Boot 2.7** with **Spring Cloud 2021.0.3**
- **Spring Cloud Gateway** — single entry point at port 8080
- **Eureka Service Discovery** — all services register themselves
- **Spring Cloud Config** — centralized configuration from `config-server/src/main/resources/config/`
- **MySQL 8** — separate database per service
- **Redis** — SSO authorization code storage
- Shared library: local Maven module `kz.sec.lms:shared-library:1.1.0` (in `backend/shared-library/`)
