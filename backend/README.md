# HealthConnect

HealthConnect is a personal health and wellness application that empowers
users to track various aspects of their health, such as daily activities, sleep patterns, water
intake, and mood. It aims to provide users with a centralized platform to monitor their
well-being, visualize trends, and encourage healthier habits.

---

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Overview](#api-overview)
- [Security](#security)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

---

## Features
- **User Authentication**: Secure registration, login, and JWT-based session management
- **Activity Tracking**: Log, update, and analyze daily physical activities
- **Sleep Monitoring**: Record sleep sessions, quality, and notes
- **Mood Logging**: Track daily mood with ratings and notes
- **Water Intake**: Monitor daily hydration
- **User Profile**: Manage personal health profile (age, gender, weight, height)
- **Aggregated Metrics**: View statistics and trends for all health data
- **RESTful API**: Clean, versioned endpoints with OpenAPI/Swagger documentation

---

## Architecture
- **Backend**: Spring Boot (Java 21), layered architecture (Controller, Service, Repository)
- **Database**: MySQL, managed via Spring Data JPA
- **Security**: Spring Security with JWT authentication, stateless sessions
- **API Docs**: Swagger UI (OpenAPI 3)

## Tech Stack
- Java 21
- Spring Boot 3.5
- Spring Security (JWT)
- Spring Data JPA (MySQL)
- Lombok
- Swagger/OpenAPI (springdoc)
- JUnit, MockMvc (for testing)

---

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- MySQL 8+

### Setup
1. **Clone the repository:**
   ```bash
   git clone https://github.com/a97u/Health_Connect
   cd Health_Connect
   ```
2. **Configure the database:**
   - Edit `src/main/resources/application.properties` with your MySQL credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/healthconnect
     spring.datasource.username=YOUR_DB_USER
     spring.datasource.password=YOUR_DB_PASSWORD
     ```
3. **Build and run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
4. **Access API documentation:**
   - Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Configuration
- **JWT:** Secret and expiration are set in `application.properties`
- **CORS:** Configured globally for API access

---

## API Overview

All endpoints are prefixed with `/api/`.

| Resource      | Endpoint Prefix      | Description                        |
|---------------|---------------------|------------------------------------|
| Auth          | `/api/auth`         | Register, login, logout            |
| Users         | `/api/users`        | Profile management                 |
| Activities    | `/api/activities`   | CRUD for activity records          |
| Sleep         | `/api/sleep`        | CRUD for sleep records             |
| Mood          | `/api/mood`         | CRUD for mood records              |
| Water Intake  | `/api/water`        | CRUD for water intake records      |
| Metrics       | `/api/metrics`      | Aggregated health statistics       |

- **Authentication:** Use `/api/auth/signin` to obtain a JWT. Pass it as `Authorization: Bearer <token>` in all protected requests.
- **Swagger UI:** Full API docs and try-it-out at `/swagger-ui.html`.

---

## Security
- **JWT Authentication:** Stateless, secure endpoints
- **Password Hashing:** BCrypt
- **Validation:** All requests validated (see `@Valid` in controllers)
- **Exception Handling:** Centralized via `GlobalExceptionHandler`

---

## Testing
- Integration and unit tests are located in `src/test/java/com/healthconnect/`
- Run all tests:
  ```bash
  ./mvnw test
  ```

---

## Contributing
Contributions are welcome! To contribute:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes
4. Push to your fork and open a Pull Request
