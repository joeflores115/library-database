# Library Management System

This is a Spring Boot application for managing a library system, including libraries, books, borrowers, and checkouts.

## Features
- Manage multiple libraries and their inventory.
- Track book availability and checkout history.
- Input validation to ensure data integrity.
- Centralized error handling.

## Setup Instructions

### Prerequisites
- Java 17
- Maven
- MySQL (or another SQL database)

### Database Configuration
Update `src/main/resources/application.yaml` with your database connection details.

### Running the Application
```bash
mvn spring-boot:run
```

## API Usage

### Libraries
- `GET /library` - List all libraries.
- `POST /library` - Create a new library.

### Books
- `GET /library/{libraryId}/book` - List books in a library.
- `POST /library/{libraryId}/book` - Add a book.

### Checkouts
- `POST /library/book/{bookId}/checkout/{borrowerId}` - Checkout a book.
- `PUT /library/book/{bookId}/checkout/{checkoutId}` - Return a book or update checkout details.
