# ResumeAnalyzer

A full-stack resume analysis platform with:
- Java 17 Spring Boot backend (Maven, Spring Web, Actuator, Security, PostgreSQL, Micrometer)
- Vite React TypeScript frontend (served via Nginx)
- PostgreSQL database

## Features
- RESTful API with Spring Web
- Health and metrics endpoints via Actuator (`/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`)
- Basic authentication with Spring Security (except `/api/health`)
- PostgreSQL database connectivity
- Prometheus metrics via Micrometer
- JSON logging (Logback)

## Getting Started
### Prerequisites
- Docker & Docker Compose
- Node.js (for local frontend development)
- Java 17 & Maven (for local backend development)

### Local Development
1. Build backend:
   ```sh
   cd resumeanalyzer
   mvn clean install
   ```
2. Build frontend:
   ```sh
   cd resumeanalyzer-ui
   npm install
   npm run build
   ```
3. Run backend:
   ```sh
   mvn spring-boot:run
   ```
4. Run frontend:
   ```sh
   npm run dev
   ```

### Docker Compose
1. Create a `.env` file in the project root:
   ```env
   POSTGRES_DB=resumeanalyzer
   POSTGRES_USER=<user_name>
   POSTGRES_PASSWORD=<password>
   SPRING_DATASOURCE_USERNAME=<user_name>
   SPRING_DATASOURCE_PASSWORD=<password>
   ```
2. Start all services:
   ```sh
   docker compose up --build
   ```
3. Access services:
   - Frontend: [http://localhost:3000](http://localhost:3000)
   - Backend: [http://localhost:8080](http://localhost:8080)
   - Prometheus metrics: [http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)

## Configuration
- Backend: `src/main/resources/application.properties`
- Frontend: `.env`, `vite.config.ts`
- Docker Compose: `.env`, `docker-compose.yml`

## Monitoring & Logging
- Metrics: `/actuator/metrics`, `/actuator/prometheus`
- Logging: JSON format via Logback (LogstashEncoder)

## Project Structure
- `resumeanalyzer/`: Spring Boot backend
- `resumeanalyzer-ui/`: React frontend
- `docker-compose.yml`: Multi-service orchestration

## License
MIT
