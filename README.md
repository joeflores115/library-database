# Library Management System

A robust, professional backend application for managing a distributed network of libraries. This system handles the complex relationships between libraries, their physical book inventory, registered borrowers, and the lifecycle of book checkouts and returns.

## 🚀 Overview
This project was developed as a comprehensive exercise in modern Java backend development, focusing on RESTful API design, database integrity, and professional coding standards using the Spring ecosystem.

### Core Features
- **Multi-Library Management:** Register and manage multiple physical library branches.
- **Inventory Tracking:** Maintain detailed book records within specific branches, including automatic quantity tracking.
- **Borrower Registry:** Centralized management of library patrons.
- **Transaction Lifecycle:** Robust checkout and return logic with built-in availability checks and "undo" capabilities.
- **Automatic Auditing:** All records automatically track their creation and last update timestamps.
- **API Documentation:** Interactive documentation powered by Swagger UI for easy integration and testing.

## 🛠 Technologies Used
- **Language:** Java 17
- **Framework:** Spring Boot 3.4.4
- **Persistence:** Spring Data JPA / Hibernate
- **Database:** MySQL (Production/Development), H2 (Testing)
- **Validation:** Jakarta Bean Validation (Hibernate Validator)
- **Documentation:** OpenAPI 3 / springdoc-openapi
- **Testing:** JUnit 5, Mockito, Spring Boot Test
- **Build Tool:** Maven
- **Utilities:** Lombok

## 📂 Project Structure
The project follows a clean, layered architecture to ensure separation of concerns and maintainability:
- `controller/`: REST API endpoints and request mapping using Spring MVC.
- `service/`: Business logic layer where transactions are managed and core rules are enforced.
- `dao/` (Repository): Data access layer utilizing Spring Data JPA for boilerplate-free database operations.
- `entity/`: JPA entities representing the relational database schema, featuring auditing and cascading.
- `model/` (DTO): Data Transfer Objects used to decouple the API contract from the internal database model, also serving as the primary layer for input validation.
- `exception/`: Dedicated package for custom business exceptions, improving code readability.
- `controller/error/`: Centralized Global Error Handler (`@RestControllerAdvice`) for standardized, user-friendly API error responses.

## ⚙️ Setup and Run Instructions

### Prerequisites
- JDK 17 or higher
- Maven 3.6+
- MySQL Server

### Database Configuration
1. Create a MySQL database named `library-management`.
2. Update the credentials in `src/main/resources/application.yaml`.

### Running the Application
```bash
mvn spring-boot:run
```
- **API Base URL:** `http://localhost:8080/libraries`
- **Swagger UI (Interactive Docs):** `http://localhost:8080/swagger-ui.html`

### Running Tests
```bash
mvn test
```

## ⚠️ Current Limitations
- **Security:** No authentication or authorization is currently implemented.
- **Search:** Basic search functionality is limited to simple title/author matches.
- **Media:** Only physical books are supported; no support for digital media or magazines.

## 💡 What I Learned
- **RESTful Best Practices:** I learned the importance of plural resource naming (e.g., `/libraries` vs `/library`), logical endpoint hierarchies, and the consistent use of HTTP verbs to create intuitive APIs.
- **Data Integrity and Validation:** I implemented multi-level validation using Jakarta Bean Validation (JSR-303) annotations on DTOs and reinforced it with service-layer logic to ensure the system remains in a valid state.
- **JPA and Hibernate Mastery:** I explored advanced JPA features like `CascadeType`, `OrphanRemoval`, and `MappedSuperclass` for auditing, which significantly reduced boilerplate code and improved data management efficiency.
- **Defensive Programming:** I moved away from inner-class exception handling to a dedicated exception package and utilized a Global Error Handler to ensure the API never leaks internal stack traces, providing clean JSON error feedback instead.
- **Verification and Testing:** I learned how to use Mockito to isolate and test business rules (like book quantity transitions during checkout) and `MockMvc` for verifying API contracts and validation logic in a near-production environment.
- **Professional Project Hygiene:** This project taught me the value of a clean repository—removing IDE-specific artifacts and binary files—and the critical role of documentation in the software development lifecycle.

## 🔮 Possible Next Improvements
- **Security Integration:** Implement Spring Security with JWT for protected access.
- **Pagination & Sorting:** Add support for large datasets using Spring Data `Pageable`.
- **Soft Deletion:** Implement a "trash" system instead of hard-deleting historical data.
- **Dockerization:** Create a `Dockerfile` and `docker-compose.yaml` for one-command deployment.
- **Frontend Client:** Build a React or Vue.js dashboard to consume this API.

---

## 👨‍🏫 Student Feedback & Refactor Summary (Teaching Review)

This project underwent a comprehensive refactor to move it from a "beginner student" state to a "professional portfolio" state. Below are the key teaching points from that review:

### 1. Separation of Concerns (DTO vs Entity)
- **Problem:** Originally, the project used nested static classes within a single file to handle both database logic and API responses.
- **Fix:** Created a dedicated `dto` package. Each resource (Library, Book, etc.) now has its own DTO file.
- **Learning Point:** Decoupling your API contract (DTOs) from your internal data model (Entities) allows you to change your database without breaking your API users.

### 2. Package Organization
- **Problem:** Data access objects were in a package named `dao`.
- **Fix:** Renamed the package to `repository`.
- **Learning Point:** While "DAO" is a valid pattern, the Spring community almost universally uses the "Repository" naming convention when working with Spring Data JPA.

### 3. Standardized Error Handling
- **Problem:** Error responses were inconsistent, sometimes returning raw Maps and other times leaking internal exception messages.
- **Fix:** Implemented a unified `ErrorResponse` DTO and updated the `GlobalErrorHandler`.
- **Learning Point:** Professional APIs should always return a predictable JSON structure for errors (e.g., status, message, timestamp) so that frontend clients can handle them gracefully.

### 4. RESTful API Design
- **Problem:** Some endpoint paths were overly complex or action-oriented rather than resource-oriented.
- **Fix:** Simplified paths like `POST /checkouts` and utilized request bodies for IDs instead of path variables when appropriate.
- **Learning Point:** REST APIs should be intuitive. Focus on "Resources" (Nouns) and use HTTP Verbs (GET, POST, PUT, DELETE) to define "Actions".

### 5. Code Hygiene
- **Problem:** Unused parameters and redundant transactional attributes cluttered the code.
- **Fix:** Removed default `(readOnly = false)` from `@Transactional` and purged unused method arguments.
- **Learning Point:** "Clean Code" reduces cognitive load. If code doesn't serve a purpose, it shouldn't be there.
