# Fundoo Notes Application

A Google Keep-inspired notes backend built with Spring Boot and JWT authentication.

## Tech Stack
- Java 17
- Spring Boot 3.5.13 (Gradle - Groovy)
- Spring Web (REST)
- Spring Data JPA
- MySQL 8
- JWT Authentication (jjwt 0.11.5)
- Spring Security
- Lombok

## Project Structure
```
com.example.fundoo
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── repository
├── security
├── service
│   └── impl
└── util
```

## Setup
1. Clone the repository
2. Create MySQL database: `fundoo_notes` (auto-created on first boot)
3. Update credentials in `src/main/resources/application.properties`
4. Run: `./gradlew bootRun`

## API Endpoints

### User
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/users/register | Register new user |
| POST | /api/users/login | Login and get JWT token |

### Notes (require Authorization header with Bearer token)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/notes | Create note |
| GET | /api/notes | Get all active notes |
| GET | /api/notes/{id} | Get note by ID |
| PUT | /api/notes/{id} | Update note |
| DELETE | /api/notes/{id} | Delete note |
| PATCH | /api/notes/{id}/pin | Toggle pin |
| PATCH | /api/notes/{id}/archive | Toggle archive |
| PATCH | /api/notes/{id}/trash | Toggle trash |
| GET | /api/notes/search?keyword= | Search notes by title |
