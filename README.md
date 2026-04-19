# Private Shared Library

A Spring Boot backend for a private book-sharing club among friends and family.

## 📖 Features
- **Member Management:** Add friends and family to the club.
- **Private Inventory:** Track books owned by members.
- **Borrowing System:** See who has which book and when it's due.
- **Availability Tracking:** Filter for books that are currently available to borrow.

## 🗄 Data Model
- **Member:** Name, Email, owned books, active loans.
- **Book:** Title, Author, ISBN, and its Owner (Member).
- **Loan:** Tracking the transaction between a Book and a Borrower (Member).

## ⚙️ How to Run
1. Ensure MySQL is running and a database `library-management` is created.
2. Update `src/main/resources/application.yaml` with your credentials.
3. Run: `mvn spring-boot:run`

## 📡 API Endpoints

### Members
- `POST /api/members` - Join the club.
- `GET /api/members` - List all members.

### Books
- `POST /api/books` - Register a book you own.
- `GET /api/books` - View all books in the club.
- `GET /api/books/available` - See what you can borrow right now.

### Loans
- `POST /api/loans` - Borrow a book (requires `bookId` and `borrowerId`).
- `PUT /api/loans/{id}/return` - Mark a book as returned.
- `GET /api/loans/active` - See all books currently out.

## 🔮 Future Ideas
- **Notifications:** Email reminders when a book is due back.
- **Wishlists:** Request books from other members.
- **Reviews:** Rate and comment on books you've borrowed.
