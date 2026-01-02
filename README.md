# Ticket System

A RESTful ticket management system built with Spring Boot. The application enables users to create, track, and manage support tickets with role-based access control and JWT authentication.

## Features

- **Ticket Management** - Create tickets, update status, assign to users, add comments
- **User Management** - User registration and administration (Admin only)
- **Authentication & Authorization** - JWT-based authentication with role-based access control (USER, MANAGER, ADMIN)
- **Filtering & Pagination** - Search tickets by status, priority, date range, and assignee
- **Audit Trail** - Automatic tracking of creation and modification timestamps

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
- Test coverage for services and domain logic

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

2. Start the database

```bash
docker-compose up -d
```

3. Run the application

```bash
./mvnw spring-boot:run
```

4. Access the API documentation at `http://localhost:8080/swagger-ui.html`

### Environment Variables

| Variable      | Description                               | Default |
| ------------- | ----------------------------------------- | ------- |
| `DB_URL`      | PostgreSQL connection URL                 | -       |
| `DB_USERNAME` | Database username                         | -       |
| `DB_PASSWORD` | Database password                         | -       |
| `JWT_SECRET`  | Secret key for JWT signing (min 32 chars) | -       |

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
NEW → IN_PROGRESS → CLOSED
```

## Planned Features

- Integration and controller tests
- Ticket update endpoint (title, description, priority)
- Soft delete or delete for tickets and comments
- Enhanced OpenAPI documentation with detailed descriptions
- Current user utility for cleaner JWT handling

## License

This project is for educational and portfolio purposes.
