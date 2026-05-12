# Private Shared Library

A Spring Boot application for a private book-sharing club among friends and family.

## 🚀 Current Status
The application is currently a **functional MVP (Minimum Viable Product)** that provides a full backend API and a minimalist web frontend.

- **What it does:** Allows users to manage a peer-to-peer library. You can add members, register books owned by those members, and track borrowing/returning transactions between members.
- **Dashboard UI:** A modern, professional dashboard accessible at `http://localhost:8080/`. It features summary stats, real-time search, and a sidebar layout.
- **Interactive API Docs:** Accessible via Swagger UI at `http://localhost:8080/swagger-ui.html`. This is primarily for developers to test raw endpoints.
- **Intentionally Omitted Features:** To keep this educational project focused on core Spring Boot patterns, the following are not yet implemented:
    - **Authentication/Login:** The system assumes a trusted environment; there are no passwords or user sessions.
    - **Cloud Deployment:** Configured for local development.
    - **Persistent Production DB:** Currently defaults to an H2 in-memory database for easy testing, but can be switched to MySQL in `application.yaml`.

## 📖 Features
- **Dashboard Overview**: At-a-glance metrics for collection size and borrowing status.
- **Member Management**: Add friends and family to the club.
- **Private Inventory**: Track books owned by members with live search and filtering.
- **Smart Borrowing**: Dynamic dropdowns only show available books, preventing double-borrowing.
- **One-Click Returns**: Easily manage active loans from the dashboard.
- **Audit Logs**: Automatically tracks when members and books were added.

## ⚙️ How to Run
1. **Prerequisites:** Java 17+ and Maven installed.
2. **Standard Run:** Execute `mvn spring-boot:run`.
3. **Database:** By default, it uses an in-memory H2 database. To use MySQL:
    - Ensure MySQL is running and a database `library-management` is created.
    - Update `src/main/resources/application.yaml` with your credentials.
4. **Access:**
    - **Frontend:** Open `http://localhost:8080` in your browser.
    - **Swagger:** Open `http://localhost:8080/swagger-ui.html`.

## 📡 API Endpoints

### Members
- `POST /api/members` - Join the club.
- `GET /api/members` - List all members.

### Books
- `POST /api/books` - Register a book you own.
- `GET /api/books` - View all books in the club (supports pagination).
- `GET /api/books/available` - See what you can borrow right now.

### Loans
- `POST /api/loans` - Borrow a book (requires `bookId` and `borrowerId`).
- `PUT /api/loans/{id}/return` - Mark a book as returned.
- `GET /api/loans/active` - See all books currently out.

## 🔮 Future Ideas
- **Notifications:** Email reminders when a book is due back.
- **Wishlists:** Request books from other members.
- **Reviews:** Rate and comment on books you've borrowed.
