# AGENTS.md

## Project

Backend service written in:

- Java 25
- Spring Boot 4
- Gradle Kotlin DSL
- PostgreSQL
- Flyway
- Spring Security

## Concept

- Password or any credential storage, like last pass, 
1password but selfhosted solution
- 

## Architecture

The project follows Clean Architecture.

```
controller
    ↓
application (use cases)
    ↓
domain
    ↓
infrastructure
```

Rules:

- Domain must not depend on Spring.
- Business logic belongs only to application/domain.
- Controllers should contain no business logic.
- Infrastructure contains repositories, external clients and persistence.

---

## Coding Style

### Java

- Prefer immutable objects.
- Use records for DTOs.
- Avoid field injection.
- Use constructor injection.
- Avoid static mutable state.
- Use Optional only as a return type.
- Never return null.

### Spring

- Prefer constructor injection.
- Use @Transactional only on service/use-case layer.
- Configuration goes into @Configuration classes.
- Avoid using @Component unless Service/Repository fits better.

### Exceptions

- Never catch Exception unless rethrowing with context.
- Use custom domain exceptions.
- REST errors must be mapped using @RestControllerAdvice.

---

## Validation

Use Jakarta Validation.

Example:

```java
@NotBlank
String email;

@Positive
Long id;
```

Controllers should use:

```java
@Valid
@RequestBody
```

---

## Persistence

Use Spring Data JPA.

Rules:

- Repository interfaces belong in infrastructure.
- Domain objects must not depend on JPA.
- Avoid exposing Entity objects outside infrastructure.
- Fetch only required data.
- Avoid N+1 queries.

---

## Database

Use PostgreSQL.

Migrations:

- Flyway only.
- Never edit existing migration.
- Create a new migration for every schema change.

Naming:

```
V15__add_customer_table.sql
```

---

## Logging

Use SLF4J.

Rules:

- Log business events.
- Never log passwords.
- Never log access tokens.
- Use parameterized logging.

Good:

```java
log.info("Created order {}", orderId);
```

Bad:

```java
log.info("Created order " + orderId);
```

---

## Security

Never:

- disable CSRF without reason
- hardcode secrets
- commit credentials
- store passwords in plain text

Passwords must use BCrypt.

---

## Testing

Use:

- JUnit 5
- Mockito
- Testcontainers

Every new service should have:

- unit tests
- integration tests for repositories

Avoid mocking simple POJOs.

---

## API

REST conventions:

```
GET    /users
GET    /users/{id}
POST   /users
PUT    /users/{id}
DELETE /users/{id}
```

Return:

- 200
- 201
- 204
- 400
- 404
- 409

Use Problem Details (RFC7807) for errors.

---

## Build

Before committing run:

```
./gradlew clean test
./gradlew check
```

Code must compile without warnings.

---

## Dependencies

Prefer:

- Spring Boot starters
- Jackson
- MapStruct

Avoid adding libraries unless necessary.

---

## AI Agent Instructions

When generating code:

- Follow existing package structure.
- Reuse existing services before creating new ones.
- Prefer modifying existing code over duplicating functionality.
- Keep methods under ~40 lines where practical.
- Keep classes focused on a single responsibility.
- Preserve formatting used by the project.
- Do not introduce new frameworks.
- Do not change public APIs unless explicitly requested.
- Explain non-obvious architectural decisions in comments or commit messages.

Before suggesting code:

1. Check whether similar functionality already exists.
2. Reuse existing abstractions.
3. Keep changes minimal.
4. Generate production-ready code only.