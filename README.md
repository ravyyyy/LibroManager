# LibroManager - Library Management System

## Prerequisites
* **Java 17** or higher
* **Maven** installed
* **PostgreSQL** database running on port 5432

## Database Configuration
Ensure you have a PostgreSQL database named `librodba`.
Configure your username and password in `src/main/resources/application.properties` or via environment variables.

## How to Run
1.  Open a terminal in the project root.
2.  Run the command:
    ```bash
    mvn spring-boot:run
    ```
3.  The application will start at `http://localhost:8080`.

## API Documentation (Swagger)
Once the application is running, you can access the interactive API documentation here:
 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

## Main Endpoints Overview
* **Books**
    * `GET /api/books` - List all books (Sorted by ID)
    * `POST /api/books` - Add a new book
    * `PUT /api/books/{id}` - Update book details
    * `DELETE /api/books/{id}` - Delete a book
    * `GET /api/books/search?title=...` - Search by title

* **Loans**
    * `GET /api/loans` - List all loans
    * `POST /api/loans` - Create a loan (Requires `readerId` and `bookId`)
    * `PUT /api/loans/{id}` - Update loan (e.g., mark as COMPLETED to return book)
    * `DELETE /api/loans/{id}` - Cancel/Delete a loan

* **Readers**
    * `GET /api/readers` - List all readers
    * `POST /api/readers` - Register a new reader
    * `PUT /api/readers/{id}` - Update reader profile
    * `DELETE /api/readers/{id}` - Delete a reader (Cascades to loans/reviews)

* **Reviews**
    * `POST /api/reviews` - Add a review
    * `GET /api/reviews/book/{id}` - Get reviews for a specific book
    * `PUT /api/reviews/{id}` - Edit a review
    * `DELETE /api/reviews/{id}` - Delete a review

* **Authors & Publishers**
    * Full CRUD endpoints available at `/api/authors` and `/api/publishers`.