# Spring Async Data Pipeline

A production-ready Spring Boot 3.5.12 application demonstrating asynchronous data processing, queue management, caching, and security best practices using **Java 17**, **MySQL**, and **Docker Compose**.

## 🎯 Features

- ✅ **REST API** with JSON content type validation
- ✅ **Spring Security** with Basic Authentication (ADMIN/USER roles)
- ✅ **Input Validation** using Jakarta Bean Validation
- ✅ **Async Processing** with thread pool and scheduled tasks (every 10 seconds)
- ✅ **Queue Management** using MySQL database with status tracking
- ✅ **Webhook Integration** for external system communication
- ✅ **Caching** with configurable TTL (30 minutes)
- ✅ **MySQL 8.0 Database** with Hibernate ORM (auto table creation)
- ✅ **Global Exception Handling** for consistent error responses
- ✅ **Comprehensive Unit & Integration Tests** (24 tests, 85% coverage)
- ✅ **Docker Compose** for multi-container orchestration
- ✅ **Health Checks** for monitoring application status
- ✅ **Multi-Stage Docker Build** (optimized image size: ~160MB)

## 📋 Table of Contents

- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Testing Application](#testing-application)
- [Architecture](#architecture)
- [Running Tests](#running-tests)
- [Deployment](#deployment)
- [Git Workflow](#git-workflow)

## 🛠️ Technology Stack

| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| **Language** | Java | 17 | Core application language |
| **Framework** | Spring Boot | 3.5.12 | Application framework |
| **Spring Version** | Spring Framework | 6.2.17 | Core Spring libraries |
| **ORM** | Hibernate | 6.6.44 | Database ORM mapping |
| **Database** | MySQL | 8.0 | Data persistence |
| **Build Tool** | Maven | 3.8.1+ | Dependency management |
| **Container** | Docker | Latest | Application containerization |
| **Compose** | Docker Compose | v2.0+ | Multi-container orchestration |
| **Testing** | JUnit 5 | Latest | Unit testing framework |
| **Mocking** | Mockito | Latest | Test mocking library |
| **Cache** | Spring Cache | Built-in | In-memory caching |

## 📁 Project Structure

```text
spring-async-data-pipeline/
├── src/
│   ├── main/
│   │   ├── java/com/asyncpipeline/spring_async_data_pipeline/
│   │   │   ├── config/              # Spring configurations
│   │   │   │   ├── AsyncConfig.java
│   │   │   │   ├── CacheConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebClientConfig.java
│   │   │   ├── controller/          # REST endpoints
│   │   │   │   └── DataController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── DataResponse.java
│   │   │   │   └── SubmitDataRequest.java
│   │   │   ├── entity/              # JPA Database Entities
│   │   │   │   └── DataRequest.java
│   │   │   ├── exception/           # Global Error Handling
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/          # Data Access Layer
│   │   │   │   └── DataRequestRepository.java
│   │   │   ├── service/             # Business Logic
│   │   │   │   └── DataProcessingService.java
│   │   │   └── SpringAsyncDataPipelineApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application.properties
│   └── test/                        # Unit & Integration Tests
│       └── java/com/asyncpipeline/
│           ├── controller/
│           │   └── DataControllerTest.java
│           └── service/
│               └── DataProcessingServiceTest.java
├── pom.xml                          # Maven Dependencies
├── Dockerfile                       # Container Definition
├── docker-compose.yml               # Infrastructure
└── README.md
```

## 🔧 Prerequisites

Before running the application, ensure you have installed:

- **Java 17** or higher
  ```bash
  java --version
  # Should show: java 17.x.x
  ```
- Maven 3.8.1 or higher
  ```bash
  mvn --version
  # Should show: Apache Maven 3.8.1+
  ```
- Docker and Docker Compose
  ```bash
  docker --version
  docker-compose --version
  ```
- Git (for version control)
  ```bash
  git --version
  ```

## 🚀 Quick Start
  Using Docker Compose 
  ```bash
# 1. Clone the repository
git clone https://github.com/Shibilibacker/spring-async-data-pipeline.git
cd spring-async-data-pipeline/spring-async-data-pipeline

# 2. Start all services (MySQL + Application)
docker-compose up -d

# 3. Wait for services to be ready
sleep 30

# 4. Verify containers are running
docker ps

# Expected output:
# CONTAINER ID   IMAGE                        PORTS                    STATUS
# abc123def456   spring-async-pipeline:1.0.0  0.0.0.0:8080->8080/tcp   Up 30 seconds
# def456ghi789   mysql:8.0                    0.0.0.0:3306->3306/tcp   Up 30 seconds

# 5. Test the application
curl http://localhost:8080/api/v1/data/health

# 6. View logs
docker-compose logs -f app

# 7. Stop services
docker-compose down
  ```

Access Points:
* **Application:** http://localhost:8080/api/v1/data/health
* **MySQL:** localhost:3306 (from host: mysql-async from containers)

## ⚙️ Configuration

* **Application Properties:** `src/main/resources/application.properties`
* **Docker Compose:** `docker-compose.yml`

### 🖥️ Database Configuration

| Variable | Default Value | Description | Example |
| :--- | :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/async_pipeline_db` | MySQL database connection URL | `jdbc:mysql://mysql:3306/async_pipeline_db` |
| `SPRING_DATASOURCE_USERNAME` | `root` | Database username | `root` |
| `SPRING_DATASOURCE_PASSWORD` | `root` | Database password | `root` |
| `DB_HOST` | `mysql-async` | Database service name (Docker) | `mysql` |
| `DB_NAME` | `async_pipeline_db` | MySQL database name | `async_db` |

## 📡 API Endpoints
### 1. Health Check Endpoint (Public - No Authentication)
**Endpoint:**
   ```bash
   curl http://localhost:8080/api/v1/data/health
   ```
Response (200 OK):
  ```bash
{
  "success": true,
  "message": "Service is running",
  "data": "OK",
  "timestamp": "2026-03-25T12:00:00"
}
  ```
### 2. Submit Data Endpoint (Protected - Authentication Required)
**Endpoint:**
  ```bash
curl -X POST http://localhost:8080/api/v1/data/submit \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "payload": "Test data for processing"
  }'
  ```
  Request Body:
  ```bash
  {
  "name": "John Doe",
  "email": "john@example.com",
  "payload": "Test payload data"
  }
  ```
Response (201 Created):
```bash
{
  "success": true,
  "message": "Data submitted successfully and queued for processing",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "payload": "Test payload data",
    "status": "PENDING",
    "createdAt": "2026-03-25T12:00:00"
  },
  "timestamp": "2026-03-25T12:00:00"
}
  ```
**Validation Rules:**

* **name:** Required, string (max 255 chars)
* **email:** Required, valid email format
* **payload:** Required, string (no max limit)
* **Content-Type:** Must be `application/json`


3. Retrieve Data Endpoint (Protected - Cached)
Endpoint:
  ```bash
curl http://localhost:8080/api/v1/data/1 \
  -u admin:admin123
  ```

Response (200 OK):
  ```bash
{
  "success": true,
  "message": "Data retrieved successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "payload": "Test payload data",
    "status": "PENDING",
    "createdAt": "2026-03-25T12:00:00",
    "processedAt": null
  },
  "timestamp": "2026-03-25T12:00:00"
}
  ```
**Cache Behavior:**

* **First request:** Retrieved from database
* **Subsequent requests (within 30 minutes):** Retrieved from cache
* **After cache TTL:** Retrieved from database again

Error Response (404 Not Found):
  ```bash
{
  "success": false,
  "message": "Resource not found",
  "data": "Data with ID 999 not found",
  "timestamp": "2026-03-25T12:00:00"
}
  ```
  ```bash
{
  "success": false,
  "message": "Resource not found",
  "data": "Data with ID 999 not found",
  "timestamp": "2026-03-25T12:00:00"
}
  ```
## 🔐 Authentication

### Default Credentials

| Username | Password | Role | Access |
| :--- | :--- | :--- | :--- |
| **admin** | `admin123` | ADMIN | All endpoints |
| **user** | `user123` | USER | All endpoints |

How to Use:

  ```bash
# Basic Authentication Header
curl -u admin:admin123 http://localhost:8080/api/v1/data/1

# Or with Authorization header (Base64 encoded)
curl -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" http://localhost:8080/api/v1/data/1
  ```
### 🧪 Running Tests

When you start the application, you can use these commands to verify functionality:
  ```bash
# Test Data Set 1: John Doe
curl -X POST http://localhost:8080/api/v1/data/submit \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "payload": "Order #12345 - Processing payment"
  }'

# Test Data Set 2: Jane Smith
curl -X POST http://localhost:8080/api/v1/data/submit \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "name": "Jane Smith",
    "email": "jane@example.com",
    "payload": "Invoice #INV-001 - Generate report"
  }'

# Test Data Set 3: Bob Wilson
curl -X POST http://localhost:8080/api/v1/data/submit \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "name": "Bob Wilson",
    "email": "bob@example.com",
    "payload": "User registration - Send welcome email"
  }'

# Test Data Set 4: Alice Johnson
curl -X POST http://localhost:8080/api/v1/data/submit \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "payload": "Data export - CSV format requested"
  }'

# Test Data Set 5: Charlie Brown
curl -X POST http://localhost:8080/api/v1/data/submit \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "name": "Charlie Brown",
    "email": "charlie@example.com",
    "payload": "API integration test - Webhook callback verification"
  }'
  ```
  
  Retrieve Test Data
  
  ```bash
      # Get data by ID (e.g., ID=1)
    curl http://localhost:8080/api/v1/data/1 \
      -u admin:admin123
    
    # Get data by ID (e.g., ID=2)
    curl http://localhost:8080/api/v1/data/2 \
      -u admin:admin123
    
    # Get data by ID (e.g., ID=3)
    curl http://localhost:8080/api/v1/data/3 \
      -u admin:admin123
  ```

  Verify Data in MySQL
  ```bash
        # Connect to MySQL container
      docker exec -it async-pipeline-mysql mysql -u root -proot async_pipeline_db
      
      # View all submitted data
      mysql> SELECT * FROM data_requests;
      
      # Count total records
      mysql> SELECT COUNT(*) as total_records FROM data_requests;
      
      # View by status
      mysql> SELECT status, COUNT(*) FROM data_requests GROUP BY status;
      
      # View processing details
      mysql> SELECT id, name, email, status, created_at, processed_at FROM data_requests;
      
      # Exit MySQL
      mysql> EXIT;
  ```

## 🏗️ Architecture
  ```bash
    ┌─────────────────────────────────────────────────────────┐
    │ 1. CLIENT SUBMITS DATA                                  │
    │    POST /api/v1/data/submit                             │
    │    ↓                                                    │
    │    Spring Security validates credentials                │
    │    ↓                                                    │
    │    Controller validates JSON (Content-Type check)       │
    │    ↓                                                    │
    │    Input validation (name, email, payload)              │
    │    ↓                                                    │
    │    Service saves to database (PENDING status)           │
    │    ↓                                                    │
    │    201 Created response sent to client                  │
    └─────────────────────────────────────────────────────────┘
    
    ┌─────────────────────────────────────────────────────────┐
    │ 2. SCHEDULER PROCESSES QUEUE (Every 10 seconds)         │
    │    processQueue() method runs                           │
    │    ↓                                                    │
    │    Finds all PENDING records from database              │
    │    ↓                                                    │
    │    For each record: calls processRequestAsync()         │
    │    ↓                                                    │
    │    Updates status to PROCESSING                         │
    │    ↓                                                    │
    │    (Continues in background thread pool)                │
    └─────────────────────────────────────────────────────────┘
    
    ┌─────────────────────────────────────────────────────────┐
    │ 3. ASYNC PROCESSING (In thread pool - 5 threads)        │
    │    Makes HTTPS POST to webhook endpoint                 │
    │    ↓                                                    │
    │    Webhook processes data asynchronously                │
    │    ↓                                                    │
    │    Application receives response                        │
    │    ↓                                                    │
    │    Updates status to COMPLETED                          │
    │    ↓                                                    │
    │    Saves webhook response in database                   │
    │    ↓                                                    │
    │    Invalidates cache entry                              │
    └─────────────────────────────────────────────────────────┘
    
    ┌─────────────────────────────────────────────────────────┐
    │ 4. CLIENT RETRIEVES DATA                                │
    │    GET /api/v1/data/{id}                                │
    │    ↓                                                    │
    │    Spring Security validates credentials                │
    │    ↓                                                    │
    │    Check cache (TTL: 30 minutes)                        │
    │    ├─ Cache HIT: Return immediately (1ms)               │
    │    └─ Cache MISS: Query database (100ms)                │
    │    ↓                                                    │
    │    Store in cache                                       │
    │    ↓                                                    │
    │    200 OK response with data                            │
    └─────────────────────────────────────────────────────────┘
  ```
Layered Architecture

  ```bash
      ┌──────────────────────────────────────┐
      │   Presentation Layer                 │
      │  ┌────────────────────────────────┐  │
      │  │  DataController                │  │
      │  │  - REST endpoints              │  │
      │  │  - Request/Response DTOs       │  │
      │  │  - Input validation            │  │
      │  └────────────────────────────────┘  │
      └────────────────────┬─────────────────┘
                           │
      ┌────────────────────▼─────────────────┐
      │   Business Logic Layer               │
      │  ┌────────────────────────────────┐  │
      │  │  DataProcessingService         │  │
      │  │  - Async processing            │  │
      │  │  - Scheduled tasks             │  │
      │  │  - Caching logic               │  │
      │  │  - Webhook calls               │  │
      │  └────────────────────────────────┘  │
      └────────────────────┬─────────────────┘
                           │
      ┌────────────────────▼─────────────────┐
      │   Data Access Layer                  │
      │  ┌────────────────────────────────┐  │
      │  │  DataRequestRepository         │  │
      │  │  - CRUD operations             │  │
      │  │  - Custom queries              │  │
      │  │  - Hibernate mapping           │  │
      │  └────────────────────────────────┘  │
      └────────────────────┬─────────────────┘
                           │
      ┌────────────────────▼─────────────────┐
      │   Persistence Layer                  │
      │  ┌────────────────────────────────┐  │
      │  │  MySQL Database                │  │
      │  │  - data_requests table         │  │
      │  │  - Auto-created by Hibernate   │  │
      │  └────────��─────────────────────── │
      └──────────────────────────────────────┘
  ```
## 🧪 Running Tests

  ```bash
Test Data Sets

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=DataProcessingServiceTest

# Generate coverage report
mvn test jacoco:report

# View report
# Open: target/site/jacoco/index.html
  ```

## 🐳 Deployment
Docker Build
  ```bash
# Build image
docker build -t spring-async-pipeline:1.0.0 .

# Run container
docker run -p 8080:8080 spring-async-pipeline:1.0.0
  ```
Docker Compose
  ```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop services
docker-compose down
  ```
## 🌿 Git Workflow

### 1. Initial Setup
```bash
# Clone the repository
git clone [https://github.com/Shibilibacker/spring-async-data-pipeline.git](https://github.com/Shibilibacker/spring-async-data-pipeline.git)
cd spring-async-data-pipeline

# Configure local identity
git config user.name "Your Name"
git config user.email "your.email@example.com"
```
2. Feature Development
```bash
# Create and switch to a new feature branch
git checkout -b feature/feature-name

# Make changes, then check status
git status
git diff

# Stage and commit changes
git add .
git commit -m "feat: add new feature description"
# Standard prefixes: feat, fix, docs, test, refactor

# Push to remote repository
git push origin feature/feature-name
```

3. Pull Request Process

* **Open PR: Click the "New Pull Request" button.**

* **Compare: Select your feature branch as the compare and main as the base.**

* **Describe: Add a clear description of your changes and request a review.**

* **Finalize: Merge once CI checks pass and approval is received.**

4. Merging to Main (Local)
```bash
git checkout main
git pull origin main
git merge feature/feature-name
git push origin main
```
5. Viewing History
```bash
# Clean, one-line view
git log --oneline

# Visual graph of all branches
git log --graph --all --decorate

# View specific commit details
git show <commit-hash>
```

Shibilibacker

GitHub: @Shibilibacker

Last Updated: March 26, 2026
Version: 1.0.0
