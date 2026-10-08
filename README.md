# Assessment Service — Smart Assessment Platform

A production-ready microservice built with **Java 21 LTS**, **Spring Boot 3.3.4**, **Maven**, and **MySQL 8.x** for managing the complete assessment configuration, sections, proctoring rules, candidate enrollments, and examination lifecycle contracts.

---

## 1. Overview & Service Responsibilities

As specified in the **Smart Assessment Platform Backend FRS (Sections 5, 6, 9, 32–63)**, the **Assessment Service** acts as the central examination configuration engine.

### Key Capabilities:
- **Assessment Management**: Create, update, soft-delete, search, publish, and archive assessments with custom timers, passing criteria, and category tagging.
- **Section Management**: Multi-section exam structures (e.g. MCQ, Live Coding, Debugging) with custom weightage, cutoff marks, section timers, and question bank distribution (Easy/Medium/Hard).
- **Proctoring Rules Configuration**: Anti-cheating controls including webcam requirement, microphone requirement, fullscreen lock, tab-switch warning limits (e.g., max 3 warnings), AI face detection, and copy-paste blocking.
- **Candidate Enrollment & Invitation**: Single & bulk candidate roster enrollment, unique UUID invitation token generation, candidate schedule windows, and asynchronous email triggers via Notification Service.
- **Candidate Access Verification**: Authoritative pre-exam verification validating invitation tokens, access windows, passcodes, and device requirements before exam entry.
- **Inter-Service Configuration Export**: Delivers full assessment configurations, timers, and rules to `ExamSessionService`, `SubmissionService`, and `EvaluationService`.
- **Comprehensive Audit Trail**: Automatically tracks administrative operations (`ASSESSMENT_CREATED`, `ASSESSMENT_PUBLISHED`, `SECTION_ADDED`, `RULES_UPDATED`, `CANDIDATE_ENROLLED`).
- **Standardized Error Handling**: Strict centralized exception handling with consistent JSON error response schema and distributed tracing (`traceId`).

---

## 2. Technology Stack & Dependencies

| Component | Technology / Version | Purpose |
| :--- | :--- | :--- |
| **Language** | Java 21 LTS | Core programming language |
| **Framework** | Spring Boot 3.3.4 | Microservice framework |
| **Build Tool** | Maven 3.9.x | Dependency & build management |
| **Database** | MySQL 8.x | Persistent relational data store |
| **Database Migrations** | Flyway | Controlled, versioned schema management |
| **Security** | Spring Security & JJWT | Stateless JWT authentication & role authorization |
| **Data Access** | Spring Data JPA / Hibernate | High-performance ORM (`ddl-auto=validate`) |
| **Validation** | Jakarta Bean Validation | Request payload validation (`@Valid`, `@NotNull`) |
| **API Documentation**| SpringDoc OpenAPI 2.6.0 | Swagger UI interactive documentation |
| **Testing** | JUnit 5 & Mockito | Unit, slice, and integration testing |

---

## 3. Microservice Project Structure

```
assessment-service/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/smartassessment/assessment/
│   │   │   ├── AssessmentServiceApplication.java
│   │   │   ├── client/                 # Inter-service REST clients with fallback handling
│   │   │   │   ├── IdentityServiceClient.java
│   │   │   │   ├── NotificationServiceClient.java
│   │   │   │   └── QuestionBankClient.java
│   │   │   ├── config/                 # Spring Security, OpenAPI, JPA, CORS configuration
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── security/
│   │   │   │       ├── CorrelationIdFilter.java
│   │   │   │       ├── JwtAuthenticationFilter.java
│   │   │   │       ├── JwtTokenProvider.java
│   │   │   │       ├── SecurityConfig.java
│   │   │   │       └── UserPrincipal.java
│   │   │   ├── controller/             # REST API Controllers
│   │   │   │   ├── AssessmentAdminController.java
│   │   │   │   ├── AssessmentCandidateController.java
│   │   │   │   ├── AssessmentEnrollmentController.java
│   │   │   │   ├── AssessmentInternalController.java
│   │   │   │   ├── AssessmentProctoringController.java
│   │   │   │   └── AssessmentSectionController.java
│   │   │   ├── dto/                    # Request & Response Contracts
│   │   │   │   ├── request/
│   │   │   │   │   ├── AssessmentSearchCriteria.java
│   │   │   │   │   ├── BulkEnrollCandidatesRequest.java
│   │   │   │   │   ├── CreateAssessmentRequest.java
│   │   │   │   │   ├── CreateSectionRequest.java
│   │   │   │   │   ├── EnrollCandidateRequest.java
│   │   │   │   │   ├── PublishAssessmentRequest.java
│   │   │   │   │   ├── UpdateAssessmentRequest.java
│   │   │   │   │   ├── UpdateProctoringRulesRequest.java
│   │   │   │   │   ├── UpdateSectionRequest.java
│   │   │   │   │   └── VerifyPasscodeRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── ApiErrorResponse.java
│   │   │   │       ├── ApiResponse.java
│   │   │   │       ├── AssessmentAuditLogResponse.java
│   │   │   │       ├── AssessmentDetailResponse.java
│   │   │   │       ├── AssessmentResponse.java
│   │   │   │       ├── AssessmentSummaryResponse.java
│   │   │   │       ├── CandidateAssessmentAccessResponse.java
│   │   │   │       ├── EnrollmentResponse.java
│   │   │   │       ├── ExamSessionAssessmentConfigResponse.java
│   │   │   │       ├── PagedResponse.java
│   │   │   │       ├── ProctoringRuleResponse.java
│   │   │   │       └── SectionResponse.java
│   │   │   ├── entity/                 # JPA Entities & Enums
│   │   │   │   ├── AccessType.java
│   │   │   │   ├── Assessment.java
│   │   │   │   ├── AssessmentAuditLog.java
│   │   │   │   ├── AssessmentEnrollment.java
│   │   │   │   ├── AssessmentProctoringRule.java
│   │   │   │   ├── AssessmentSection.java
│   │   │   │   ├── AssessmentStatus.java
│   │   │   │   ├── AuditAction.java
│   │   │   │   ├── EnrollmentStatus.java
│   │   │   │   └── QuestionType.java
│   │   │   ├── exception/              # Centralized Error Handling
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── DuplicateResourceException.java
│   │   │   │   ├── ExternalServiceException.java
│   │   │   │   ├── ForbiddenException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── InvalidRequestException.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── UnauthorizedException.java
│   │   │   │   └── ValidationException.java
│   │   │   ├── mapper/                 # Entity <-> DTO Mappers
│   │   │   │   ├── AssessmentMapper.java
│   │   │   │   ├── AuditLogMapper.java
│   │   │   │   ├── EnrollmentMapper.java
│   │   │   │   ├── ProctoringRuleMapper.java
│   │   │   │   └── SectionMapper.java
│   │   │   ├── repository/             # Spring Data JPA Repositories
│   │   │   │   ├── AssessmentAuditLogRepository.java
│   │   │   │   ├── AssessmentEnrollmentRepository.java
│   │   │   │   ├── AssessmentProctoringRuleRepository.java
│   │   │   │   ├── AssessmentRepository.java
│   │   │   │   └── AssessmentSectionRepository.java
│   │   │   └── service/                # Business Logic Interfaces & Implementations
│   │   │       ├── AssessmentService.java
│   │   │       ├── AuditLogService.java
│   │   │       ├── EnrollmentService.java
│   │   │       ├── ProctoringRuleService.java
│   │   │       ├── SectionService.java
│   │   │       └── impl/
│   │   │           ├── AssessmentServiceImpl.java
│   │   │           ├── AuditLogServiceImpl.java
│   │   │           ├── EnrollmentServiceImpl.java
│   │   │           ├── ProctoringRuleServiceImpl.java
│   │   │           └── SectionServiceImpl.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   │           ├── V1__init_assessment_schema.sql
│   │           └── V2__seed_sample_assessments.sql
│   └── test/
│       ├── java/com/smartassessment/assessment/
│       │   ├── controller/AssessmentAdminControllerTest.java
│       │   ├── exception/GlobalExceptionHandlerTest.java
│       │   └── service/
│       │       ├── AssessmentServiceTest.java
│       │       ├── EnrollmentServiceTest.java
│       │       └── SectionServiceTest.java
│       └── resources/
│           └── application.properties
```

---

## 4. REST API Reference

### 4.1 Admin Assessment Endpoints (`/api/assessments/admin/**`)
- `POST /api/assessments/admin` — Create new assessment (DRAFT status).
- `GET /api/assessments/admin` — Search & list assessments (filters: `query`, `category`, `status`, pagination).
- `GET /api/assessments/admin/{id}` — Get complete assessment details.
- `GET /api/assessments/admin/code/{code}` — Get assessment by unique code.
- `PUT /api/assessments/admin/{id}` — Update assessment configuration.
- `DELETE /api/assessments/admin/{id}` — Soft delete assessment.
- `POST /api/assessments/admin/{id}/publish` — Validate configuration and publish assessment.
- `POST /api/assessments/admin/{id}/archive` — Archive assessment.
- `GET /api/assessments/admin/{id}/audit-logs` — Retrieve immutable audit log trail.

### 4.2 Section Management (`/api/assessments/admin/{assessmentId}/sections/**`)
- `POST /` — Add section (MCQ, CODING, DEBUGGING, TEXT).
- `GET /` — List sections in sequence order.
- `GET /{sectionId}` — Get section details.
- `PUT /{sectionId}` — Update section parameters and question counts.
- `DELETE /{sectionId}` — Delete section.
- `PUT /reorder` — Reorder sections sequence.

### 4.3 Proctoring Configuration (`/api/assessments/admin/{assessmentId}/proctoring-rules/**`)
- `GET /` — Get proctoring and anti-cheating configuration.
- `PUT /` — Update camera, mic, fullscreen, tab-switch warning limits, and AI face-matching rules.

### 4.4 Candidate Enrollment (`/api/assessments/admin/{assessmentId}/enrollments/**`)
- `POST /` — Enroll single candidate & generate invitation token.
- `POST /bulk` — Bulk enroll candidate roster and trigger notifications.
- `GET /` — Get paginated enrollment list with status filters.
- `PATCH /{enrollmentId}/status` — Update enrollment status.
- `DELETE /{enrollmentId}` — Cancel candidate enrollment.

### 4.5 Candidate Portal Endpoints (`/api/assessments/candidate/**`)
- `GET /verify-access/{invitationToken}` — Candidate checks entry validity, schedule window, instructions, and proctoring requirements.
- `POST /{id}/verify-passcode` — Validates passcode for protected assessments.
- `GET /my-assessments` — List assessments assigned to authenticated candidate.

### 4.6 Internal Inter-Service Endpoints (`/api/assessments/internal/**`)
- `GET /{id}/config` — Exports complete exam configuration (timers, navigation rules, sections, proctoring constraints) for `ExamSessionService`.

---

## 5. Standard Error Handling Specification (Section 46)

All error responses strictly follow the centralized specification:

```json
{
  "timestamp": "2026-09-22T10:20:00Z",
  "status": 404,
  "error": "RESOURCE_NOT_FOUND",
  "message": "Assessment not found with id: 123",
  "path": "/api/assessments/admin/123",
  "traceId": "c8a4f910-0bf1-42e1-a20c-c76b91176b6b"
}
```

---

## 6. How to Build & Run

### Prerequisites:
- Java 21 LTS installed
- MySQL 8.x running on localhost:3306 (or configured via environment variables)

### Configuration Environment Variables:
```properties
SERVER_PORT=8082
DB_HOST=localhost
DB_PORT=3306
DB_NAME=assessment_db
DB_USERNAME=root
DB_PASSWORD=root
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
```

### Build and Run with Maven:
```bash
# Clean and compile
mvn clean package

# Run unit and integration tests
mvn test

# Start the Spring Boot application
mvn spring-boot:run
```

### Access Documentation:
- Swagger UI: `http://localhost:8082/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8082/v3/api-docs`
- Health Endpoint: `http://localhost:8082/actuator/health`
