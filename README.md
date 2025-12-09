# Hello World Spring Boot Application

A comprehensive Java 17 Spring Boot application demonstrating the use of Java profiles, Spring Boot profiles, unit testing, Docker containerization, and Jenkins CI/CD pipelines.

## Table of Contents
- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Java Profiles vs Spring Boot Profiles](#java-profiles-vs-spring-boot-profiles)
- [Building the Application](#building-the-application)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Docker](#docker)
- [Jenkins Pipeline](#jenkins-pipeline)
- [API Endpoints](#api-endpoints)
- [Candidate Test Instructions](#candidate-test-instructions)

## Overview

This application is a Hello World REST API built with Spring Boot 3.2.0 and Java 17. It demonstrates:
- Spring Boot REST controllers
- Java Maven profiles configuration
- Spring Boot profiles (dev, test, prod)
- Comprehensive unit testing with JUnit 5
- Multi-stage Docker builds with security best practices
- Jenkins CI/CD pipeline

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker (optional, for containerization)
- Jenkins (optional, for CI/CD)

## Project Structure

```
hello-world-app/
├── src/
│   ├── main/
│   │   ├── java/com/example/helloworld/
│   │   │   ├── HelloWorldApplication.java
│   │   │   ├── config/
│   │   │   │   └── AppConfig.java
│   │   │   ├── controller/
│   │   │   │   └── HelloWorldController.java
│   │   │   └── service/
│   │   │       └── GreetingService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-test.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/com/example/helloworld/
│           ├── HelloWorldApplicationTests.java
│           ├── controller/
│           │   └── HelloWorldControllerTest.java
│           └── service/
│               └── GreetingServiceTest.java
├── Dockerfile
├── Jenkinsfile
└── pom.xml
```

## Java Profiles vs Spring Boot Profiles

### Java Profiles (Maven Profiles)

Java/Maven profiles are defined in `pom.xml` and control **build-time** behavior:
- Determine which dependencies are included
- Control build configurations
- Set Maven properties
- Customize build process

**Available Maven Profiles:**
- `dev` (default) - Development build configuration
- `test` - Test/QA build configuration
- `prod` - Production build configuration

### Spring Boot Profiles

Spring Boot profiles control **runtime** behavior:
- Load different application properties
- Enable/disable specific beans
- Configure environment-specific settings
- Control logging levels

**Available Spring Boot Profiles:**
- `dev` - Development environment (port 8080, debug logging)
- `test` - Test environment (port 8081, detailed logging)
- `prod` - Production environment (port 8080, minimal logging)

## Building the Application

### Build with Default Profile (dev)
```bash
mvn clean package
```

### Build with Specific Maven Profile
```bash
# Build with test profile
mvn clean package -Ptest

# Build with production profile
mvn clean package -Pprod
```

### Skip Tests During Build
```bash
mvn clean package -DskipTests
```

## Running the Application

### Run with Maven
```bash
# Run with default profile (dev)
mvn spring-boot:run

# Run with specific Maven profile
mvn spring-boot:run -Ptest

# Run with specific Spring Boot profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Run the JAR
```bash
# Build first
mvn clean package -Pprod

# Run with default Spring profile from Maven
java -jar target/hello-world-app-1.0.0.jar

# Run with specific Spring Boot profile
java -jar -Dspring.profiles.active=prod target/hello-world-app-1.0.0.jar

# Run with JVM options
java -Xmx512m -Xms256m -Dspring.profiles.active=dev -jar target/hello-world-app-1.0.0.jar
```

### Using Environment Variables
```bash
# Set Spring profile via environment variable
export SPRING_PROFILES_ACTIVE=prod
java -jar target/hello-world-app-1.0.0.jar
```

## Testing

### Run All Tests
```bash
mvn test
```

### Run Tests with Specific Profile
```bash
mvn test -Ptest
```

### Run Specific Test Class
```bash
mvn test -Dtest=HelloWorldControllerTest
```

### Run Tests with Coverage
```bash
mvn clean test jacoco:report
```

### Test Coverage Report
After running tests with coverage, view the report at:
`target/site/jacoco/index.html`

## Docker

### Dockerfile Best Practices Implemented

1. **Multi-stage Build**: Separates build and runtime stages to minimize final image size
2. **Non-root User**: Runs application as non-privileged user for security
3. **Layer Caching**: Optimizes build by copying pom.xml before source code
4. **Minimal Base Image**: Uses Alpine-based images for smaller footprint
5. **Health Check**: Includes container health check endpoint
6. **Security**: No secrets in image, uses appropriate file permissions
7. **JVM Optimization**: Container-aware JVM settings

### Build Docker Image
```bash
# Build with default settings
docker build -t hello-world-app:latest .

# Build with build arguments
docker build --build-arg SPRING_PROFILE=prod -t hello-world-app:prod .
```

### Run Docker Container
```bash
# Run with dev profile
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  --name hello-world-dev \
  hello-world-app:latest

# Run with production profile and JVM options
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JAVA_OPTS="-Xmx1g -Xms512m" \
  --name hello-world-prod \
  hello-world-app:latest

# Check container health
docker ps
docker logs hello-world-dev
```

### Docker Compose (Optional)
Create `docker-compose.yml`:
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - JAVA_OPTS=-Xmx512m
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/api/health"]
      interval: 30s
      timeout: 3s
      retries: 3
```

## Jenkins Pipeline

### Pipeline Features

The Jenkinsfile implements a complete CI/CD pipeline with:

1. **Parameterized Builds**: Select Maven and Spring profiles
2. **Multi-stage Build**: Validate → Compile → Test → Package → Quality → Docker → Deploy
3. **Parallel Execution**: Code quality checks run in parallel
4. **Test Reporting**: JUnit test results and code coverage
5. **Docker Integration**: Build, test, and push Docker images
6. **Conditional Stages**: Deploy only on main branch with prod profile
7. **Post Actions**: Cleanup and notifications

### Pipeline Parameters

- `MAVEN_PROFILE`: Select build-time profile (dev/test/prod)
- `SPRING_PROFILE`: Select runtime profile (dev/test/prod)

### Running the Pipeline

1. **Configure Jenkins**:
   - Install required plugins: Pipeline, Docker, JUnit
   - Configure Maven and JDK tools
   - Set up credentials for Docker registry

2. **Create Pipeline Job**:
   - New Item → Pipeline
   - Configure SCM to point to this repository
   - Pipeline script from SCM
   - Select Jenkinsfile

3. **Run Build**:
   - Click "Build with Parameters"
   - Select desired Maven and Spring profiles
   - Build Now

### Pipeline Stages Explained

- **Checkout**: Gets source code from repository
- **Validate**: Validates Maven project structure
- **Compile**: Compiles Java code with selected profile
- **Unit Tests**: Runs all unit tests and generates reports
- **Package**: Creates executable JAR file
- **Code Quality**: Runs SonarQube and dependency checks
- **Build Docker**: Creates Docker image
- **Test Docker**: Smoke tests the container
- **Push to Registry**: Pushes image (main branch only)
- **Deploy**: Deploys to environment (prod profile on main)

## API Endpoints

### Health Check
```bash
curl http://localhost:8080/api/health
```
Response:
```json
{
  "status": "UP",
  "profile": "dev"
}
```

### Hello Endpoint
```bash
# Without name parameter
curl http://localhost:8080/api/hello

# With name parameter
curl http://localhost:8080/api/hello?name=John
```
Response:
```json
{
  "message": "Hello from Development, John! [Environment: development, Max Connections: 50, Timestamp: 2024-01-01T10:30:00]",
  "profile": "dev",
  "profileMessage": "Development Environment - Debug Mode Enabled"
}
```

### Environment Info
```bash
curl http://localhost:8080/api/environment
```
Response:
```json
{
  "environment": "development",
  "debugEnabled": true,
  "activeSpringProfile": "dev",
  "maxConnections": 50,
  "profileSpecificMessage": "Development Environment - Debug Mode Enabled"
}
```

### Spring Boot Actuator
```bash
curl http://localhost:8080/actuator/health
```

## Candidate Test Instructions

### Objective
Evaluate candidate's understanding of Java profiles, Spring Boot profiles, Docker best practices, and Jenkins pipelines.

### Test Scenario

You are tasked with deploying this application to three environments: Development, Test, and Production.

### Questions for the Candidate

#### Part 1: Java and Spring Boot Profiles (30 minutes)

1. **Explain the difference between Java/Maven profiles and Spring Boot profiles.**
   - When would you use each?
   - How do they interact?

2. **Run the application in each profile:**
   ```bash
   # Task: Run the app with test Maven profile and prod Spring profile
   # What command would you use?
   ```

3. **Modify configuration:**
   - Add a new property `app.feature.analytics=true` for prod profile only
   - Create a new Spring Boot profile called `staging`
   - Show how to activate multiple profiles simultaneously

4. **Debug issue:**
   The application is showing incorrect max connections value. 
   - How would you verify which profile is active?
   - Where would you look to troubleshoot?

#### Part 2: Docker Best Practices (30 minutes)

1. **Analyze the Dockerfile:**
   - List at least 5 DevOps best practices implemented
   - Explain why multi-stage builds are used
   - Why run as non-root user?

2. **Modify the Dockerfile:**
   - Add a build argument for selecting Maven profile
   - Add environment variable for database connection
   - Implement a way to pass JVM memory settings

3. **Security improvements:**
   - What security vulnerabilities should be avoided in Dockerfiles?
   - How would you scan this image for vulnerabilities?
   - Explain the health check implementation

4. **Practical task:**
   ```bash
   # Build and run the container with prod profile and custom JVM settings
   # Verify it's running with correct configuration
   ```

#### Part 3: Jenkins Pipeline (30 minutes)

1. **Pipeline understanding:**
   - Explain each stage in the Jenkinsfile
   - What is the purpose of parallel execution?
   - When does deployment occur?

2. **Modify the pipeline:**
   - Add a stage for integration tests
   - Add email notification on failure
   - Implement branch-specific deployment strategies

3. **Troubleshooting:**
   - Pipeline fails at "Test Docker Image" stage
   - How would you debug this?
   - What logs would you check?

4. **Best practices:**
   - What improvements would you suggest for this pipeline?
   - How would you implement secrets management?
   - Explain the cleanup strategy in post actions

#### Part 4: Practical Exercise (45 minutes)

**Scenario**: Deploy a new feature with different configurations per environment

1. Add a new feature flag `app.feature.newui=true`
2. Enable it only in dev and test, disabled in prod
3. Create a new REST endpoint that checks this flag
4. Write unit tests for the new endpoint
5. Update the Dockerfile to support the change
6. Update the Jenkins pipeline to validate the feature
7. Document the changes

### Evaluation Criteria

- **Profile Management** (25%): Understanding of Java and Spring Boot profiles
- **Docker Knowledge** (25%): Best practices and security awareness
- **Jenkins/CI-CD** (25%): Pipeline design and troubleshooting
- **Code Quality** (15%): Clean code, testing, documentation
- **Problem Solving** (10%): Debugging and optimization skills

### Expected Deliverables

1. Working code with all required changes
2. Updated documentation
3. Test evidence (screenshots, logs)
4. Explanation of design decisions
5. Any scripts or configuration files created

## Additional Resources

- [Spring Boot Profiles Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
- [Maven Profiles Guide](https://maven.apache.org/guides/introduction/introduction-to-profiles.html)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)

## License

This project is for educational and evaluation purposes.

## Contact

For questions or issues, please open a GitHub issue or contact the DevOps team.