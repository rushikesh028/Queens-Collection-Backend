# Queen's Collection – Backend API

Production-ready REST API for the **Queen's Collection** Fashion Magazine website.

Built with **Java 25 (LTS)**, **Spring Boot 3.5**, **MySQL 8**, **Redis**, and **JWT**.

---

## Quick Start

### Prerequisites
- Java 25+
- Maven 3.9+
- Docker & Docker Compose (recommended)

### 1. Start infrastructure

```bash
docker compose up -d mysql redis
```

### 2. Run the application

```bash
mvn spring-boot:run
```

API base URL: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

### 3. Full stack with Docker

```bash
docker compose up --build
```

---

## Default Credentials

| Role   | Email                        | Password   |
|--------|------------------------------|------------|
| Admin  | admin@queenscollection.com   | Admin@123  |
| Editor | editor@queenscollection.com  | Editor@123 |

---

## API Overview

### Public Endpoints (no auth)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/hero` | Active hero section |
| GET | `/api/v1/articles` | Published articles (paginated) |
| GET | `/api/v1/articles/featured` | Featured stories |
| GET | `/api/v1/articles/latest` | Latest 5 articles |
| GET | `/api/v1/articles/{slug}` | Article by slug |
| GET | `/api/v1/categories` | All categories |
| GET | `/api/v1/products` | Product catalog |
| GET | `/api/v1/products/featured` | Featured products |
| GET | `/api/v1/lookbooks` | Active lookbooks |
| GET | `/api/v1/instagram` | Instagram feed |
| POST | `/api/v1/newsletter/subscribe` | Subscribe to newsletter |
| POST | `/api/v1/auth/register` | Register |
| POST | `/api/v1/auth/login` | Login |

### Authenticated / Role-protected

- `ROLE_USER` – favorites, profile
- `ROLE_EDITOR` – create/update articles & lookbooks
- `ROLE_ADMIN` – full management (users, products, categories, hero, instagram, newsletter)

---

## Architecture

```
com.queenscollection
├── config          # Security, Redis, CORS, OpenAPI, Data seeding
├── controller      # REST controllers
├── service         # Business logic
├── repository      # Spring Data JPA
├── entity          # JPA entities
├── dto             # Request / Response DTOs
├── security        # JWT filter, services, handlers
├── exception       # Global exception handling
└── enums           # Role, ArticleStatus, ProductAvailability
```

### Key Features
- JWT access + refresh token authentication
- Role-based authorization (`ADMIN`, `EDITOR`, `USER`)
- Redis caching for public content
- Bean Validation on all requests
- Pagination & sorting
- Global exception handling with structured error responses
- OpenAPI 3 / Swagger UI
- Spring Boot Actuator health endpoints
- Docker Compose for MySQL + Redis + App

---

## Frontend Integration Notes

Your existing frontend sections map to these endpoints:

| Frontend Section       | API Endpoint                          |
|------------------------|---------------------------------------|
| Hero                   | `GET /api/v1/hero`                    |
| Featured Stories       | `GET /api/v1/articles/featured`       |
| Shop by Category       | `GET /api/v1/categories` + products   |
| Lookbook               | `GET /api/v1/lookbooks`               |
| Instagram Feed         | `GET /api/v1/instagram`               |
| Newsletter form        | `POST /api/v1/newsletter/subscribe`   |

Example newsletter call from your form:

```javascript
fetch('http://localhost:8080/api/v1/newsletter/subscribe', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email: 'user@example.com' })
});
```

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `JWT_SECRET` | (see application.yml) | HMAC secret for JWT |
| `SPRING_DATASOURCE_URL` | localhost MySQL | JDBC URL |
| `SPRING_DATA_REDIS_HOST` | localhost | Redis host |

---

## Testing

```bash
mvn test
```

---

## License

MIT License

Copyright (c) 2026 Rushikesh Asawale
