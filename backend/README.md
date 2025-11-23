# E-Commerce Platform - Backend

Spring Boot backend for the E-Commerce Platform.

## Technology Stack

- Spring Boot 3.2.0
- Java 17
- MySQL 8.x
- JWT Authentication
- Maven

## Project Structure

```
src/main/java/com/ecommerce/
├── controller/      # REST API controllers
├── service/         # Business logic layer
├── dao/             # Data access layer (JDBC)
├── model/           # Entity models
├── config/          # Configuration classes
├── exception/       # Custom exceptions
├── util/            # Utility classes
├── servlet/         # Servlet module
├── async/           # Async services (multithreading)
└── payment/         # Payment gateway integration
```

## Setup

1. Configure MySQL database in `application.properties`
2. Run `mvn clean install`
3. Start application: `mvn spring-boot:run`

## API Documentation

See `/docs/API_DOCUMENTATION.md` for complete API reference.

## Testing

Run tests with:
```bash
mvn test
```

## Building

Build JAR file:
```bash
mvn clean package
```

Run JAR:
```bash
java -jar target/ecommerce-platform-1.0.0.jar
```
