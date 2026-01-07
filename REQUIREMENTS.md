# Business Requirements - LibroManager

### 1. Project Description
LibroManager is a robust Library Management System designed to handle the entire lifecycle of library operations. It allows for the complete administration of books, authors, publishers, and readers, as well as managing loan workflows and user feedback through reviews.

### 2. The 10 Implemented Business Requirements
1.  **Publisher Management:** Complete lifecycle management (add, view, update, delete) for publishers.
2.  **Author Management:** Complete lifecycle management (add, view, update, delete) for authors, including safe unlink mechanisms.
3.  **Book Cataloging:** Comprehensive book management including stock control and mandatory references to Authors and Publishers.
4.  **Stock Validation:** A book cannot be loaned if the stock is 0.
5.  **Reader Management:** Registration and management of readers, including email uniqueness validation and profile updates.
6.  **Loan System:** Readers can borrow books for a default period of 2 weeks.
7.  **Loan Lifecycle:** Ability to create loans, track them, and mark them as completed (returned), which automatically restores book stock.
8.  **Loan Limit:** A reader cannot have more than 3 active loans simultaneously.
9.  **Review System:** Readers can leave, edit, or delete reviews for books they have read.
10. **Search & Sort:** Ability to search for books by title and automated sorting of all lists by ID for consistent data presentation.

### 3. Main Features
* **Full CRUD Architecture:** Complete Create, Read, Update, and Delete operations for all 6 major entities (Book, Author, Publisher, Reader, Loan, Review).
* **Smart Stock Management:** Automated stock adjustment when books are borrowed or returned.
* **Data Consistency:** Implementation of `CascadeType.ALL` and transactional logic to ensure related data (like reviews or loans) is handled correctly when deleting users or books.
* **Search Engine:** A dedicated endpoint for filtering books by title.
* **Data Integrity:** Strict validations using Hibernate Validator and database constraints to prevent duplicates and invalid states.