# Queen's Collection

### Fashion Editorial & Creative Platform

Queen's Collection is a full-stack fashion editorial platform designed to deliver a modern digital fashion experience. The application combines an interactive frontend with a Java Spring Boot backend, providing RESTful APIs, authentication, content management, and data persistence.

The project follows a layered backend architecture using Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate, MySQL, and Redis.

---

## Project Overview

The platform is designed to manage fashion-related content, editorial articles, products, lookbooks, categories, and user interactions through a centralized backend.

### Key Features

- **User Authentication:** Secure registration and login using JWT-based authentication.
- **Role-Based Access Control:** Access management using Spring Security.
- **Article Management:** Create, retrieve, update, and manage editorial articles.
- **Product Management:** Manage fashion products and their availability.
- **Category Management:** Organize content and products into categories.
- **Lookbook Management:** Manage fashion lookbooks and associated images.
- **Hero Section Management:** Manage homepage hero content.
- **Newsletter Subscription:** Handle newsletter subscriber registrations.
- **Instagram Content:** Manage Instagram-related content.
- **Admin Management:** Administrative operations and content management.
- **RESTful APIs:** Structured communication between frontend and backend.

---

## Technology Stack

### Frontend

| Technology | Purpose |
|---|---|
| HTML5 | Application structure |
| CSS3 | Styling and responsive design |
| JavaScript | Client-side functionality |
| Fetch API | Backend API communication |

### Backend

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Spring Boot | Backend application framework |
| Spring Security | Authentication and authorization |
| JWT | Token-based authentication |
| Spring Data JPA | Data access and persistence |
| Hibernate | ORM implementation |
| MySQL | Relational database |
| Redis | Caching and in-memory data storage |
| Maven | Dependency management and build automation |
| Springdoc OpenAPI | API documentation |

### Development & Deployment Tools

- Git and GitHub
- Docker
- Docker Compose
- Postman
- Maven

---

## Project Architecture

The application is organized into two primary modules.

```text
Queens-Collection/
│
├── frontend/
│   ├── index.html
│   ├── css/
│   │   └── style.css
│   ├── js/
│   │   └── main.js
│   ├── README.md
│   ├── CHANGELOG.md
│   └── UPDATE_LOG.md
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/queenscollection/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── dto/
│   │   │   │       ├── entity/
│   │   │   │       ├── enums/
│   │   │   │       ├── exception/
│   │   │   │       ├── repository/
│   │   │   │       ├── security/
│   │   │   │       └── service/
│   │   │   └── resources/
│   │   └── test/
│   ├── docker/
│   ├── pom.xml
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── README.md
│
├── .github/
├── .gitignore
├── LICENSE
└── README.md
```

---

## Getting Started

Follow these instructions to set up the project locally.

### Pr
