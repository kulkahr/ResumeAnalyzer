# ResumeAnalyzer

A Java 17 Spring Boot 3.x application using Maven, with Spring Web, Spring Boot Actuator, Spring Security (basic config), and PostgreSQL driver.

## Features
- RESTful API with Spring Web
- Health and metrics endpoints via Actuator
- Basic authentication with Spring Security
- PostgreSQL database connectivity

## Getting Started
1. Ensure you have Java 17 and Maven installed.
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```

## Configuration
- Database and security settings can be configured in `src/main/resources/application.properties`.

## Project Structure
- `src/main/java`: Application source code
- `src/main/resources`: Configuration files
- `src/test/java`: Test code

## License
MIT
