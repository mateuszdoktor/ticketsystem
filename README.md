# Ticket System

A RESTful ticket management system built with Spring Boot. The application enables users to create, track, and manage support tickets with role-based access control and JWT authentication.

## Features

- **Ticket Management** - Create tickets, update status, assign to users, add comments
- **User Management** - User registration and administration (Admin only)
- **Authentication & Authorization** - JWT-based authentication with role-based access control (USER, MANAGER, ADMIN)
- **Filtering & Pagination** - Search tickets by status, priority, date range, and assignee
- **Audit Trail** - Automatic tracking of creation and modification timestamps
- **Integration Tests** - HTTP integration coverage for auth, ticket, and user controllers
- **Production Readiness** - Profile-based runtime configuration and hardened compose defaults

## Tech Stack

### Backend

- **Java 25** with **Spring Boot 4.0**
- **Spring Security** with JWT authentication (OAuth2 Resource Server)
- **Spring Data JPA** with Hibernate & PostgreSQL
- **Flyway** for database migrations

### Key Features & Libraries

- **MapStruct** for DTO mapping
- **Bean Validation** for input validation
- **SpringDoc OpenAPI** for API documentation
- **Lombok** for boilerplate reduction

### Architecture & Patterns

- Layered architecture (Controller-Service-Repository)
- DTO pattern with separate request/response models
- JPA Specification pattern for dynamic queries
- Method-level security with role-based access control
- Optimistic locking with `@Version`
- EntityGraph for N+1 query prevention
- Centralized exception handling with Problem Details (RFC 7807)

### Testing

- **JUnit 5** with **Mockito**
- BDD-style unit tests
- Integration tests for key 200/401/403/404 controller flows

## Getting Started

### Prerequisites

- Java 25+
- Docker & Docker Compose

### Running the Application

1. Clone the repository

```bash
git clone https://github.com/yourusername/ticketsystem.git
cd ticketsystem
```

2. Prepare local environment variables (kept outside git)

```bash
cp .env.example .env
```

3. Start the database

```bash
docker-compose up -d
```

4. Run the application

```bash
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

5. Access API documentation at `http://localhost:8080/swagger-ui.html`

### Environment Variables

| Variable                 | Description                                    | Example                                         |
| ------------------------ | ---------------------------------------------- | ----------------------------------------------- |
| `SPRING_PROFILES_ACTIVE` | Active runtime profile (`dev` or `prod`)       | `dev`                                           |
| `POSTGRES_DB`            | Database name used by docker compose           | `ticketsystem`                                  |
| `POSTGRES_USER`          | Database user used by docker compose           | `postgres`                                      |
| `POSTGRES_PASSWORD`      | Local compose password (keep only in `.env`)   | `change-me`                                     |
| `POSTGRES_PORT`          | Host port bound to postgres container          | `5432`                                          |
| `POSTGRES_INITDB_ARGS`   | Extra init args for Postgres (`scram-sha-256`) | `--auth-host=scram-sha-256`                     |
| `DB_URL`                 | Spring datasource JDBC URL                     | `jdbc:postgresql://localhost:5432/ticketsystem` |
| `DB_USERNAME`            | Spring datasource username                     | `postgres`                                      |
| `DB_PASSWORD`            | Spring datasource password                     | `change-me`                                     |
| `JWT_SECRET`             | Secret key for JWT signing (min 32 chars)      | `replace-with-at-least-32-characters`           |

### Runtime Profiles

- `dev`
  - SQL logging enabled
  - verbose app logs for local debugging
- `prod`
  - reduced log verbosity
  - SQL logging disabled
  - safer default error detail exposure
- `test`
  - in-memory H2 datasource
  - Flyway disabled for isolated integration tests

Compose hardening notes:

- Postgres is bound to localhost only
- `POSTGRES_INITDB_ARGS` enables SCRAM host auth
- healthcheck is configured (`pg_isready`)
- restart policy is enabled (`unless-stopped`)
- secrets should be supplied through local `.env` (ignored by git)

## Access Control Policy

Core policy:

- `/auth/**` is public
- `/api/**` requires JWT
- role checks are enforced at service layer via `@PreAuthorize`
- ticket visibility:
  - ADMIN/MANAGER can view and manage all tickets
  - USER can view tickets they created or are assigned to
  - USER can change status only on tickets they created

### Endpoint Authorization Matrix

#### Authentication

| Method | Endpoint      | Access |
| ------ | ------------- | ------ |
| POST   | `/auth/login` | Public |

#### Tickets

| Method | Endpoint                     | Access                                 |
| ------ | ---------------------------- | -------------------------------------- |
| GET    | `/api/tickets`               | ADMIN, MANAGER                         |
| GET    | `/api/tickets/{id}`          | Authenticated + ticket visibility rule |
| GET    | `/api/tickets/my`            | Any authenticated user                 |
| POST   | `/api/tickets`               | Any authenticated user                 |
| PATCH  | `/api/tickets/{id}/status`   | Authenticated + ticket management rule |
| PATCH  | `/api/tickets/{id}/assignee` | ADMIN, MANAGER                         |
| GET    | `/api/tickets/{id}/comments` | Authenticated + ticket visibility rule |
| POST   | `/api/tickets/{id}/comments` | Authenticated + ticket visibility rule |

#### Users

| Method | Endpoint                           | Access                              |
| ------ | ---------------------------------- | ----------------------------------- |
| GET    | `/api/users`                       | ADMIN                               |
| GET    | `/api/users/{id}`                  | ADMIN or same user (`userId` claim) |
| POST   | `/api/users`                       | ADMIN                               |
| GET    | `/api/users/{id}/created-tickets`  | ADMIN or same user (`userId` claim) |
| GET    | `/api/users/{id}/assigned-tickets` | ADMIN or same user (`userId` claim) |
| GET    | `/api/users/{id}/comments`         | ADMIN or same user (`userId` claim) |

## API Endpoints

### Authentication

| Method | Endpoint      | Description                       |
| ------ | ------------- | --------------------------------- |
| POST   | `/auth/login` | Authenticate user and receive JWT |

### Tickets

| Method | Endpoint                     | Description                    |
| ------ | ---------------------------- | ------------------------------ |
| GET    | `/api/tickets`               | Get all tickets (with filters) |
| GET    | `/api/tickets/{id}`          | Get ticket details             |
| GET    | `/api/tickets/my`            | Get current user's tickets     |
| POST   | `/api/tickets`               | Create a new ticket            |
| PATCH  | `/api/tickets/{id}/status`   | Update ticket status           |
| PATCH  | `/api/tickets/{id}/assignee` | Assign ticket to user          |
| GET    | `/api/tickets/{id}/comments` | Get ticket comments            |
| POST   | `/api/tickets/{id}/comments` | Add comment to ticket          |

### Users

| Method | Endpoint          | Description             |
| ------ | ----------------- | ----------------------- |
| GET    | `/api/users`      | Get all users (Admin)   |
| GET    | `/api/users/{id}` | Get user details        |
| POST   | `/api/users`      | Create new user (Admin) |

## Ticket Status Flow

```
NEW -> IN_PROGRESS -> CLOSED
```

## Integration Test Scope

Current HTTP integration tests cover key controller scenarios:

- 200 for valid auth and authorized access
- 401 for missing or invalid authentication
- 403 for authenticated but unauthorized role access
- 404 for missing domain resources

## License

This project is for educational and portfolio purposes.
