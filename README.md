# PrakartiAyurVeda - AI-Powered Ayurveda Diet Platform

An intelligent Ayurveda diet recommendation system built with Spring Boot 3.5.9, featuring agent-based architecture, JWT authentication, and AI integration (OpenAI/Anthropic).

## 🚀 Features

### Core Functionality
- **Dosha Assessment**: Automated Vata, Pitta, and Kapha evaluation
- **AI-Powered Diet Generation**: Personalized diet plans using OpenAI GPT-4
- **Agent-Based Architecture**: Sequential processing with 4 specialized agents
- **JWT Authentication**: Secure stateless authentication
- **RESTful API**: Complete CRUD operations for users, assessments, and diet plans

### AI Integration
- **OpenAI GPT-4** - Primary AI for diet recommendations
- **Anthropic Claude** - Ready for integration (commented out)
- **SimpleLoggerAdvisor** - Request/response logging for AI calls

## 📋 Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Agent Architecture](#agent-architecture)
- [Configuration](#configuration)
- [Database Schema](#database-schema)
- [Security](#security)
- [Development](#development)

## 🛠️ Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.5.9**
- **Spring Security** - JWT-based authentication
- **Spring Data JPA** - Database persistence
- **Spring AI 1.1.2** - OpenAI/Anthropic integration

### Database
- **MySQL 8.0** - Primary database
- **Hibernate** - ORM

### AI/ML
- **OpenAI GPT-4** - Diet plan generation
- **Anthropic Claude 3.5 Sonnet** - (Optional) Alternative AI provider

### Tools & Libraries
- **Lombok** - Reduce boilerplate code
- **Jackson** - JSON processing
- **jjwt** - JWT token handling
- **BCrypt** - Password encryption

## 📁 Project Structure

```
PrakartiAyurVeda/
├── src/
│   ├── main/
│   │   ├── java/com/PrakartiAyurVeda/
│   │   │   ├── agent/                          # Agent-based architecture
│   │   │   │   ├── context/
│   │   │   │   │   └── AgentContext.java       # Shared state across agents
│   │   │   │   ├── diet/
│   │   │   │   │   └── DietRecommendationAgent.java  # AI diet generation
│   │   │   │   ├── dosha/
│   │   │   │   │   └── DoshaEvaluationAgent.java     # Dosha calculation
│   │   │   │   ├── orchestrator/
│   │   │   │   │   ├── AgentOrchestrator.java        # Agent pipeline
│   │   │   │   │   ├── AgentExecutionService.java    # Transactional wrapper
│   │   │   │   │   └── AgentTestController.java      # Testing endpoint
│   │   │   │   ├── question/
│   │   │   │   │   └── QuestionAgent.java            # Answer validation
│   │   │   │   ├── safety/
│   │   │   │   │   └── SafetyAgent.java              # Content safety checks
│   │   │   │   └── Agent.java                        # Base agent interface
│   │   │   │
│   │   │   ├── assessment/                     # Assessment module
│   │   │   │   ├── controller/
│   │   │   │   │   ├── AssessmentController.java     # Assessment APIs
│   │   │   │   │   └── SecureAssessmentController.java  # JWT-protected
│   │   │   │   ├── entity/
│   │   │   │   │   ├── Assessment.java
│   │   │   │   │   └── Answer.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── AssessmentRepository.java
│   │   │   │   └── service/
│   │   │   │       └── AssessmentService.java
│   │   │   │
│   │   │   ├── auth/                           # Authentication
│   │   │   │   ├── dto/
│   │   │   │   │   ├── AuthResponse.java
│   │   │   │   │   ├── LoginRequest.java            # With validation
│   │   │   │   │   └── RegisterRequest.java         # With validation
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtProvider.java                 # Token management
│   │   │   │   └── UserPrincipal.java
│   │   │   │
│   │   │   ├── common/                         # Shared components
│   │   │   │   ├── enums/
│   │   │   │   │   └── DoshaType.java              # VATA, PITTA, KAPHA
│   │   │   │   ├── exception/
│   │   │   │   │   ├── ApiErrorResponse.java
│   │   │   │   │   └── GlobalExceptionHandler.java # Validation handler
│   │   │   │   └── logging/
│   │   │   │       └── RequestIdFilter.java        # MDC logging
│   │   │   │
│   │   │   ├── config/                         # Configuration
│   │   │   │   ├── AnthropicConfig.java            # Anthropic AI
│   │   │   │   ├── AsyncConfig.java                # Thread pool
│   │   │   │   ├── OpenAIConfig.java               # OpenAI ChatClient
│   │   │   │   └── SecurityConfig.java             # JWT filter chain
│   │   │   │
│   │   │   ├── diet/                           # Diet module
│   │   │   │   ├── controller/
│   │   │   │   │   └── DietPlanController.java     # CRUD endpoints
│   │   │   │   ├── entity/
│   │   │   │   │   └── DietPlan.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── DietPlanRepository.java
│   │   │   │   └── service/
│   │   │   │       └── DietService.java
│   │   │   │
│   │   │   ├── user/                           # User module
│   │   │   │   ├── controller/
│   │   │   │   │   └── UserController.java         # CRUD endpoints
│   │   │   │   ├── entity/
│   │   │   │   │   └── User.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── UserRepository.java
│   │   │   │   └── service/
│   │   │   │       └── UserService.java
│   │   │   │
│   │   │   └── PrakartiAyurVedaApplication.java    # Main application
│   │   │
│   │   └── resources/
│   │       └── application.properties          # Configuration file
│   │
│   └── test/
│       └── java/com/PrakartiAyurVeda/
│           ├── auth/
│           │   └── JwtProviderTest.java        # JWT tests
│           └── PrakartiAyurVedaApplicationTests.java
│
├── .env                                        # Environment variables (git-ignored)
├── .gitignore                                  # Git ignore rules
├── pom.xml                                     # Maven dependencies
├── run.sh                                      # Startup script
└── README.md                                   # This file
```

## 🏗️ Agent Architecture

The system uses a **Sequential Agent Pipeline** for assessment processing:

### Agent Flow

```
QuestionAgent (1) → DoshaEvaluationAgent (2) → DietRecommendationAgent (3) → SafetyAgent (4)
```

### Agent Details

#### 1. **QuestionAgent** (@Order(1))
- **Purpose**: Validates answers and checks data completeness
- **Responsibilities**:
  - Ensure all required answers are present
  - Validate answer format and values
  - Set processing flag

#### 2. **DoshaEvaluationAgent** (@Order(2))
- **Purpose**: Calculates Vata, Pitta, and Kapha scores
- **Responsibilities**:
  - Sum weighted scores for each dosha
  - Determine dominant dosha
  - Update assessment entity

#### 3. **DietRecommendationAgent** (@Order(3))
- **Purpose**: Generates personalized diet plan using AI
- **Responsibilities**:
  - Build structured prompt for AI
  - Call OpenAI GPT-4 API
  - Parse AI response (JSON)
  - Create DietPlan entity

#### 4. **SafetyAgent** (@Order(4))
- **Purpose**: Validates diet plan content for safety
- **Responsibilities**:
  - Check for harmful recommendations
  - Validate plan completeness
  - Flag unsafe content

### AgentOrchestrator

The orchestrator manages the agent pipeline:
- **Sequential Execution**: Agents run in order
- **Shared Context**: AgentContext passed between agents
- **MDC Logging**: Request tracking across agents
- **Timing Metrics**: Logs execution time for each agent
- **Error Handling**: Pipeline halts on any agent failure

## 🚦 Getting Started

### Prerequisites

- **Java 17** or higher
- **MySQL 8.0** or higher
- **Maven 3.6+**
- **OpenAI API Key** (for diet generation)
- *(Optional)* **Anthropic API Key**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/raghav28o/PrakartiAyurveda.git
   cd PrakartiAyurveda
   ```

2. **Create `.env` file**
   ```bash
   cp .env.example .env
   ```

3. **Configure environment variables in `.env`**
   ```properties
   # Database
   MYSQL_HOST=localhost
   MYSQL_PORT=3306
   MYSQL_USER=root
   MYSQL_PASSWORD=your_password

   # AI APIs
   OPENAI_API_KEY=sk-proj-your-openai-key
   ANTHROPIC_API_KEY=sk-ant-your-anthropic-key  # Optional

   # JWT
   JWT_SECRET=your-256-bit-secret-key
   JWT_EXPIRATION_MS=604800000  # 7 days
   ```

4. **Create MySQL database**
   ```sql
   CREATE DATABASE Ayurveda;
   ```

5. **Build the project**
   ```bash
   mvn clean install
   ```

6. **Run the application**
   ```bash
   # Option 1: Using run script
   ./run.sh

   # Option 2: Using Maven
   mvn spring-boot:run

   # Option 3: Using JAR
   java -jar target/PrakartiAyurVeda-0.0.1-SNAPSHOT.jar
   ```

7. **Verify application is running**
   ```bash
   curl http://localhost:8090/actuator/health
   # Expected: {"status":"UP"}
   ```

## 📡 API Endpoints

### Authentication (Public)

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/auth/register` | Register new user | `{name, email, password}` |
| POST | `/auth/login` | Login and get JWT | `{email, password}` |

### User Management (Authenticated)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/users` | Create user | ✅ |
| GET | `/api/users/{id}` | Get user by ID | ✅ |
| PUT | `/api/users/{id}` | Update user | ✅ |
| DELETE | `/api/users/{id}` | Delete user | ✅ |

### Assessment Management (Authenticated)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/assessments/user/{userId}` | Create assessment | ✅ |
| POST | `/api/assessment/run` | Run agent pipeline | ✅ |
| GET | `/api/assessments/{id}` | Get assessment by ID | ✅ |
| GET | `/api/assessments/user/{userId}` | Get user assessments | ✅ |
| DELETE | `/api/assessments/{id}` | Delete assessment | ✅ |

### Diet Plan Management (Authenticated)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/diets/{id}` | Get diet plan by ID | ✅ |
| GET | `/api/diets/assessment/{assessmentId}` | Get diet by assessment | ✅ |
| DELETE | `/api/diets/{id}` | Delete diet plan | ✅ |

### Health & Monitoring (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/actuator/health` | Health check |
| GET | `/actuator/info` | App information |

## 🔒 Security

### Authentication Flow

1. **Register**: User creates account with encrypted password
2. **Login**: User receives JWT token (7-day expiration)
3. **Authenticated Requests**: Include `Authorization: Bearer <token>` header
4. **Token Validation**: JwtAuthenticationFilter validates each request

### Security Features

- **BCrypt Password Encryption**: Industry-standard hashing
- **JWT Stateless Authentication**: No server-side session storage
- **CORS Support**: Configurable cross-origin requests
- **CSRF Disabled**: Safe for stateless JWT architecture
- **Input Validation**: `@Valid` annotations with custom messages

### Security Headers

```
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
```

## 💾 Database Schema

### Entity Relationships

```
User (1) ──────< (N) Assessment (1) ──────< (N) Answer
                        │
                        │ (1:1)
                        ↓
                    DietPlan
```

### Tables

#### **users**
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
```

#### **assessments**
```sql
CREATE TABLE assessments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    dominant_dosha VARCHAR(10),
    vata_score INT,
    pitta_score INT,
    kapha_score INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### **answers**
```sql
CREATE TABLE answers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    assessment_id BIGINT NOT NULL,
    question_code VARCHAR(50),
    answer_value VARCHAR(255),
    dosha_type VARCHAR(10),
    weight INT,
    FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE
);
```

#### **diet_plans**
```sql
CREATE TABLE diet_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    assessment_id BIGINT UNIQUE NOT NULL,
    dosha_type VARCHAR(10),
    breakfast VARCHAR(500),
    lunch VARCHAR(500),
    dinner VARCHAR(500),
    avoid_foods VARCHAR(500),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE
);
```

## ⚙️ Configuration

### Application Properties

Key configurations in `application.properties`:

```properties
# Server
server.port=8090

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/Ayurveda
spring.jpa.hibernate.ddl-auto=update

# OpenAI
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.model=gpt-4

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration-ms=604800000

# Logging
logging.level.org.springframework.ai=INFO
logging.level.org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor=DEBUG

# Actuator
management.endpoints.web.exposure.include=health,info
```

### Thread Pool Configuration

Async processing configured with:
- **Core Pool Size**: 5 threads
- **Max Pool Size**: 10 threads
- **Queue Capacity**: 100 tasks
- **Thread Prefix**: `async-`

## 🧪 Testing

### Run Tests

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=JwtProviderTest

# Run with coverage
mvn test jacoco:report
```

### Available Tests

- **JwtProviderTest**: JWT token generation, validation, extraction

## 📊 Monitoring & Logging

### Request Tracking

Every request gets a unique `requestId` via MDC:
```
2025-01-01 12:00:00 [requestId=abc123] INFO - Processing assessment...
```

### AI Request Logging

SimpleLoggerAdvisor logs all AI interactions:
```
DEBUG - AI Request: [prompt details]
DEBUG - AI Response: [response details]
```

### Metrics Endpoints

- `/actuator/health` - Application health status
- `/actuator/info` - Application metadata

## 🐛 Error Handling

### Global Exception Handler

All exceptions are caught and formatted:

**Validation Error (400)**
```json
{
  "errors": {
    "email": "Email must be valid",
    "password": "Password must be at least 8 characters"
  },
  "errorCode": "VALIDATION_FAILED",
  "timestamp": "2025-01-01T12:00:00"
}
```

**Not Found (404)**
```json
{
  "message": "Assessment not found with id: 123",
  "errorCode": "NOT_FOUND",
  "timestamp": "2025-01-01T12:00:00"
}
```

**Server Error (500)**
```json
{
  "message": "Something went wrong. Please try again later.",
  "errorCode": "INTERNAL_ERROR",
  "timestamp": "2025-01-01T12:00:00"
}
```

## 🔧 Development

### Code Structure Guidelines

- **Controllers**: Handle HTTP requests, delegate to services
- **Services**: Business logic, transactional operations
- **Repositories**: Database access via JPA
- **DTOs**: Data transfer with validation
- **Entities**: Database models with relationships
- **Agents**: Specialized processing units

### Best Practices

1. ✅ Use `@Valid` for all request bodies
2. ✅ Add `@Transactional` on service methods
3. ✅ Use DTOs instead of exposing entities
4. ✅ Log important operations with MDC context
5. ✅ Handle exceptions with custom messages
6. ✅ Write unit tests for all services

## 📝 TODO / Roadmap

### High Priority
- [ ] Add Swagger/OpenAPI documentation
- [ ] Implement refresh tokens
- [ ] Add pagination to list endpoints
- [ ] Create UserDTO to hide password field
- [ ] Add database indexes for performance

### Medium Priority
- [ ] Implement RBAC (Role-Based Access Control)
- [ ] Add password reset functionality
- [ ] Implement email verification
- [ ] Add rate limiting
- [ ] Improve JSON parsing in DietRecommendationAgent

### Low Priority
- [ ] Add caching layer (Redis)
- [ ] Implement diet plan regeneration
- [ ] Add multi-language support
- [ ] PDF report generation
- [ ] Analytics dashboard

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- **Raghav Agarwal** - [@raghav28o](https://github.com/raghav28o)

## 🙏 Acknowledgments

- **Spring AI Team** - OpenAI/Anthropic integration
- **OpenAI** - GPT-4 API for diet generation
- **Anthropic** - Claude AI (optional integration)

---

**Built with ❤️ using Spring Boot and AI**

For questions or support, please open an issue on GitHub.
