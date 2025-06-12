# Backend API - JSONPlaceholder Clone

All tests are passing.

This is a Spring Boot application that replicates the behavior of JSONPlaceholder with added features like JWT authentication and PostgreSQL database support.

## Features

- Full CRUD operations for users
- JWT-based authentication
- PostgreSQL database with Flyway migrations
- Docker and Docker Compose support
- Structured user data storage
- Input validation
- Unit and integration tests

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Maven

## Getting Started

### Local Development

1. Clone the repository
2. Configure the database in `src/main/resources/application.yml`
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Docker Deployment

1. Build and start the containers:
   ```bash
   docker-compose up --build
   ```

2. The application will be available at `http://localhost:8080`

## API Endpoints

### Authentication
- POST `/api/auth/register` - Register a new user
- POST `/api/auth/login` - Login and get JWT token

### Users
- GET `/api/users` - Get all users
- GET `/api/users/{id}` - Get user by ID
- POST `/api/users` - Create new user
- PUT `/api/users/{id}` - Update user
- DELETE `/api/users/{id}` - Delete user

## Database Schema

The application uses PostgreSQL with the following main entities:
- User
- Address
- Company
- AuthUser (for authentication)

## Testing

Run the tests with:
```bash
./mvnw test
```

## Security

- JWT-based authentication
- Password hashing with BCrypt
- Protected endpoints
- Input validation

## License

MIT 