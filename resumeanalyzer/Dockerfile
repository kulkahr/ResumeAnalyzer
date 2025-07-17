# ---- ResumeAnalyzer Spring Boot ----
FROM eclipse-temurin:17-jre-alpine AS resumeanalyzer
WORKDIR /app
COPY target/resumeanalyzer-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
