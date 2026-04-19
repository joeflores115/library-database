# 📚 Learning Notes: Library Management Refactor

This document summarizes the professional improvements made to the Library Management System. Use this as a guide to understand the "why" behind modern backend development practices.

---

## 🛠 Important Changes

### 1. Entity Naming and Modeling
- **File:** `Library.java` (previously `Libraries.java`)
- **What was changed:** Renamed the class and file from plural to singular.
- **Before:** `public class Libraries { ... }`
- **Now:** `public class Library { ... }`
- **Why:** In JPA (Java Persistence API), a class represents a **single row** in the database. "Libraries" implies a collection, whereas "Library" correctly describes a single library record.
- **Label:** `refactor`

### 2. Data Retrieval Filtering
- **File:** `LibraryManagementService.java`
- **What was changed:** Updated retrieval methods to filter results by `libraryId`.
- **Before:** `BookDao.findAll()` (Returned every book in the entire database).
- **Now:** `bookRepository.findAllByLibraryLibraryId(libraryId)` (Returns only books for a specific library).
- **Why:** This fixed a "data leakage" bug. Previously, if you asked for books from Library A, the API would incorrectly show you books from Library B and C as well.
- **Label:** `bug fix`

### 3. DTO Pattern Implementation
- **Files:** `LibraryDto.java`, `BookDto.java`, `BorrowerDto.java`, `CheckoutDto.java`
- **What was changed:** Split the monolithic `LibraryManagementData.java` into individual, decoupled files.
- **Before:** One giant file with multiple nested static classes used for both input and output.
- **Now:** A dedicated `dto` package with clean, single-purpose classes.
- **Why:** This improves maintainability. Decoupling your API "Contract" (DTOs) from your "Internal Model" (Entities) means you can change your database structure without breaking the API for your users.
- **Label:** `refactor`

### 4. RESTful API Design
- **File:** `LibraryManagementController.java`
- **What was changed:** Pluralized endpoint paths and simplified checkout registration.
- **Before:** `POST /library/book/borrower`, `POST /book/{bookId}/checkout/{borrowerId}`
- **Now:** `GET /libraries`, `POST /borrowers`, `POST /checkouts` (using request body for IDs).
- **Why:** REST best practices use plural nouns for resources. Simplifying paths makes the API more intuitive and predictable for other developers.
- **Label:** `refactor`

### 5. Input Validation
- **File:** `LibraryDto.java`, `BookDto.java`, etc.
- **What was changed:** Added Jakarta Validation annotations like `@NotBlank`, `@Min`, and `@Email`.
- **Before:** No validation; the system would crash or save "empty" books if bad data was sent.
- **Now:** Fields are validated at the entry point; the API returns clear error messages.
- **Why:** This is "Defensive Programming." It ensures that only valid data ever reaches your business logic and database.
- **Label:** `new feature` / `refactor`

### 6. Centralized Exception Handling
- **File:** `GlobalErrorHandler.java` & `ErrorResponse.java`
- **What was changed:** Implemented a standardized JSON error response.
- **Before:** Returned raw Java Maps or leaked internal stack traces.
- **Now:** Returns a consistent object with `timestamp`, `status`, `error`, and `message`.
- **Why:** Professional APIs should always be predictable. Standardized errors help frontend developers write better logic to show users what went wrong.
- **Label:** `refactor`

### 7. Automatic Auditing
- **File:** `AbstractAuditingEntity.java`
- **What was changed:** Added automatic tracking for `createdAt` and `updatedAt` timestamps.
- **Before:** No record of when data was added or changed.
- **Now:** All entities automatically record timestamps using `@EnableJpaAuditing`.
- **Why:** Auditing is a standard requirement for production systems to track history and debug data issues.
- **Label:** `new feature`

---

## 🧠 Concepts I should review based on these changes

1.  **DTO (Data Transfer Object) Pattern:** Understand why we never want to expose our database Entities directly to the API.
2.  **Spring Data JPA Derived Queries:** Review how naming a method `findAllByLibraryLibraryId` allows Spring to write the SQL for you automatically.
3.  **RESTful Resource Naming:** Learn the difference between "Action-based" URLs and "Resource-based" URLs.
4.  **JPA Cascading (`CascadeType`):** Review how deleting a Library can automatically clean up its Books and Checkouts safely.
5.  **Centralized Error Handling (`@RestControllerAdvice`):** Understand how to intercept exceptions across your entire application to provide uniform responses.
6.  **Jakarta Bean Validation:** Study annotations like `@Size`, `@NotNull`, and `@Valid` to enforce data integrity.
