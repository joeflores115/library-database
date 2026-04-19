# 📚 Shared Library - Learning Notes (Evolution)

This document maps the transformation of a student library project into a real-world Private Shared Library app for friends and family.

---

## 🛠 Project Evolution

### 1. Data Model Shift (Ownership over Location)
- **Files:** `Member.java`, `Book.java`
- **What changed:** Replaced the concept of a central "Library" building with individual "Members" who own books.
- **Why:** In a shared library among friends, books live at people's houses. The system now tracks **who owns** the book and **who has** the book.
- **Label:** `refactor` / `new feature`
- **Review Concept:** Many-to-One relationships (A member can own many books; a book has one owner).

### 2. Loans vs. Checkouts
- **File:** `Loan.java` (previously `Loan.java`)
- **What changed:** Renamed "Checkout" to "Loan" to better fit the informal borrowing context.
- **Why:** Using precise domain language (Domain Driven Design Lite) makes the code more intuitive.
- **Label:** `refactor`
- **Review Concept:** Domain-Driven Design (DDD) naming.

### 3. Business Rule Enforcement
- **File:** `SharedLibraryService.java`
- **What changed:** Added logic to prevent borrowing a book that is already out.
- **Why:** Real-world systems must enforce constraints to prevent impossible states (like two people having the same physical book).
- **Label:** `new feature`
- **Review Concept:** Java Stream API for filtering and matching.

### 4. API Simplification
- **File:** `SharedLibraryController.java`
- **What changed:** Flattened the API paths (e.g., `/api/loans` instead of `/libraries/{id}/books/checkouts`).
- **Why:** Since there's no central library anymore, the API is easier to use when it focuses on the primary actions (members, books, loans).
- **Label:** `refactor`
- **Review Concept:** RESTful path design.

---

## 🧠 Concepts to Review
- **JPA Queries:** Look at `BookRepository.findAvailableBooks()` to see how to find items NOT in another table.
- **Mocking:** See `SharedLibraryServiceTest` to learn how to test logic without a real database.
- **Validation:** Notice how `@Valid` and `@NotBlank` protect the app from bad data.
