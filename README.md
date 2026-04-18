# Library Management System

This is a Spring Boot application for managing a library system, including libraries, books, borrowers, and checkouts.

## Features
- Manage multiple libraries.
- Manage books within each library.
- Register borrowers.
- Handle book checkouts and returns.
- Track book availability.

## Technologies Used
- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- MySQL (for production/development)
- H2 (for testing)
- Lombok
- Maven

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MySQL Server

### Database Configuration
1. Create a MySQL database named `library-management`.
2. Create a user `library-management` with password `library-management` and grant all privileges on the database.
   ```sql
   CREATE DATABASE `library-management`;
   CREATE USER 'library-management'@'localhost' IDENTIFIED BY 'library-management';
   GRANT ALL PRIVILEGES ON `library-management`.* TO 'library-management'@'localhost';
   FLUSH PRIVILEGES;
   ```

### Running the Application
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080`.

## API Endpoints

### Libraries
- `POST /library` - Create a new library.
- `GET /library` - Retrieve all libraries.
- `GET /library/{libraryId}` - Retrieve a library by ID.
- `PUT /library/{libraryId}` - Update a library.
- `DELETE /library/{libraryId}` - Delete a library.

### Books
- `POST /library/{libraryId}/book` - Add a book to a library.
- `GET /library/{libraryId}/book` - Retrieve all books for a library.
- `GET /library/{libraryId}/book/{bookId}` - Retrieve a book by ID.
- `PUT /library/{libraryId}/book/{bookId}` - Update a book.
- `DELETE /library/{libraryId}/book/{bookId}` - Delete a book.

### Borrowers
- `POST /library/{libraryId}/book/borrower` - Register a new borrower.
- `GET /library/{libraryId}/book/borrower` - Retrieve all borrowers.
- `GET /library/{libraryId}/book/borrower/{borrowerId}` - Retrieve a borrower by ID.
- `PUT /library/{libraryId}/book/borrower/{borrowerId}` - Update a borrower.
- `DELETE /library/book/borrower/{borrowerId}` - Delete a borrower.

### Checkouts
- `POST /library/book/{bookId}/checkout/{borrowerId}` - Checkout a book.
- `GET /library/{libraryId}/book/checkout` - Retrieve all checkouts for a library.
- `GET /library/{libraryId}/book/checkout/{checkoutId}` - Retrieve a checkout by ID.
- `PUT /library/book/{bookId}/checkout/{checkoutId}` - Return a book or update checkout.
- `DELETE /library/checkout/{checkoutId}` - Delete a checkout record.
