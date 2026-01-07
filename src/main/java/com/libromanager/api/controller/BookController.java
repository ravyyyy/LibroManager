package com.libromanager.api.controller;

import com.libromanager.api.dto.BookRequestDTO;
import com.libromanager.api.entity.Book;
import com.libromanager.api.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Endpoints for managing books inventory")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Operation(summary = "Add a new book", description = "Creates a new book entry. Requires valid Publisher ID and Authors IDs.")
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookRequestDTO bookRequest) {
        return new ResponseEntity<>(bookService.addBook(bookRequest), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all books", description = "Returns a list of all books in the library, sorted by ID.")
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Find books containing the specific string in their title (case-insensitive).")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String title) {
        return new ResponseEntity<>(bookService.searchBookByTitle(title), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Updates details of an existing book. Can reassign authors or publishers.")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDTO request) {
        return new ResponseEntity<>(bookService.updateBook(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Removes a book from the system. Cascades to delete associated loans and reviews.")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
