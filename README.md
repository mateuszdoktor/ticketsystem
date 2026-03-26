<div align="center">

# Ticket System API

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?style=flat&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)](https://www.docker.com/)

*A resilient REST API for comprehensive issue tracking, user management, and seamless collaboration.*

</div>

## About The Project

This repository contains a RESTful backend service built to manage the complete lifecycle of support requests. By exposing a structured API, the system enables users to create tickets, dynamically reassign them, update their resolution statuses, and communicate transparently through threaded comments.

The project demonstrates a robust, enterprise-grade architecture using **Spring Boot 4.0**. It heavily emphasizes domain-driven design principles, comprehensive role-based access control, and automated testing to ensure reliability and security.

### Key Highlights
* **Secure Architecture:** Built on Spring Security and OAuth2 Resource Server to ensure rigorous, JWT-based layered protection across all endpoints.
* **Intelligent Querying:** Uses JPA Specifications to enable dynamic, advanced filtering (by status, priority, timeframe, and assignee) without boilerplate query code.
* **Granular Audit Trail:** Automated auditing seamlessly tracks creation and modification timestamps (`@EntityListeners`) across the domain entities.
* **Reliable Persistence:** PostgreSQL database seamlessly managed by Flyway migrations to ensure structural consistency alongside the codebase.

---

## API Reference

The service exposes a clean RESTful interface for managing tickets, users, and conversations. Full API documentation is available via **Swagger/OpenAPI** after application startup.

### Core Endpoints

| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/auth/login` | Authenticate and obtain a JWT access token. |
| `GET` | `/api/tickets` | View tickets (with advanced filtering). |
| `POST` | `/api/tickets` | Create a new support ticket. |
| `PATCH` | `/api/tickets/{id}/status` | Update the lifecycle status of a ticket. |
| `PATCH` | `/api/tickets/{id}/assignee` | Reassign the ticket to another support agent. |
| `POST` | `/api/tickets/{id}/comments` | Append a new comment to a specific ticket. |
| `GET` | `/api/users` | Retrieve a list of registered users. |

<details>
<summary><b>Click to see Swagger UI information</b></summary>
<br>

Once the application is running, the interactive API documentation is automatically generated.
You can explore all endpoints, request/response schemas, and test them directly in your browser at: 
`http://localhost:8080/swagger-ui.html`

</details>

---

## Tech Stack

* **Core:** Java 25, Spring Boot 4.0
* **Data Layer:** PostgreSQL 16, Flyway, Spring Data JPA
* **Security:** Spring Security, OAuth2 Resource Server
* **Utilities:** MapStruct (DTO mapping), Lombok
* **Testing:** JUnit 5, Mockito, Spring Boot Test, H2 (isolated tests)
* **DevOps:** Docker, Docker Compose, Maven Wrapper

---

## Project Structure

```text
ticketsystem/
├── src/main/java/com/example/ticketsystem/
│   ├── controller/      # REST API endpoints (Tickets, Users, Auth)
│   ├── dto/             # Request/Response models mapped via MapStruct
│   ├── entity/          # JPA Domain Entities natively audited
│   ├── exceptions/      # Centralized error mapping logic (RFC 7807)
│   ├── repository/      # JPA Repositories and dynamic specifications
│   ├── security/        # JWT Authentication mechanisms and filtering
│   └── service/         # Method-level security and core business logic
├── src/main/resources/
│   ├── db/migration/    # Flyway SQL migrations schemas
│   └── application*.properties # Environment-based runtime flags
├── src/test/            # BDD-style unit & integration tests
├── docker-compose.yml   # Infrastructure provisioning (PostgreSQL)
└── pom.xml              # Project dependencies and build config
```

---

## Getting Started

### Prerequisites
* **Java 25** installed on your local machine.
* **Docker & Docker Compose** for spinning up the database seamlessly.

### Installation & Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ticketsystem.git
   cd ticketsystem
   ```

2. Provision the local environment requirements (and prepare your \`.env\`):
   ```bash
   cp .env.example .env
   ```

3. Start the local PostgreSQL instance:
   ```bash
   docker compose up -d
   ```

### Usage

**Run the REST API:**
Use the provided Maven wrapper to launch the application (using isolated dev properties):
```bash
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```
*Flyway will automatically detect environment databases and apply required structural SQL migrations progressively.*

**Run Test Suite:**
Execute the internal test suite (integration tests utilize an embedded H2 variant avoiding actual database spin-ups):
```bash
./mvnw clean test
```
