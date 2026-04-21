# 📚 Shared Library - Learning Notes

This document summarizes the professional improvements made to transform a student project into a Private Shared Library application.

---

## 🏆 Top 5 Most Important Changes

### 1. Ownership-based Data Model
- **Original Problem:** The project modeled a single physical "Library" building.
- **New Solution:** Changed to a peer-to-peer model where individual **Members** own books.
- **Why Better:** It reflects real-world private sharing among friends. Data models should always match the "Domain" (the real problem you are solving).
- **Study Concept:** **Domain-Driven Design (DDD)** basics and JPA relationships.

### 2. DTO Decoupling (Separation of Concerns)
- **Original Problem:** Database entities were used directly in the API controllers.
- **New Solution:** Created a dedicated `dto` package with single-purpose classes (e.g., `BookDto`).
- **Why Better:** Protecting your internal database structure from the public API is critical for security and future changes.
- **Study Concept:** **DTO (Data Transfer Object)** pattern and why "leaking" entities is a risk.

### 3. Business Rule Enforcement
- **Original Problem:** The app allowed "impossible" states, like borrowing a book that was already lent out.
- **New Solution:** Added checks in `SharedLibraryService.borrowBook` using Java Streams and active loan filtering.
- **Why Better:** Real applications must protect the integrity of their data. Logic belongs in the Service layer, not the Controller.
- **Study Concept:** **Java Stream API** for data processing.

### 4. Standardized Error Handling
- **Original Problem:** The API returned raw error maps or leaked internal stack traces.
- **New Solution:** Implemented a unified `ErrorResponse` DTO and a `@RestControllerAdvice`.
- **Why Better:** Standardized JSON errors make it much easier for frontend developers to handle bugs and show user-friendly messages.
- **Study Concept:** **Global Exception Handling** in Spring Boot.

### 5. Pagination and Sorting
- **Original Problem:** Listing all books returned every single record at once, which fails as data grows.
- **New Solution:** Integrated `Pageable` and `Page<T>` into the Service and Controller layers.
- **Why Better:** This is an essential "enterprise-grade" feature that improves performance and professional usability.
- **Study Concept:** **Spring Data JPA Pagination** and the `Page` interface.

---

## 🛠 File-by-File Changes

### 1. `Member.java` / `MemberDto.java`
- **What changed:** Replaced "Borrower" with "Member" and added an `ownedBooks` relationship.
- **Label:** `refactor` / `new feature`

### 2. `SharedLibraryService.java`
- **What changed:** Implemented filtered retrieval (availability check) and core borrowing rules.
- **Label:** `refactor` / `new feature`

### 3. `GlobalErrorHandler.java`
- **What changed:** Standardized error formats for `IllegalStateException` and `NoSuchElementException`.
- **Label:** `refactor`

### 4. `BookRepository.java`
- **What changed:** Added a custom JPQL query `findAvailableBooks` to identify books not currently on loan.
- **Label:** `new feature`

---

## 🧠 Concepts to Review
- **JPA Auditing:** See `AbstractAuditingEntity` to learn how `createdAt` is handled automatically.
- **Mocking:** Check `SharedLibraryServiceTest` to understand testing logic without a database.
- **Jakarta Validation:** Review annotations like `@NotBlank` and `@Email` in the DTOs.
