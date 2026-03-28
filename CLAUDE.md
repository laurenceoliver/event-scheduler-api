# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build
./mvnw clean package

# Build (skip tests)
./mvnw clean package -DskipTests

# Run locally
./mvnw spring-boot:run

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=AdacaApplicationTests

# Docker
docker-compose up -d
```

The app runs on **port 8080**. H2 console is available at `/h2-console` (datasource: `jdbc:h2:mem:sampledb`, user: `sa`, no password).

## Architecture

Spring Boot 3.5.5 / Java 21 REST API for project task scheduling. Standard layered architecture:

**Controllers** (`/api/tasks/{projectId}`, `/api/projects`, `/api/developers`) → **Services** → **JPA Repositories** → **H2 (dev) / PostgreSQL (prod)**

### Domain Model

- **Project** ← has many **Tasks** ← each assigned to a **Developer**
- Task has: name, priority, due_date, status (1=PENDING, 2=IN_PROGRESS, 3=COMPLETED)
- Unique constraint on (task name, project_id) — violations return 409 via `GlobalExceptionHandler`

### Key Design Decisions

- **Async email**: `EmailService` sends notifications asynchronously via a `ThreadPoolTaskExecutor` (core: 5, max: 10). Currently logs only — no SMTP configured.
- **CORS**: Configured in `WebConfig` for `http://localhost:3000` (React frontend).
- **Seed data**: `data.sql` runs on every startup (`sql.init.mode: always`), loading 2 projects, 5 developers, and 50 sample tasks.
- **TaskDTO**: Uses a hardcoded `testId` field (default 104) — carries task data between layers.
- **Pagination**: Task queries support Spring Data pagination with date range filtering.

## CI/CD

GitHub Actions (`.github/workflows/ci.yml`): pushes to `master` build a JAR, create a Docker image, and deploy to a GCP VM via SSH using docker-compose.
