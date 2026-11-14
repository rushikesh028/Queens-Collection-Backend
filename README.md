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

### Prerequisites

Install the following tools:

- Java Development Kit (JDK)
- Apache Maven
- MySQL Server
- Redis Server
- Git
- Visual Studio Code or IntelliJ IDEA

Docker can also be used for containerized services where configured.

### 1. Clone the Repository

```bash
git clone https://github.com/rushikesh028/Queens-Collection-Backend.git
```

Navigate into the project:

```bash
cd Queens-Collection-Backend
```

### 2. Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

Configure the database and Redis connection details in the application configuration.

Make sure the required database and Redis services are running.

Build the project:

```bash
mvn clean install
```

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend is configured to use port `8080` by default, unless changed in the application configuration.

### 3. Frontend Setup

Navigate to the frontend directory:

```bash
cd frontend
```

Open `index.html` in a browser or run it using a local development server.

Configure the frontend API base URL in the JavaScript code to point to the running Spring Boot backend.

---

## Backend Architecture

The backend follows a layered architecture to separate responsibilities and improve maintainability.

| Layer | Responsibility |
|---|---|
| Controller | Handles HTTP requests and API responses |
| Service | Implements business logic |
| Repository | Handles database operations |
| Entity | Represents database models |
| DTO | Transfers structured request and response data |
| Security | Handles authentication and authorization |
| Config | Manages application configurations |
| Exception | Handles application-level exceptions |

---

## Security

The backend uses Spring Security and JWT to secure application endpoints.

Security-related capabilities include:

- User authentication.
- Token-based authorization.
- Role-based access control.
- Protected API endpoints.
- Custom authentication filters.
- Access-denied handling.

**Security Note:** Never commit database passwords, JWT secrets, API keys, or other sensitive configuration values to GitHub. Use environment variables or an appropriate secrets-management solution.

---

## API Documentation

The backend includes OpenAPI configuration for API documentation.

Once the application is running, check the configured Swagger UI endpoint:

```text
http://localhost:8080/swagger-ui/index.html
```

The exact endpoint may depend on the application's OpenAPI configuration.

---

## Future Improvements

Potential areas for further development include:

- Enhanced product discovery and filtering.
- Advanced editorial content management.
- Improved caching strategies using Redis.
- Automated backend testing.
- CI/CD pipeline integration.
- Production deployment and monitoring.
- Enhanced frontend-backend integration.

---

## Developer

**Rushikesh Asawale**

Java Backend Developer | Full Stack Developer

GitHub: [@rushikesh028](https://github.com/rushikesh028)

---

## License

This project is licensed under the MIT License.

Copyright (c) 2026 Rushikesh Asawale.

See the [LICENSE](LICENSE) file for details.

---

**Queen's Collection — Fashion, Creativity & Technology.**
